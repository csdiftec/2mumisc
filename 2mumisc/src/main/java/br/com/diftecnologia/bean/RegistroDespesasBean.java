package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.DespesasDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.PagamentoDespesaDAO;
import br.com.diftecnologia.dao.RegistroDespesasDAO;
import br.com.diftecnologia.domain.Despesas;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.PagamentoDespesa;
import br.com.diftecnologia.domain.RegistroDespesas;
import br.com.diftecnologia.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class RegistroDespesasBean implements Serializable {
	private RegistroDespesas registroDespesas;
	private List<RegistroDespesas> listaRegistroDespesas;
	private List<Despesas> listaDespesas;
	BigDecimal valorTotalAtualTemp;
	public Despesas tipoDespesasSelecionado;
	public String despesas;
	public long numeroDaDespesa;
	private List<PagamentoDespesa> listaPagamentosDespesas;
private PagamentoDespesa pagamentoDespesaSelecionada; 
@ManagedProperty(value="#{autenticacaoBean}")
private AutenticacaoBean autenticacaoBean;

	@PostConstruct
	public void novo() {
		registroDespesas = new RegistroDespesas();
		RegistroDespesasDAO registroDespesasDAO = new RegistroDespesasDAO();
		DespesasDAO despesasDAO = new DespesasDAO();
		PagamentoDespesaDAO pagamentoDespesaDAO = new PagamentoDespesaDAO();
		listaPagamentosDespesas=	pagamentoDespesaDAO.listarUltimas1000PagamentosDespesas();
		listaDespesas = despesasDAO.listar();
		listar();
		listaRegistroDespesas = registroDespesasDAO.listarUltimas1000Contas();
	}

	@PostConstruct
	public void listar() {
		try {
			RegistroDespesasDAO registroDAO = new RegistroDespesasDAO();
			listaRegistroDespesas = registroDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		try {
			registroDespesas = (RegistroDespesas) evento.getComponent().getAttributes().get("registroSelecionado");

			DespesasDAO despesasDAO = new DespesasDAO();
			listaDespesas = despesasDAO.listar();
			valorTotalAtualTemp = registroDespesas.getValorTotalAtual();

			zerarValores();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar a despesa!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			RegistroDespesasDAO RegistroDespesasDAO = new RegistroDespesasDAO();
			DespesasDAO despesasDAO = new DespesasDAO();

			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo());

			Despesas despesa = despesasDAO.buscar(registroDespesas.getDespesas().getCodigo());

			registroDespesas.setDespesas(despesa);
			registroDespesas.setDataRegistro(new java.util.Date());
			registroDespesas.setFuncionario(funcionario);
			registroDespesas.setValorTotalAtual(registroDespesas.getValorTotal());
			registroDespesas.setValorDesconto(new BigDecimal(0));
			registroDespesas.setValorJuros(new BigDecimal(0));
			registroDespesas.setValorPago(new BigDecimal(0));
			
			RegistroDespesasDAO.merge(registroDespesas);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			Messages.addGlobalInfo("Registro salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o registro!");
			erro.printStackTrace();
		}
	}

	public void calcular() {
		valorTotalAtualTemp = registroDespesas.getValorTotalAtual();
		BigDecimal valorTotalAtual = registroDespesas.getValorTotalAtual();
		BigDecimal desconto = registroDespesas.getValorDesconto();
		BigDecimal juros = registroDespesas.getValorJuros();
		BigDecimal valorPago = registroDespesas.getValorPago();
		BigDecimal resultado = new BigDecimal(0.0);

		resultado = valorTotalAtual.add(juros).subtract(valorPago.add(desconto));
		System.out.println("resultado calculo:" + resultado);
		BigDecimal valorZero = new BigDecimal(0.0);
		if (resultado.compareTo(valorZero) < 0) {
			Messages.addGlobalError("Calculo não realizado, resultado menor que 0");
		} else {
			registroDespesas.setValorTotalAtual(resultado);
		}
	}

	public void zerarValores() {
		registroDespesas.setValorTotalAtual(valorTotalAtualTemp);
		registroDespesas.setValorDesconto(new BigDecimal(0.0));
		registroDespesas.setValorJuros(new BigDecimal(0.0));
		registroDespesas.setValorPago(new BigDecimal(0.0));
	}

	public void salvarPagamento() {
		PagamentoDespesa pgtoPagamento = new PagamentoDespesa();
		PagamentoDespesaDAO pgtoPagamentoDAO = new PagamentoDespesaDAO();
		RegistroDespesasDAO regDespesas = new RegistroDespesasDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {

			RegistroDespesas codigoRegistroDespesa = regDespesas.buscar(registroDespesas.getCodigo());

			registroDespesas.setValorTotalAtual(registroDespesas.getValorTotalAtual());
			Funcionario funcionario = funcionarioDAO.buscar(1L);

			pgtoPagamento.setDataPagamento(new Date());
			pgtoPagamento.setRegistroDespesa(codigoRegistroDespesa);
			pgtoPagamento.setValorDesconto(registroDespesas.getValorDesconto());
			pgtoPagamento.setValorJuros(registroDespesas.getValorJuros());
			pgtoPagamento.setValorPago(registroDespesas.getValorPago());
			pgtoPagamento.setFuncionario(funcionario);
			regDespesas.merge(registroDespesas);
			pgtoPagamentoDAO.merge(pgtoPagamento);
			 RequestContext.getCurrentInstance().execute("PF('dialogoPagamento').hide();");
			Messages.addGlobalInfo("Pagamento salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o pagamento!");
			erro.printStackTrace();
		}
	}
	public List<RegistroDespesas>  buscaPorPeriodo(){
		try{
			RegistroDespesasDAO registroDespesasDAO = new RegistroDespesasDAO();
			listaRegistroDespesas = registroDespesasDAO.listarContasPagarPeriodo(registroDespesas.getDataInicial(), registroDespesas.getDataFinal());
		
			}catch(RuntimeException ex){
			
	System.err.println(ex);
			}
		return listaRegistroDespesas;
		}

	public List<RegistroDespesas>  buscaPorPeriodoTipoDespesa(){
		try{
			RegistroDespesasDAO registroDespesasDAO = new RegistroDespesasDAO();
			listaRegistroDespesas = registroDespesasDAO.listarContasPagarPeriodoTipoDespesa(registroDespesas.getDataInicial(), registroDespesas.getDataFinal(),
					tipoDespesasSelecionado.getCodigo());
		
			}catch(RuntimeException ex){
			
	System.err.println(ex);
			}
		return listaRegistroDespesas;
		}


	public List<PagamentoDespesa>  buscaPorPeriodoPagamentoDespesas(){
		try{
			PagamentoDespesaDAO pagamentoDespesaDAO = new PagamentoDespesaDAO();
			listaPagamentosDespesas = pagamentoDespesaDAO.listarPagamentoDespesaPorPeriodoDespesa(registroDespesas.getDataInicial(), registroDespesas.getDataFinal(),
					registroDespesas.getCodigoDespesa());
		
			}catch(RuntimeException ex){
			
	System.err.println(ex);
			}
		return listaPagamentosDespesas;
		}
	
	public List<PagamentoDespesa>  buscaPorPeriodoPagamento(){
		try{
			PagamentoDespesaDAO pagamentoDespesaDAO = new PagamentoDespesaDAO();
			listaPagamentosDespesas = pagamentoDespesaDAO.listarPagamentoDespesaPorPeriodo(registroDespesas.getDataInicial(), registroDespesas.getDataFinal());
		
			}catch(RuntimeException ex){
			
	System.err.println(ex);
			}
		return listaPagamentosDespesas;
		}


	
	public void selecionarTipoDespesa(ActionEvent evento) {
		try {
			tipoDespesasSelecionado =  (Despesas) evento.getComponent().getAttributes().get("tipoDespesaSelecionado");
			despesas = tipoDespesasSelecionado.getDescricao();
			registroDespesas.setFlagDespesaSelecionada(despesas);

		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar o fornecedor:  "+ e);
		}
	}
	public void selecionarPagamentoDespesas(ActionEvent evento) {
		try {
			registroDespesas =   (RegistroDespesas) evento.getComponent().getAttributes().get("pagamentoDespesaSelecionada");
			System.out.println("registroDespesas ................"+registroDespesas);
			
			numeroDaDespesa = registroDespesas.getCodigoDespesa();
System.out.println("numeroDaDespesa..........."+numeroDaDespesa);
registroDespesas.setFlagPagamentoDespesaSelecionada(numeroDaDespesa);
			
		
		} catch (Exception e) {
			Messages.addGlobalError("Não foi possivel selecionar o fornecedor:  "+ e);
		}
	}

	//imprimirContaPagarPeriodo
	
		public void imprimirContaPagarPeriodo(ActionEvent evento) {
			try {					
			
				String caminho = Faces.getRealPath("/report/relatorioContaPagarPeriodo.jasper");

				Map<String, Object> parametros = new HashMap<>();

				parametros.put("DATA_INICIAL",registroDespesas.getDataInicial());
				parametros.put("DATA_FINAL",registroDespesas.getDataFinal());
				
				
				Connection conexao = HibernateUtil.getConexao();

				JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			
				System.out.println( "registroDespesas.getDataInicial(..................:"+registroDespesas.getDataInicial());
				System.out.println( "registroDespesas.getDataFinal()..................:"+registroDespesas.getDataFinal());
				 JasperViewer.viewReport(relatorio, false);
			} catch (JRException erro) {
				Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
				erro.printStackTrace();
			}
	}
	
	
		//imprimirContaPagarPorTipoDespesa
		
				public void imprimirContaPagarPorTipoDespesa(ActionEvent evento) {
					try {					
					
						String caminho = Faces.getRealPath("/report/relatorioContaPagarTipoDespesa.jasper");

						Map<String, Object> parametros = new HashMap<>();

						parametros.put("COD_DESPESA",tipoDespesasSelecionado.getCodigo());
						parametros.put("DATA_INICIAL",registroDespesas.getDataInicial());
						parametros.put("DATA_FINAL",registroDespesas.getDataFinal());
						
						
						Connection conexao = HibernateUtil.getConexao();

						JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
						System.out.println( "tipoDespesasSelecionado...................:"+tipoDespesasSelecionado.getCodigo());
						System.out.println( "registroDespesas.getDataInicial(..................:"+registroDespesas.getDataInicial());
						System.out.println( "registroDespesas.getDataFinal()..................:"+registroDespesas.getDataFinal());
						 JasperViewer.viewReport(relatorio, false);
					} catch (JRException erro) {
						Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
						erro.printStackTrace();
					}
			}
				
				//imprimirPagamentoDespesaPorPeriodo
					public void imprimirPagamentoDespesaPeriodo(ActionEvent evento) {
					try {					
					
						String caminho = Faces.getRealPath("/report/relatorioPgtoDespesaPeriodo.jasper");

						Map<String, Object> parametros = new HashMap<>();

						parametros.put("DATA_INICIAL",registroDespesas.getDataInicial());
						parametros.put("DATA_FINAL",registroDespesas.getDataFinal());
						
						
						Connection conexao = HibernateUtil.getConexao();

						JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
						System.out.println( "registroDespesas.getDataInicial(..................:"+registroDespesas.getDataInicial());
						System.out.println( "registroDespesas.getDataFinal()..................:"+registroDespesas.getDataFinal());
						 JasperViewer.viewReport(relatorio, false);
					} catch (JRException erro) {
						Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
						erro.printStackTrace();
					}
			}
					
				//imprimirPagamentoDespesaPorDespesaSelecionada
				public void imprimirPagamentoDespesaSelecionada(ActionEvent evento) {
					try {					
					
						String caminho = Faces.getRealPath("/report/relatorioPagamentoDespesa.jasper");

						Map<String, Object> parametros = new HashMap<>();

						parametros.put("COD_REGISTRO_PGTO",registroDespesas.getCodigo());
						parametros.put("DATA_INICIAL",registroDespesas.getDataInicial());
						parametros.put("DATA_FINAL",registroDespesas.getDataFinal());
						
						
						Connection conexao = HibernateUtil.getConexao();

						JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
						System.out.println( "egistroDespesas.getCodigo...................:"+registroDespesas.getCodigo());
						System.out.println( "registroDespesas.getDataInicial(..................:"+registroDespesas.getDataInicial());
						System.out.println( "registroDespesas.getDataFinal()..................:"+registroDespesas.getDataFinal());
						 JasperViewer.viewReport(relatorio, false);
					} catch (JRException erro) {
						Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
						erro.printStackTrace();
					}
			}
				
				//imprimirContaPagarPorCodigo
				
				public void imprimirContaPagarPorCodigo(ActionEvent evento) {
					try {	
						RegistroDespesas	registroDespesaSelecionado=   (RegistroDespesas) evento.getComponent().getAttributes().get("registroDespesaSelecionado");
						System.out.println("registroDespesas ................"+registroDespesaSelecionado.getCodigo());
						
						String caminho = Faces.getRealPath("/report/relatorioContaPagarPorCodigo.jasper");

					Map<String, Object> parametros = new HashMap<>();

					parametros.put("COD_REGISTRO",registroDespesaSelecionado.getCodigo());
										
					Connection conexao = HibernateUtil.getConexao();

					JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
					System.out.println( "registroDespesaSelecionado.getCodigo...................:"+registroDespesaSelecionado.getCodigo());
				
					 JasperViewer.viewReport(relatorio, false);
				} catch (JRException erro) {
					Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
					erro.printStackTrace();
				}
		}		
				
						
				//imprimirContaPagarPorCodigoPagamento
				public void imprimirContaPagarPorCodigoPagamento(ActionEvent evento) {
					try {	
						pagamentoDespesaSelecionada	=    (PagamentoDespesa) evento.getComponent().getAttributes().get("registroDespesaSelecionado");
						System.out.println("pagamentoDespesaSelecionada ................"+pagamentoDespesaSelecionada.getCodigo());
						
						String caminho = Faces.getRealPath("//report//relatorioPorCodigoPagamentoDespesa.jasper");

					Map<String, Object> parametros = new HashMap<>();
					
			Connection conexao = HibernateUtil.getConexao();
					parametros.put("COD_REGISTRO",pagamentoDespesaSelecionada.getCodigo());

					JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
					System.out.println("pagamentoDespesaSelecionada ................"+pagamentoDespesaSelecionada.getCodigo());
				
					 JasperViewer.viewReport(relatorio, false);
				} catch (JRException erro) {
					Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
					erro.printStackTrace();
				}
		}		
	public RegistroDespesas getRegistroDespesas() {
		return registroDespesas;
	}

	public void setRegistroDespesas(RegistroDespesas registroDespesas) {
		this.registroDespesas = registroDespesas;
	}

	public List<RegistroDespesas> getListaRegistroDespesas() {
		return listaRegistroDespesas;
	}

	public void setListaRegistroDespesas(List<RegistroDespesas> listaRegistroDespesas) {
		this.listaRegistroDespesas = listaRegistroDespesas;
	}

	public List<Despesas> getListaDespesas() {
		return listaDespesas;
	}

	public void setListaDespesas(List<Despesas> listaDespesas) {
		this.listaDespesas = listaDespesas;
	}

	public Despesas getTipoDespesasSelecionado() {
		return tipoDespesasSelecionado;
	}

	public void setTipoDespesasSelecionado(Despesas tipoDespesasSelecionado) {
		this.tipoDespesasSelecionado = tipoDespesasSelecionado;
	}

	public String getDespesas() {
		return despesas;
	}

	public void setDespesas(String despesas) {
		this.despesas = despesas;
	}

	public List<PagamentoDespesa> getListaPagamentosDespesas() {
		return listaPagamentosDespesas;
	}

	public void setListaPagamentosDespesas(
			List<PagamentoDespesa> listaPagamentosDespesas) {
		this.listaPagamentosDespesas = listaPagamentosDespesas;
	}

	public PagamentoDespesa getPagamentoDespesaSelecionada() {
		return pagamentoDespesaSelecionada;
	}

	public void setPagamentoDespesaSelecionada(
			PagamentoDespesa pagamentoDespesaSelecionada) {
		this.pagamentoDespesaSelecionada = pagamentoDespesaSelecionada;
	}

	public long getNumeroDaDespesa() {
		return numeroDaDespesa;
	}

	public void setNumeroDaDespesa(long numeroDaDespesa) {
		this.numeroDaDespesa = numeroDaDespesa;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}



}
