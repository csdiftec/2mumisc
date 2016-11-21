package br.com.diftecnologia.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.diftecnologia.domain.Empresa;
import br.com.diftecnologia.domain.Fornecedor;
import br.com.diftecnologia.util.HibernateUtil;

public class FornecedorDAO extends GenericDAO<Fornecedor> {
	@SuppressWarnings("unchecked")
	public List<Fornecedor> listarOrdenado() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Empresa.class);
			consulta.createAlias("nomeFantasia", "p");
			consulta.addOrder(Order.asc("p.nomeFantasia"));
			List<Fornecedor> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	@SuppressWarnings("unchecked")
	public List<Fornecedor> listaFornecedor() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		List<Fornecedor> fornecedor = null;

		try {
			Query consulta = sessao.getNamedQuery("Fornecedor.listaFornecedor");
			fornecedor =  consulta.list();
		} catch (Exception erro) {
			System.out.println("DAO listaFornecedor "+erro); 
		} finally {
			sessao.close();
		}
		return fornecedor;

	}
	@SuppressWarnings("unchecked")
	public List<Empresa> buscarPorCNPJ(String cnpj) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Empresa.class);
			consulta.add(Restrictions.eq("cnpj", cnpj));	
					List<Empresa> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public Fornecedor verificarCnpj(String cnpj) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Fornecedor fornecedor = null;

		try {
			Query consulta = sessao.getNamedQuery("Fornecedor.verificaCNPJ");
			consulta.setString("cnpj", cnpj);
			fornecedor = (Fornecedor) consulta.uniqueResult();
		} catch (Exception erro) {
			System.out.println("DAO verificaCNPJ "+erro); 
		} finally {
			sessao.close();
		}
		return fornecedor;
	}
	

//	public Long buscarCodigoFornecedorPorCNPJ(String cnpj) {
//		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
//		try {
//			Criteria consulta = sessao.createCriteria(Fornecedor.class);
//			consulta.add(Restrictions.eq("cnpj", cnpj));	
//					Long resultado = (Long) consulta.uniqueResult();
//			return resultado;
//		} catch (RuntimeException erro) {
//			throw erro;
//		} finally {
//			sessao.close();
//		}
//	}

}