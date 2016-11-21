package br.com.diftecnologia.domain;

import java.math.BigDecimal;
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
import javax.persistence.Transient;

@Entity
@Table(name="compra")
@NamedQueries({
	@NamedQuery(name = "Compra.buscaUltimaCompra",query = "SELECT max(compra) FROM Compra compra")
})
public class Compra extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="valor_total", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorTotal;
	
	@Column(name="numero_documento_fiscal", length = 48, nullable = false)
	private String numeroDocumentoFiscal;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_compra",nullable = false)
	private Date dataCompra;
		
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private TipoPagamento tipoPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Fornecedor fornecedor;

	@Transient
	public Date dataInicial;
	@Transient
	public Date dataFinal;

	@Transient
	public String flagFornecedorSelecionado;

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getNumeroDocumentoFiscal() {
		return numeroDocumentoFiscal;
	}

	public void setNumeroDocumentoFiscal(String numeroDocumentoFiscal) {
		this.numeroDocumentoFiscal = numeroDocumentoFiscal;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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

	public String getFlagFornecedorSelecionado() {
		return flagFornecedorSelecionado;
	}

	public void setFlagFornecedorSelecionado(String flagFornecedorSelecionado) {
		this.flagFornecedorSelecionado = flagFornecedorSelecionado;
	}



	
}