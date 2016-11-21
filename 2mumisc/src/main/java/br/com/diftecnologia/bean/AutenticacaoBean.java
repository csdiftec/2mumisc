package br.com.diftecnologia.bean;

import java.io.File;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.RegistroLoginDAO;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.Pessoa;
import br.com.diftecnologia.domain.RegistroLogin;

@ManagedBean
@SessionScoped
public class AutenticacaoBean {

	private Funcionario funcionarioLogado;
	private Funcionario funcionario;
	private boolean mostrarBotaoAbrirCaixa = true;

	
	@PostConstruct
	public void iniciar() {
		funcionarioLogado = null;
		funcionarioLogado = new Funcionario();
		funcionario = new Funcionario();
		funcionario.setPessoa(new Pessoa());
		 criarDiretorioImportacaoWindows();
	}
	
	public void criarDiretorioImportacaoWindows(){
		
		File file = new File("C://DifTecnologia-ImportarNota/");
		System.out.println("Windows................"+file);

		File theDir = new File("C://DifTecnologia-ImportarNota");
		if (!theDir.exists()) theDir.mkdir();

	}

	public String autenticar() {
		
		try {
		
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarioLogado = funcionarioDAO.autenticar(funcionario.getPessoa().getCpf(), funcionario.getSenha());

			if (funcionarioLogado == null) {
				Messages.addGlobalError("CPF e/ou senha inválidos!");
				return null;
			} else if (funcionarioLogado.getSituacao() == false) {
				Messages.addGlobalInfo(
						"Funcionário inativo, favor entrar em contato com administrador para regularizar sua situação.");
			} else {
				Messages.addGlobalInfo("Funcionário autenticado com sucesso!");
				System.out.println("Funcionário: " + funcionarioLogado.getPessoa().getNome() + " logou ás "
						+ new Date().toString());

				// Registrando a entrada o Usu�rio
				registrarLogin();
				this.mostrarBotaoAbrirCaixa = false;
				return "/pages/principal.xhtml?faces-redirect=true";
			}
		
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar acessar o sistema!");
			erro.printStackTrace();
			return null;
		}
		return null;
	}

	// M�todo que registra a entrada do usu�rio no sistema
	private void registrarLogin() {
		try {
			RegistroLoginDAO registroLoginDAO = new RegistroLoginDAO();
			RegistroLogin registroLogin = new RegistroLogin();
			registroLogin.setDataLogin(new Date());
			registroLogin.setFuncionario(funcionarioLogado);

			registroLoginDAO.merge(registroLogin);
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar registrar o login!");
			erro.printStackTrace();
		}
	}

	public String sair() {
		System.out.println(
				"Funcionário: " + funcionarioLogado.getPessoa().getNome() + " saiu ás " + new Date().toString());
		funcionarioLogado = null;

		return "/pages/autenticacao.xhtml?faces-redirect=true";// ?faces-redirect
															// faz que o
															// endereço do
															// navegador mude
	}

	// public void conectarBanco() throws SQLException {
	// java.sql.Connection conn = ConexaoMySQL.getConexaoMySQL();
	//
	// Statement s = (Statement) conn.createStatement();
	//
	// s.executeUpdate("CREATE DATABASE IF NOT EXISTS victoria");
	//
	// // s.executeUpdate("CREATE DATABASE victoria");
	//
	// s.close();
	// }
	

	public Funcionario getFuncionarioLogado() {
		if (funcionarioLogado == null) {
			funcionarioLogado = new Funcionario();
		}
		return funcionarioLogado;
	}

	public void setFuncionarioLogado(Funcionario funcionarioLogado) {
		this.funcionarioLogado = funcionarioLogado;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public boolean getMostrarAbrirCaixa() {
		return mostrarBotaoAbrirCaixa;
	}


	public boolean isMostrarBotaoAbrirCaixa() {
		return mostrarBotaoAbrirCaixa;
	}

	public void setMostrarBotaoAbrirCaixa(boolean mostrarBotaoAbrirCaixa) {
		this.mostrarBotaoAbrirCaixa = mostrarBotaoAbrirCaixa;
	}

} // fim da classe