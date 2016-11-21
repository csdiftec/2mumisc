package br.com.diftecnologia.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="produto")
@NamedQueries({
	@NamedQuery(name = "Produto.verificaCodigoBarraComDescricao", 
			query = "SELECT produto FROM Produto produto WHERE produto.codigoBarras = :codigoBarras and produto.descricao = :descricao"),
	@NamedQuery(name = "Produto.listaTodosProdutoEmEstoque",
	query = "SELECT produto FROM Produto produto "),
	@NamedQuery(name = "Produto.listaProdutoEmEstoque",
	query = "SELECT produto FROM Produto produto WHERE produto.quantidadeEstoque > 0"),
	@NamedQuery(name = "Produto.listaProdutoEmEstoqueNaQtdCritica",
	query = "SELECT produto FROM Produto produto WHERE produto.quantidadeEstoque <= produto.quantidadeCritica and "
			+ "quantidadeCritica > 0")

})
public class Produto extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="codigo_barras", length = 15, nullable = false)
	private Long codigoBarras;
	
	@Column(name="descricao", length = 120, nullable = false)
	private String descricao;
	
	@Column(name="valor", nullable = false, precision = 10, scale = 7)
	private BigDecimal valor;	
	
	@Column(name="situacao", nullable = false)
	private Boolean situacao;
	
	@Column(name="quantidade_estoque", length = 11, nullable = false)
	private Double quantidadeEstoque;
	
	@Column(name="quantidade_critica", length = 2, nullable = false)
	private Integer quantidadeCritica;
	
	@ManyToOne
  	@JoinColumn(nullable = false)
 	public Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private UnidadeMedida unidadeMedida;
	
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

	public Double getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Double quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Integer getQuantidadeCritica() {
		return quantidadeCritica;
	}

	public void setQuantidadeCritica(Integer quantidadeCritica) {
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

}
