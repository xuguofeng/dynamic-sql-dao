package org.net5ijy.dao.jdbc;

import org.net5ijy.dao.DepartmentDao;
import org.net5ijy.dao.bean.Department;
import org.net5ijy.dao.dynamic.BaseDaoSupport;
import org.net5ijy.util.annotation.Transactional;

@Transactional
public class DepartmentDaoImpl extends BaseDaoSupport<Department> implements
		DepartmentDao {

}
