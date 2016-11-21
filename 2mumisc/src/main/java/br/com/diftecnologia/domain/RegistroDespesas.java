package br.com.diftecnologia.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="registro_despesa")
public class RegistroDespesas extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="codigo_Despesa", length = 20, nullable = false)
	private Long codigoDespesa;
	
	@Column(name = "data_registro", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRegistro;
	
	@Column(name = "data_vencimento", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVencimento;
	
	@Column(name="valor_total", nullable = false, precision = 9, scale = 2)
	private BigDecimal valorTotal;
	
	@Column(length=100)
	private String observacoes;
	
	@Column(name="valor_total_atual", nullable = false, precision = 9, scale = 2)
	private BigDecimal valorTotalAtual;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Despesas despesas;
	

	@Transient
	@Column(name="valor_desconto", precision = 13, scale = 2)
	private BigDecimal valorDesconto;
	
	@Transient
	@Column(name="valor_juros",  precision = 13, scale = 2)
	private BigDecimal valorJuros;
	
	@Transient
	@Column(name="valor_pago",  precision = 13, scale = 2)
	private BigDecimal valorPago;
		
	@Transient
	@Temporal(TemporalType.DATE)
	@Column(name="data_ultimo_pagamento", nullable = false)
	private Date dataUltimoPagamento;
	
	@Transient
	public Date dataInicial;
	@Transient
	public Date dataFinal;

	@Transient
	public String flagDespesaSelecionada;
	
	@Transient
	public long flagPagamentoDespesaSelecionada;

	public Long getCodigoDespesa() {
		return codigoDespesa;
	}
	public void setCodigoDespesa(Long codigoDespesa) {
		this.codigoDespesa = codigoDespesa;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Despesas getDespesas() {
		return despesas;
	}

	public void setDespesas(Despesas despesas) {
		this.despesas = despesas;
	}
	public BigDecimal getValorTotalAtual() {
		return valorTotalAtual;
	}
	public void setValorTotalAtual(BigDecimal valorTotalAtual) {
		this.valorTotalAtual = valorTotalAtual;
	}
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public BigDecimal getValorJuros() {
		return valorJuros;
	}
	public void setValorJuros(BigDecimal valorJuros) {
		this.valorJuros = valorJuros;
	}
	public BigDecimal getValorPago() {
		return valorPago;
	}
	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}
	public Date getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}
	public void setDataUltimoPagamento(Date dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getFlagDespesaSelecionada() {
		return flagDespesaSelecionada;
	}
	public void setFlagDespesaSelecionada(String flagDespesaSelecionada) {
		this.flagDespesaSelecionada = flagDespesaSelecionada;
	}
	public long getFlagPagamentoDespesaSelecionada() {
		return flagPagamentoDespesaSelecionada;
	}
	public void setFlagPagamentoDespesaSelecionada(
			long flagPagamentoDespesaSelecionada) {
		this.flagPagamentoDespesaSelecionada = flagPagamentoDespesaSelecionada;
	}
	
}
