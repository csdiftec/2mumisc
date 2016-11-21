package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="unidade_medida")
public class UnidadeMedida extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Size(min=1, max=20, message="Tamanho inválido para o campo 'Descrição' (1 - 20)")
	@Column(name="descricao", length = 20, nullable = false)
	private String descricao;
	
	@Size(min=1, max=5, message="Tamanho inválido para o campo 'Sigla' (1 - 5)")
	@Column(name="sigla", length = 5, nullable = false)
	private String sigla;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
}
