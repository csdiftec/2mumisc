package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="tipo_pagamento")
public class TipoPagamento extends GenericDomain {
	
	private static final long serialVersionUID = 1L;

	@Size(min=1, max=60, message="Tamanho inválido para o campo 'Descrição' (1 - 20)")
	@Column(name="descricao", length = 20, nullable = false)
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
