package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="fabricante")
@NamedQueries({
//	@NamedQuery(name = "Fabricante.verificaCNPJ", query = "SELECT fabricante FROM Fabricante fabricante WHERE fabricante.cnpj = :cnpj"),
//	@NamedQuery(name = "Fabricante.verificaProduto", query = "SELECT fabricante FROM Fabricante fabricante WHERE fabricante.codigo = :codigo AND fabricante IN (SELECT produto.fabricante FROM Produto produto)") // Query que verifica se o fabricante está em algum produto
})
public class Fabricante extends Empresa {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="autorizacao_Anvisa", length = 7)
	private Integer autorizacaoAnvisa;
	
	@Transient
	private String telefone; // Usado para salvar vários telefones para o fabricante

	public Integer getAutorizacaoAnvisa() {
		return autorizacaoAnvisa;
	}
	
	public void setAutorizacaoAnvisa(Integer autorizacaoAnvisa) {
		this.autorizacaoAnvisa = autorizacaoAnvisa;
	}
	
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
		
}
