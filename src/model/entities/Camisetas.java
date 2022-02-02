package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Camisetas implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Integer quantidade;
	private Double price;
	
	
	public Camisetas() {
	}


	public Camisetas(Integer id, String name, Integer quantidade, Double price) {
		super();
		this.id = id;
		this.name = name;
		this.quantidade = quantidade;
		this.price = price;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Camisetas other = (Camisetas) obj;
		return Objects.equals(id, other.id);
	}	
}
