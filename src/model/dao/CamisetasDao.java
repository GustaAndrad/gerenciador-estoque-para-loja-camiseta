package model.dao;

import java.util.List;

import model.entities.Camisetas;

public interface CamisetasDao {

	void insert(Camisetas obj);
	void update(Camisetas obj);
	void deleteById(Integer id);
	Camisetas findById(Integer id);
	List<Camisetas> findAll();
}
