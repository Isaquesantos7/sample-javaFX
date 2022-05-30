package model.model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	void insert(Seller obj);
	void deleteById(int id);
	void updateById(Seller obj, int id);
	Seller findById(int id);
	List<Seller> findByDepartment(Department department);
	List<Seller> findAll();
}
