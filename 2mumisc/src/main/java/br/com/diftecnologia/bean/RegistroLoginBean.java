package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.swing.text.MaskFormatter;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.RegistroLoginDAO;
import br.com.diftecnologia.domain.RegistroLogin;

@ManagedBean
@ViewScoped
public class RegistroLoginBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private RegistroLogin registroLogin;
	private List<RegistroLogin> registrosLogin;
	
	@PostConstruct
	public void listar() {
		try {
			RegistroLoginDAO registroLoginDAO = new RegistroLoginDAO();
			registrosLogin = registroLoginDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos!");
			erro.printStackTrace();
		}
	}
	
	// Método para formatar o CPF no XHTML
	public String formatarCPF(String pattern, Object value, boolean suppressZero) {
	    if (!suppressZero || Double.parseDouble(value.toString()) != 0) {
	        MaskFormatter mask;
	        try {
	            mask = new MaskFormatter(pattern);
	            mask.setValueContainsLiteralCharacters(false);
	            return mask.valueToString(value);
	        } catch (ParseException e) {
	            throw new RuntimeException(e);
	        }
	    } else {
	        return "";
	    }
	}

	public RegistroLogin getRegistroLogin() {
		return registroLogin;
	}

	public void setRegistroLogin(RegistroLogin registroLogin) {
		this.registroLogin = registroLogin;
	}

	public List<RegistroLogin> getRegistrosLogin() {
		return registrosLogin;
	}

	public void setRegistrosLogin(List<RegistroLogin> registrosLogin) {
		this.registrosLogin = registrosLogin;
	}

}
