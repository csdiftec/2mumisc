package br.com.diftecnologia.dao;


import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.util.HibernateUtil;

public class FuncionarioDAO extends GenericDAO<Funcionario> {
	
	public Funcionario autenticar(String cpf, String senha) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		
		try {
			Funcionario funcionario=null;
			Criteria consulta = sessao.createCriteria(Funcionario.class);
			consulta.createAlias("pessoa", "p");
			
			consulta.add(Restrictions.eq("p.cpf",cpf));
			System.out.println(cpf);
			
			SimpleHash hash = new SimpleHash("md5",senha);
			System.out.println(senha);
			
			consulta.add(Restrictions.eq("senha",hash.toHex()));
			System.out.println( hash.toHex());
			
			funcionario = (Funcionario) consulta.uniqueResult();
			System.out.println("Funcionario do DAO...........:"+funcionario);
			return funcionario;
		} catch (RuntimeException erro) {
			System.err.println(erro);
			System.out.println(erro);
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Funcionario> listarOrdenado() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Funcionario.class);
			consulta.createAlias("pessoa", "p");
			consulta.addOrder(Order.asc("p.nome"));
			List<Funcionario> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public Funcionario verificarCpf(String cpf) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Funcionario funcionario = null;

		try {
			Query consulta = sessao.getNamedQuery("Funcionario.verificaCPF");
			consulta.setString("cpf", cpf);
			funcionario = (Funcionario) consulta.uniqueResult();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return funcionario;
	}
	@SuppressWarnings("unchecked")
	public List<Funcionario> listaFuncionarios() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		List<Funcionario> funcionarios = null;

		try {
			Query consulta = sessao.getNamedQuery("Funcionario.listaFuncionarios");
			funcionarios = (List<Funcionario>) consulta.list();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return funcionarios;
	}
/*	public Funcionario autenticar(String cpf,String senha){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Funcionario funcionario = null;

		try {
			Query consulta = sessao.getNamedQuery("Funcionario.autenticar");
			consulta.setString("cpf", cpf);
			SimpleHash hash = new SimpleHash("md5", senha);
			System.out.println(senha);
			
			consulta.setString("senha", hash.toHex());
			System.out.println( hash.toHex());
			
			System.out.println(senha);
			funcionario = (Funcionario) consulta.list();
				 
			System.out.println("Funcionario.....:"+ funcionario);
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return funcionario;
	}*/
}  