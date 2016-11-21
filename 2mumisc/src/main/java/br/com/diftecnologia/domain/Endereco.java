
package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="endereco")
@NamedQueries({
		@NamedQuery(name = "Endereco.buscarUltimoRegistro",
	query = "SELECT  max(endereco.codigo) FROM Endereco endereco ")
})
public class Endereco extends GenericDomain {
	
	private static final long serialVersionUID = 1L;

	@Size(min=1, max=60, message="Tamanho inválido para o campo Rua(1 - 60)")
	@Column(name="rua", length = 60, nullable = false)
	private String rua;
	
//	@Size(min=1, max=6, message="Tamanho inválido para o campo Razão Social(1 - 6)")
	@Column(name="numero", length = 6, nullable = true)
	private Integer numero;
	
	@Size(min=1, max=60, message="Tamanho inválido para o campo Bairro(1 - 60)")
	@Column(name="bairro", length = 60, nullable = false)
	private String bairro;
	
	@Column(name="cep", length = 9)
	private String cep;
	
	@Column(name="complemento", length = 50)
	private String complemento;
	

	@ManyToOne
	@JoinColumn(nullable = false)
	private Cidade cidade;

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
}

