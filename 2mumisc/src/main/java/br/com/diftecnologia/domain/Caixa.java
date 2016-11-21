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

@Entity
@Table(name="caixa")
@NamedQueries({
	@NamedQuery(name = "Caixa.caixaAbertoProfissionalLogado", 
			query = "SELECT max(caixa) FROM Caixa caixa "
					+ "WHERE caixa.funcionario.codigo = :codigo "),
					
})
public class Caixa extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_abertura", nullable = false)
	private Date dataAbertura;
	
	@Column(name="valor_inicial", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorInicial;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_fechamento", nullable = true)
	private Date dataFechamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private TerminalAtendimento terminalAtendimento;

	@Column(name="valor_total_fechamento", nullable = true, precision = 13, scale = 2)
	private BigDecimal valorTotalFechamento;
	
	@Column(name="valor_total_venda", nullable = true, precision = 13, scale = 2)
	private BigDecimal valorTotalVenda;
	
	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public TerminalAtendimento getTerminalAtendimento() {
		return terminalAtendimento;
	}

	public void setTerminalAtendimento(TerminalAtendimento terminalAtendimento) {
		this.terminalAtendimento = terminalAtendimento;
	}

	public BigDecimal getValorTotalFechamento() {
		return valorTotalFechamento;
	}

	public void setValorTotalFechamento(BigDecimal valorTotalFechamento) {
		this.valorTotalFechamento = valorTotalFechamento;
	}

	public BigDecimal getValorTotalVenda() {
		return valorTotalVenda;
	}

	public void setValorTotalVenda(BigDecimal valorTotalVenda) {
		this.valorTotalVenda = valorTotalVenda;
	}
	
	
}

