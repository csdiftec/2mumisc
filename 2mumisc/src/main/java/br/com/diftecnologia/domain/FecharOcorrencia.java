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
@Table(name="fechar_ocorrencia")
public class FecharOcorrencia extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="observacao_fechamento", length = 100)
	private String observacaoFechamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_fechamento", nullable = false)
	private Date dataFechamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private AberturaOcorrencia aberturaOcorrencia;

	public String getObservacaoFechamento() {
		return observacaoFechamento;
	}

	public void setObservacaoFechamento(String observacaoFechamento) {
		this.observacaoFechamento = observacaoFechamento;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public AberturaOcorrencia getAbrirOcorrencia() {
		return aberturaOcorrencia;
	}

	public void setAbrirOcorrencia(AberturaOcorrencia aberturaOcorrencia) {
		this.aberturaOcorrencia = aberturaOcorrencia;
	}
	
}
