package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="estado")
public class Estado extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="sigla", length = 2, nullable = false)
	private String sigla;
	
	@Column(name="nome", length = 60, nullable = false)
	private String nome;
	
	@Column(name="codigo_ibge", length = 2, nullable = false)
	private Byte codigoIBGE;
	
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Byte getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(Byte codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}
}
