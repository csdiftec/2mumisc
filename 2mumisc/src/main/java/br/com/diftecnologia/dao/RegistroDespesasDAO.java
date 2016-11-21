package br.com.diftecnologia.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.RegistroDespesas;
import br.com.diftecnologia.util.HibernateUtil;

public class RegistroDespesasDAO extends GenericDAO<RegistroDespesas> {
	
	@SuppressWarnings("unchecked")
	public List<RegistroDespesas> listarUltimas1000Contas(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT conta FROM RegistroDespesas conta  order by  conta.codigo DESC");
			query.setMaxResults(1000);
	
			 List<RegistroDespesas> listaContas = query.list();
			 System.out.println("listaContas....."+listaContas);
			 			return listaContas;
			 		} catch (RuntimeException erro) {

			 			throw erro;
			 		} finally {
			 			sessao.close();
			 		}

			 		}
			 	
	@SuppressWarnings("unchecked")
	public List<RegistroDespesas> listarContasPagarPeriodo(Date dataInicial, Date dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT conta FROM RegistroDespesas conta WHERE conta.dataRegistro  "
					+ "between :dataInicial and :dataFinal order by  conta.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
	
				
			 List<RegistroDespesas> listaContas = query.list();
System.out.println("listaContas....."+listaContas);
			return listaContas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}

	@SuppressWarnings("unchecked")
	public List<RegistroDespesas> listarContasPagarPeriodoTipoDespesa(Date dataInicial, Date dataFinal,long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT conta FROM RegistroDespesas conta WHERE conta.despesas.codigo =:codigo and conta.dataRegistro  "
					+ "between :dataInicial and :dataFinal order by  conta.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codigo", codigo);
			System.out.println("DAO codigo:"+codigo);
	
				
			 List<RegistroDespesas> listaContas = query.list();
System.out.println("listaContas....."+listaContas);
			return listaContas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
}


