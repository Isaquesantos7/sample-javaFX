package model.model.dao.iplm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.db.DB;
import model.db.DBException;
import model.entities.Department;
import model.entities.Seller;
import model.model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {
	Connection conn = null;
	private ResultSet rs;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;

		try {
			String sql = "insert into tb_seller (Name, Email, BirthDate, BaseSalary, DepartmentId) Values (?, ?, ?, ?, ?);";
			st = conn.prepareStatement(sql);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartmentId().getId());

			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Rows Affect " + rowsAffect);
			} else {
				System.out.println("No rows Affect!");
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
			String sql = "delete from tb_seller where Id = ?;";
			st = conn.prepareStatement(sql);
			st.setInt(1, id);

			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Rows affected " + rowsAffect);
			} else {
				System.out.println("No rows affectd!");
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void updateById(Seller obj, int id) {
		PreparedStatement st = null;

		try {
			String sql = "update tb_seller set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? where Id = ?;";
			st = conn.prepareStatement(sql);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartmentId().getId());
			st.setInt(6, id);

			int rowsAffect = st.executeUpdate();

			if (rowsAffect > 0) {
				System.out.println("Rows affected " + rowsAffect);
			} else {
				System.out.println("No rows affected!");
			}
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT tb_seller.*, tb_department.Name as DepName FROM tb_seller INNER JOIN tb_department"
					+ " ON tb_seller.DepartmentId = tb_department.Id" + " WHERE tb_seller.Id = ?";

			st = conn.prepareStatement(sql);
			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = initiateDepartment(rs);
				Seller obj = initiateSeller(rs, dep);
				return obj;
			}

		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		rs = null;

		try {
			String sql = "SELECT tb_seller.*, tb_department.Name as DepName"
					+ " FROM tb_seller INNER JOIN tb_department" + " ON tb_seller.DepartmentId = tb_department.Id"
					+ " WHERE DepartmentId = ?" + " ORDER BY Name";
			;

			st = conn.prepareStatement(sql);
			st.setInt(1, department.getId());

			rs = st.executeQuery();

			Map<Integer, Department> map = new HashMap<>();
			List<Seller> list = new ArrayList<>();

			while (rs.next()) {
				Department dep;

				dep = map.get(rs.getInt("id"));

				if (dep == null) {
					dep = initiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = initiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DBException("Error: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT tb_seller.*,tb_department.Name as DepName "
					+ "FROM tb_seller INNER JOIN tb_department " + "ON tb_seller.DepartmentId = tb_department.Id "
					+ "ORDER BY Name";

			st = conn.prepareStatement(sql);
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = initiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = initiateSeller(rs, dep);
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

	private static Seller initiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartmentId(dep);
		return obj;
	}
}
