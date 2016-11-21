package br.com.diftecnologia.domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cidade")
public class Cidade extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="nome", length = 60, nullable = false)
	private String nome;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Estado estado;
	
	@Column(name="codigo_ibge", length = 7, nullable = false)
	private Integer codigoIBGE;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Integer getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(Integer codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}
	
}
