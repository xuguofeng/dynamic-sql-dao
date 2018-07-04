package org.net5ijy.dao.dynamic;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.net5ijy.dao.dynamic.util.GenericsUtil;
import org.net5ijy.dao.dynamic.util.TransactionManager;
import org.net5ijy.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDaoSupport<E> implements BaseDao<E> {

	private static Logger log = LoggerFactory.getLogger(BaseDaoSupport.class);

	private DynamicSQLParser sqlParser = DynamicSQLParser.getDynamicSQLParser();

	private static Pattern argPattern = Pattern.compile("#([^\\s]+)#");

	@SuppressWarnings("unchecked")
	protected Class<E> entityClass = GenericsUtil
			.getSuperClassGenricType(getClass());

	@Override
	public int addObject(E e) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_addObject",
					null, false);
			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				try {
					Method getter = this.entityClass.getMethod("get"
							+ StringUtil.captureName(arg));
					Object ret = getter.invoke(e, new Object[] {});
					l.add(ret);
				} catch (Exception e1) {
					throw new RuntimeException("SQL模板[" + sql + "]参数注入出错["
							+ arg + "]", e1);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			// 执行SQL获取id
			return qr
					.insert(conn, sql, new ScalarHandler<Long>(1), l.toArray())
					.intValue();
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public boolean deleteObject(Integer id) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();
			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();
			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_deleteObject",
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + id + "]");
			}
			// 执行SQL获取影响的行数
			int rows = qr.update(conn, sql, id);
			return rows == 1;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public boolean updateObject(E e) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_updateObject",
					null, false);
			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				try {
					Method getter = this.entityClass.getMethod("get"
							+ StringUtil.captureName(arg));
					Object ret = getter.invoke(e, new Object[] {});
					l.add(ret);
				} catch (Exception e1) {
					throw new RuntimeException("SQL模板[" + sql + "]参数注入出错["
							+ arg + "]", e1);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			// 执行SQL获取影响的行数
			int rows = qr.update(conn, sql, l.toArray());
			return rows == 1;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public E getObject(Integer id) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();
			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();
			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_getObject_int",
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + id + "]");
			}
			// 执行SQL获取返回对象
			ResultSetHandler<E> rsh = getResultSetHandler();
			E e = (E) qr.query(conn, sql, rsh, id);
			return e;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public E getObject(Map<String, Object> paramaters) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_getObject_map",
					paramaters, true);

			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					// #ids# —> ?, ?, ?
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			ResultSetHandler<E> rsh = getResultSetHandler();
			// 执行SQL获取返回对象
			E e = (E) qr.query(conn, sql, rsh, l.toArray());
			return e;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public List<E> getObjects() {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_getObjects",
					null, false);
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "]");
			}
			ResultSetHandler<List<E>> rsh = getResultSetListHandler();
			// 执行SQL获取返回集合
			List<E> e = (List<E>) qr.query(conn, sql, rsh);
			return e;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public List<E> getObjects(Map<String, Object> paramaters) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_getObjects_map",
					paramaters, true);
			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			ResultSetHandler<List<E>> rsh = getResultSetListHandler();
			// 执行SQL获取返回对象
			List<E> e = (List<E>) qr.query(conn, sql, rsh, l.toArray());
			return e;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public List<E> getObjects(Map<String, Object> paramaters, Integer pageNum,
			Integer pageSize) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName
					+ "_getObjects_map_int_int", paramaters, true);
			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			l.add((pageNum - 1) * pageSize);
			l.add(pageSize);
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			ResultSetHandler<List<E>> rsh = getResultSetListHandler();
			// 执行SQL获取返回对象
			List<E> e = (List<E>) qr.query(conn, sql, rsh, l.toArray());
			return e;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	@Override
	public int count(Map<String, Object> paramaters) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 获取插入数据的类型、和简单名
			String clazzName = this.entityClass.getSimpleName();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(clazzName + "_count_map",
					paramaters, true);
			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			// 执行SQL获取返回对象
			return qr.query(conn, sql, new ScalarHandler<Long>(1), l.toArray())
					.intValue();
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	protected boolean execute(String sqlId, Object... args) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();
			// 获取SQL模板
			String sql = this.sqlParser.parseSql(sqlId, null, false);
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + Arrays.toString(args)
						+ "]");
			}
			return qr.update(conn, sql, args) > 0;
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	protected <T> List<T> selectColumn(String sqlId,
			Map<String, Object> paramaters) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(sqlId, paramaters, true);

			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					// #ids# —> ?, ?, ?
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			// 执行SQL获取返回指定列的集合
			return qr
					.query(conn, sql, new ColumnListHandler<T>(1), l.toArray());
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	protected List<Map<String, Object>> selectColumns(String sqlId,
			Map<String, Object> paramaters) {
		QueryRunner qr = new QueryRunner();
		try {
			Connection conn = TransactionManager.getConnection();

			// 保存全部注入参数
			List<Object> l = new ArrayList<Object>();

			// 获取SQL模板
			String sql = this.sqlParser.parseSql(sqlId, paramaters, true);

			// 匹配SQL中待赋值的参数
			Matcher m = argPattern.matcher(sql);
			while (m.find()) {
				String arg = m.group(1);
				Object val = paramaters.get(arg);
				if (val instanceof List) {
					StringBuilder sb = new StringBuilder();
					List v = (List) val;
					int iMax = v.size() - 1;
					for (int i = 0;; i++) {
						sb.append("?");
						l.add(v.get(i));
						if (i == iMax) {
							break;
						}
						sb.append(", ");
					}
					// #ids# —> ?, ?, ?
					sql = sql.replace("#" + arg + "#", sb.toString());
				} else {
					l.add(val);
				}
			}
			sql = sql.replaceAll("#([^\\s]+)#", "?");
			if (log.isDebugEnabled()) {
				log.debug("SQL[" + sql + "], ARGS[" + l + "]");
			}
			// 执行SQL
			return qr.query(conn, sql, new MapListHandler(), l.toArray());
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new RuntimeException(e1.getMessage(), e1);
		}
	}

	protected ResultSetHandler<E> getResultSetHandler() {
		return new BeanHandler<E>(this.entityClass);
	}

	protected ResultSetHandler<List<E>> getResultSetListHandler() {
		return new BeanListHandler<E>(this.entityClass);
	}
}
