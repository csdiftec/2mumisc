package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.ClienteDAO;
import br.com.diftecnologia.dao.DuplicataDAO;
import br.com.diftecnologia.dao.FormaPagamentoDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.ItemVendaDAO;
import br.com.diftecnologia.dao.ProdutoDAO;
import br.com.diftecnologia.dao.TipoPagamentoDAO;
import br.com.diftecnologia.dao.VendaDAO;
import br.com.diftecnologia.domain.Cliente;
import br.com.diftecnologia.domain.Duplicata;
import br.com.diftecnologia.domain.FormaPagamento;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.ItemVenda;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.domain.TipoPagamento;
import br.com.diftecnologia.domain.Venda;
import br.com.diftecnologia.util.HibernateUtil;
@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class VendaBean implements Serializable {

	@ManagedProperty(value = "#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	private Venda venda;
	private Duplicata duplicata;
	private List<Duplicata> duplicatasGeradas;
	private List<Produto> produtos;
	private List<ItemVenda> itensVenda;
	private List<Cliente> clientes;
	private List<FormaPagamento> FormasPagamentos;
	private List<TipoPagamento> tiposPagamentos;
	private List<Funcionario> funcionarios;
	private List<Venda> listaVendas;
	private List<Venda> listaVendasParceladas;
	public Long numeroDocumentoNotaAtualizado;
	public Long numeroDeVenda;
	public Integer porcentagemDesconto;
	BigDecimal valorPrimeiraParcela;
	BigDecimal[] valorDivisao;
	public Funcionario funcionarioVenda;
	public FormaPagamento formaSelecionada;
	public TipoPagamento tipoSelecionado;
	public String vendedor;
	public Funcionario funcionarioSelecionado;
	private Long codigoVendaSalva;
	
	@PostConstruct
	public void novo() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			ProdutoDAO produtoDAO = new ProdutoDAO();
			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();

			venda = new Venda();
			duplicata = new Duplicata();

			venda.setValorTotal(new BigDecimal("0.00"));
			setPorcentagemDesconto(0);

			produtos = produtoDAO.listaProdutoEmEstoque();

			itensVenda = new ArrayList<>();

			tiposPagamentos = tipoPagamentoDAO.listar("codigo");

			listaVendas = vendaDAO.listarUltimas1000Vendas();

			listaVendasParceladas = vendaDAO.listarVendasParceladas();
			RequestContext.getCurrentInstance().execute("PF('dialogoImpressãoNota').hide();");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar carregar a tela de vendas");
			erro.printStackTrace();
		}
	}

	public void adicionar(ActionEvent evento) {
		Produto produto = (Produto) evento.getComponent().getAttributes()
				.get("produtoSelecionado");
		if (porcentagemDesconto.equals("")) {
			setPorcentagemDesconto(0);

		}
		int achou = -1;
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			if (itensVenda.get(posicao).getProduto().equals(produto)) {
				achou = posicao;
			}
		}

		if (achou < 0) {
			ItemVenda itemVenda = new ItemVenda();
			itemVenda.setValorParcial(produto.getValor());
			itemVenda.setProduto(produto);
			itemVenda.setQuantidade(1);
			itemVenda.setPorcentagemDoDesconto(porcentagemDesconto);
			BigDecimal valorDoDesconto = produto.getValor()
					.multiply(new BigDecimal(porcentagemDesconto))
					.divide(new BigDecimal(100));
			;
			itemVenda.setValorDoDesconto(valorDoDesconto);
			itemVenda.setValorComDesconto(produto.getValor().subtract(
					valorDoDesconto));
			itensVenda.add(itemVenda);

		} else {
			ItemVenda itemVenda = itensVenda.get(achou);
			itemVenda.setQuantidade(itemVenda.getQuantidade() + 1);
			itemVenda.setValorParcial(produto.getValor().multiply(
					new BigDecimal(itemVenda.getQuantidade())));

			itemVenda.setPorcentagemDoDesconto(porcentagemDesconto);

			BigDecimal precoParcialSomado = produto.getValor().multiply(
					new BigDecimal(itemVenda.getQuantidade()));

			BigDecimal valorDoDesconto = precoParcialSomado.divide(
					new BigDecimal(100)).multiply(
					new BigDecimal(itemVenda.getPorcentagemDoDesconto()));
			itemVenda.setValorDoDesconto(valorDoDesconto);
			itemVenda.setValorComDesconto(precoParcialSomado
					.subtract(valorDoDesconto));

		}
		calcular();

	}

	public void remover(ActionEvent evento) {
		ItemVenda itemVenda = (ItemVenda) evento.getComponent().getAttributes()
				.get("itemSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			if (itensVenda.get(posicao).getProduto()
					.equals(itemVenda.getProduto())) {
				achou = posicao;
			}
		}

		if (achou > -1) {
			itensVenda.remove(achou);
		}

		calcular();
	}

	public void calcular() {
		venda.setValorTotal(new BigDecimal("0.00"));

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			ItemVenda itemVenda = itensVenda.get(posicao);
			venda.setValorTotal(venda.getValorTotal().add(
					itemVenda.getValorComDesconto()));
		}
	}

	public void finalizar() {
		try {
			venda.setDataVenda(new Date());
			duplicata.setTotalParcelas((short) 1);
			duplicata.setValorParcela(new BigDecimal("0.00"));

			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listarOrdenado();

			FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
			FormasPagamentos = formaPagamentoDAO.listar("descricao");

			ClienteDAO clienteDAO = new ClienteDAO();
			clientes = clienteDAO.listar();

			verificaNumeroRegistros();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar a venda");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			if (venda.getValorTotal().signum() == 0) {
				Messages.addGlobalError("Informe pelo menos um item para a venda");
				return;
			}
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean
					.getFuncionarioLogado().getCodigo());

			venda.setFuncionario(funcionario);
			VendaDAO vendaDAO = new VendaDAO();
			vendaDAO.salvar(venda, itensVenda);
