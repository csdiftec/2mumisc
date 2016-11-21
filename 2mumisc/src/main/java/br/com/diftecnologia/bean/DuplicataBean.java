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

import br.com.diftecnologia.dao.DuplicataDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.PagamentoDuplicataVendaDAO;
import br.com.diftecnologia.domain.Cliente;
import br.com.diftecnologia.domain.Duplicata;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.PagamentoDuplicataVenda;
import br.com.diftecnologia.domain.Venda;
import br.com.diftecnologia.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class DuplicataBean implements Serializable {
	Date dataAtual = new Date();
	private Duplicata duplicata;
	private List<Duplicata> listaDuplicatas;
	private List<Duplicata> listaDuplicatasVencidas;
	BigDecimal valorTotalAtualTemp ;
	public String nomeCliente;
	public Cliente clienteSelecionado;
	private Duplicata duplicataSelecionada;
	private 	Venda vendaSelecionada;
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;

	@PostConstruct
	public void novo() {
		try {
			duplicata = new Duplicata();
			
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		listaDuplicatas = duplicataDAO.listarUltimas1000Duplicatas();
		
        listaDuplicatasVencidas = duplicataDAO.listarDuplicatasVencidas();
System.out.println("dataAtual   Bean -------------"+dataAtual);
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo produto!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		try {
			duplicata = (Duplicata) evento.getComponent().getAttributes().get("duplicataSelecionada");
			
			valorTotalAtualTemp = duplicata.getValorTotalAtual();
			
			duplicata.setDataUltimoPagamento(new Date());
			
			if(duplicata.getDataVencimento().before(dataAtual)){
				duplicata.setStatus("Pendente");
			}
			if(duplicata.getValorTotalAtual().compareTo(BigDecimal.ZERO) == 0){
				duplicata.setStatus("Finalizado");
			}
		
			
			zerarValores();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar um produto!");
			erro.printStackTrace();
		}
	}
	
	public void salvar() {
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		PagamentoDuplicataVenda pgtoDuplicata = new PagamentoDuplicataVenda();
		PagamentoDuplicataVendaDAO pgtoDuplicataDAO = new PagamentoDuplicataVendaDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		try {
					
	duplicata.setDataUltimoPagamento(new Date());
	duplicata.setValorDesconto(duplicata.getValorDesconto());
	duplicata.setValorJuros( duplicata.getValorJuros());
	duplicata.setValorPago(duplicata.getValorPago());
	duplicata.setValorTotalAtual(duplicata.getValorTotalAtual());
	
	Duplicata duplicataGerada = duplicataDAO.buscar(duplicata.getCodigo());
	Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo());
	
	pgtoDuplicata.setValorDesconto(duplicata.getValorDesconto());
	pgtoDuplicata.setValorJuros(duplicata.getValorJuros());
	pgtoDuplicata.setValorPago(duplicata.getValorPago());
	pgtoDuplicata.setDuplicata(duplicataGerada);
	pgtoDuplicata.setDataPagamento(dataAtual);
	pgtoDuplicata.setFuncionario(funcionario);
	

	 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
	pgtoDuplicataDAO.salvar(pgtoDuplicata);
	
	if(duplicata.getValorTotalAtual().compareTo(BigDecimal.ZERO) == 0){
		duplicata.setStatus("Finalizada");
	}

	duplicataDAO.merge(duplicata);
			Messages.addGlobalInfo("Duplicata salva com sucesso");
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Erro ao salvar a duplicata");
			erro.printStackTrace();
		}
	}
	
public void calcular(){
	valorTotalAtualTemp = duplicata.getValorTotalAtual();
	BigDecimal valorTotalAtual = duplicata.getValorTotalAtual();
	BigDecimal desconto = duplicata.getValorDesconto();
	BigDecimal juros = duplicata.getValorJuros();
	BigDecimal valorPago = duplicata.getValorPago();
	BigDecimal  resultado = new BigDecimal(0.0);
		
	resultado=valorTotalAtual.add(juros).subtract(valorPago.add(desconto));
	System.out.println("resultado calculo:"+ resultado);
	BigDecimal valorZero = new BigDecimal(0.0);
	if(resultado.compareTo(valorZero)<0){
		Messages.addGlobalError("Calculo não realizado, resultado menor que 0");
	}else{
		duplicata.setValorTotalAtual(resultado);
	}
	
}
	
public void zerarValores(){
	duplicata.setValorTotalAtual(valorTotalAtualTemp);
	duplicata.setValorDesconto(new BigDecimal (0.0));
	duplicata.setValorJuros(new BigDecimal (0.0));
	duplicata.setValorPago(new BigDecimal (0.0));
}

public List<Duplicata> buscaDuplicatasPorPeriodoCliente() {
	try {
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		listaDuplicatas = duplicataDAO.listarDuplicatasPorPeriodoCliente(
				duplicata.getDataInicial(),duplicata.getDataFinal(),
				clienteSelecionado.getCodigo());

	} catch (RuntimeException ex) {

		System.err.println(ex);
	}
	return listaDuplicatas;
}

public List<Duplicata> buscaPorDuplicatasPorPeriodo() {
	try {
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		listaDuplicatas = duplicataDAO.listarDuplicatasPorPeriodo(
				duplicata.getDataInicial(),duplicata.getDataFinal());

	} catch (RuntimeException ex) {

		System.err.println(ex);
	}
	return listaDuplicatas;
}

public List<Duplicata> buscaPorPeriodoPagamentoDuplicata() {
	try {
		DuplicataDAO duplicataDAO = new DuplicataDAO();
		listaDuplicatas = duplicataDAO.listarDuplicatasPorPeriodo(
				duplicata.getDataInicial(),duplicata.getDataFinal());

	} catch (RuntimeException ex) {

		System.err.println(ex);
	}
	return listaDuplicatas;
}

public void selecionarCliente(ActionEvent evento) {
	try {
		clienteSelecionado = new Cliente();

		clienteSelecionado = (Cliente) evento.getComponent()	.getAttributes().get("clienteSelecionado");
		nomeCliente = clienteSelecionado.getPessoa().getNome();
		duplicata.setNomeCliente(nomeCliente);

	} catch (Exception e) {
		Messages.addGlobalError("Não foi possivel selecionar o cliente: "
				+ e);
	}
}

//imprimirDuplicataPorPeriodo
public void imprimirDuplicataPeriodo(ActionEvent evento) {
try {					

	String caminho = Faces.getRealPath("/report/relatorioDuplicatasPeriodo.jasper");

	Map<String, Object> parametros = new HashMap<>();

	parametros.put("DATA_INICIAL",duplicata.getDataInicial());
	parametros.put("DATA_FINAL",duplicata.getDataFinal());
	
	
	Connection conexao = HibernateUtil.getConexao();

	JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
	System.out.println( "	duplicata.getDataInicial()..................:"+	duplicata.getDataInicial());
	System.out.println( "	duplicata.getDataFinal()..................:"+	duplicata.getDataFinal());
    JasperViewer.viewReport(relatorio, false);
} catch (JRException erro) {
	Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
	erro.printStackTrace();
}
}

//imprimirDuplicataPorPeriodoCliente
public void imprimirDuplicataPeriodoCliente(ActionEvent evento) {
try {					
	String caminho = Faces.getRealPath("/report/relatorioDuplicatasPeriodoCliente.jasper");

	Map<String, Object> parametros = new HashMap<>();
	
	parametros.put("DATA_INICIAL",duplicata.getDataInicial());
	parametros.put("DATA_FINAL",duplicata.getDataFinal());
	parametros.put("	COD_CLIENTE",clienteSelecionado.getCodigo());
	
	Connection conexao = HibernateUtil.getConexao();

	JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
	 JasperViewer.viewReport(relatorio, true);

System.out.println( "	clienteSelecionado.getCodigo()..................:"+	clienteSelecionado.getCodigo());
System.out.println( "	duplicata.getDataInicial()..................:"+	duplicata.getDataInicial());
System.out.println( "	duplicata.getDataFinal()..................:"+	duplicata.getDataFinal());

} catch (JRException erro) {
	Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"+erro);
	erro.printStackTrace();
	System.err.println("Erro.......................................:"+erro);
}
}
//imprimirDuplicataPorCodigoVenda
	public void imprimirRelatorioDuplicataPorCodVenda(ActionEvent evento) {
		try {
			vendaSelecionada = (Venda) evento.getComponent()
					.getAttributes().get("vendaSelecionada");

			String caminho = Faces	.getRealPath("/report/relatorioDuplicataDetalhadaPorVenda.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("	COD_NUMERO_NF", vendaSelecionada.getNumeroDocumentoFiscal());
System.out.println("vendaSelecionada.NumeroDocumentoFiscal()............:"+vendaSelecionada.getNumeroDocumentoFiscal());
			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			 JasperViewer.viewReport(relatorio, true);

		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"
					+ erro);
			erro.printStackTrace();

		}
	}

	// imprimirListaDuplicatasPorCodigoVenda
	public void imprimirListaDuplicatasPorCodigoVenda(ActionEvent evento) {
		try {
			String caminho = Faces.getRealPath("/report/relatorioListaDuplicatasPorCodigoVenda.jasper");

			Map<String, Object> parametros = new HashMap<>();

			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho,	parametros, conexao);
			 JasperViewer.viewReport(relatorio, true);


		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"
					+ erro);
			erro.printStackTrace();
			System.err.println("Erro.......................................:"
					+ erro);
		}
	}
	// imprimirListaDuplicatasPorCodigoParcela
	public void imprimirListaDuplicatasPorCodigoParcela(ActionEvent evento) {
		try {
			duplicataSelecionada =  (Duplicata) evento.getComponent().getAttributes().get("duplicataSelecionada");
			
			String caminho = Faces.getRealPath("/report/relatorioDuplicatasPorCodigoParcela.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("COD_PARCELA", duplicataSelecionada.getCodigo());
System.out.println("duplicataSelecionada.getCodigo()............:"+duplicataSelecionada.getCodigo());

			Connection conexao = HibernateUtil.getConexao();

			JasperPrint relatorio = JasperFillManager.fillReport(caminho,	parametros, conexao);
			 JasperViewer.viewReport(relatorio, false);


		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório"
					+ erro);
			erro.printStackTrace();
			System.err.println("Erro.......................................:"+ erro);
		}
	}

