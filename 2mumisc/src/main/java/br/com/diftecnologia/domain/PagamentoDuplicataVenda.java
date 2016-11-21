package br.com.diftecnologia.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="pagamento_duplicata_venda")
public class PagamentoDuplicataVenda extends GenericDomain {
	
	private static final long serialVersionUID = 1L;

	@Column(name="valor_desconto", length = 2, nullable = false)
	private BigDecimal valorDesconto;
	
	@Column(name="valor_juros", nullable = false, precision = 4, scale = 2)
	private BigDecimal valorJuros;
	
	@Column(name="valor_pago", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorPago;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_pagamento", nullable = false)
	private Date dataPagamento;
	
	@OneToOne
	@JoinColumn(nullable = false)
	private Duplicata duplicata;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;



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

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Duplicata getDuplicata() {
		return duplicata;
	}

	public void setDuplicata(Duplicata duplicata) {
		this.duplicata = duplicata;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}

