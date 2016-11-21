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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.dao.FornecedorDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Cliente;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;
import br.com.diftecnologia.domain.Fornecedor;
import br.com.diftecnologia.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class FornecedorBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Fornecedor fornecedor;
	private Estado estado;
	private Endereco endereco;
	private Fornecedor cnpjFornecedor;

	private List<Fornecedor> fornecedores;
	private List<Estado> estados;
	private List<Cidade> cidades;


	@PostConstruct
	private void listar() {
		try {
			FornecedorDAO fornecedorDAO = new FornecedorDAO();
			fornecedores = fornecedorDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os fabricantes!");
			erro.printStackTrace();
		}
	}

	public void novo() {
		try {
			fornecedor = new Fornecedor();
			endereco = new Endereco();
			estado = new Estado();

			listarEstados();

			cidades = new ArrayList<>();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo fornecedor!");
			erro.printStackTrace();
		}
	}

	// Método que salva o endereço antes do fornecedor
	public void salvarEndereco() {
		try {
	
			
			EnderecoDAO enderecoDAO = new EnderecoDAO();
	
		System.err.println("xxxxxxxxxxxxxxx..............xxxxxxxxxxxxxxxxx");
			
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

	public void salvar() {
	
		try {

				FornecedorDAO fornecedorDAO = new FornecedorDAO();
		
				salvarEndereco();
				
				EnderecoDAO enderecoDAO = new EnderecoDAO();
				Long codigoEndereco = enderecoDAO.buscarUltimoRegistro();
				endereco = enderecoDAO.buscar(codigoEndereco);
				
						
				fornecedor.setDataCadastro(new Date());
				fornecedor.setEndereco(endereco);
				
				fornecedorDAO.merge(fornecedor);
				novo();
				
				 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
				 RequestContext.getCurrentInstance().execute("PF('dialogoEdit').hide();");
				Messages.addGlobalInfo("Fornecedor salvo com sucesso!");
		
		} catch (RuntimeException erro) {
		
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o fornecedor!");
			Messages.addFlashGlobalError("Verifique a duplicidade dos  dados");
			erro.printStackTrace();
		
		}finally{
			listar();
		}
		
	}

	public void editar(ActionEvent evento) {
		try {
	fornecedor = (Fornecedor) evento.getComponent().getAttributes().get("fornecedorSelecionado");
	 System.out.println("Fornecedor............:"+fornecedor.getCodigo());
	 System.out.println("Fornecedor endereco Bairro...."+fornecedor.getEndereco().getBairro());

	EnderecoDAO enderecoDAO = new EnderecoDAO();
   endereco = enderecoDAO.buscar(fornecedor.getEndereco().getCodigo());
	System.err.println("endereco codigo...:"+endereco.getCodigo());

	fornecedor.setEndereco(endereco);
	
	estado = endereco.getCidade().getEstado();
	EstadoDAO estadoDAO = new EstadoDAO();
	System.err.println("endereco estado...:"+endereco.getCidade().getEstado());

	estados = estadoDAO.listar("nome");
	
	CidadeDAO cidadeDAO = new CidadeDAO();
	cidades = cidadeDAO.buscarPorEstado(estado.getCodigo()); 
 
 
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar um fornecedor!");
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

	// método que verifica se o cnpj já existe no banco de dados
	public boolean verificarCnpj() {
		FornecedorDAO fornecedorDAO = new FornecedorDAO();
		cnpjFornecedor = fornecedorDAO.verificarCnpj(fornecedor.getCnpj());

		if (cnpjFornecedor != null) {
			return true;
		} else {
			return false;
		}
	}
	public void excluir(ActionEvent evento) {
		try {
			fornecedor = (Fornecedor) evento.getComponent().getAttributes().get("fornecedorSelecionado");

			FornecedorDAO fornecedorDAO = new FornecedorDAO();
	
//				EnderecoDAO enderecoDAO = new EnderecoDAO();
				
				endereco = fornecedor.getEndereco();
			
				
				fornecedorDAO.excluir(fornecedor);
		//		enderecoDAO.excluir(endereco);
				

				listar();
				novo();
				listarEstados();
	
				Messages.addGlobalInfo("Fornecedor removido com sucesso!");
			
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o fornecedor!");
			erro.printStackTrace();
		}
	}
	
	//imprimirFornecedorPorCodigo
	public void imprimirFornecedorPorCodigo(ActionEvent evento) {
	try {					
		Fornecedor fornecedorSelecionado = (Fornecedor) evento.getComponent().getAttributes().get("fornecedorSelecionado");
		System.out.println( "	fornecedorSelecionado.getCodigo()..................:"+fornecedorSelecionado.getCodigo());
		String caminho = Faces.getRealPath("//report//relatorioFornecedorPorCodigo.jasper");

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("COD_FORNECEDOR",fornecedorSelecionado.getCodigo());
     
		Connection conexao = HibernateUtil.getConexao();
        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
      
        JasperViewer.viewReport(jp, false);
     
	System.out.println( "	fornecedorSelecionado.getCodigo()..................:"+	fornecedorSelecionado.getCodigo());

	} catch (JRException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
		erro.printStackTrace();
		System.err.println("Erro.......................................:"+erro);
	}
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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

	public Fornecedor getCnpjFornecedor() {
		return cnpjFornecedor;
	}

	public void setCnpjFornecedor(Fornecedor cnpjFornecedor) {
		this.cnpjFornecedor = cnpjFornecedor;
	}

}
