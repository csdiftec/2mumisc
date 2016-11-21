package br.com.diftecnologia.bean;



import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.ClienteDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.PessoaDAO;
import br.com.diftecnologia.dao.SexoDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Cliente;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.Pessoa;
import br.com.diftecnologia.domain.Sexo;
import br.com.diftecnologia.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	
	private Cliente cliente;
	private Endereco endereco;
	private Estado estado;
	private Pessoa pessoa;

	private List<Cliente> clientes;
	private List<Pessoa> pessoas;
	private List<Sexo> sexos;
	private List<Estado> estados;
	private List<Cidade> cidades;
	
	private String acao;
	private Long codigo;

	@PostConstruct
	public void listar() {
		try {
			ClienteDAO clienteDAO = new ClienteDAO();
			clientes = clienteDAO.listar("dataCadastro");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os clientes!");
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
	
	private void listarEstados() {
		try {
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os estados!");
			erro.printStackTrace();
		}
	}

	public void novo() {
		try {
			cliente = new Cliente();

			listarPessoas();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar criar um novo cliente!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			ClienteDAO clienteDAO = new ClienteDAO();
			cliente.setDataCadastro(new Date());
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 
			System.out.println("Funcionario Logado no cadastro cliente..... "+autenticacaoBean.getFuncionarioLogado().getCodigo());
			cliente.setFuncionario(funcionario);
			clienteDAO.merge(cliente);

			cliente = new Cliente();

			clientes = clienteDAO.listar("dataCadastro");

			listarPessoas();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");

			Messages.addGlobalInfo("Cliente salvo com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Dados do cliente não pode ser salvo.");
			Messages.addFlashGlobalError("Verifique a duplicidade dos  dados");
			erro.printStackTrace();
		}
	}
	public void salvarEdicao() {
		try {
			ClienteDAO clienteDAO = new ClienteDAO();
	
			cliente.setPessoa(pessoa);
System.err.println("Pessoa................."+pessoa);
System.err.println("Cliente................."+cliente);

			clienteDAO.merge(cliente);

			cliente = new Cliente();

			clientes = clienteDAO.listar("dataCadastro");

			listarPessoas();
			 RequestContext.getCurrentInstance().execute("PF('dialogoEdit').hide();");

			Messages.addGlobalInfo("Cliente editado com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Edição dos dados não realizada.");
			Messages.addFlashGlobalError("Verifique a duplicidade dos  dados");
			erro.printStackTrace();
		}
	}
	
	public void editar(ActionEvent evento) {

		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			cliente = (Cliente) evento.getComponent().getAttributes().get("clienteSelecionado");			
			System.err.println("Cliente selecionado......."+cliente);
			listarPessoas();
			
			pessoa = pessoaDAO.buscar(cliente.getPessoa().getCodigo());
			cliente.setPessoa(pessoa);
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar o cliente!");
			erro.printStackTrace();
		}
	}
	
	public void alterar() {
		try {
			salvarEndereco();
			ClienteDAO clienteDAO = new ClienteDAO();
			cliente.getPessoa().setEndereco(endereco);
			cliente.getPessoa().setSexo(cliente.getPessoa().getSexo());
			clienteDAO.merge(cliente);

			novo();
			listar();

			Messages.addGlobalInfo("Pessoa salva com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar alterar os dados do cliente!");
			erro.printStackTrace();
		}
	}
	
	public void salvarEndereco() {
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			enderecoDAO.salvar(endereco);

		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar dados do endereço!");
			erro.printStackTrace();
		}
	}
	
	// Carrega o cadastro do Fabricante (Usado para a página de detalhes /
	// Editar)
	public void carregarCadastro() {

		try {
			ClienteDAO clienteDAO = new ClienteDAO();
			cliente = clienteDAO.buscar(codigo);

			estado = cliente.getPessoa().getEndereco().getCidade().getEstado();
			endereco = cliente.getPessoa().getEndereco();
			pessoa = cliente.getPessoa();

			listarEstados();
			listarPessoas();

			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
			
			System.out.println("-------------------------------------");
			System.out.println("\nCliente: " + cliente);
			System.out.println("\nEndereço: " + cliente);
			System.out.println("\nPessoa: " + pessoa);
			System.out.println("-------------------------------------");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar exibir os dados do cliente selecionado!");
			erro.printStackTrace();
		}
	}
	
	private void listarPessoas() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar exibir os dados pessoais do cliente selecionado!");
			erro.printStackTrace();
		}

	}
	
	public void popular() {
		try {
			if (estado != null) {
				
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
				System.err.println("Busca cidade metodo popular...."+cidades);
endereco.setCidade(cidades.get(0));
			} else {
				cidades = new ArrayList<>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades");
			erro.printStackTrace();
		}
	}
	//imprimirClientePorCodigo
	public void imprimirClientePorCodigo(ActionEvent evento) {
	try {					
		Cliente clienteSelecionado = (Cliente) evento.getComponent().getAttributes().get("clienteSelecionado");
		System.out.println( "	getPessoa().getCodigo()..................:"+	clienteSelecionado.getPessoa().getCodigo());
		String caminho = Faces.getRealPath("//report//relatorioClientePorCodigo.jasper");

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("COD_PESSOA",clienteSelecionado.getPessoa().getCodigo());
     
		Connection conexao = HibernateUtil.getConexao();
        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
      
        JasperViewer.viewReport(jp, false);
     
	System.out.println( "	clienteSelecionado.getCodigo()..................:"+	clienteSelecionado.getPessoa().getCodigo());

	} catch (JRException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
		erro.printStackTrace();
		System.err.println("Erro.......................................:"+erro);
	}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
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
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}
	
}