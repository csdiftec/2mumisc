package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@SuppressWarnings("serial")
@Entity
@Table(name="fornecedor")
@NamedQueries({
	@NamedQuery(name = "Fornecedor.verificaCNPJ", query = "SELECT fornecedor FROM Fornecedor fornecedor WHERE fornecedor.cnpj = :cnpj"),
	@NamedQuery(name = "Fornecedor.verificaCompra", query = "SELECT fornecedor FROM Fornecedor fornecedor WHERE fornecedor.codigo = :codigo AND fornecedor IN (SELECT compra.fornecedor FROM Compra compra)"), // Query que verifica se o fornecedor está em alguma compra
	@NamedQuery(name = "Fornecedor.listaFornecedor", query = "SELECT fornecedor FROM Fornecedor fornecedor")
})
public class Fornecedor extends Empresa {

	@Size(min=1, max=60, message="Tamanho inválido para o campo Responsável Legal(1 - 60)")
	@Column(name="responsavel_legal", length = 60, nullable = false)
	private String responsavelLegal;
	
	@Column(name="status", nullable = false)
	private Boolean status;
	
	public String getResponsavelLegal() {
		return responsavelLegal;
	}

	public void setResponsavelLegal(String responsavelLegal) {
		this.responsavelLegal = responsavelLegal;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}



	
}
