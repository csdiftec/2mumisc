package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
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

import br.com.diftecnologia.dao.AberturaOcorrenciaDAO;
import br.com.diftecnologia.dao.CaixaDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.TerminalAtendimentoDAO;
import br.com.diftecnologia.dao.VendaDAO;
import br.com.diftecnologia.domain.AberturaOcorrencia;
import br.com.diftecnologia.domain.Caixa;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.TerminalAtendimento;
import br.com.diftecnologia.domain.Venda;
import br.com.diftecnologia.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CaixaBean implements Serializable {

	private TerminalAtendimento terminal;
	private Caixa caixa;
	byte numTerminal;
	byte terminalAberto;
	private boolean botaoAbrirOcorrencia=true;
	private boolean botaoAberturaCaixa=true;
	private boolean botaoNovaVenda=false;
	long flagQtdOcorrenciaPorCaixa = 0;
	
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	private Funcionario funcionario;
	private AberturaOcorrencia ocorrencia;
	private List<Funcionario> funcionarios;
	private Long flagExistenciaOcorrencia;
	private Caixa fechamentoCaixa;
	private Long codigoOcorrenciaSalva, codigoFechamentoSalvo;


	private List<Venda> vendas;
	private TerminalAtendimento terminalAtendimento;

	
	public void contaQtdTerminaisAbertos() {
		try {
		
			confereStatusFlagSituacao();
			terminal = new TerminalAtendimento();
			caixa = new Caixa();
			caixa.setDataAbertura(new Date());
			caixa.setValorInicial(new BigDecimal(0));
			TerminalAtendimentoDAO terminalDAO = new TerminalAtendimentoDAO();
			long termAtendimento = (long) terminalDAO.ContarTerminaisAberto();
			System.out.println("numero terminal.......... "+termAtendimento);
			if (termAtendimento == 0) {
				terminal.setTituloStatus("Terminais ocupado");
				this.botaoAberturaCaixa=false;
				this.botaoNovaVenda=false;
			} else {
				terminalAberto = terminalDAO.buscarTerminalAberto();
				terminal.setTituloStatus("         " + terminalAberto + "  Aberto");
				this.botaoAberturaCaixa=true;
				TerminalAtendimentoDAO buscaTerminal = new TerminalAtendimentoDAO();
				terminalAberto = buscaTerminal.buscarTerminalAberto();
				terminal.setTituloStatus("Terminal Aberto:" + terminalAberto);
			
			}

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar contar terminais abertos!");
			erro.printStackTrace();
		}
	}
	
	private String confereStatusFlagSituacao() {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		funcionario= funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo());
		if(funcionario.getFlagSituacaoCaixa().equals(1)){
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			 RequestContext.getCurrentInstance().execute("PF('dialogoFechamento').show();");
			System.out.println("Igual a false ...."+autenticacaoBean.getFuncionarioLogado().getFlagSituacaoCaixa());
		

			MostrarCaixaAbertoPorFuncionario();
	//		this.botaoNovaVenda=false;
			Messages.addGlobalInfo("Existe caixa em aberto para este profissional");
			return "/pages/principal.xhtml?faces-redirect=true";
		}else if(funcionario.getFlagSituacaoCaixa().equals(0)){
			 RequestContext.getCurrentInstance().execute("PF('dialogo').show();");
			 RequestContext.getCurrentInstance().execute("PF('dialogoFechamento').hide();");

			System.out.println("Igual a true ...."+autenticacaoBean.getFuncionarioLogado().getFlagSituacaoCaixa());
	//		this.botaoNovaVenda=true;
			return "/pages/principal.xhtml?faces-redirect=true";
		}
		return "/pages/principal.xhtml?faces-redirect=true";
		
	}

	public void novo() {
		try {
				 FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				ocorrencia = new AberturaOcorrencia();
				funcionarios = funcionarioDAO.listar();
				ocorrencia.setDataAbertura(new Date());	
				botaoAbrirOcorrencia=false;
				RequestContext.getCurrentInstance().execute("PF('dialogoAberturaOcorrencia').hide();");
				 RequestContext.getCurrentInstance().execute("PF('dialogoAbertura').hide();");
					
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar abrir o caixa!");
			erro.printStackTrace();
		}
	}
