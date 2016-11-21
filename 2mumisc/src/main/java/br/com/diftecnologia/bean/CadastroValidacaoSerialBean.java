package br.com.diftecnologia.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.swing.JOptionPane;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.CadastroSerialDAO;
import br.com.diftecnologia.dao.CadastroValidacaoSerialDAO;
import br.com.diftecnologia.domain.CadastroSerial;
import br.com.diftecnologia.domain.CadastroValidacaoSerial;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CadastroValidacaoSerialBean implements Serializable {
	private CadastroValidacaoSerial cadastroValidacao;
	private CadastroSerial cadastroSerial;
	long dataUltimoRegistro;
	@PostConstruct
	public void novo() {
		cadastroValidacao = new CadastroValidacaoSerial();
		
	}

	
	public void validar(){
		try{
			Long senha = cadastroValidacao.getCodigo();
			CadastroSerialDAO cadastroSerialDAO = new CadastroSerialDAO();
			cadastroSerial = cadastroSerialDAO.buscarUltimoRegistro();
             dataUltimoRegistro = Long.parseLong(cadastroSerial.getDataVencimento());
	
			
			long calculo = ((dataUltimoRegistro+999099999)/4)+129990;

			if(calculo==senha){
				CadastroValidacaoSerialDAO  cadastroDAO = new CadastroValidacaoSerialDAO();
				cadastroValidacao=cadastroDAO.buscar((long) 1);
				cadastroValidacao.setStatusValidacao(true);
				cadastroDAO.merge(cadastroValidacao);
				
				Messages.addGlobalInfo("Registro salvo com sucesso. ");
				Messages.addGlobalInfo("Para completar a validaçao reinicie o sistema. ");
			}else{
				Messages.addGlobalError("Chave não pode ser validada, verifique o numero da chave com a Diftecnologia.com.");
			}
	
		novo();
		}catch(RuntimeException e){
			Messages.addGlobalError("O serial não pode ser salvo."+ e);
		}
	}


	public CadastroValidacaoSerial getCadastroValidacao() {
		return cadastroValidacao;
	}


	public void setCadastroValidacao(CadastroValidacaoSerial cadastroValidacao) {
		this.cadastroValidacao = cadastroValidacao;
	}


	public CadastroSerial getCadastroSerial() {
		return cadastroSerial;
	}


	public void setCadastroSerial(CadastroSerial cadastroSerial) {
		this.cadastroSerial = cadastroSerial;
	}

}