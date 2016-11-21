package br.com.diftecnologia.domain;

/*
@Entity
@Table(name="telefone")
@NamedQueries({
	@NamedQuery(name = "Telefone.listarPorFFP", query = "SELECT telefone FROM Telefone telefone WHERE telefone.fornecedor =:codigoFornecedor")
})*/
public class Telefone extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * @Column(name="numero_telefone", length = 14, nullable = false)
	 
	private String numeroTelefone;
	
	@ManyToOne
	private Pessoa pessoa;
	
	@ManyToOne
	private Fornecedor fornecedor;
	
	@ManyToOne
	private Fabricante fabricante;

	public String getNumeroTelefone() {
		return numeroTelefone;
	}

	public void setNumeroTelefone(String numeroTelefone) {
		this.numeroTelefone = numeroTelefone;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}
	*/
}
