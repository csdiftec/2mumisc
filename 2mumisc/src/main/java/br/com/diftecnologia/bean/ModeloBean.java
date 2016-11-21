package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.CadastroSerialDAO;
import br.com.diftecnologia.dao.CadastroValidacaoSerialDAO;
import br.com.diftecnologia.domain.CadastroSerial;
import br.com.diftecnologia.domain.CadastroValidacaoSerial;
import br.com.diftecnologia.domain.Modelo;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class ModeloBean implements Serializable {
	private Modelo cadastroModelo;
	private CadastroValidacaoSerial CadValidacaoSerial;
	private CadastroSerial cadastroSerial;
	private boolean situacaoChaveSistema,situacaoDataVencimento;
	
	@PostConstruct
	public void validadadeChave(){
	//	verificaDataValidadeExpirou();
	verificaValidadeChaveSistema();
	
		}
	
	public void verificaValidadeChaveSistema(){
		try {
			CadastroSerialDAO cadastroSerialDAO = new CadastroSerialDAO();
			cadastroSerial =cadastroSerialDAO.buscarUltimoRegistro();
			String dataString = cadastroSerial.getDataVencimento();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			sdf.setLenient(false);
			Date dataVencimento = sdf.parse(dataString);
			Date dataAtual = new Date();
			System.out.println("dataVencimento"+dataVencimento+" dataAtual:"+ dataAtual);
			
	if(dataVencimento.before(dataAtual)){
				AlteraSituacaoChaveFalse();
				situacaoChaveSistema = false;
				Messages.addGlobalInfo("Verifique a data de vencimento ou a validade da chave do sistema. ");
				}else if(dataVencimento.after(dataAtual)){
				
				System.out.println("situacaoDataVencimento:"+situacaoDataVencimento);
			}
			
			CadastroValidacaoSerialDAO cadastroSerial = new CadastroValidacaoSerialDAO();
			CadValidacaoSerial =cadastroSerial.buscar((long) 1);
			
			if(CadValidacaoSerial.getStatusValidacao()==false){
				situacaoChaveSistema = false;
			}else{
				situacaoChaveSistema = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void verificaDataValidadeExpirou(){
		try {
			CadastroSerialDAO cadastroSerialDAO = new CadastroSerialDAO();
			cadastroSerial =cadastroSerialDAO.buscarUltimoRegistro();
			String dataString = cadastroSerial.getDataVencimento();
			
		
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			sdf.setLenient(false);
			Date dataVencimento = sdf.parse(dataString);
			
			Date dataAtual = new Date();
			System.out.println("dataVencimento"+dataVencimento+" dataAtual:"+ dataAtual);
			
			if(dataVencimento.after(dataAtual)){
				
				AlteraSituacaoChaveFalse();
				situacaoChaveSistema = false;
				System.out.println("situacaoDataVencimento Vencida:"+situacaoDataVencimento);
				
			}else if(dataVencimento.before(dataAtual)){
				AlteraSituacaoChaveTrue();
				situacaoChaveSistema = true;
				System.out.println("situacaoDataVencimento NÃO Vencida:"+situacaoDataVencimento);
			}else{
				
				AlteraSituacaoChaveTrue();
				situacaoChaveSistema = true;
				System.out.println("situacaoDataVencimento NÃO VENCIDA ou IGUAL:"+situacaoDataVencimento);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void AlteraSituacaoChaveFalse(){
		CadastroValidacaoSerialDAO cadastroValidacaoSerialDAO = new CadastroValidacaoSerialDAO();
		CadValidacaoSerial =cadastroValidacaoSerialDAO.buscar((long) 1);
		
		CadValidacaoSerial.setStatusValidacao(false);
	
		
		cadastroValidacaoSerialDAO.merge(CadValidacaoSerial);
	}
	
	public void AlteraSituacaoChaveTrue(){
		CadastroValidacaoSerialDAO cadastroValidacaoSerialDAO = new CadastroValidacaoSerialDAO();
		CadValidacaoSerial =cadastroValidacaoSerialDAO.buscar((long) 1);
		
		CadValidacaoSerial.setStatusValidacao(true);
	
		
		cadastroValidacaoSerialDAO.merge(CadValidacaoSerial);
	}
	public Modelo getCadastroModelo() {
		return cadastroModelo;
	}

	public void setCadastroModelo(Modelo cadastroModelo) {
		this.cadastroModelo = cadastroModelo;
	}


	public CadastroValidacaoSerial getCadValidacaoSerial() {
		return CadValidacaoSerial;
	}

	public void setCadValidacaoSerial(CadastroValidacaoSerial cadValidacaoSerial) {
		CadValidacaoSerial = cadValidacaoSerial;
	}

	public boolean isSituacaoChaveSistema() {
		return situacaoChaveSistema;
	}

	public void setSituacaoChaveSistema(boolean situacaoChaveSistema) {
		this.situacaoChaveSistema = situacaoChaveSistema;
	}
	public boolean isSituacaoDataVencimento() {
		return situacaoDataVencimento;
	}

	public void setSituacaoDataVencimento(boolean situacaoDataVencimento) {
		this.situacaoDataVencimento = situacaoDataVencimento;
	}
	

	
	
}