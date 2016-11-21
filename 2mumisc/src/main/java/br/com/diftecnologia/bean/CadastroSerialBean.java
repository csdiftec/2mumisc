package br.com.diftecnologia.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.CadastroSerialDAO;
import br.com.diftecnologia.domain.CadastroSerial;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CadastroSerialBean implements Serializable {
	private CadastroSerial cadastro;

	@PostConstruct
	public void novo() {
		cadastro = new CadastroSerial();
		BuscaUltimoRegistroCadastroSerial();
	}

	public void cadastrar(){
		try{
		CadastroSerialDAO cadastroDAO = new CadastroSerialDAO();
		cadastroDAO.merge(cadastro);
		Messages.addGlobalInfo("Salvo com sucesso.");
		novo();
		}catch(RuntimeException e){
			System.out.println("Serial não pode ser salvo: "+ e);
			Messages.addGlobalError("O registro não pode ser salvo: ");
		}
	}
	public void BuscaUltimoRegistroCadastroSerial(){
	
		try {
			CadastroSerialDAO cadastroSerialDAO = new CadastroSerialDAO();
			cadastro = cadastroSerialDAO.buscarUltimoRegistro();
			char [] data = cadastro.getDataVencimento().toCharArray();
			
			cadastro.setDataProximoVencimentoRegistrada("O sistema expira dia:  "+data[0]+data[1]+"/"+data[2]+data[3]+"/"+data[4]+data[5]+data[6]+data[7]);
			cadastro.setDataVencimento("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public CadastroSerial getCadastro() {
		return cadastro;
	}

	public void setCadastro(CadastroSerial cadastro) {
		this.cadastro = cadastro;
	}

}