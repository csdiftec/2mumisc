package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.dao.PessoaDAO;
import br.com.diftecnologia.dao.SexoDAO;
import br.com.diftecnologia.dao.TelefoneDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;
import br.com.diftecnologia.domain.Pessoa;
import br.com.diftecnologia.domain.Sexo;
import br.com.diftecnologia.domain.Telefone;

@ManagedBean
@ViewScoped
public class PessoaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Pessoa pessoa;
	private Endereco endereco;
	private List<Pessoa> pessoas;
	private List<Telefone> telefones;
	private List<Sexo> sexos;
	private Estado estado;
	private List<Estado> estados;
	private List<Cidade> cidades;
	private Date currentDate = new Date();
	
	public void novo() {
		try {
			endereco = new Endereco();
			pessoa = new Pessoa();
			estado = new Estado();
//		pessoa.setEndereco(getEndereco());
		
			listarSexo();
			listarEstados();

			cidades = new ArrayList<>();
			
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar criar um novo registro'Pessoa'!");
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
	
	@PostConstruct
	public void listar() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listaPessoasExcetoAdmin();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as pessoas!");
			erro.printStackTrace();
		}
	}
	
	@PostConstruct
	public void listarSexo() {
		try {
			SexoDAO sexoDAO = new SexoDAO();
			sexos = sexoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os sexos!");
			erro.printStackTrace();
		}
	}
	
	public void salvar() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			Long quantidadeCPF = pessoaDAO.buscaQtdCpfCadastrado(pessoa.getCpf());
			System.out.println("Pessoa CPF..........."+pessoa.getCpf());
			if (quantidadeCPF >=1) {
				Messages.addFlashGlobalError( " CPF já existe no sistema!");

			} else {

	
			salvarEndereco();

			EnderecoDAO enderecoDAO = new EnderecoDAO();
			Long codigoEndereco = enderecoDAO.buscarUltimoRegistro();
			endereco = enderecoDAO.buscar(codigoEndereco);
			
								
			pessoa.setEndereco(endereco);
			pessoa.setSexo(pessoa.getSexo());
			
			
			pessoaDAO.merge(pessoa);

			novo();
			
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");

			Messages.addGlobalInfo("Pessoa salva com sucesso!");
			}
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar a pessoa!");
			erro.printStackTrace();
		}finally{
			listar();
		}
	}
	public void salvarEdicao() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			
			salvarEndereco();

			EnderecoDAO enderecoDAO = new EnderecoDAO();
			Long codigoEndereco = enderecoDAO.buscarUltimoRegistro();
			endereco = enderecoDAO.buscar(codigoEndereco);
			
								
			pessoa.setEndereco(endereco);
			pessoa.setSexo(pessoa.getSexo());
			
			
			pessoaDAO.merge(pessoa);

			novo();
			
			 RequestContext.getCurrentInstance().execute("PF('dialogoEdit').hide();");

			Messages.addGlobalInfo("Pessoa salva com sucesso!");
					} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar a pessoa!");
			Messages.addFlashGlobalError("Verifique a duplicidade dos  dados");
			erro.printStackTrace();
		}finally{
			listar();
		}
	}
	public void salvarEndereco() {
		try {
			EnderecoDAO enderecoDAO =  new EnderecoDAO();
			endereco.setCodigo(endereco.getCodigo());
			System.err.println("Metodo salvar endereco..............Codigo: "+endereco.getCodigo());
			endereco.setCep(endereco.getCep());
			System.err.println("Metodo salvar endereco..............Cep: "+endereco.getCep());
			endereco.setRua(endereco.getRua());
			System.err.println("Metodo salvar endereco..............Rua: "+endereco.getRua());
			endereco.setNumero(endereco.getNumero());
			System.err.println("Metodo salvar endereco..............Numero: "+endereco.getNumero());
			endereco.setComplemento(endereco.getComplemento());
			System.err.println("Metodo salvar endereco..............Complemento: "+endereco.getComplemento());
			endereco.setCidade(endereco.getCidade());
			System.err.println("Metodo salvar endereco..............CIdade: "+endereco.getCidade());
			endereco.setBairro(endereco.getBairro());
			System.err.println("Metodo salvar endereco..............Bairro: "+endereco.getBairro());
			
			enderecoDAO.merge(endereco);	
	
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar dados do endereço!");
			erro.printStackTrace();
		}
	}
	
	public void editar(ActionEvent evento) {
		try {
			pessoa = (Pessoa) evento.getComponent().getAttributes().get("pessoaSelecionada");
					listarSexo();
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			   endereco = enderecoDAO.buscar(pessoa.getEndereco().getCodigo());
				System.err.println("endereco codigo...:"+endereco.getCodigo());

				pessoa.setEndereco(endereco);
				
				estado = endereco.getCidade().getEstado();
				EstadoDAO estadoDAO = new EstadoDAO();
				System.err.println("endereco estado...:"+endereco.getCidade().getEstado());

				estados = estadoDAO.listar("nome");
				
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo()); 
			 
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa!");
			erro.printStackTrace();
		}
	}
	
	public void excluir(ActionEvent evento) {
		try {
			pessoa = (Pessoa) evento.getComponent().getAttributes().get("pessoaSelecionada");

			PessoaDAO pessoaDAO = new PessoaDAO();
		//ṕ/[klop	excluirTelefones();
			pessoaDAO.excluir(pessoa);

			listar();

			Messages.addGlobalInfo("Registro removido com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover a pessoa!");
			erro.printStackTrace();
		}
	}
	
	// Método que exclui os telefones antes da pessoa
	@SuppressWarnings("unused")
	private void excluirTelefones() {
		try {
			TelefoneDAO telefoneDAO = new TelefoneDAO();
			telefones = telefoneDAO.listarPorFFP(pessoa.getCodigo());
			for (Telefone telefone : telefones) {
				telefoneDAO.excluir(telefone);
			}
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Houve um erro ao tentar remover os telefones do fornecedor!");
			erro.printStackTrace();
		}
	}
	
	// Método que busca as cidades pelo estado selecionado, e popula a combo de cidades
	public void popular() {
		try {
			if (estado != null) {
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
				System.out.println("Total: " + cidades.size());
			} else {
				cidades = new ArrayList<>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades");
			erro.printStackTrace();
		}
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<Sexo> getSexos() {
		return sexos;
	}

	public void setSexos(List<Sexo> sexos) {
		this.sexos = sexos;
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


	public Date getCurrentDate() {
	    return currentDate;
	}
}
