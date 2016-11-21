package br.com.diftecnologia.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Caixa;
import br.com.diftecnologia.util.HibernateUtil;

public class CaixaDAO extends GenericDAO<Caixa> {
	
		public Caixa  caixaAbertoProfissionalLogado(Long codigo) {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			Caixa  caixa = null;
			try {
				Query consulta = sessao.getNamedQuery("Caixa.caixaAbertoProfissionalLogado");
				consulta.setLong("codigo", codigo);
				caixa = (Caixa) consulta.uniqueResult();
				System.out.println(caixa);
			} catch (Exception erro) {
				System.err.println("Não existe caixa aberto"); 
			} finally {
				sessao.close();
			}
			return caixa;
		}
}
