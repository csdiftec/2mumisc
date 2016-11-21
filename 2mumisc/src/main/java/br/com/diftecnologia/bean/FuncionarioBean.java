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
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.commons.codec.digest.DigestUtils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.PessoaDAO;
import br.com.diftecnologia.dao.SexoDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.Pessoa;
import br.com.diftecnologia.domain.Sexo;
import br.com.diftecnologia.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@ManagedBean
@ViewScoped
public class FuncionarioBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Funcionario funcionario;
	private Endereco endereco;
	private Estado estado;
	private Pessoa pessoa;

	private List<Funcionario> funcionarios;
	private List<Pessoa> pessoas;
	private List<Sexo> sexos;
	private List<Estado> estados;
	private List<Cidade> cidades;

	private String senhaNova;
	private Long codigo;

	private Funcionario cpfFuncionario;

	@PostConstruct
	public void listar() {
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listaFuncionarios();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os funcionários!");
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
			funcionario = new Funcionario();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar criar um novo funcionário!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			
			funcionario.setSenha(DigestUtils.md5Hex((funcionario.getSenha())));
			funcionario.setDataAdmissao(new Date());
			funcionario.setFlagSituacaoCaixa(0);

				funcionario.setSituacao(true );
			
			funcionarioDAO.merge(funcionario);

			funcionario = new Funcionario();

			funcionarios = funcionarioDAO.listaFuncionarios();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");

			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");

			Messages.addGlobalInfo("Funcionário salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o funcionário!");
			erro.printStackTrace();
		}
	}
	public void salvarEdicao() {
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
	
System.out.println(senhaNova);
		
		
			if(( !senhaNova.equals(null))  &&  (! senhaNova.isEmpty()) ){
			
			funcionario.setSenha(DigestUtils.md5Hex(senhaNova));
System.err.println("Senha Nova........."+ DigestUtils.md5Hex((funcionario.getSenha())));
			}else{
				funcionario.setSenha(funcionario.getSenha());	
				System.err.println("Senha Antiga........."+ funcionario.getSenha());

			}

			System.out.println("fora..............................."+funcionario.getDataSaida());
			if(funcionario.getDataSaida()==null){
				funcionario.setSituacao(true);
				System.out.println("dentro..............................."+funcionario.getDataSaida());
			}else{
				funcionario.setSituacao(false);
			}
Date dataAdimissao = funcionario.getDataAdmissao();
funcionario.setDataAdmissao(dataAdimissao);

			funcionarioDAO.merge(funcionario);

			funcionario = new Funcionario();

			funcionarios = funcionarioDAO.listaFuncionarios();

			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");

			 RequestContext.getCurrentInstance().execute("PF('dialogoEdicao').hide();");

	
			Messages.addGlobalInfo("Funcionário salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar o funcionário!");
			erro.printStackTrace();
		}
	}
	// Carrega o cadastro do Funcionario (Usado para a página de detalhes /
	// Editar)
	public void carregarCadastro() {

		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionario = funcionarioDAO.buscar(codigo);

			estado = funcionario.getPessoa().getEndereco().getCidade().getEstado();
			endereco = funcionario.getPessoa().getEndereco();
			pessoa = funcionario.getPessoa();

			listarEstados();
			listarSexo();
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");

			CidadeDAO cidadeDAO = new CidadeDAO();
			cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar exibir os dados do funcionário selecionado!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		try {
			funcionario = (Funcionario) evento.getComponent().getAttributes().get("funcionarioSelecionado");
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoas = pessoaDAO.listar("nome");
		
			funcionario.setTipo(funcionario.getTipo());
			//PessoaDAO pessoaDAO = new PessoaDAO();
			   pessoa = pessoaDAO.buscar(funcionario.getPessoa().getCodigo());
				System.err.println("funcionario codigo...:"+funcionario.getCodigo());
				System.err.println("funcionario Pessoa codigo...:"+funcionario.getPessoa().getCodigo());
				funcionario.setPessoa(pessoa);
			
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar alterar os dados do funcionário!");
			erro.printStackTrace();
		}
	}

	// Método que Edita o endereço antes do funcionário
	public void editarEndereco() {
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			enderecoDAO.merge(endereco);
			System.out.println("Endereço foi salvo: " + endereco);

		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar alterar dados do endereço!");
			erro.printStackTrace();
		}
	}

	// Método que Edita a pessoa antes do funcionário
	public void editarPessoa() {
		try {
			PessoaDAO pessoaDAO = new PessoaDAO();
			pessoaDAO.merge(pessoa);
			System.out.println("Pessoa foi salva: " + pessoa);

		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar alterar dados do funcionário!");
			erro.printStackTrace();
		}
	}

	// método que verifica se o cpf já existe no banco de dados
	public boolean verificarCpf() {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		cpfFuncionario = funcionarioDAO.verificarCpf(funcionario.getPessoa().getCpf());

		if (cpfFuncionario != null) {
			return true;
		} else {
			return false;
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

	//imprimirFuncionarioPorCodigo
		public void imprimirFuncionarioPorCodigo(ActionEvent evento) {
		try {					
			Funcionario funcionarioSelecionado = (Funcionario) evento.getComponent().getAttributes().get("funcionarioSelecionado");
			System.out.println( "	funcionarioSelecionado.getCodigo()..................:"+funcionarioSelecionado.getCodigo());
			String caminho = Faces.getRealPath("//report//relatorioFuncionarioPorCodigo.jasper");

	        Map<String, Object> parametros = new HashMap<>();
	        parametros.put("COD_FUNCIONARIO",funcionarioSelecionado.getCodigo());
	     
			Connection conexao = HibernateUtil.getConexao();
	        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
	      
	        JasperViewer.viewReport(jp, false);
	     
	    	System.out.println( "	funcionarioSelecionado.getCodigo()..................:"+	funcionarioSelecionado.getCodigo());

		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
			erro.printStackTrace();
			System.err.println("Erro.......................................:"+erro);
		}
		}

	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
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

	

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Funcionario getCpfFuncionario() {
		return cpfFuncionario;
	}

	public void setCpfFuncionario(Funcionario cpfFuncionario) {
		this.cpfFuncionario = cpfFuncionario;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}