@PostConstruct
public void novaOcorrencia(){
	try {
			 FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			ocorrencia = new AberturaOcorrencia();
			funcionarios = funcionarioDAO.listar();
			ocorrencia.setDataAbertura(new Date());	

	} catch (RuntimeException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar abrir o caixa!");
		erro.printStackTrace();
	}
}

	public void salvar() {
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 

			TerminalAtendimentoDAO terminalDAO = new TerminalAtendimentoDAO();
			TerminalAtendimento termAberto = terminalDAO.buscar((long) terminalAberto);

			funcionario.setFlagSituacaoCaixa(1);
			
			funcionarioDAO.merge(funcionario);
			
			caixa.setDataAbertura(new Date());
			caixa.setFuncionario(funcionario);
			caixa.setTerminalAtendimento(termAberto);
			caixa.setValorInicial(caixa.getValorInicial());
			
			terminal.setCodigo((long) terminalAberto);
			terminal.setNumeroTerminal(termAberto.getNumeroTerminal());
			terminal.setStatus(true);
			
			TerminalAtendimentoDAO terminalAtendDAO= new TerminalAtendimentoDAO();
			terminalAtendDAO.merge(terminal);
			
			CaixaDAO caixaDAO = new CaixaDAO();
			caixaDAO.merge(caixa);
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
				Messages.addGlobalInfo("Caixa aberto com sucesso");
	 
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar abrir o caixa");
			erro.printStackTrace();
		}
	}

	public void verificaSituacaoCaixa() {
		TerminalAtendimentoDAO terminalDAO = new TerminalAtendimentoDAO();
		byte numTerminalAberto = terminalDAO.buscarTerminalAberto();
try{
		if (numTerminalAberto == terminalAberto) {
			
			System.out.println("Terminal aberto");
				this.botaoNovaVenda=true;
			salvar();
		} else {
					this.botaoNovaVenda=false;
			Messages.addGlobalError("Terminal já ocupado, atualize a tela para abrir outro terminal");
				}
}catch(RuntimeException erro){
	erro.printStackTrace();
}
	}
	
	//-----------------------------------------------------------------------------------------------------
	
