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

/*
 * Classe que irá registrar como pago as despesas extras
 * @author Gabriel Zanlorenzi
 * @since Classe criada em 16/03/2016
 */

@Entity
@Table(name="pagamento_despesa")
public class PagamentoDespesa extends GenericDomain {

	private static final long serialVersionUID = 1L;
	
	@Column(name="valor_pago", nullable = false, precision = 9, scale = 2)
	private BigDecimal valorPago;
	
	@Column(name="valor_desconto", length = 2, nullable = false)
	private BigDecimal valorDesconto;
	
	@Column(name="valor_juros", nullable = false, precision = 4, scale = 2)
	private BigDecimal valorJuros;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_pagamento")
	private Date dataPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private RegistroDespesas registroDespesas;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@Transient
	public Date dataInicial;
	@Transient
	public Date dataFinal;
	
	@Transient
	public long numeroDespesaRegistrada;

	
	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
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

	public RegistroDespesas getRegistroDespesas() {
		return registroDespesas;
	}

	public void setRegistroDespesas(RegistroDespesas registroDespesas) {
		this.registroDespesas = registroDespesas;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public RegistroDespesas getRegistroDespesa() {
		return registroDespesas;
	}

	public void setRegistroDespesa(RegistroDespesas registroDespesas) {
		this.registroDespesas = registroDespesas;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
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

	public long getNumeroDespesaRegistrada() {
		return numeroDespesaRegistrada;
	}

	public void setNumeroDespesaRegistrada(long numeroDespesaRegistrada) {
		this.numeroDespesaRegistrada = numeroDespesaRegistrada;
	}


}
