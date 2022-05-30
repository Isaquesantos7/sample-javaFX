package model.service;

import java.util.List;

import model.entities.Department;
import model.model.dao.DaoFactory;
import model.model.dao.DepartmentDao;

public class DepartmentService {
	private DepartmentDao dao = DaoFactory.createDepartmentDaoJDBC();

	public List<Department> findAll() {
		List<Department> list = dao.findAll();
		return list;
	}

	public void saveOrUpdate(Department obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.updateById(obj, obj.getId());
		}
	}
}
