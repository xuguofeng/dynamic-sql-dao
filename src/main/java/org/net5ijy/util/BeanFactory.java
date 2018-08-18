package org.net5ijy.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.net5ijy.util.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象工厂<br />
 * 
 * 内部使用一个Map保存对象信息<br />
 * 
 * 在类加载时解析beans.properties文件（格式为：接口简单名—>实现类全名）。实例化类对象，并保存到Map中<br />
 * 
 * 在实例化对象之后，会判断该类是否加了@Transactional注解，如果有，会创建代理对象实现事务的管理<br />
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月8日 上午10:56:57
 */
@SuppressWarnings("unchecked")
public final class BeanFactory {

	private static Logger log = LoggerFactory.getLogger(BeanFactory.class);

	private BeanFactory() {
	}

	/**
	 * 保存对象，接口名 —> 实现类对象
	 */
	private static Map<String, Object> beans = new HashMap<String, Object>();

	static {
		// 解析对象工厂配置文件
		Properties prop = new Properties();
		try {
			log.info("初始化对象工厂");

			prop.load(BeanFactory.class.getClassLoader().getResourceAsStream(
					"beans.properties"));

			Set<String> keys = prop.stringPropertyNames();

			// 遍历，实例化，放入对象缓存beans
			for (String key : keys) {
				String className = prop.getProperty(key);
				try {
					Class<?> clazz = Class.forName(className);
					Object obj = clazz.newInstance();

					// 放入对象缓存beans
					beans.put(key, obj);

					// debug log
					if (log.isDebugEnabled()) {
						log.debug("实例化成功[" + key + ", " + className + "]");
					}
				} catch (ClassNotFoundException e) {
					log.error(className + "类未找到", e);
				} catch (InstantiationException e) {
					log.error(className + "类实例化出错", e);
				} catch (IllegalAccessException e) {
					log.error(className + "类访问出错", e);
				}
			}

			// 依赖注入
			Collection<Object> objects = beans.values();
			for (Object obj : objects) {
				Method[] methods = obj.getClass().getMethods();
				for (Method method : methods) {
					if (method.getAnnotation(Resource.class) != null) {
						String methodName = method.getName().replace("set", "");
						Object o = beans.get(methodName);
						try {
							method.invoke(obj, o);
						} catch (Exception e) {
							log.error("依赖注入出错", e);
						}
					}
				}
			}

			// 事务扫描
			Set<String> beanNames = beans.keySet();
			for (String name : beanNames) {
				Object obj = beans.get(name);
				Class<?> clazz = obj.getClass();
				// 事务扫描
				if (clazz.getAnnotation(Transactional.class) != null) {
					// 使用JDK动态代理
					obj = TransactionProxy.proxyFor(obj);
					// 使用cglib动态代理
					// debug log
					if (log.isDebugEnabled()) {
						log.debug("实例化并创建代理[" + name + ", " + clazz.getName()
								+ "]");
					}
					beans.put(name, obj);
				}
			}

			log.info("初始化对象工厂完成");
		} catch (IOException e) {
			log.error("对象工厂配置文件读取出错", e);
			throw new RuntimeException("对象工厂配置文件读取出错", e);
		}
	}

	/**
	 * 根据指定接口类对象获取实现类对象
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月8日 上午10:55:30
	 * @param clazz
	 * @return
	 */
	public static <T> T getObject(Class<T> clazz) {
		Object bean = beans.get(clazz.getSimpleName());
		// debug log
		if (log.isDebugEnabled()) {
			log.debug("获取bean[" + clazz.getSimpleName() + ", "
					+ bean.getClass().getName() + "]");
		}
		if (bean != null) {
			return (T) bean;
		}
		return null;
	}
}
