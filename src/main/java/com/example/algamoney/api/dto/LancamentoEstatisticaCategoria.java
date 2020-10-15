package com.example.algamoney.api.dto;

import java.math.BigDecimal;

import com.example.algamoney.api.model.Categoria;

public class LancamentoEstatisticaCategoria {
	
	private Categoria categotia;
	
	private BigDecimal total;

	public LancamentoEstatisticaCategoria(Categoria categotia, BigDecimal total) {
		this.categotia = categotia;
		this.total = total;
	}

	public Categoria getCategotia() {
		return categotia;
	}

	public void setCategotia(Categoria categotia) {
		this.categotia = categotia;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
