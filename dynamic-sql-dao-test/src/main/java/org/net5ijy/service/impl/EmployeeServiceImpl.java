package org.net5ijy.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.net5ijy.dao.EmployeeDao;
import org.net5ijy.service.EmployeeService;
import org.net5ijy.util.annotation.Transactional;

@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeDao employeeDao;

	@Resource
	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}

	@Override
	// @Transactional
	public void deleteEmployees(List<Integer> ids) {
		int i = 0;
		for (Integer id : ids) {
			i++;
			if (i == 2) {
				// throw new RuntimeException("测试事务管理");
			}
			this.employeeDao.deleteObject(id);
		}
	}
}
