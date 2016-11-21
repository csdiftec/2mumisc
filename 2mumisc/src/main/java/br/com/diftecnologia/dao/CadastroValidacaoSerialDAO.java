package br.com.diftecnologia.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.CadastroSerial;
import br.com.diftecnologia.domain.CadastroValidacaoSerial;
import br.com.diftecnologia.util.HibernateUtil;

public class CadastroValidacaoSerialDAO extends GenericDAO<CadastroValidacaoSerial> {

	public CadastroSerial buscarUltimoRegistro() {
		
		
		try {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			CadastroSerial ultimoRegistro = null;
			Query consulta = sessao.getNamedQuery("CadastroValidacaoSerial.buscaUltimoRegistro");
			ultimoRegistro = (CadastroSerial) consulta.uniqueResult();
			System.out.println("Ultimo registro do dao ............"+ ultimoRegistro);
			
			return ultimoRegistro;
		} catch (RuntimeException e) {

		}
		return null;
	


	}

}
