package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Camisetas implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private Integer quantidade;
	private Double preco;

	public Camisetas() {
	}

	public Camisetas(Integer id, String name, Integer quantidade, Double preco) {
		super();
		this.id = id;
		this.name = name;
		this.quantidade = quantidade;
		this.preco = preco;
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

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Double getValorTotal() {
		return preco * quantidade;
	}

	public double getValorTotal(double double1) {
		return preco * quantidade;
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
