package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.diftecnologia.dao.CompraDAO;
import br.com.diftecnologia.dao.FormaPagamentoDAO;
import br.com.diftecnologia.dao.FornecedorDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.ProdutoDAO;
import br.com.diftecnologia.dao.TipoPagamentoDAO;
import br.com.diftecnologia.domain.Compra;
import br.com.diftecnologia.domain.FormaPagamento;
import br.com.diftecnologia.domain.Fornecedor;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.ItemCompra;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.domain.TipoPagamento;
import br.com.diftecnologia.util.HibernateUtil;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CompraBean implements Serializable {
	private Compra compra;
	
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	private List<Produto> produtos;
	private List<Compra> listaCompras;
	private List<ItemCompra> itensCompra;
	private List<Fornecedor> fornecedores;
	private List<FormaPagamento> formasPagamentos;
	private List<TipoPagamento> tiposPagamentos;
	private List<Funcionario> funcionarios;
	public Long numeroDocumentoNotaAtualizado;
	public Compra numeroDaCompra;
	public Integer porcentagemDesconto;
	public Fornecedor fornecedorSelecionado;
	public String fornecedor;
	private Long codigoCompraSalva;
	public Fornecedor fornecedorCompra;

	
	@PostConstruct
	public void novo() {
		try {
		
			ProdutoDAO produtoDAO = new ProdutoDAO();
CompraDAO compraDAO = new CompraDAO();
			compra = new Compra();

			compra.setValorTotal(new BigDecimal("0.00"));
			setPorcentagemDesconto(0);

			produtos = produtoDAO.listaTodosProdutoEmEstoque();

			itensCompra = new ArrayList<>();

			FornecedorDAO fornecedorDAO = new FornecedorDAO();
			fornecedores = fornecedorDAO.listar();
			
			listaCompras = compraDAO.listarUltimas1000Compras();
			
			RequestContext.getCurrentInstance().execute("PF('dialogoImpressãoNota').hide();");
			System.err.println("..................................................................");
			System.getProperty("os.name");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar carregar a tela de compras");
			erro.printStackTrace();
		}
	}

	public void adicionar(ActionEvent evento) {
		Produto produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");
		if (porcentagemDesconto.equals("")) {
			setPorcentagemDesconto(0);

		}
		int achou = -1;
		for (int posicao = 0; posicao < itensCompra.size(); posicao++) {
			if (itensCompra.get(posicao).getProduto().equals(produto)) {
				achou = posicao;
			}
		}

		if (achou < 0) {
			ItemCompra itemCompra = new ItemCompra();
			itemCompra.setValorParcial(produto.getValor());
			itemCompra.setProduto(produto);
			itemCompra.setQuantidade((double) 1);
			itemCompra.setPorcentagemDoDesconto(new Short(porcentagemDesconto + ""));
			BigDecimal valorDoDesconto = produto.getValor().multiply(new BigDecimal(porcentagemDesconto))
					.divide(new BigDecimal(100));
			;
			itemCompra.setValorDoDesconto(valorDoDesconto);
			itemCompra.setValorComDesconto(produto.getValor().subtract(valorDoDesconto));
			itensCompra.add(itemCompra);

		} else {
			ItemCompra itemCompra = itensCompra.get(achou);
			itemCompra.setQuantidade((itemCompra.getQuantidade() + 1));
			itemCompra.setValorParcial(produto.getValor().multiply(new BigDecimal(itemCompra.getQuantidade())));

			itemCompra.setPorcentagemDoDesconto(new Short(porcentagemDesconto + ""));

			BigDecimal precoParcialSomado = produto.getValor().multiply(new BigDecimal(itemCompra.getQuantidade()));

			BigDecimal valorDoDesconto = precoParcialSomado.divide(new BigDecimal(100))
					.multiply(new BigDecimal(itemCompra.getPorcentagemDoDesconto()));
			itemCompra.setValorDoDesconto(valorDoDesconto);
			itemCompra.setValorComDesconto(precoParcialSomado.subtract(valorDoDesconto));

		}
		calcular();

	}

	public void remover(ActionEvent evento) {
		ItemCompra itemCompra = (ItemCompra) evento.getComponent().getAttributes().get("itemSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < itensCompra.size(); posicao++) {
			if (itensCompra.get(posicao).getProduto().equals(itemCompra.getProduto())) {
				achou = posicao;
			}
		}

		if (achou > -1) {
			itensCompra.remove(achou);
		}

		calcular();
	}

	public void calcular() {
		compra.setValorTotal(new BigDecimal("0.00"));

		for (int posicao = 0; posicao < itensCompra.size(); posicao++) {
			ItemCompra itemCompra = itensCompra.get(posicao);
			compra.setValorTotal(compra.getValorTotal().add(itemCompra.getValorComDesconto()));
		}
	}

	public void finalizar() {
		try {
			compra.setDataCompra(new Date());


			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
			tiposPagamentos = tipoPagamentoDAO.listar("descricao");

			FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
			formasPagamentos = formaPagamentoDAO.listar("descricao");

			FornecedorDAO fornecedorDAO = new FornecedorDAO();
			fornecedores = fornecedorDAO.listar();
		
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar a compra");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			if (compra.getValorTotal().signum() == 0) {
				Messages.addGlobalError("Informe pelo menos um item para a compra");
				return;
			}
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 
		
			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
		TipoPagamento	tipoPagamento=	tipoPagamentoDAO.buscar(1L);
		//     A compra esta sendo somente a vista
					compra.setFuncionario(funcionario);
			compra.setTipoPagamento(tipoPagamento);
			CompraDAO compraDAO = new CompraDAO();
			compraDAO.salvar(compra, itensCompra);

			codigoCompraSalva = compra.getCodigo();
			//novo();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
				RequestContext.getCurrentInstance().execute("PF('dialogoImpressãoNota').show();");
			Messages.addGlobalInfo("Compra realizada com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Registro não pode ser salvo");
	
			erro.printStackTrace();
		}
	}
	
	public List<Compra>  buscaPorPeriodo(){
	try{
			CompraDAO compraDAO = new CompraDAO();
			listaCompras = compraDAO.listarComprasPorPeriodo(compra.getDataInicial(), compra.getDataFinal());
	
		}catch(RuntimeException ex){
		
System.err.println(ex);
		}
	return listaCompras;
	}
	
	public List<Compra> buscaPorPeriodoFornecedor() {
		try {
			CompraDAO compraDAO = new CompraDAO();
			listaCompras = compraDAO.listarComprasPorPeriodofornecedor(compra.getDataInicial(), compra.getDataFinal(),
					fornecedorSelecionado.getCodigo());

		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return listaCompras;
	}
	
	public void selecionarFornecedor(ActionEvent evento) {
		try {
			fornecedorCompra = new Fornecedor();

			fornecedorSelecionado = (Fornecedor) evento.getComponent().getAttributes().get("fornecedorSelecionado");
			fornecedor = fornecedorSelecionado.getNomeFantasia();
			compra.setFlagFornecedorSelecionado(fornecedor);

		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar o fornecedor:  "+ e);
		}
	}
	//imprimirCompraPeriodo
	
	public void imprimirCompraPeriodo(ActionEvent evento) {
		try {					
		
			String caminho = Faces.getRealPath("/report/relatorioCompraPeriodo.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("DATA_INICIAL",compra.getDataInicial());
			parametros.put("DATA_FINAL",compra.getDataFinal());
			
			
			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
//	   System.out.println( "funcionarioSelecionado..................:"+funcionarioSelecionado.getCodigo());
			System.out.println( "compra.getDataInicial(..................:"+compra.getDataInicial());
			System.out.println( "compra.getDataFinal()..................:"+compra.getDataFinal());
			 JasperViewer.viewReport(relatorio, false);
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	//imprimirCompraFornecedor
	
	public void imprimirCompraFornecedor(ActionEvent evento) {
		try {					
		
			String caminho = Faces.getRealPath("/report/relatorioCompraFornecedor.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("COD_FORNECEDOR",fornecedorSelecionado.getCodigo());
			parametros.put("DATA_INICIAL",compra.getDataInicial());
			parametros.put("DATA_FINAL",compra.getDataFinal());
			
			
			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			System.out.println( "fornecedorSelecionado..................:"+fornecedorSelecionado.getCodigo());
			System.out.println( "compra.getDataInicial(..................:"+compra.getDataInicial());
			System.out.println( "compra.getDataFinal()..................:"+compra.getDataFinal());
			 JasperViewer.viewReport(relatorio, false);
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	//imprimirCompraCodigo
		public void imprimirCompraCodigo(ActionEvent evento) {
			try {					
				Compra compraSelecionada = (Compra) evento.getComponent()	.getAttributes().get("compraSelecionada");
				String caminho = Faces.getRealPath("//report//relatorioCompraCodigo.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_COMPRA",compraSelecionada.getCodigo());				
				Connection conexao = HibernateUtil.getConexao();
		        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
		      
		        JasperViewer.viewReport(jp, false);
				System.out.println( "compraSelecionada..................:"+compraSelecionada.getCodigo());

			
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}

		//imprimirUltimaCompraCodigo
		public void imprimirUltimaCompraCodigo(ActionEvent evento) {
			try {					

				String caminho = Faces.getRealPath("//report//relatorioCompraCodigo.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_COMPRA",codigoCompraSalva);				
				Connection conexao = HibernateUtil.getConexao();
		        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
		      
		        JasperViewer.viewReport(jp, false);
				System.out.println( "codigoCompraSalva..................:"+codigoCompraSalva);

			
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<FormaPagamento> getFormasPagamentos() {
		return formasPagamentos;
	}

	public void setFormasPagamentos(List<FormaPagamento> formasPagamentos) {
		this.formasPagamentos = formasPagamentos;
	}

	public List<TipoPagamento> getTiposPagamentos() {
		return tiposPagamentos;
	}

	public void setTiposPagamentos(List<TipoPagamento> tiposPagamentos) {
		this.tiposPagamentos = tiposPagamentos;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Integer getPorcentagemDesconto() {
		return porcentagemDesconto;
	}

	public void setPorcentagemDesconto(Integer porcentagemDesconto) {
		this.porcentagemDesconto = porcentagemDesconto;
	}

	public List<ItemCompra> getItensCompra() {
		return itensCompra;
	}

	public void setItensCompra(List<ItemCompra> itensCompra) {
		this.itensCompra = itensCompra;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}
	

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public List<Compra> getListaCompras() {
		return listaCompras;
	}

	public void setListaCompras(List<Compra> listaCompras) {
		this.listaCompras = listaCompras;
	}

	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public Fornecedor getFornecedorCompra() {
		return fornecedorCompra;
	}

	public void setFornecedorCompra(Fornecedor fornecedorCompra) {
		this.fornecedorCompra = fornecedorCompra;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Long getCodigoCompraSalva() {
		return codigoCompraSalva;
	}

	public void setCodigoCompraSalva(Long codigoCompraSalva) {
		this.codigoCompraSalva = codigoCompraSalva;
	}

	
}
