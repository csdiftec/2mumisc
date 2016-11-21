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
@Table(name="venda")
@NamedQueries({
	@NamedQuery(name = "Venda.verificaUltimoNumeroNota", query = "SELECT max(venda.numeroDocumentoFiscal) FROM Venda venda"),
	@NamedQuery(name = "Venda.buscaVendaPorTerminalFuncionario", query = "SELECT venda FROM Venda venda "
			+ "WHERE venda.dataVenda > :dataAbertura and venda.funcionario =:funcionarioLogado"),
	@NamedQuery(name = "Venda.buscaVendaPorTerminalFuncionarioAvista", query = "SELECT venda FROM Venda venda "
			+ "WHERE venda.dataVenda > :dataAbertura and venda.funcionario =:funcionarioLogado "
			+ "AND venda.tipoPagamento.codigo=1")
})
public class Venda extends GenericDomain {
	
private static final long serialVersionUID = 1L;
	
	@Column(name="valor_total", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorTotal;
	
	@Column(name="numero_documento_fiscal", length = 48, nullable = false)
	private Long numeroDocumentoFiscal;
	
	@Column(name="data_venda", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionarioVenda;
	
	@ManyToOne // Cliente que efetuou a compra
	@JoinColumn(nullable = false)
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private TipoPagamento tipoPagamento;
	@Transient
	public Date dataInicial;
	@Transient
	public Date dataFinal;
	
	@Transient
	public String vendedor;
	
	@Transient
	public String DescricaoFormaPagamento;
	
	@Transient
	public String DescricaoTipoPagamento;
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Long getNumeroDocumentoFiscal() {
		return numeroDocumentoFiscal;
	}

	public void setNumeroDocumentoFiscal(Long numeroDocumentoFiscal) {
		this.numeroDocumentoFiscal = numeroDocumentoFiscal;
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

	public Funcionario getFuncionarioVenda() {
		return funcionarioVenda;
	}

	public void setFuncionarioVenda(Funcionario funcionarioVenda) {
		this.funcionarioVenda = funcionarioVenda;
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

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getDescricaoFormaPagamento() {
		return DescricaoFormaPagamento;
	}

	public void setDescricaoFormaPagamento(String descricaoFormaPagamento) {
		DescricaoFormaPagamento = descricaoFormaPagamento;
	}

	public String getDescricaoTipoPagamento() {
		return DescricaoTipoPagamento;
	}

	public void setDescricaoTipoPagamento(String descricaoTipoPagamento) {
		DescricaoTipoPagamento = descricaoTipoPagamento;
	}

}

