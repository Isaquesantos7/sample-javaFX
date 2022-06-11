package model.model.dao;

import model.db.DB;
import model.model.dao.iplm.DepartmentDaoJDBC;
import model.model.dao.iplm.SellerDaoJDBC;

public class DaoFactory {
	public static DepartmentDao createDepartmentDaoJDBC() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
	public static SellerDao createSellerDaoJDBC() {
		return new SellerDaoJDBC(DB.getConnection());
	}
}
