package br.com.diftecnologia.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="duplicata")
@NamedQueries({
	@NamedQuery(name = "Duplicata.buscaPorCodigo",
			query = "SELECT duplicata FROM Duplicata duplicata WHERE duplicata.codigo =:codigo"),
	@NamedQuery(name = "Duplicata.buscaUltimasDuplicatasGeradas", 
			query = "SELECT duplicata FROM Duplicata duplicata WHERE venda.codigo = :cod"),
	@NamedQuery(name = "Duplicata.buscaDuplicatas", 
	query = "SELECT duplicata FROM Duplicata duplicata "),
	@NamedQuery(name = "Duplicata.buscaDuplicatasVencidas", 
	query = "SELECT duplicata FROM Duplicata duplicata  where duplicata.dataVencimento > :dataAtual")

})
public class Duplicata extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="numero_parcela", length = 2, nullable = false)
	private Short numeroParcela;
	
	@Column(name="total_parcelas", length = 2, nullable = false)
	private Short totalParcelas;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_emissao", nullable = false)
	private Date dataEmissao;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_vencimento", nullable = false)
	private Date dataVencimento;
	
	@Column(name="valor_parcela", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorParcela;
	
	@Column(name="status", nullable = false, length=8)
	private String status;
	
	@Transient
	@Column(name="valor_desconto", precision = 13, scale = 2)
	private BigDecimal valorDesconto;
	
	@Transient
	@Column(name="valor_juros",  precision = 13, scale = 2)
	private BigDecimal valorJuros;
	
	@Transient
	@Column(name="valor_pago",  precision = 13, scale = 2)
	private BigDecimal valorPago;
	
	@Column(name="valor_total_atual",  precision = 13, scale = 2)
	private BigDecimal valorTotalAtual;
	
	@Transient
	@Temporal(TemporalType.DATE)
	@Column(name="data_ultimo_pagamento", nullable = false)
	private Date dataUltimoPagamento;
//	@ManyToOne // Venda e compra podem ser nulo, pois a parcela pertence a uma compra ou venda
//	private Compra compra;
	
	@ManyToOne
	private Venda venda;
	
	@Transient
	public Date dataInicial;
	@Transient
	public Date dataFinal;
	
	@Transient
	public String nomeCliente;
	
	public Short getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Short numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Short getTotalParcelas() {
		return totalParcelas;
	}

	public void setTotalParcelas(Short totalParcelas) {
		this.totalParcelas = totalParcelas;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}



	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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

	public BigDecimal getValorTotalAtual() {
		return valorTotalAtual;
	}

	public void setValorTotalAtual(BigDecimal valorTotalAtual) {
		this.valorTotalAtual = valorTotalAtual;
	}

	public Date getDataUltimoPagamento() {
		return dataUltimoPagamento;
	}

	public void setDataUltimoPagamento(Date dataUltimoPagamento) {
		this.dataUltimoPagamento = dataUltimoPagamento;
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

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	


	
}

