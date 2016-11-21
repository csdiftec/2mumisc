package br.com.diftecnologia.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Duplicata;
import br.com.diftecnologia.util.HibernateUtil;

public class DuplicataDAO extends GenericDAO<Duplicata> {

	@SuppressWarnings("unchecked")
	public List<Duplicata> buscaDuplicatasDaVenda(Long cod) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		List<Duplicata> duplicatas = null;
		try {
			Query consulta = sessao.getNamedQuery("Duplicata.buscaUltimasDuplicatasGeradas");
			consulta.setLong("cod", cod);
			duplicatas = consulta.list();
			System.out.println("DAO:  "+duplicatas);
			return duplicatas;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Duplicata> listarUltimas1000Duplicatas(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT duplicata FROM Duplicata duplicata  order by  duplicata.codigo DESC");
			query.setMaxResults(1000);
	
			 List<Duplicata> listaDuplicatas = query.list();
System.out.println("listaDuplicatas....."+listaDuplicatas);
			return listaDuplicatas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	
	@SuppressWarnings("unchecked")
	public List<Duplicata> listarDuplicatasPorPeriodo(Date dataInicial, Date dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT duplicata FROM Duplicata duplicata WHERE duplicata.dataVencimento  "
					+ "between :dataInicial and :dataFinal order by  duplicata.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			
			 List<Duplicata> listaDuplicatas = query.list();
System.out.println("listaDuplicatas....."+listaDuplicatas);
			return listaDuplicatas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	
	
	@SuppressWarnings("unchecked")
	public List<Duplicata> listarDuplicatasPorPeriodoCliente(Date dataInicial, Date dataFinal,long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT duplicata FROM Duplicata duplicata WHERE duplicata.venda.cliente.codigo =:codigo and duplicata.dataVencimento "
					+ "between :dataInicial and :dataFinal  order by  duplicata.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codigo", codigo);
				System.out.println("DAO codigo:"+codigo);
			 List<Duplicata> listaDuplicatas = query.list();
System.out.println("listaDuplicatas....."+listaDuplicatas);
			return listaDuplicatas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}

	@SuppressWarnings("unchecked")
	public List<Duplicata> listarDuplicatasVencidas(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT duplicata FROM Duplicata duplicata WHERE duplicata.dataVencimento <= current_date()");
				
			 List<Duplicata> listaDuplicatasVencidas = query.list();
System.out.println("listaDuplicatasVencidas....."+listaDuplicatasVencidas);
			return listaDuplicatasVencidas;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
}
