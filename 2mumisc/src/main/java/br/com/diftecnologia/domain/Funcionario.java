package br.com.diftecnologia.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="funcionario")
@NamedQueries({
		@NamedQuery(name = "Funcionario.verificaCPF", query = "SELECT funcionario FROM Funcionario funcionario WHERE funcionario.pessoa.cpf = :cpf"),
		@NamedQuery(name="Funcionario.autenticar",query="SELECT funcionario FROM Funcionario funcionario WHERE funcionario.pessoa.cpf = :cpf AND funcionario.senha=:senha"),
		@NamedQuery(name="Funcionario.listaFuncionarios",query="SELECT funcionario FROM Funcionario funcionario WHERE funcionario.codigo > 2 AND funcionario.tipo != 'Administrador' ")
//		@NamedQuery(name="Funcionario.existenciaAdministrador",query="SELECT count(funcionario) FROM Funcionario funcionario WHERE funcionario.pessoa.cpf =326.366.326-70 AND funcionario.senha=0cc7a08b2338aa6c2c6e7506feecdd56")
		
})
public class Funcionario extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="carteira_trabalho", length = 15, nullable = false)
	private String carteiraTrabalho;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_admissao", nullable = false)
	private Date dataAdmissao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_saida", nullable = true)
	private Date dataSaida;
	
	@Column(name = "senha", length = 32, nullable = false)
	private String senha;
	
	@Column(name = "tipo", length = 15, nullable = false)
	private String tipo;

	@Column(name = "situacao", nullable = false)
	private Boolean situacao;
	
	@Column(name = "flag_situacao_caixa", nullable = false) 
	private Integer  flagSituacaoCaixa;
	
	@OneToOne
	@JoinColumn(nullable = false)
	private Pessoa pessoa;

	public String getCarteiraTrabalho() {
		return carteiraTrabalho;
	}

	public void setCarteiraTrabalho(String carteiraTrabalho) {
		this.carteiraTrabalho = carteiraTrabalho;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	
	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}



	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

	public Integer getFlagSituacaoCaixa() {
		return flagSituacaoCaixa;
	}

	public void setFlagSituacaoCaixa(Integer flagSituacaoCaixa) {
		this.flagSituacaoCaixa = flagSituacaoCaixa;
	}



}