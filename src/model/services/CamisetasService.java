package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.CamisetasDao;
import model.entities.Camisetas;

public class CamisetasService {
	
	private CamisetasDao dao = DaoFactory.createCamisetasDao();
	
	public List<Camisetas> findAll(){
		return dao.findAll();
	}
	
	public void saveOrUpdate (Camisetas obj) {
		if(obj.getId() == null){
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Camisetas obj) {
		dao.deleteById(obj.getId());
	}
}
