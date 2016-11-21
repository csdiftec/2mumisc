package br.com.diftecnologia.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.util.HibernateUtil;

public class EnderecoDAO extends GenericDAO<Endereco> {

	
	public Long  buscarUltimoRegistro() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query consulta = sessao.getNamedQuery("Endereco.buscarUltimoRegistro");
			 long resultado = (long) consulta.uniqueResult();
			System.out.println("Buscar Ultimo registro....."+resultado);
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	

}
