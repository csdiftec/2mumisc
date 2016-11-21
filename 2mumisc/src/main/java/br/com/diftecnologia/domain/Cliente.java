package br.com.diftecnologia.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="cliente")
public class Cliente extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="data_cadastro", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;
	
	@Column(name="status", nullable = false)
	private Boolean status;
	
	@OneToOne
	@JoinColumn(name="pessoa", nullable = false,unique=true)
	private Pessoa pessoa;
	
	@OneToOne // Indica qual funcionário cadastrou o cliente
	@JoinColumn(nullable = false)
	private Funcionario funcionario;

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getLiberado() {
		return status;
	}

	public void setLiberado(Boolean liberado) {
		this.status = liberado;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}

