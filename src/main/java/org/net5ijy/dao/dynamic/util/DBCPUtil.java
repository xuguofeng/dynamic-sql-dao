package org.net5ijy.dao.dynamic.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

/**
 * DBCP数据源工具类<br />
 * 
 * 解析dbcp.properties配置文件，初始化数据源<br />
 * 
 * 使用者通过getConnection方法从数据源获取一个连接
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月11日 上午8:32:32
 */
public class DBCPUtil {

	private static Logger log = Logger.getLogger(DBCPUtil.class);

	/**
	 * 数据源对象
	 */
	private static DataSource ds;

	static {
		try {
			// 加载dbcp配置文件
			Properties prop = new Properties();
			prop.load(DBCPUtil.class.getClassLoader().getResourceAsStream(
					"dbcp.properties"));
			// 获取数据源
			ds = BasicDataSourceFactory.createDataSource(prop);
			// debug
			log.info("数据源加载完成[" + ((BasicDataSource) ds).getUrl() + ", "
					+ ((BasicDataSource) ds).getUsername() + ", "
					+ ((BasicDataSource) ds).getPassword() + "]");
		} catch (Exception e) {
			log.error("数据源配置文件读取出错", e);
			throw new RuntimeException("数据源配置文件读取出错", e);
		}
	}

	/**
	 * 从数据源中得到一个连接对象
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午9:48:46
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
