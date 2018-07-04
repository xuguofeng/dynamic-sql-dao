package org.net5ijy.dao;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.net5ijy.dao.bean.Department;
import org.net5ijy.dao.bean.Employee;
import org.net5ijy.dao.jdbc.DepartmentDaoImpl;
import org.net5ijy.dao.jdbc.EmployeeDaoImpl;

public class TestEmployeeDao {

	private static EmployeeDao employeeDao = new EmployeeDaoImpl();
	private static DepartmentDao departmentDao = new DepartmentDaoImpl();

	static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Before
	public void before() {
		// 添加IT技术部
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("IT技术部描述");
		dept.setName("IT技术部");

		// 插入数据
		departmentDao.addObject(dept);
	}

	@After
	public void after() {
		// 把旧数据删除
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "徐国峰");
		List<Employee> tmps = employeeDao.getObjects(map);
		for (Employee tmp : tmps) {
			employeeDao.deleteObject(tmp.getId());
		}

		// 删除IT技术部
		map.put("name", "IT技术部");

		List<Department> depts = departmentDao.getObjects(map);

		for (Department dept : depts) {
			departmentDao.deleteObject(dept.getId());
		}
	}

	@Test
	public void testAdd() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 再把数据获取出来
		map.put("name", "徐国峰");

		Employee tmp = employeeDao.getObject(map);

		// 断言
		assertEmployeeEqual(emp, tmp);
		assertEquals(Integer.valueOf(0), tmp.getVersion());
	}

	@Test
	public void testDelete() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 再把数据获取出来
		map.put("name", "徐国峰");

		Employee tmp = employeeDao.getObject(map);

		// 删除数据
		employeeDao.deleteObject(tmp.getId());

		// 根据ID获取数据
		tmp = employeeDao.getObject(tmp.getId());

		// 断言
		assertNull(tmp);
	}

	@Test
	public void testUpdate() throws ParseException {

		// 插入新数据
		Employee emp = new Employee();
		emp.setName("徐国峰2");
		emp.setBirthday(ymd.parse("1990-09-17"));
		emp.setCreateTime(new Date());
		emp.setEmail("xuguofeng1991@126.com");
		emp.setGender(0);
		emp.setJoinDate(ymd.parse("2018-01-11"));
		emp.setPhone("18902120813");
		emp.setSalary(4201d);

		// 设置部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 再把数据获取出来
		map.put("name", "徐国峰2");
		Employee tmp = employeeDao.getObject(map);

		tmp.setName("徐国峰");
		tmp.setBirthday(ymd.parse("1990-09-16"));
		tmp.setEmail("xuguofeng1990@126.com");
		tmp.setGender(1);
		tmp.setJoinDate(ymd.parse("2018-01-10"));
		tmp.setPhone("18902120812");
		tmp.setSalary(4200d);
		tmp.setDepartmentId(null);

		// 更新数据
		employeeDao.updateObject(tmp);

		// 再把数据获取出来
		map.put("name", "徐国峰");

		Employee tmp2 = employeeDao.getObject(map);

		// 断言
		assertEmployeeEqual(tmp, tmp2);
		assertEquals(Integer.valueOf(tmp.getVersion() + 1), tmp2.getVersion());
	}

	@Test
	public void testGetById() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 再把数据获取出来
		map.put("name", "徐国峰");
		Employee tmp = employeeDao.getObject(map);

		// 根据id获取数据
		Employee tmp2 = employeeDao.getObject(tmp.getId());

		// 断言
		assertEmployeeEqual(emp, tmp2);
		assertEquals(Integer.valueOf(0), tmp2.getVersion());
		assertEquals(tmp.getId(), tmp2.getId());
	}

	@Test
	public void testGetByMap() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 再把数据获取出来
		map.put("name", "徐国峰");
		Employee tmp = employeeDao.getObject(map);

		// 断言
		assertEmployeeEqual(emp, tmp);
		assertNotNull(tmp.getId());
		assertEquals(Integer.valueOf(0), tmp.getVersion());
	}

	@Test
	public void testGets() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 查询全部数据
		List<Employee> emps = employeeDao.getObjects();

		// 查询数据量
		map.clear();
		int count = employeeDao.count(map);

		assertEquals(emps.size(), count);
	}

	@Test
	public void testGetsByMap() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 参数
		map.put("name", "徐国峰");

		// 查询全部数据
		List<Employee> emps = employeeDao.getObjects(map);

		// 查询数据量
		int count = employeeDao.count(map);

		assertEquals(emps.size(), count);
	}

	@Test
	public void testGetsByMapIntInt() throws ParseException {

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
		Department dept = departmentDao.getObject(map);

		emp.setDepartmentId(dept.getId());

		employeeDao.addObject(emp);

		// 参数
		map.put("name", "徐国峰");

		// 查询全部数据
		List<Employee> emps = employeeDao.getObjects(map, 1, 10);

		// 查询数据量
		int count = employeeDao.count(map);

		assertEquals(emps.size(), count);
	}

	private void assertEmployeeEqual(Employee expected, Employee actual) {
		if (expected.getId() != null) {
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getBirthday(), actual.getBirthday());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getGender(), actual.getGender());
		assertEquals(expected.getJoinDate(), actual.getJoinDate());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPhone(), actual.getPhone());
		assertEquals(expected.getSalary(), actual.getSalary());
		assertEquals(ymdhms.format(expected.getCreateTime()),
				ymdhms.format(actual.getCreateTime()));
		assertEquals(expected.getDepartmentId(), actual.getDepartmentId());
	}
}
