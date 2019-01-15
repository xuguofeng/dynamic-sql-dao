package org.net5ijy.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.net5ijy.dao.DepartmentDao;
import org.net5ijy.dao.EmployeeDao;
import org.net5ijy.dao.bean.Department;
import org.net5ijy.dao.bean.Employee;
import org.net5ijy.util.BeanFactory;

public class TestEmployeeService {

	private EmployeeDao employeeDao;
	private DepartmentDao departmentDao;

	private EmployeeService employeeService;

	static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Test
	public void testDeleteTransaction() throws ParseException {

		// 获取工厂里面的DAO实现对象
		departmentDao = BeanFactory.getObject(DepartmentDao.class);
		employeeDao = BeanFactory.getObject(EmployeeDao.class);
		employeeService = BeanFactory.getObject(EmployeeService.class);

		// 添加IT技术部
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("IT技术部描述");
		dept.setName("IT技术部");

		// 插入数据
		departmentDao.addObject(dept);

		// 插入新数据
		Employee emp = new Employee();
		emp.setName("徐国峰");
		emp.setBirthday(ymd.parse("1990-09-16"));
		emp.setCreateTime(new Date());
		emp.setEmail("xuguofeng1990@126.com");
		emp.setGender(1);
		emp.setJoinDate(ymd.parse("2018-01-10"));
		emp.setPhone("18902120812");
		emp.setSalary(4200d);

		// 设置部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");
		Department dept2 = departmentDao.getObject(map);

		emp.setDepartmentId(dept2.getId());

		int id1 = employeeDao.addObject(emp);

		// 插入新数据
		Employee emp2 = new Employee();
		emp2.setName("徐国峰2");
		emp2.setBirthday(ymd.parse("1990-09-16"));
		emp2.setCreateTime(new Date());
		emp2.setEmail("xuguofeng1990@126.com");
		emp2.setGender(1);
		emp2.setJoinDate(ymd.parse("2018-01-10"));
		emp2.setPhone("18902120812");
		emp2.setSalary(4200d);

		emp2.setDepartmentId(dept2.getId());

		int id2 = employeeDao.addObject(emp2);

		// 创建需要删除的id的集合
		List<Integer> ids = Arrays.asList(id1, id2);

		// 删除数据
		employeeService.deleteEmployees(ids);
	}
}
