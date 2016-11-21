package br.com.diftecnologia.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.br.CNPJ;

@MappedSuperclass
public class Empresa extends GenericDomain {	

	private static final long serialVersionUID = 1L;
	

	@Size(min=1, max=60, message="Tamanho inválido para o campo Razão Social(1 - 60)")
	@Column(name="razao_social", length = 60, nullable = false)
	private String razaoSocial;
	
	@Size(min=1, max=60, message="Tamanho inválido para o campo Nome Fantasia(1 - 60)")
	@Column(name="nome_fantasia", length = 60, nullable = false)
	private String nomeFantasia;
	
	@CNPJ
	@Column(name="cnpj", length = 19, nullable = false, unique = true)
	private String cnpj;
	
	@Email
	@Column(name="email", length = 100, nullable = false)
	private String email;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_cadastro", nullable = false)
	private Date dataCadastro;
	
	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(nullable = true)
	private Endereco endereco;
	

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}


	
}