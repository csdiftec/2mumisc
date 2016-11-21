package br.com.diftecnologia.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Table(name="importaNFE")
public class ImportarNFE extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	@Transient
	@Column(name="codigo_barras", length = 15, nullable = false)
	private Long codigoBarras;
	
	@Transient
	@Column(name="descricao", length = 120, nullable = false)
	private String descricao;
	
	@Transient
	@Column(name="nome_fantasia", length = 120, nullable = false)
	private String nomeFantasia;
	
	@Transient
	@Column(name="valor", nullable = false, precision = 6, scale = 2)
	private BigDecimal valor;	
		
	@Transient
	@Column(name="situacao", nullable = false)
	private Boolean situacao;
	
	@Transient
	@Column(name="quantidade_estoque", length = 4, nullable = false)
	private Integer quantidadeEstoque;
	
	@Transient
	@Column(name="cnpj")
	private String  cnpj;
	
	@Transient
	@Column(name="quantidade_critica", length = 2, nullable = false)
	private Short quantidadeCritica;
	
	@Transient
	@ManyToOne
	@JoinColumn(nullable = false)
	private Funcionario funcionario;
	
	@Transient
	@ManyToOne
	@JoinColumn(nullable = false)
	private UnidadeMedida unidadeMedida;
	
	@Transient
	@ManyToOne
	@JoinColumn(nullable = false)
	private Fabricante fabricante;

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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getSituacao() {
		return situacao;
	}

	public void setSituacao(Boolean situacao) {
		this.situacao = situacao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Short getQuantidadeCritica() {
		return quantidadeCritica;
	}

	public void setQuantidadeCritica(Short quantidadeCritica) {
		this.quantidadeCritica = quantidadeCritica;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public void setCodigoBarras(String cean) {
		// TODO Auto-generated method stub
		
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	

}