//	@PostConstruct
	public void MostrarCaixaAbertoPorFuncionario(){

		CaixaDAO caixaDAO = new CaixaDAO();
		VendaDAO vendaDAO=new VendaDAO();
		
		fechamentoCaixa = caixaDAO.caixaAbertoProfissionalLogado(autenticacaoBean.getFuncionarioLogado().getCodigo());
		System.err.println(".................................................................................."); 
	
		fechamentoCaixa.setValorTotalFechamento(fechamentoCaixa.getValorInicial());
		System.err.println("Data da abertura......."+fechamentoCaixa.getDataAbertura()); 
	
		vendas = (List<Venda>) vendaDAO.buscaVendaDataProfissionalLogadoAvista((Timestamp) fechamentoCaixa.getDataAbertura(), autenticacaoBean.getFuncionarioLogado().getCodigo());
	System.out.println("vendas........."+fechamentoCaixa);
	
	calcularFechamento();
	}

	private void calcularFechamento() {
		fechamentoCaixa.setValorTotalVenda(new BigDecimal("0.00"));
		fechamentoCaixa.setValorTotalFechamento(new BigDecimal("0.00"));
		fechamentoCaixa.setValorTotalFechamento(fechamentoCaixa.getValorInicial());
		for (int posicao = 0; posicao < vendas.size(); posicao++) {
			Venda venda =  vendas.get(posicao);
			//se a venda diferente de parcelado faz calculo a baixo
			System.out.println("........................................................"+venda.getDescricaoTipoPagamento());
	
			fechamentoCaixa.setValorTotalVenda(fechamentoCaixa.getValorTotalVenda().add(venda.getValorTotal()));
			fechamentoCaixa.setValorTotalFechamento(fechamentoCaixa.getValorTotalVenda().add(fechamentoCaixa.getValorInicial()));
			
			System.err.println("Valor Total Fechamento....."+fechamentoCaixa.getValorTotalFechamento()); 
			System.err.println("Valor Total da venda............"+venda.getValorTotal()); 
			System.err.println("................................................"); 
			System.err.println("..............................................."+venda); 
			System.err.println("..............................................."+fechamentoCaixa.getValorTotalFechamento()); 
			System.err.println(".................................................................................."); 

		}
		
	}
	public void salvarFechamento(){
		try{
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 

			funcionario.setFlagSituacaoCaixa(0);
			
			funcionarioDAO.merge(funcionario);
			
		CaixaDAO caixaDAO = new CaixaDAO();
		fechamentoCaixa.setDataFechamento(new Date());
		fechamentoCaixa.setValorTotalFechamento(fechamentoCaixa.getValorTotalFechamento());
		codigoFechamentoSalvo= fechamentoCaixa.getCodigo();

		caixaDAO.merge(fechamentoCaixa);
		
		TerminalAtendimentoDAO termAtendimentoDAO =new TerminalAtendimentoDAO();
		terminalAtendimento  = termAtendimentoDAO.buscar(fechamentoCaixa.getTerminalAtendimento().getCodigo());
	
		terminalAtendimento.setStatus(false);
		System.out.println("terminalAtendimento........"+terminalAtendimento);
		termAtendimentoDAO.merge(terminalAtendimento);
		

		confereStatusFlagSituacao();
		 RequestContext.getCurrentInstance().execute("PF('dialogoFechamento').hide();");
		 contaQtdTerminaisAbertos();
		 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			RequestContext.getCurrentInstance().execute("PF('dialogoAbertura').show();");
		Messages.addGlobalInfo("Caixa fechado com sucesso");
		}catch(RuntimeException erro){
			Messages.addGlobalError("Fechamento não pode ser realizado");
			erro.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------------
	public void salvarOcorrencia(){
	try{
		verificaExistenciaOcorrencia();
		if(flagQtdOcorrenciaPorCaixa==0){

			AberturaOcorrenciaDAO aberturaOcorrenciaDAO = new AberturaOcorrenciaDAO();
			
			ocorrencia.setStatus(true);
			CaixaDAO caixaDAO = new CaixaDAO();
			fechamentoCaixa = caixaDAO.buscar(fechamentoCaixa.getCodigo());
			System.out.println("fechamentoCaixa  codigo....................:"+fechamentoCaixa.getCodigo());
			ocorrencia.setCaixa(fechamentoCaixa);
			
			aberturaOcorrenciaDAO.merge(ocorrencia);
		
			botaoAbrirOcorrencia=true;
				 RequestContext.getCurrentInstance().execute("PF('dialogoAberturaOcorrencia').hide();");
				 RequestContext.getCurrentInstance().execute("PF('dialogoAbertura').hide();");
				 RequestContext.getCurrentInstance().execute("PF('dialogoImprimirOcorrencia').show();");
			Messages.addGlobalInfo("Ocorrencia salva com sucesso");
			}
		else{
			Messages.addGlobalWarn("Ocorrencia já existente para este fechamento");
		}
	}catch(RuntimeException erro){
		Messages.addGlobalError("Ocorrencia não pode ser  aberta");
	}
	}
	
	public Long verificaExistenciaOcorrencia(){
		try{
		AberturaOcorrenciaDAO 	aberturaOcorrenciaDAO=new	AberturaOcorrenciaDAO();
		CaixaDAO caixaDAO = new CaixaDAO();
		fechamentoCaixa = caixaDAO.buscar(fechamentoCaixa.getCodigo());
		System.out.println("verifica....................:"+fechamentoCaixa.getCodigo());
		flagQtdOcorrenciaPorCaixa=aberturaOcorrenciaDAO.verificaExistenciaOcorrencia(fechamentoCaixa.getCodigo() );

		}catch(RuntimeException erro){
			System.err.println("Erro ao verificar existencia da ocorrencia   :"+erro);
		}
		return flagQtdOcorrenciaPorCaixa;
	}

	public void ImprimirUltimaOcorrencia(ActionEvent evento) {
		try {
			AberturaOcorrenciaDAO 	aberturaOcorrenciaDAO=new	AberturaOcorrenciaDAO();
			codigoOcorrenciaSalva=	aberturaOcorrenciaDAO.buscarUltimaOcorenciaAberta();
			
			String caminho = Faces.getRealPath("//report//relatorioOcorrenciaAbertaPorCodigo.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("CODIGO_OCORRENCIA",codigoOcorrenciaSalva);
			
			Connection conexao = HibernateUtil.getConexao();
	        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
	      
	        JasperViewer.viewReport(jp, false);
			System.out.println("Codigo da ocorrencia no imprimir ......:"+codigoOcorrenciaSalva);
		
			
			RequestContext.getCurrentInstance().execute("PF('dialogoImprimirOcorrencia').hide();");
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	
	
	public void ImprimirFechamento(ActionEvent evento) {
		try {
		
			String caminho = Faces.getRealPath("//report//relatorioFechamentoPorCodigo.jasper");

			Map<String, Object> parametros = new HashMap<>();

			parametros.put("CODIGO_FECHAMENTO",codigoFechamentoSalvo);
			
			Connection conexao = HibernateUtil.getConexao();
	        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
	      
	        JasperViewer.viewReport(jp, true);
			System.out.println("Codigo do fechamento no imprimir ......:"+codigoFechamentoSalvo);
		
			
		} catch (JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
			erro.printStackTrace();
		}
}
	
	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public TerminalAtendimento getTerminal() {
		return terminal;
	}

	public void setTerminal(TerminalAtendimento terminal) {
		this.terminal = terminal;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Caixa getFechamentoCaixa() {
		return fechamentoCaixa;
	}

	public void setFechamentoCaixa(Caixa fechamentoCaixa) {
		this.fechamentoCaixa = fechamentoCaixa;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public TerminalAtendimento getTerminalAtendimento() {
		return terminalAtendimento;
	}

	public void setTerminalAtendimento(TerminalAtendimento terminalAtendimento) {
		this.terminalAtendimento = terminalAtendimento;
	}
	

public AberturaOcorrencia getOcorrencia() {
	return ocorrencia;
}

public void setOcorrencia(AberturaOcorrencia ocorrencia) {
	this.ocorrencia = ocorrencia;
}

public List<Funcionario> getFuncionarios() {
	return funcionarios;
}

public void setFuncionarios(List<Funcionario> funcionarios) {
	this.funcionarios = funcionarios;
}

public Long getFlagExistenciaOcorrencia() {
	return flagExistenciaOcorrencia;
}

public void setFlagExistenciaOcorrencia(Long flagExistenciaOcorrencia) {
	this.flagExistenciaOcorrencia = flagExistenciaOcorrencia;
}


public boolean isBotaoAbrirOcorrencia() {
	return botaoAbrirOcorrencia;
}


public void setBotaoAbrirOcorrencia(boolean botaoAbrirOcorrencia) {
	this.botaoAbrirOcorrencia = botaoAbrirOcorrencia;
}


public boolean isBotaoAberturaCaixa() {
	return botaoAberturaCaixa;
}


public void setBotaoAberturaCaixa(boolean botaoAberturaCaixa) {
	this.botaoAberturaCaixa = botaoAberturaCaixa;
}


public boolean isBotaoNovaVenda() {
	return botaoNovaVenda;
}


public void setBotaoNovaVenda(boolean botaoNovaVenda) {
	this.botaoNovaVenda = botaoNovaVenda;
}

public Long getCodigoOcorrenciaSalva() {
	return codigoOcorrenciaSalva;
}

public void setCodigoOcorrenciaSalva(Long codigoOcorrenciaSalva) {
	this.codigoOcorrenciaSalva = codigoOcorrenciaSalva;
}

public Long getCodigoFechamentoSalvo() {
	return codigoFechamentoSalvo;
}

public void setCodigoFechamentoSalvo(Long codigoFechamentoSalvo) {
	this.codigoFechamentoSalvo = codigoFechamentoSalvo;
}

}