public Duplicata getDuplicata() {
	return duplicata;
}

public void setDuplicata(Duplicata duplicata) {
	this.duplicata = duplicata;
}


public List<Duplicata> getDuplicatas() {
	return listaDuplicatas;
}


public void setDuplicatas(List<Duplicata> duplicatas) {
	this.listaDuplicatas = duplicatas;
}

public String getNomeCliente() {
	return nomeCliente;
}

public void setNomeCliente(String nomeCliente) {
	this.nomeCliente = nomeCliente;
}

public List<Duplicata> getListaDuplicatas() {
	return listaDuplicatas;
}

public void setListaDuplicatas(List<Duplicata> listaDuplicatas) {
	this.listaDuplicatas = listaDuplicatas;
}

public Cliente getClienteSelecionado() {
	return clienteSelecionado;
}

public void setClienteSelecionado(Cliente clienteSelecionado) {
	this.clienteSelecionado = clienteSelecionado;
}

public Duplicata getDuplicataSelecionada() {
	return duplicataSelecionada;
}

public void setDuplicataSelecionada(Duplicata duplicataSelecionada) {
	this.duplicataSelecionada = duplicataSelecionada;
}

public AutenticacaoBean getAutenticacaoBean() {
	return autenticacaoBean;
}

public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
	this.autenticacaoBean = autenticacaoBean;
}

public List<Duplicata> getListaDuplicatasVencidas() {
	return listaDuplicatasVencidas;
}

public void setListaDuplicatasVencidas(List<Duplicata> listaDuplicatasVencidas) {
	this.listaDuplicatasVencidas = listaDuplicatasVencidas;
}

}










