package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="forma_pagamento")
public class FormaPagamento extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="descricao", length = 20, nullable = false)
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
