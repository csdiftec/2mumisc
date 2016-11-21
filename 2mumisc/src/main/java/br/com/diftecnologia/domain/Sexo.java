package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sexo")
public class Sexo extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="sigla", length = 1, nullable = false)
	private char sigla;

	public char getSigla() {
		return sigla;
	}

	public void setSigla(char sigla) {
		this.sigla = sigla;
	}
	
}
