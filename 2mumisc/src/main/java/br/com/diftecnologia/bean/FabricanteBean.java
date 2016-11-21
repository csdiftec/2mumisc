package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.event.CloseEvent;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.dao.FabricanteDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;
import br.com.diftecnologia.domain.Fabricante;
import br.com.diftecnologia.domain.Telefone;

@ManagedBean
@ViewScoped
public class FabricanteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Fabricante fabricante;
	private Fabricante cnpjFabricante;
	private Endereco endereco;
	private Telefone telefone;
	private List<Telefone> telefones;
	private List<Fabricante> fabricantes;
	private Estado estado;
	private List<Estado> estados;
	private List<Cidade> cidades;
	
	private String acao;
	private Long codigo;

	public void novo() {
		try {
			endereco = new Endereco();
			fabricante = new Fabricante();
			estado = new Estado();
			fabricante.setEndereco(getEndereco());

			listarEstados();

			cidades = new ArrayList<>();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo fabricante!");
			erro.printStackTrace();
		}
	}

	private void listarEstados() {
		try {
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os estados!");
			erro.printStackTrace();
		}
	}
	
	// Carrega o cadastro do Fabricante (Usado para a página de detalhes / Editar)
	public void carregarCadastro() {

		try {
			FabricanteDAO fabricanteDAO = new FabricanteDAO();
			fabricante = fabricanteDAO.buscar(codigo);

			estado = fabricante.getEndereco().getCidade().getEstado();
			endereco = fabricante.getEndereco();

			listarEstados();

			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar exibir os dados do fabricante selecionado!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	private void listar() {
		try {
			FabricanteDAO fabricanteDAO = new FabricanteDAO();
			fabricantes = fabricanteDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os fabricantes!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			if (verificarCnpj()) {
				Messages.addFlashGlobalError(cnpjFabricante.getCnpj() + " já existe no sistema!");

			} else {
			
		/*		FabricanteDAO fabricanteDAO = new FabricanteDAO();
	
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			//	Funcionario funcionario = funcionarioDAO.buscar(1L);
			//	fabricante.setFuncionario(funcionario);
				fabricante.setDataCadastro(new Date());
				
				salvarEndereco();
	
				fabricanteDAO.merge(fabricante);
	
				novo();
				listar();
				listarEstados();			
	*/
				Messages.addGlobalInfo("Fabricante salvo com sucesso!");
			}
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o fabricante!");
			erro.printStackTrace();
		}
	}
	
	// método que verifica se o cnpj já existe no banco de dados
	public boolean verificarCnpj() {
		FabricanteDAO fabricanteDAO = new FabricanteDAO();
		cnpjFabricante = fabricanteDAO.verificarCnpj(fabricante.getCnpj());

		if (cnpjFabricante != null) {
			return true;
		} else {
			return false;
		}
	}
	
	// Método que salva o endereço antes do fornecedor
	public void salvarEndereco() {
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			enderecoDAO.salvar(endereco);

		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar dados do endereço!");
			erro.printStackTrace();
		}
	}
	
	// Método que Edita o endereço antes do fornecedor
	public void editarEndereco() {
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			enderecoDAO.merge(endereco);

		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar dados do endereço!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		try {
			if (verificarCnpj()) {
				Messages.addFlashGlobalError(cnpjFabricante.getCnpj() + " já existe no sistema!");

			} else {
				
		/*		FabricanteDAO fabricanteDAO = new FabricanteDAO();

				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				Funcionario funcionario = funcionarioDAO.buscar(1L);
				((Object) fabricante).setFuncionario(funcionario);

				editarEndereco();

				fabricanteDAO.merge(fabricante);
*/
				novo();
				listar();
				listarEstados();

				Messages.addGlobalInfo("Fabricante salvo com sucesso!");
			}
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar alterar os dados do fabricante!");
			erro.printStackTrace();
		}
	}

	public void excluir(ActionEvent evento) {
		try {
			fabricante = (Fabricante) evento.getComponent().getAttributes().get("fabricanteSelecionado");

			FabricanteDAO fabricanteDAO = new FabricanteDAO();
			Fabricante f = fabricanteDAO.verificarProduto(fabricante.getCodigo());
			
			if (f != null) {
				Messages.addFlashGlobalError(
						fabricante.getNomeFantasia() + " Não pode ser excluído, pois há um produto vinculado a ele!");
			} else {
				endereco = fabricante.getEndereco();

				EnderecoDAO enderecoDAO = new EnderecoDAO();
				
				fabricanteDAO.excluir(fabricante);
				enderecoDAO.excluir(endereco);

				listar();
				novo();
				listarEstados();

				Messages.addGlobalInfo("Fabricante removido com sucesso!");
			}
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o fabricante!");
			erro.printStackTrace();
		}
	}

	public void popular() {
		try {
			if (estado != null) {
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
			} else {
				cidades = new ArrayList<>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades");
			erro.printStackTrace();
		}
	}

	/*public void adicionar(ActionEvent evento) {

		Fabricante fabricante = (Fabricante) evento.getComponent().getAttributes().get("telefoneSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < telefones.size(); posicao++) {
			if (telefones.get(posicao).getFabricante().equals(fabricante)) {
				achou = posicao;
			}
		}

		if (achou < 0) {
			Telefone telefone = new Telefone();
			telefone.setFabricante(fabricante);
			telefone.setNumeroTelefone(telefone.getNumeroTelefone());

			telefones.add(telefone);
		} else {
			Telefone telefone = telefones.get(achou);

		}
	}

	public void remover(ActionEvent evento) {
		Telefone telefone = (Telefone) evento.getComponent().getAttributes().get("itemSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < telefones.size(); posicao++) {
			if (telefones.get(posicao).getFabricante().equals(telefone.getFabricante())) {
				achou = posicao;
			}
		}

		if (achou > -1) {
			telefones.remove(achou);
		}
	}*/
	
	// método que fecha a caixa de diálogo caso o formulário conseguir salvar
	public void fecharDialogo(CloseEvent event) {
		novo();
		limparCampos(event.getComponent());
	}

	public void limparCampos(UIComponent component) {
		if (component instanceof EditableValueHolder) {
			EditableValueHolder evh = (EditableValueHolder) component;
			evh.setSubmittedValue(null);
			evh.setValue(null);
			evh.setLocalValueSet(false);
			evh.setValid(true);
		}
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				limparCampos(child);
			}
		}
	}

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}
	
	public Fabricante getCnpjFabricante() {
		return cnpjFabricante;
	}

	public void setCnpjFabricante(Fabricante cnpjFabricante) {
		this.cnpjFabricante = cnpjFabricante;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}

	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

}
