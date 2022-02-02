package model.dao;

import db.DB;
import model.dao.impl.CamisetasDaoJDBC;

public class DaoFactory {

	public static CamisetasDao createCamisetasDao() {
		return new CamisetasDaoJDBC(DB.getConnection());
	}
}
