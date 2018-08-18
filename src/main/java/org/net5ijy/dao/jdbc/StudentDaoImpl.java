package org.net5ijy.dao.jdbc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.net5ijy.dao.StudentDao;
import org.net5ijy.dao.TeacherDao;
import org.net5ijy.dao.bean.Student;
import org.net5ijy.dao.bean.Teacher;
import org.net5ijy.dao.dynamic.BaseDaoSupport;
import org.net5ijy.util.annotation.Transactional;

@Transactional
public class StudentDaoImpl extends BaseDaoSupport<Student> implements
		StudentDao {

	private TeacherDao teacherDao;

	@Resource
	public void setTeacherDao(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Override
	@Transactional
	public int addObject(Student e) {
		int id = super.addObject(e);
		Set<Teacher> teachers = e.getTeachers();
		this.addStudentTeacher(id, teachers);
		return id;
	}

	private void addStudentTeacher(int id, Set<Teacher> teachers) {
		for (Teacher t : teachers) {
			super.execute("Student_add_student_teacher", id, t.getId());
		}
	}

	@Override
	@Transactional
	public boolean deleteObject(Integer id) {
		super.execute("Student_deleteTeachersById", id);
		return super.deleteObject(id);
	}

	@Override
	@Transactional
	public boolean updateObject(Student e) {
		super.execute("Student_deleteTeachersById", e.getId());
		Set<Teacher> teachers = e.getTeachers();
		this.addStudentTeacher(e.getId(), teachers);
		return super.updateObject(e);
	}

	@Override
	public Student getObject(Integer id) {
		Student student = super.getObject(id);
		if (student != null) {
			student.setTeachers(getTeachers(id));
		}
		return student;
	}

	@Override
	public Student getObject(Map<String, Object> paramaters) {
		Student student = super.getObject(paramaters);
		if (student != null) {
			student.setTeachers(getTeachers(student.getId()));
		}
		return student;
	}

	private Set<Teacher> getTeachers(int studentId) {
		Set<Teacher> teachers = new HashSet<Teacher>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("studentId", studentId);
		List<Integer> ids = super
				.selectColumn("Student_getTeacherIdsById", map);
		for (Integer i : ids) {
			Teacher t = this.teacherDao.getObject(i);
			teachers.add(t);
		}
		return teachers;
	}
}
