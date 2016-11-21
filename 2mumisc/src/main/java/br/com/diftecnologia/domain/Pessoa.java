package br.com.diftecnologia.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "pessoa")  
@NamedQueries({
	@NamedQuery(name = "Pessoa.buscaQtdCpfCadastrado", query = "SELECT Count(pessoa) FROM Pessoa pessoa WHERE pessoa.cpf = :cpf"),
	@NamedQuery(name="Pessoa.listaPessoasExcetoAdmin",query="SELECT pessoa FROM Pessoa pessoa WHERE pessoa.codigo > 1 ")

})
public class Pessoa extends GenericDomain {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "nome", length = 60, nullable = false)
	private String nome;

	@CPF(message = "O CPF informado não é valido!")
	@Column(name = "cpf", length = 14, nullable = false, unique=true)
	private String cpf;

	@Column(name = "numero_rg", length = 15)
	private String numeroRG;

	@Column(name = "orgao_emissor", length = 6)
	private String orgaoEmissor;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Sexo sexo;
	
	@Email(message = "O email digitado não é valido!")
	@Column(name="email", length = 100, nullable = false)
	private String email;

	@Column(name = "data_emissao_rg")
	@Temporal(TemporalType.DATE)
	private Date dataEmissaoRG;

	@Column(name = "data_nascimento", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@Column(name = "telefone", nullable = true)
	private String  telefone;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Endereco endereco;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getDataEmissaoRG() {
		return dataEmissaoRG;
	}

	public void setDataEmissaoRG(Date dataEmissaoRG) {
		this.dataEmissaoRG = dataEmissaoRG;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getNumeroRG() {
		return numeroRG;
	}

	public void setNumeroRG(String numeroRG) {
		this.numeroRG = numeroRG;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	

}

