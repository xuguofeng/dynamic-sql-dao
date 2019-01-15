package org.net5ijy.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.net5ijy.dao.bean.Student;
import org.net5ijy.dao.bean.Teacher;
import org.net5ijy.util.BeanFactory;

public class TestStudentDao {

	private StudentDao studentDao;
	private TeacherDao teacherDao;

	static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Before
	public void before() {

		// 获取工厂里面的DAO实现对象
		studentDao = BeanFactory.getObject(StudentDao.class);
		teacherDao = BeanFactory.getObject(TeacherDao.class);
	}

	public Set<Teacher> preAddStudent() {

		// 首先插入两个Teacher数据
		Teacher t1 = new Teacher();
		t1.setName("王老师");

		Teacher t2 = new Teacher();
		t2.setName("李老师");

		int id1 = teacherDao.addObject(t1);
		int id2 = teacherDao.addObject(t1);
		t1.setId(id1);
		t2.setId(id2);

		Set<Teacher> ts = new HashSet<Teacher>();
		ts.add(t1);
		ts.add(t2);

		return ts;
	}

	@After
	public void after() {
		// 删除学生数据
		List<Student> students = studentDao.getObjects();
		for (Student student : students) {
			studentDao.deleteObject(student.getId());
		}
		// 删除老师数据
		List<Teacher> teachers = teacherDao.getObjects();
		for (Teacher teacher : teachers) {
			teacherDao.deleteObject(teacher.getId());
		}
	}

	@Test
	public void testAdd() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 插入第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		// 插入第二个学生数据
		Student s2 = new Student();
		s2.setName("徐国峰B");
		s2.setTeachers(ts);

		Integer id1 = studentDao.addObject(s1);
		Integer id2 = studentDao.addObject(s2);

		Student tmp1 = studentDao.getObject(id1);
		Student tmp2 = studentDao.getObject(id2);

		assertEquals(id1, tmp1.getId());
		assertEquals(id2, tmp2.getId());

		assertEquals(s1.getName(), tmp1.getName());
		assertEquals(s2.getName(), tmp2.getName());

		assertEquals(ts, tmp1.getTeachers());
		assertEquals(ts, tmp2.getTeachers());
	}

	@Test
	public void testDelete() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 插入第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		// 插入第二个学生数据
		Student s2 = new Student();
		s2.setName("徐国峰B");
		s2.setTeachers(ts);

		Integer id1 = studentDao.addObject(s1);
		Integer id2 = studentDao.addObject(s2);

		// 删除id2
		studentDao.deleteObject(id2);

		// 再去获取
		Student tmp1 = studentDao.getObject(id1);
		Student tmp2 = studentDao.getObject(id2);

		// 判断数据正确性
		assertNull(tmp2);
		assertEquals(id1, tmp1.getId());
		assertEquals(s1.getName(), tmp1.getName());
		assertEquals(ts, tmp1.getTeachers());
	}

	@Test
	public void testUpdate() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 插入第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		Integer id1 = studentDao.addObject(s1);

		// 再去获取
		Student tmp1 = studentDao.getObject(id1);

		// 修改数据
		tmp1.setName("徐国峰AA");

		Set<Teacher> ts2 = new HashSet<Teacher>();
		for (Teacher teacher : ts) {
			ts2.add(teacher);
			break;
		}
		tmp1.setTeachers(ts2);

		studentDao.updateObject(tmp1);

		// 再去获取修改后的数据
		Student tmp2 = studentDao.getObject(id1);

		// 判断数据正确性
		assertEquals(tmp1.getId(), tmp2.getId());
		assertEquals(tmp1.getName(), tmp2.getName());
		assertEquals(Integer.valueOf(1),
				Integer.valueOf(tmp2.getTeachers().size()));
		assertEquals(tmp1.getTeachers(), tmp2.getTeachers());
	}

	@Test
	public void testGetById() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		Integer id1 = studentDao.addObject(s1);

		// 再去获取
		Student tmp1 = studentDao.getObject(id1);

		// 判断数据正确性
		assertEquals(id1, tmp1.getId());
		assertEquals(s1.getName(), tmp1.getName());
		assertEquals(ts, tmp1.getTeachers());
	}

	@Test
	public void testGetByMap() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		Integer id1 = studentDao.addObject(s1);

		// 再去获取
		Student tmp1 = studentDao.getObject(id1);

		// 根据map获取
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "徐国峰A");
		Student tmp2 = studentDao.getObject(map);

		// 判断数据正确性
		assertEquals(tmp1.getId(), tmp2.getId());
		assertEquals(tmp1.getName(), tmp2.getName());
		assertEquals(tmp1.getTeachers(), tmp2.getTeachers());
	}

	@Test
	public void testGets() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		studentDao.addObject(s1);

		// 查询全部数据
		List<Student> ss = studentDao.getObjects();

		// 查询数据量
		Integer count = studentDao.count(null);

		assertEquals(Integer.valueOf(ss.size()), count);
	}

	@Test
	public void testGetsByMap() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		studentDao.addObject(s1);

		// 根据Map查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "徐国峰A");
		List<Student> ss = studentDao.getObjects(map);

		// 查询数据量
		Integer count = studentDao.count(map);

		assertEquals(Integer.valueOf(ss.size()), count);
	}

	@Test
	public void testGetsByMapIntInt() throws ParseException {

		Set<Teacher> ts = preAddStudent();

		// 第一个学生数据
		Student s1 = new Student();
		s1.setName("徐国峰A");
		s1.setTeachers(ts);

		studentDao.addObject(s1);

		// 根据Map查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "徐国峰A");
		List<Student> ss = studentDao.getObjects(map, 1, 10);

		// 查询数据量
		Integer count = studentDao.count(map);

		assertEquals(Integer.valueOf(ss.size()), count);
	}
}
