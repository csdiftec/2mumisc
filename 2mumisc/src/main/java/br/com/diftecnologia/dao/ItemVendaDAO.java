package br.com.diftecnologia.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.ItemVenda;
import br.com.diftecnologia.util.HibernateUtil;

public class ItemVendaDAO extends GenericDAO<ItemVenda> {

	@SuppressWarnings("unchecked")
	public List<ItemVenda> listarProdutosVendidosPorPeriodoVendedor(Date dataInicial, Date dataFinal,long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT item FROM ItemVenda item WHERE item.venda.funcionarioVenda.codigo =:codigo and item.venda.dataVenda  "
					+ "between :dataInicial and :dataFinal  order by  item.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codigo", codigo);
				System.out.println("DAO codigo:"+codigo);
			 List<ItemVenda> listaItens = query.list();
System.out.println("listaItens....."+listaItens);
			return listaItens;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<ItemVenda> listarProdutosVendidosPorPeriodo(Date dataInicial, Date dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT item FROM ItemVenda item WHERE item.venda.dataVenda  "
					+ "between :dataInicial and :dataFinal  order by  item.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
		
			 List<ItemVenda> listaItens = query.list();
System.out.println("listaItens....."+listaItens);
			return listaItens;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	
}
