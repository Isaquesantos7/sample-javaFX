package model.model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	void insert(Department obj);
	void updateById(Department obj, int id);
	void deleteById(int id);
	List<Department> findById(int id);
	List<Department> findAll();
}
