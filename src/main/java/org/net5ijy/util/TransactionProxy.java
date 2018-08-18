package org.net5ijy.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.net5ijy.dao.dynamic.util.TransactionManager;
import org.net5ijy.util.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成事务管理代理对象
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月11日 上午8:30:09
 */
public class TransactionProxy {

	private static Logger log = LoggerFactory.getLogger(TransactionProxy.class);

	/**
	 * 为指定的object对象生成事务管理代理对象
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月11日 上午8:30:34
	 * @param object
	 * @return
	 */
	public static Object proxyFor(Object object) {
		// debug log
		if (log.isDebugEnabled()) {
			log.debug("创建代理[" + object.getClass().getName() + "]");
		}
		return Proxy.newProxyInstance(object.getClass().getClassLoader(),
				object.getClass().getInterfaces(),
				new TransactionInvocationHandler(object));
	}
}

class TransactionInvocationHandler implements InvocationHandler {

	private static Logger log = LoggerFactory
			.getLogger(TransactionInvocationHandler.class);

	private Object proxy;
	private Class<?> proxyClass;

	TransactionInvocationHandler(Object object) {
		this.proxy = object;
		this.proxyClass = object.getClass();
	}

	public Object invoke(Object obj, Method method, Object[] objects)
			throws Throwable {
		Object result = null;
		// 判断是否有Transactional注解
		Transactional t = this.proxyClass.getMethod(method.getName(),
				method.getParameterTypes()).getAnnotation(Transactional.class);
		try {
			// 没有Transactional注解直接执行返回
			if (t == null) {
				if (log.isDebugEnabled()) {
					log.debug("业务方法不需要开启事务");
				}
				return method.invoke(proxy, objects);
			}
			// 有Transactional注解
			// 开启事务
			TransactionManager.startTransacation();
			if (log.isDebugEnabled()) {
				log.debug("开启事务");
			}
			// 获取业务操作返回值
			result = method.invoke(proxy, objects);
			// 提交事务
			TransactionManager.commit();
			if (log.isDebugEnabled()) {
				log.debug("提交事务");
			}
		} catch (Exception e) {
			log.error("业务操作失败", e);
			// 异常时事务回滚
			if (t != null) {
				TransactionManager.rollback();
				if (log.isDebugEnabled()) {
					log.debug("回滚事务");
				}
			}
			throw e;
		} finally {
			// 关闭连接
			if (log.isDebugEnabled()) {
				log.debug("关闭数据库连接");
			}
			TransactionManager.close();
		}
		return result;
	}
}
