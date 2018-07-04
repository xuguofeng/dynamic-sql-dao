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
import org.net5ijy.dao.jdbc.DepartmentDaoImpl;

public class TestDepartmentDao {

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
		// 删除测试添加的数据
		// 删除研发一部
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "研发一部");
		List<Department> depts = departmentDao.getObjects(map);

		for (Department dept : depts) {
			departmentDao.deleteObject(dept.getId());
		}

		// 删除IT技术部
		map.put("name", "IT技术部");

		depts = departmentDao.getObjects(map);

		for (Department dept : depts) {
			departmentDao.deleteObject(dept.getId());
		}
	}

	@Test
	public void testAdd() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);
		dept.setParentDepartmentId(d.getId());

		// 插入数据
		int id = departmentDao.addObject(dept);

		// 根据id获取数据
		d = departmentDao.getObject(id);

		dept.setId(id);

		// 对比数据
		assertDepartmentEqual(dept, d);
		assertTrue(d.getVersion() == 0);
	}

	@Test
	public void testDelete() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);
		dept.setParentDepartmentId(d.getId());

		// 插入数据
		int id = departmentDao.addObject(dept);

		// 删除数据
		departmentDao.deleteObject(id);

		// 再根据id获取出来
		d = departmentDao.getObject(id);

		// 判断为null
		assertNull(d);
	}

	@Test
	public void testUpdate() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部xx描述");
		dept.setName("研发一部xx");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		int id = departmentDao.addObject(dept);

		// 再获取出来
		d = departmentDao.getObject(id);

		// 修改数据
		d.setDescription("研发一部描述");
		d.setName("研发一部");
		d.setParentDepartmentId(null);

		// 修改数据
		departmentDao.updateObject(d);

		// 再根据id获取
		Department tmp = departmentDao.getObject(d.getId());

		// 判断数据是否相同
		assertDepartmentEqual(d, tmp);
		assertEquals(Integer.valueOf(d.getVersion() + 1), tmp.getVersion());
	}

	@Test
	public void testGetById() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		int id = departmentDao.addObject(dept);

		// 再获取出来
		d = departmentDao.getObject(id);

		// 判断数据是否相同
		assertDepartmentEqual(dept, d);
		assertEquals(d.getVersion(), d.getVersion());
	}

	@Test
	public void testGetByMap() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		departmentDao.addObject(dept);

		// 再获取出来
		map.put("name", "研发一部");
		d = departmentDao.getObject(map);

		// 判断数据是否相同
		assertDepartmentEqual(dept, d);
		assertEquals(Integer.valueOf(0), d.getVersion());
	}

	@Test
	public void testGets() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		departmentDao.addObject(dept);

		// 获取全部部门数据
		List<Department> depts = departmentDao.getObjects();

		// 获取数量
		int rows = departmentDao.count(null);

		// 判断数据量
		assertEquals(Integer.valueOf(rows), Integer.valueOf(depts.size()));
	}

	@Test
	public void testGetsByMap() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		departmentDao.addObject(dept);

		// 重新设置name参数
		map.put("name", "xxx");

		// 根据map获取部门数据
		List<Department> depts = departmentDao.getObjects(map);

		// 获取数量
		int rows = departmentDao.count(map);

		// 判断数据量
		assertEquals(Integer.valueOf(rows), Integer.valueOf(depts.size()));
	}

	@Test
	public void testGetsByMapIntInt() throws ParseException {

		// 创建部门对象
		Department dept = new Department();
		dept.setCreateTime(new Date());
		dept.setDescription("研发一部描述");
		dept.setName("研发一部");

		// 获取上级部门
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "IT技术部");

		Department d = departmentDao.getObject(map);

		dept.setParentDepartmentId(d.getId());

		// 插入数据
		departmentDao.addObject(dept);

		// 重新设置name参数
		map.put("name", "xxx");

		// 根据map获取部门数据
		List<Department> depts = departmentDao.getObjects(map, 1, 10);

		// 获取数量
		int rows = departmentDao.count(map);

		// 判断数据量
		assertEquals(Integer.valueOf(rows), Integer.valueOf(depts.size()));
	}

	private void assertDepartmentEqual(Department expected, Department actual) {
		if (expected.getId() != null) {
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(ymdhms.format(expected.getCreateTime()),
				ymdhms.format(actual.getCreateTime()));
		assertEquals(expected.getDescription(), actual.getDescription());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getParentDepartmentId(),
				actual.getParentDepartmentId());
	}
}
