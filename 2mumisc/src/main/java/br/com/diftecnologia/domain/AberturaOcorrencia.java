package br.com.diftecnologia.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="abertura_ocorrencia")
@NamedQueries({
	@NamedQuery(name = "AberturaOcorrencia.verificaOcorrenciaExistePorFechamento", 
			query = "SELECT count(aberturaOcorrencia)  FROM AberturaOcorrencia  aberturaOcorrencia "
					+ "  where aberturaOcorrencia.caixa.codigo =:caixaCodigo")
})
public class AberturaOcorrencia extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@DecimalMin(value = "-9999.99", message = "Informe um valor maior do que -10 mil para o campo 'Diferença'!")
	@DecimalMax(value = "9999.99", message = "Informe um valor menor que 10 mil para o campo 'Diferença'!")
	@NotNull(message = "O campo 'Diferença'  obrigatório!")
	@Column(name="diferenca_registrada", nullable = false, precision = 6, scale = 2)
	private BigDecimal diferencaRegistrada;

	@Temporal(TemporalType.DATE)
	@Column(name="data_abertura", nullable = false)
	private Date dataAbertura;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario_testemunha;
	
	@Column(name="observacao_abertura", length = 100)
	private String observacaoAbertura;
	
	
	
	@Column(name="status", nullable = false)
	private Boolean status;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(nullable = true)
	private Caixa caixa;

	public BigDecimal getDiferencaRegistrada() {
		return diferencaRegistrada;
	}

	public void setDiferencaRegistrada(BigDecimal diferencaRegistrada) {
		this.diferencaRegistrada = diferencaRegistrada;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	

	public Funcionario getFuncionario_testemunha() {
		return funcionario_testemunha;
	}

	public void setFuncionario_testemunha(Funcionario funcionario_testemunha) {
		this.funcionario_testemunha = funcionario_testemunha;
	}

	public String getObservacaoAbertura() {
		return observacaoAbertura;
	}

	public void setObservacaoAbertura(String observacaoAbertura) {
		this.observacaoAbertura = observacaoAbertura;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	
}
