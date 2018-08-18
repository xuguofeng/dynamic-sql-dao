package org.net5ijy.dao.jdbc;

import org.net5ijy.dao.EmployeeDao;
import org.net5ijy.dao.bean.Employee;
import org.net5ijy.dao.dynamic.BaseDaoSupport;
import org.net5ijy.util.annotation.Transactional;

@Transactional
public class EmployeeDaoImpl extends BaseDaoSupport<Employee> implements
		EmployeeDao {

}