codigoVendaSalva = venda.getCodigo();
		novo();
			
			RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			RequestContext.getCurrentInstance().execute("PF('dialogoImpressãoNota').show();");
			
			Messages.addGlobalInfo("Venda realizada com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Quantidade insuficiente em estoque");
			erro.printStackTrace();
		}
	}

	public void verificaNumeroRegistros() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			numeroDeVenda = vendaDAO.verificaNumeroRegistros();

			if (numeroDeVenda == 0 || numeroDeVenda == null) {
				venda.setNumeroDocumentoFiscal(11111111101L);

			} else {
				numeroDeVenda = vendaDAO.buscaUltimoNumeroDocumento();
				numeroDocumentoNotaAtualizado = numeroDeVenda + 1;
				venda.setNumeroDocumentoFiscal(numeroDocumentoNotaAtualizado);
			}

		} catch (Exception e) {
			System.out.println("venda:" + e);
		}
	}

	public void gerarParcela() {
		try {
			if (venda.getValorTotal().signum() == 0) {
				Messages.addGlobalError("Informe pelo menos um item para a venda");
				return;
			}
			codigoVendaSalva = venda.getCodigo();
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean
					.getFuncionarioLogado().getCodigo());

			venda.setFuncionario(funcionario);
			VendaDAO vendaDAO = new VendaDAO();
			vendaDAO.salvar(venda, itensVenda);
			venda = vendaDAO.buscar(venda.getCodigo());
			codigoVendaSalva = venda.getCodigo();
			Short numeroParcela = duplicata.getTotalParcelas();

			Date periodo = duplicata.getDataVencimento();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(periodo);
			for (int posicao = 0; posicao < numeroParcela; posicao++) {
				periodo = gc.getTime();
				if (posicao == 0) {
					duplicata.setValorParcela(valorPrimeiraParcela);
				} else {
					duplicata.setValorParcela(valorDivisao[0]);
				}
				duplicata.setDataVencimento(periodo);
				duplicata.setNumeroParcela((short) (posicao + 1));
				duplicata.setDataEmissao(venda.getDataVenda());
				duplicata.setTotalParcelas(duplicata.getTotalParcelas());
				duplicata.setValorParcela(duplicata.getValorParcela());
				duplicata.setVenda(venda);
				duplicata.setStatus("Ativa");
				duplicata.setValorDesconto(new BigDecimal(0.0));
				duplicata.setValorJuros(new BigDecimal(0.0));
				duplicata.setValorPago(new BigDecimal(0.0));
				duplicata.setValorTotalAtual(duplicata.getValorParcela());
				duplicata.setDataUltimoPagamento(new Date());
				DuplicataDAO duplicataDAO = new DuplicataDAO();
				duplicataDAO.merge(duplicata);
				gc.add(Calendar.DAY_OF_MONTH, 30);

			}
		//	 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			RequestContext.getCurrentInstance().execute("PF('dialogoDuplicatas').show();");
			Messages.addGlobalInfo("Venda realizada com sucesso");

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Quantidade insuficiente em estoque");
			erro.printStackTrace();
		}
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		duplicatasGeradas = duplicataDAO.buscaDuplicatasDaVenda(duplicata
				.getVenda().getCodigo());

	}

	public void dividirTotalPorQtdParcela() {
		valorDivisao = venda.getValorTotal().divideAndRemainder(
				BigDecimal.valueOf(duplicata.getTotalParcelas()));
		valorPrimeiraParcela = valorDivisao[0].add(valorDivisao[1]);
		duplicata.setValorParcela(valorDivisao[0]);
		System.out.println("Divisao" + valorDivisao);
		System.out.println("Primeira parcela" + valorPrimeiraParcela);
	}

	public List<Venda> buscaPorPeriodo() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendas = vendaDAO.listarVendasPorPeriodo(
					venda.getDataInicial(), venda.getDataFinal());
	
		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return listaVendas;
	}
	public List<Venda> buscaVendaParceladaPorPeriodo() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendasParceladas = vendaDAO.listarVendasParceladas(
					venda.getDataInicial(), venda.getDataFinal());
	
		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return listaVendasParceladas;
	}

	public List<Venda> buscaPorPeriodoVendedor() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendas = vendaDAO.listarVendasPorPeriodoVendedor(
					venda.getDataInicial(), venda.getDataFinal(),
					funcionarioSelecionado.getCodigo());

		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return listaVendas;
	}

	public List<Venda> buscaPorPeriodoFuncionarioLog() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendas = vendaDAO.listarVendasPorPeriodoFuncionarioLog(
					venda.getDataInicial(), venda.getDataFinal(),
					funcionarioSelecionado.getCodigo());

		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return listaVendas;
	}

	public List<Venda> buscaPorPeriodoFormaPagamento() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendas = vendaDAO.listarVendasPorPeriodoFormaPagamento(
					venda.getDataInicial(), venda.getDataFinal(),
					formaSelecionada.getCodigo());

		} catch (RuntimeException ex) {

			System.err
					.println("Busca da venda por forma de pagamento não realizada."
							+ ex);
		}
		return listaVendas;
	}

	public List<Venda> buscaPorPeriodoTipoPagamento() {
		try {
			VendaDAO vendaDAO = new VendaDAO();
			listaVendas = vendaDAO.listarVendasPorPeriodoTipoPagamento(
					venda.getDataInicial(), venda.getDataFinal(),
					tipoSelecionado.getCodigo());

		} catch (RuntimeException ex) {

			System.err
					.println("Busca da venda por tipo de pagamento não realizada."
							+ ex);
		}
		return listaVendas;
	}

	public void selecionarFuncionario(ActionEvent evento) {
		try {
			funcionarioVenda = new Funcionario();

			funcionarioSelecionado = (Funcionario) evento.getComponent()	.getAttributes().get("funcionarioSelecionado");
			vendedor = funcionarioSelecionado.getPessoa().getNome();
			venda.setVendedor(vendedor);

		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar o funcionario: "
					+ e);
		}
	}
	

	public void selecionarFormaPagamento(ActionEvent evento) {
		try {
			formaSelecionada = new FormaPagamento();

			formaSelecionada = (FormaPagamento) evento.getComponent()
					.getAttributes().get("formaSelecionada");
			System.out.println("Forma de pagamento bean......:"
					+ formaSelecionada);
			venda.setDescricaoFormaPagamento(formaSelecionada.getDescricao());

		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar a forma do pagamento: "
					+ e);
		}
	}

	public void selecionarTipoPagamento(ActionEvent evento) {
		try {
			tipoSelecionado = new TipoPagamento();

			tipoSelecionado = (TipoPagamento) evento.getComponent()
					.getAttributes().get("tipoSelecionado");
			System.out.println("Forma de pagamento bean......:"
					+ formaSelecionada);
			venda.setDescricaoTipoPagamento(tipoSelecionado.getDescricao());

		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar o tipo de  pagamento: "
					+ e);
		}
	}

	public List<ItemVenda> buscaProdutoPorPeriodoVendedor() {
		try {
			ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
			itensVenda = itemVendaDAO.listarProdutosVendidosPorPeriodoVendedor(
					venda.getDataInicial(), venda.getDataFinal(),
					funcionarioSelecionado.getCodigo());

		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return itensVenda;
	}
	
	
	public List<ItemVenda> buscaProdutoPorPeriodo() {
		try {
			ItemVendaDAO itemVendaDAO = new ItemVendaDAO();
			itensVenda = itemVendaDAO.listarProdutosVendidosPorPeriodo(
					venda.getDataInicial(), venda.getDataFinal());

		} catch (RuntimeException ex) {

			System.err.println(ex);
		}
		return itensVenda;
	}
	
	public List<Venda> buscaVendasParceladas() {
		try {
			VendaDAO VendaDAO = new VendaDAO();
			listaVendasParceladas = VendaDAO.listarVendasParceladas();

		} catch (RuntimeException ex) {
			System.err.println(ex);
		}
		return listaVendasParceladas;
	}
	
	
	
	public void imprimir(ActionEvent evento) {
		try {
			Venda imprimirVendaSelecionada = new Venda();

			imprimirVendaSelecionada = (Venda) evento.getComponent()	.getAttributes().get("vendaSelecionada");
					
			String caminho = Faces.getRealPath("/report/relatorioVendaDetalhada.jasper");

			Long codigoVenda =  imprimirVendaSelecionada.getCodigo();
			Map<String, Object> parametros = new HashMap<>();

			parametros.put("CODIGO_VENDA",codigoVenda );
			
			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			System.out.println( imprimirVendaSelecionada.getCodigo());
			 JasperViewer.viewReport(relatorio, false);
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	public void imprimirUltimaVenda(ActionEvent evento) {
		try {
		
			String caminho = Faces.getRealPath("//report//relatorioVendaDetalhada.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("CODIGO_VENDA",codigoVendaSalva);
			
			Connection conexao = HibernateUtil.getConexao();
	        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
	      
	        JasperViewer.viewReport(jp, false);
			System.out.println("Codigo da venda no imprimir venda......:"+codigoVendaSalva);
		
			
			RequestContext.getCurrentInstance().execute("PF('dialogoImpressãoNota').hide();");
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	public void imprimirDuplicataPorVenda(ActionEvent evento) {
		try {
			Venda imprimirVendaSelecionada = new Venda();

			imprimirVendaSelecionada = (Venda) evento.getComponent()	.getAttributes().get("vendaSelecionada");
					
			String caminho = Faces.getRealPath("//report//relatorioDuplicataPorVenda.jasper");

			Long codigoVenda =  imprimirVendaSelecionada.getCodigo();
			Map<String, Object> parametros = new HashMap<>();

			parametros.put("CODIGO_VENDA",codigoVenda );
			
			Connection conexao = HibernateUtil.getConexao();
	        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
	      
	        JasperViewer.viewReport(jp, false);
			System.out.println( "----------------------"+imprimirVendaSelecionada.getCodigo());
			
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	
	
	// imprimirListaDuplicatasPorCodigoVenda
		public void imprimirListaDuplicatasPorCodigoVenda(ActionEvent evento) {
			try {
				String caminho = Faces	.getRealPath("//report//relatorioDuplicataPorVenda.jasper");
		
				Map<String, Object> parametros = new HashMap<>();

				parametros.put("CODIGO_VENDA",codigoVendaSalva);
				
				Connection conexao = HibernateUtil.getConexao();
		        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
		      
		        JasperViewer.viewReport(jp, false);
				System.out.println("Codigo da venda no imprimir venda......:"+codigoVendaSalva);
			
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
		
	//imprimirVendaPeriodo
	
	public void imprimirVendaPeriodo(ActionEvent evento) {
		try {					
			String caminho = Faces.getRealPath("/report/relatorioVendasPeriodo.jasper");


			Map<String, Object> parametros = new HashMap<>();

			parametros.put("DATA_INICIAL",venda.getDataInicial());
			parametros.put("DATA_FINAL",venda.getDataFinal());
			
			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			System.out.println( "Data inicial--"+venda.getDataInicial()+"Data Final--"+venda.getDataFinal());
			JasperViewer.viewReport(relatorio, false);
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	//imprimirVendaPeriodoVendedor
		public void imprimirVendaPeriodoVendedor(ActionEvent evento) {
			try {					
			
				String caminho = Faces	.getRealPath("//report//relatorioVendasPeriodoVendedor.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("VENDEDOR",funcionarioSelecionado.getCodigo());
				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "funcionarioSelecionado..................:"+funcionarioSelecionado.getCodigo());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
		//imprimirVendaPeriodoFuncionarioLogado
		public void imprimirVendaPeriodoFuncionarioLog(ActionEvent evento) {
			try {					
			
				String caminho = Faces.getRealPath("/report/relatorioVendaPeriodoFuncionarioLog.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_PROFISSIONAL_LOG",funcionarioSelecionado.getCodigo());
				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "funcionarioSelecionado..................:"+funcionarioSelecionado.getCodigo());
				System.out.println( "venda.getDataInicial(..................:"+venda.getDataInicial());
				System.out.println( ",venda.getDataFinal()..................:"+venda.getDataFinal());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
	//imprimirVendaPeriodoFormaPagamento
		
		public void imprimirVendaPeriodoFormaPagamento(ActionEvent evento) {
			try {					
			
				String caminho = Faces.getRealPath("/report/relatorioVendaPeriodoFormaPgto.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_FORMA_PGTO",formaSelecionada.getCodigo());
				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "formaSelecionada..................:"+formaSelecionada.getCodigo());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
	//imprimirVendaPeriodotipoPagamento
		
		public void imprimirVendaPeriodoTipoPagamento(ActionEvent evento) {
			try {					
			
				String caminho = Faces.getRealPath("/report/relatorioVendaPeriodoTipoPgto.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_TIPO_PGTO",tipoSelecionado.getCodigo());
				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "tipoSelecionado..................:"+tipoSelecionado.getCodigo());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
	
		//imprimirProdutoVendidoPeriodoVendedor
				public void imprimirProdutoVendidoPeriodoVendedor(ActionEvent evento) {
			try {					
				String caminho = Faces.getRealPath("/report/relatorioProdutoVendidoVendedor.jasper");


				Map<String, Object> parametros = new HashMap<>();
				
				parametros.put("COD_VENDEDOR",funcionarioSelecionado.getCodigo());
				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "funcionarioSelecionado..................:"+funcionarioSelecionado.getCodigo());
				System.out.println( "Data inicial--"+venda.getDataInicial()+"Data Final--"+venda.getDataFinal());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
				//imprimirProdutoVendidoPeriodo
				public void imprimirProdutoVendidoPeriodo(ActionEvent evento) {
			try {					
				String caminho = Faces.getRealPath("/report/relatorioProdutoVendidoPeriodo.jasper");


				Map<String, Object> parametros = new HashMap<>();

				parametros.put("DATA_INICIAL",venda.getDataInicial());
				parametros.put("DATA_FINAL",venda.getDataFinal());
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "Data inicial--"+venda.getDataInicial()+"Data Final--"+venda.getDataFinal());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
				//imprimirProdutoCodigo
				public void imprimirProdutoCodigo(ActionEvent evento) {
			try {					
				ItemVenda  imprimirItemVendaSelecionada = (ItemVenda) evento.getComponent()	.getAttributes().get("itemSelecionado");
				
				String caminho = Faces.getRealPath("/report/relatorioProdutoPorCodigo.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("COD_PRODUTO",imprimirItemVendaSelecionada.getProduto().getCodigo());

				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
				System.out.println( "imprimirItemVendaSelecionada.getCodigo()"+imprimirItemVendaSelecionada.getProduto().getCodigo());
				JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
				
	public List<Duplicata> getDuplicatasGeradas() {
		return duplicatasGeradas;
	}
	

	public void setDuplicatasGeradas(List<Duplicata> duplicatasGeradas) {
		this.duplicatasGeradas = duplicatasGeradas;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<FormaPagamento> getFormasPagamentos() {
		return FormasPagamentos;
	}

	public void setFormasPagamentos(List<FormaPagamento> formasPagamentos) {
		FormasPagamentos = formasPagamentos;
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

	public Duplicata getDuplicata() {
		return duplicata;
	}

	public List<Duplicata> getDuplicatas() {
		return duplicatasGeradas;
	}

	public void setDuplicatas(List<Duplicata> duplicatas) {
		this.duplicatasGeradas = duplicatas;
	}

	public void setDuplicata(Duplicata duplicata) {
		this.duplicata = duplicata;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public List<Venda> getListaVendas() {
		return listaVendas;
	}

	public void setListaVendas(List<Venda> listaVendas) {
		this.listaVendas = listaVendas;
	}

	public Funcionario getFuncionarioVenda() {
		return funcionarioVenda;
	}

	public void setFuncionarioVenda(Funcionario funcionarioVenda) {
		this.funcionarioVenda = funcionarioVenda;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public FormaPagamento getFormaPagamentoVenda() {
		return formaSelecionada;
	}

	public void setFormaPagamentoVenda(FormaPagamento formaPagamentoVenda) {
		this.formaSelecionada = formaPagamentoVenda;
	}

	public TipoPagamento getTipoPagamentoVenda() {
		return tipoSelecionado;
	}

	public void setTipoPagamentoVenda(TipoPagamento tipoPagamentoVenda) {
		this.tipoSelecionado = tipoPagamentoVenda;
	}

	public List<Venda> getListaVendasParceladas() {
		return listaVendasParceladas;
	}

	public void setListaVendasParceladas(List<Venda> listaVendasParceladas) {
		this.listaVendasParceladas = listaVendasParceladas;
	}

	public Long getCodigoVendaSalva() {
		return codigoVendaSalva;
	}

	public void setCodigoVendaSalva(Long codigoVendaSalva) {
		this.codigoVendaSalva = codigoVendaSalva;
	}

	
}