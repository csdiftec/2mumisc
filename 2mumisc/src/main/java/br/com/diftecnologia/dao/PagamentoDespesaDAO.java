package br.com.diftecnologia.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.PagamentoDespesa;
import br.com.diftecnologia.util.HibernateUtil;

public class PagamentoDespesaDAO extends GenericDAO<PagamentoDespesa> {
	@SuppressWarnings("unchecked")
	public List<PagamentoDespesa> listarUltimas1000PagamentosDespesas(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT pgto FROM PagamentoDespesa pgto  order by  pgto.codigo DESC");
			query.setMaxResults(1000);
	
			 List<PagamentoDespesa> listaPagamentosDespesas = query.list();
System.out.println("listaPagamentosDespesas....."+listaPagamentosDespesas);
			return listaPagamentosDespesas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	
	
	@SuppressWarnings("unchecked")
	public List<PagamentoDespesa> listarPagamentoDespesaPorPeriodoDespesa(Date dataInicial, Date dataFinal,long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT pgto FROM PagamentoDespesa pgto WHERE pgto.registroDespesas.codigoDespesa =:codigo and pgto.dataPagamento "
					+ "between :dataInicial and :dataFinal  order by  pgto.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codigo", codigo);
				System.out.println("DAO codigo:"+codigo);
			 List<PagamentoDespesa> listaPagamentosDespesas = query.list();
System.out.println("listaPagamentosDespesas....."+listaPagamentosDespesas);
			return listaPagamentosDespesas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}
		}

	@SuppressWarnings("unchecked")
	public List<PagamentoDespesa> listarPagamentoDespesaPorPeriodo(Date dataInicial, Date dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT pgto FROM PagamentoDespesa pgto WHERE pgto.dataPagamento "
					+ "between :dataInicial and :dataFinal  order by  pgto.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			 List<PagamentoDespesa> listaPagamentosDespesas = query.list();
System.out.println("listaPagamentosDespesas....."+listaPagamentosDespesas);
			return listaPagamentosDespesas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
}
