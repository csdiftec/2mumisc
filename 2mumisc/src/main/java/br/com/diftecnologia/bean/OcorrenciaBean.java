package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.sql.Connection;
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

import br.com.diftecnologia.dao.AberturaOcorrenciaDAO;
import br.com.diftecnologia.dao.FecharOcorrenciaDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.domain.AberturaOcorrencia;
import br.com.diftecnologia.domain.FecharOcorrencia;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.util.HibernateUtil;

@ManagedBean
@ViewScoped
public class OcorrenciaBean implements Serializable {
	private static final long serialVersionUID = 1L;
private AberturaOcorrencia ocorrencia;
private List<Funcionario> funcionarios;
private List<AberturaOcorrencia> listaOcorrenciasAberta;
private List<AberturaOcorrencia> listaOcorrencias;
private FecharOcorrencia fecharOcorrencia;
private Long CodigoAberturaOcorrenciaSalva;

@PostConstruct
public void novo(){
	FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
	funcionarios = funcionarioDAO.listar();
	
	fecharOcorrencia = new FecharOcorrencia();
	listarOcorrencias();
	listarOcorrenciasAbertas();
	System.out.println("Bean lista Ocorrencias..............." + listaOcorrenciasAberta);

}

public void editar(ActionEvent evento) {

	try {
	
		ocorrencia =  (AberturaOcorrencia) evento.getComponent().getAttributes().get("ocorrenciaSelecionada");			
	System.err.println("Ocorrencia  selecionada......."+ocorrencia);
			
		fecharOcorrencia.setDataFechamento(new Date());

	} catch (RuntimeException erro) {
		Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar a Ocorrencia!");
		erro.printStackTrace();
	}
}
public void listarOcorrencias(){
	AberturaOcorrenciaDAO aberturaOcorrenciaDAO=new AberturaOcorrenciaDAO();
	listaOcorrencias = aberturaOcorrenciaDAO.listar();
}
public void listarOcorrenciasAbertas(){
	AberturaOcorrenciaDAO aberturaOcorrenciaDAO=new AberturaOcorrenciaDAO();
	listaOcorrenciasAberta = aberturaOcorrenciaDAO.listarOcorrenciasAbertas();
}
public void salvar() {
	try {

FecharOcorrenciaDAO fecharOcorrenciaDAO = new FecharOcorrenciaDAO();

fecharOcorrencia.setAbrirOcorrencia(ocorrencia);

fecharOcorrenciaDAO.merge(fecharOcorrencia);

AberturaOcorrenciaDAO aberturaOcorrencia = new AberturaOcorrenciaDAO();
ocorrencia.setStatus(false);

CodigoAberturaOcorrenciaSalva = ocorrencia.getCodigo();

aberturaOcorrencia.merge(ocorrencia);
		listarOcorrenciasAbertas();

		 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
		 RequestContext.getCurrentInstance().execute("PF('dialogoImpressao').show();");
		Messages.addGlobalInfo("Fechamento finalizado com sucesso");
	} catch (RuntimeException erro) {
		Messages.addGlobalError("Os dados não poderam ser salvo.");
		
		erro.printStackTrace();
	}
}
//imprimirOcorrenciaAbertaPorCodigo
public void imprimirOcorrenciaPorCodigo(ActionEvent evento) {
	try {
		String caminho = Faces.getRealPath("//report//relatorioOcorrenciaFinalizadaPorCodigo.jasper");

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("CODIGO_OCORRENCIA",CodigoAberturaOcorrenciaSalva);
		
		Connection conexao = HibernateUtil.getConexao();
        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
      
        JasperViewer.viewReport(jp, false);
		System.out.println("Codigo da ocorrencia -  imprimir ......:"+CodigoAberturaOcorrenciaSalva);
		
		RequestContext.getCurrentInstance().execute("PF('dialogoImprimirOcorrencia').hide();");
	} catch (JRException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
		erro.printStackTrace();
	}
}
public void imprimirOcorrenciaPorCodigoSelecionado(ActionEvent evento) {
	try {
		 AberturaOcorrencia  ocorrenciaSelecionada = (AberturaOcorrencia) evento.getComponent()	.getAttributes().get("ocorrenciaSelecionada");
			
		
		String caminho = Faces.getRealPath("//report//relatorioOcorrenciaPorCodigo.jasper");

		Map<String, Object> parametros = new HashMap<>();

		parametros.put("CODIGO_OCORRENCIA",ocorrenciaSelecionada.getCodigo());
		
		Connection conexao = HibernateUtil.getConexao();
        JasperPrint jp = JasperFillManager.fillReport(caminho, parametros, conexao);
      
        JasperViewer.viewReport(jp, false);
		System.out.println("Codigo da ocorrencia no imprimir ......:"+ocorrenciaSelecionada.getCodigo());
	
		
		RequestContext.getCurrentInstance().execute("PF('dialogoImprimirOcorrencia').hide();");
	} catch (JRException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar gerar o relatório");
		erro.printStackTrace();
	}
}

public List<AberturaOcorrencia> getListaOcorrenciasAberta() {
	return listaOcorrenciasAberta;
}



public void setListaOcorrenciasAberta(
		List<AberturaOcorrencia> listaOcorrenciasAberta) {
	this.listaOcorrenciasAberta = listaOcorrenciasAberta;
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

public FecharOcorrencia getFecharOcorrencia() {
	return fecharOcorrencia;
}

public void setFecharOcorrencia(FecharOcorrencia fecharOcorrencia) {
	this.fecharOcorrencia = fecharOcorrencia;
}

public List<AberturaOcorrencia> getListaOcorrencias() {
	return listaOcorrencias;
}

public void setListaOcorrencias(List<AberturaOcorrencia> listaOcorrencias) {
	this.listaOcorrencias = listaOcorrencias;
}

public Long getCodigoAberturaOcorrenciaSalva() {
	return CodigoAberturaOcorrenciaSalva;
}

public void setCodigoAberturaOcorrenciaSalva(Long codigoAberturaOcorrenciaSalva) {
	CodigoAberturaOcorrenciaSalva = codigoAberturaOcorrenciaSalva;
}



}
