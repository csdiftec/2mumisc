package br.com.diftecnologia.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.CadastroSerial;
import br.com.diftecnologia.util.HibernateUtil;

public class CadastroSerialDAO extends GenericDAO<CadastroSerial> {

	public CadastroSerial buscarUltimoRegistro() {
		
		
		try {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			CadastroSerial ultimoRegistro = null;
			Query consulta = sessao.getNamedQuery("CadastroSerial.buscaUltimoRegistro");
			ultimoRegistro = (CadastroSerial) consulta.uniqueResult();
			System.out.println("Ultimo registro do dao ............"+ ultimoRegistro);
			
			return ultimoRegistro;
		} catch (RuntimeException e) {

		}
		return null;
	


	}

}
