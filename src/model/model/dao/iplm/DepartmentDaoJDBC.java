package model.model.dao.iplm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entities.Department;
import model.model.DB;
import model.model.DBException;
import model.model.DBIntegrityException;
import model.model.dao.DepartmentDao;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;
	private ResultSet rs;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;

		try {
			String sql = "insert into tb_department (Name) VALUES (?);";
			st = conn.prepareStatement(sql);
			st.setString(1, obj.getName());
			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Rows affect " + rowsAffect);
			} else {
				System.out.println("No rows Affected!");
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void updateById(Department obj, int id) {
		PreparedStatement st = null;

		try {
			String sql = "update tb_department set Name = ? where Id = ?;";
			st = conn.prepareStatement(sql);
			st.setString(1, obj.getName());
			st.setInt(2, id);

			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Rows affected " + rowsAffect);
			} else {
				System.out.println("No rows Affected!");
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(int id) {
		PreparedStatement st = null;

		try {
			String sql = "delete from tb_department where Id = ?;";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Deleted with success!");
			} else {
				System.out.println("No was deleted!");
			}
		} catch (SQLException e) {
			throw new DBIntegrityException("Error: " + e.getStackTrace());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Department> findById(int id) {
		PreparedStatement st = null;
		List<Department> list = new ArrayList<>();
		
		try {
			String sql = "select * from tb_department where id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			rs = st.executeQuery();

			while (rs.next()) {
				Department obj = initiateDepartment(rs);
				list.add(obj);
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		return list;
	}

	@Override
	public List<Department> findAll() {
		Statement st = null;
		ResultSet rs = null;

		try {
			String sql = "Select * from tb_department;";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery(sql);
			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department obj = initiateDepartment(rs);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private static Department initiateDepartment(ResultSet rs) throws SQLException {
		Department obj = new Department();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		return obj;
	}
}
