package org.net5ijy.dao.jdbc;

import org.net5ijy.dao.TeacherDao;
import org.net5ijy.dao.bean.Teacher;
import org.net5ijy.dao.dynamic.BaseDaoSupport;

public class TeacherDaoImpl extends BaseDaoSupport<Teacher> implements
		TeacherDao {

	@Override
	public boolean deleteObject(Integer id) {
		super.execute("Teacher_deleteStudentsById", id);
		return super.deleteObject(id);
	}
}
