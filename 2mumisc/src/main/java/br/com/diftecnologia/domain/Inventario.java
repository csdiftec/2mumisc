package br.com.diftecnologia.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="inventario")
public class Inventario extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="codigo_barras", length = 15, nullable = false)
	private Long codigoBarras;
	
	@Column(name="descricao", length = 120, nullable = false)
	private String descricao;
	
	@Column(name="motivo", nullable = false, length=100)
	private String motivo;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	
	@Column(name="quantidade_atual", length = 3, nullable = false)
	private Integer quantidadeAtual;
	
	@Column(name="nova_quantidade", length = 3, nullable = false)
	private Integer novaQuantidade;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_modificacao", nullable = false)
	private Date dataModificacao;
	



	public Integer getQuantidadeAtual() {
		return quantidadeAtual;
	}

	public void setQuantidadeAtual(Integer quantidadeAtual) {
		this.quantidadeAtual = quantidadeAtual;
	}

	public Integer getNovaQuantidade() {
		return novaQuantidade;
	}

	public void setNovaQuantidade(Integer novaQuantidade) {
		this.novaQuantidade = novaQuantidade;
	}


	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Long getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(Long codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}

