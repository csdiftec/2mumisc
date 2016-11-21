package br.com.diftecnologia.dao;

import org.hibernate.Session;

import org.hibernate.Query;
import br.com.diftecnologia.domain.Fabricante;
import br.com.diftecnologia.util.HibernateUtil;

public class FabricanteDAO extends GenericDAO<Fabricante> {
  
	public Fabricante verificarCnpj(String cnpj) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Fabricante fabricante = null;

		try {
			Query consulta = sessao.getNamedQuery("Fabricante.verificaCNPJ");
			consulta.setString("cnpj", cnpj);
			fabricante = (Fabricante) consulta.uniqueResult();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return fabricante;
	}  
	
	// Método para verificar se o fabricante faz parte de algum produto
	public Fabricante verificarProduto(Long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Fabricante fabricante = null;
		
		try {
			Query consulta = sessao.getNamedQuery("Fabricante.verificaProduto");
			consulta.setLong("codigo", codigo);
			fabricante = (Fabricante) consulta.uniqueResult();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return fabricante;
	}

}
