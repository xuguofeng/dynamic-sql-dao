package org.net5ijy.dao.dynamic;

import java.util.List;
import java.util.Map;

public interface BaseDao<E> {

	int addObject(E e);

	boolean deleteObject(Integer id);

	boolean updateObject(E e);

	E getObject(Integer id);

	E getObject(Map<String, Object> paramaters);

	List<E> getObjects();

	List<E> getObjects(Map<String, Object> paramaters);

	List<E> getObjects(Map<String, Object> paramaters, Integer pageNum,
			Integer pageSize);
	
	int count(Map<String, Object> paramaters);
}
