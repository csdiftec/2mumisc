package br.com.diftecnologia.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Telefone;
import br.com.diftecnologia.util.HibernateUtil;

public class TelefoneDAO extends GenericDAO<Telefone> {
  
  // Método que busca todos os telefones do Fornecedor(F), Fabricante(F) e Pessoa(P)
	@SuppressWarnings("unchecked")
	public List<Telefone> listarPorFFP(Long codigo) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		List<Telefone> telefones = null;

		try {
			Query consulta = sessao.getNamedQuery("Telefone.listarPorFFP");
			consulta.setLong("codigoFornecedor", codigo);
			telefones = (List<Telefone>)consulta.list();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
		return telefones;
	}

}
