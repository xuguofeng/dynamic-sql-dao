package org.net5ijy.dao.dynamic.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理<br />
 * 
 * 内部使用ThreadLocal把数据源连接绑定到当前线程上<br />
 * 
 * 并提供了获取当前线程连接、事务管理、关闭连接的几个核心方法<br />
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月11日 上午8:34:12
 */
public class TransactionManager {

	/**
	 * 给当前线程绑定Connection
	 */
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();

	/**
	 * 获取当前线程绑定Connection
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:04:25
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		Connection conn = tl.get();
		if (conn == null) {
			conn = DBCPUtil.getConnection();
			tl.set(conn);
		}
		return conn;
	}

	/**
	 * 开启事务
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:05:20
	 * @throws SQLException
	 */
	public static void startTransacation() throws SQLException {
		getConnection().setAutoCommit(false);
	}

	/**
	 * 提交事务
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:05:27
	 * @throws SQLException
	 */
	public static void commit() throws SQLException {
		getConnection().commit();
	}

	/**
	 * 回滚事务
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:05:33
	 * @throws SQLException
	 */
	public static void rollback() throws SQLException {
		getConnection().rollback();
	}

	/**
	 * 关闭连接
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:05:41
	 * @throws SQLException
	 */
	public static void close() throws SQLException {
		getConnection().close();
		tl.remove();
	}
}
