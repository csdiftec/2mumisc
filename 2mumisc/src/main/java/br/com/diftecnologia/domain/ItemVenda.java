
package br.com.diftecnologia.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="item_venda")
public class ItemVenda extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="quantidade", length = 3, nullable = false)
	private Integer quantidade;
	
	@Column(name="valor_parcial", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorParcial;
	
	@Column(name="porcentagem_do_desconto", length = 2, nullable = false)
	private Integer porcentagemDoDesconto;
	
	@Column(name="valor_do_desconto", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorDoDesconto;
	
	@Column(name="valor_com_desconto", nullable = false, precision = 13, scale = 2)
	private BigDecimal valorComDesconto;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Venda venda;


	public BigDecimal getValorParcial() {
		return valorParcial;
	}

	public void setValorParcial(BigDecimal valorParcial) {
		this.valorParcial = valorParcial;
	}


	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getPorcentagemDoDesconto() {
		return porcentagemDoDesconto;
	}

	public void setPorcentagemDoDesconto(Integer porcentagemDoDesconto) {
		this.porcentagemDoDesconto = porcentagemDoDesconto;
	}

	public BigDecimal getValorDoDesconto() {
		return valorDoDesconto;
	}

	public void setValorDoDesconto(BigDecimal valorDoDesconto) {
		this.valorDoDesconto = valorDoDesconto;
	}

	public BigDecimal getValorComDesconto() {
		return valorComDesconto;
	}

	public void setValorComDesconto(BigDecimal valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}

