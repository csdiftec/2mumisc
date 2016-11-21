package br.com.diftecnologia.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Pessoa;
import br.com.diftecnologia.util.HibernateUtil;

public class PessoaDAO extends GenericDAO<Pessoa> {
	
	public Long  buscaQtdCpfCadastrado(String cpf) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
	Long qtdCPF= null;

		try {
			Query consulta = sessao.getNamedQuery("Pessoa.buscaQtdCpfCadastrado");
			consulta.setString("cpf", cpf);
			qtdCPF =  (Long) consulta.uniqueResult();
		} catch (Exception erro) {
			System.err.println(" Método 'buscaQtdCpfCadastrado()' esta com erro.................:  "+erro); 
		} finally {
			sessao.close();
		}
		return qtdCPF;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Pessoa> listaPessoasExcetoAdmin() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		List<Pessoa> pessoas = null;

		try {
			Query consulta = sessao.getNamedQuery("Pessoa.listaPessoasExcetoAdmin");
			pessoas = (List<Pessoa>) consulta.list();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return pessoas;
	}
}
