package org.net5ijy.util.annotation;

import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标注在需要使用事务的业务层类/接口和方法上
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年6月11日 上午8:31:59
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

}
