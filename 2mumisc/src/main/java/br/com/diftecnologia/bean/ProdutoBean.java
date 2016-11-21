package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.sql.Connection;
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

import br.com.diftecnologia.dao.FabricanteDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.ProdutoDAO;
import br.com.diftecnologia.dao.UnidadeMedidaDAO;
import br.com.diftecnologia.domain.Fabricante;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.domain.UnidadeMedida;
import br.com.diftecnologia.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class ProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	private Produto produto;
	private List<Produto> produtos;
	private List<Produto> produtosNaQtdCritica;
	private List<Fabricante> fabricantes;
	private List<UnidadeMedida> unidadesMedida;

	public void novo() {
		try {
			produto = new Produto();

			listarFabricantesUnidades();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo produto!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produto = new Produto();
			 listarProdutos();
			System.out.println("----------------------------------" + produtos);
			produtosNaQtdCritica = produtoDAO.listarProdutoEmEstoqueNaQtdCritica();
			
			System.out.println("Lista produtos no Bean......................................."+ produtosNaQtdCritica);
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos!");
			erro.printStackTrace();
		}
	}
//	@PostConstruct
	public void listarItensNaQuantidadeCritica() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtosNaQtdCritica = produtoDAO.listarProdutoEmEstoqueNaQtdCritica();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos na quantidade critica!");
			erro.printStackTrace();
		}
	}
	
	public void listarProdutos(){
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos!");
			erro.printStackTrace();
		}
	}
	public void salvar() {
		try {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 
			
			produto.setFuncionario(funcionario);
			if(produto.getQuantidadeEstoque()==null){
			produto.setQuantidadeEstoque(0.0);
			}
			produtoDAO.merge(produto);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			Messages.addGlobalInfo("Produto salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o produto!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		try {
			produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");
			listarFabricantesUnidades();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar um produto!");
			erro.printStackTrace();
		}
	}

	private void listarFabricantesUnidades() {
		FabricanteDAO fabricanteDAO = new FabricanteDAO();
		UnidadeMedidaDAO unidadeMedidaDAO = new UnidadeMedidaDAO();
		
		fabricantes = fabricanteDAO.listar();
		unidadesMedida = unidadeMedidaDAO.listar();
	}

	public void excluir(ActionEvent evento) {
		try {
			produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtoDAO.excluir(produto);

			listar();

			Messages.addGlobalInfo("Produto removido com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Remoção do produto não permitida!" );
			Messages.addFlashGlobalError("Produto pode estar vinculado a uma venda" );
			erro.printStackTrace();
		}
	}
	//imprimirProdutoPorCodigo
		public void imprimirProdutoPorCodigo(ActionEvent evento) {	
			try {					
				Produto produtoSelecionado = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");
				System.out.println( "	produtoSelecionado.getCodigo())..................:"+produtoSelecionado.getCodigo());
				String caminho = Faces.getRealPath("//report//relatorioProdutoPorCodigo.jasper");

		        Map<String, Object> parametros = new HashMap<>();
		        parametros.put("COD_PRODUTO",produtoSelecionado.getCodigo());
		     
				Connection conexao = HibernateUtil.getConexao();
		        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
		      
		        JasperViewer.viewReport(jp, false);
		     
		System.out.println( "produtoSelecionado.getCodigo()..................:"+	produtoSelecionado.getCodigo());

		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
			erro.printStackTrace();
			System.err.println("Erro.......................................:"+erro);
		}
		}
	//	imprimirProdutoNaQuantidadeCritica
		public void imprimirProdutosNaQuantidadeCritica(ActionEvent evento) {	
			try {					
	
				String caminho = Faces.getRealPath("//report//relatorioProdutoNaQuantidadeCritica.jasper");

	
		     
				Connection conexao = HibernateUtil.getConexao();
		        JasperPrint jp = JasperFillManager.fillReport(caminho,null, conexao);
		      
		        JasperViewer.viewReport(jp, false);
		     
			} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
			erro.printStackTrace();
			System.err.println("Erro.......................................:"+erro);
		}
		}
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}

	public void setFabricantes(List<Fabricante> fabricantes) {
		this.fabricantes = fabricantes;
	}

	public List<UnidadeMedida> getUnidadesMedida() {
		return unidadesMedida;
	}

	public void setUnidadesMedida(List<UnidadeMedida> unidadesMedida) {
		this.unidadesMedida = unidadesMedida;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public List<Produto> getProdutosNaQtdCritica() {
		return produtosNaQtdCritica;
	}

	public void setProdutosNaQtdCritica(List<Produto> produtosNaQtdCritica) {
		this.produtosNaQtdCritica = produtosNaQtdCritica;
	}

	
}
