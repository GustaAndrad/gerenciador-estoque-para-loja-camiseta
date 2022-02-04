package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.CamisetasDao;
import model.entities.Camisetas;

public class CamisetasDaoJDBC implements CamisetasDao {

	private Connection conn;

	public CamisetasDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Camisetas findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM camisetas WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Camisetas obj = new Camisetas();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setQuantidade(rs.getInt("Quantidade"));
				obj.setPreco(rs.getDouble("Preco"));
				obj.setPrecoCusto(rs.getDouble("PrecoDeCusto"));
				obj.getCustoTotal(rs.getDouble("CustoTotal"));
				obj.getValorTotal(rs.getDouble("ValorTotal"));
				obj.getLucroLiquido(rs.getDouble("LucroLiquido"));
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Camisetas> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM camisetas ORDER BY Name");
			rs = st.executeQuery();

			List<Camisetas> list = new ArrayList<>();

			while (rs.next()) {
				Camisetas obj = new Camisetas();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setQuantidade(rs.getInt("Quantidade"));
				obj.setPreco(rs.getDouble("Preco"));
				obj.setPrecoCusto(rs.getDouble("PrecoDeCusto"));
				obj.getCustoTotal(rs.getDouble("CustoTotal"));
				obj.getValorTotal(rs.getDouble("ValorTotal"));
				obj.getLucroLiquido(rs.getDouble("LucroLiquido"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void insert(Camisetas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO camisetas " 
					+ "(Name, Quantidade, Preco, PrecoDeCusto) " 
					+ "VALUES " + "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getQuantidade());
			st.setDouble(3, obj.getPreco());
			st.setDouble(4, obj.getPrecoCusto());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Camisetas obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE camisetas " 
					+ "SET Name = ?, Quantidade = ?, Preco = ?, PrecoDeCusto = ? " 
					+ "WHERE Id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getQuantidade());
			st.setDouble(3, obj.getPreco());
			st.setDouble(4, obj.getPrecoCusto());
			st.setInt(5, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM camisetas WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}
}
