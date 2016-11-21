package br.com.diftecnologia.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.com.diftecnologia.domain.Compra;
import br.com.diftecnologia.domain.ItemCompra;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.util.HibernateUtil;

public class CompraDAO extends GenericDAO<Compra> {
	@SuppressWarnings("unchecked")
	public List<Compra> buscarPorIdNFe(String numeroDocumentoFiscal) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Compra.class);
			consulta.add(Restrictions.eq("numeroDocumentoFiscal", numeroDocumentoFiscal));	
					List<Compra> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	public void salvar(Compra compra, List<ItemCompra> itensCompra) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();

			sessao.save(compra);

			for (int posicao = 0; posicao < itensCompra.size(); posicao++) {
				ItemCompra itemCompra = itensCompra.get(posicao);
				itemCompra.setCompra(compra);

				sessao.save(itemCompra);
				
				Produto produto = itemCompra.getProduto();
				double quantidade = (Double) (produto.getQuantidadeEstoque() + itemCompra.getQuantidade());
			
					produto.setQuantidadeEstoque((quantidade));
					sessao.update(produto);
				
			}

			transacao.commit();
		} catch (RuntimeException erro) {
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		} finally {
			sessao.close();
		}
	}

	public Compra buscarUltimaCompra() {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			Compra compra = null;
			try {
				Query consulta = sessao.getNamedQuery("Compra.buscaUltimaCompra");
				compra = (Compra) consulta.uniqueResult();
			
					} catch (Exception erro) {
				System.out.println("Método buscaUltimaCompra:"+erro); 
			} finally {
				sessao.close();
			}
			return compra;
		}
	@SuppressWarnings("unchecked")
	public List<Compra> listarUltimas1000Compras(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT compra FROM Compra compra  order by  compra.codigo DESC");
			query.setMaxResults(1000);
	
			 List<Compra> listaCompras = query.list();
System.out.println("listaCompras....."+listaCompras);
			return listaCompras;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	

	
	@SuppressWarnings("unchecked")
	public List<Compra> listarComprasPorPeriodo(Date dataInicial, Date dataFinal){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT compra FROM Compra compra WHERE compra.dataCompra  "
					+ "between :dataInicial and :dataFinal order by  compra.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
	
				
			 List<Compra> listaCompras = query.list();
System.out.println("listacompras....."+listaCompras);
			return listaCompras;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}

	@SuppressWarnings("unchecked")
	public List<Compra> listarComprasPorPeriodofornecedor(Date dataInicial, Date dataFinal,long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT compra FROM Compra compra WHERE compra.fornecedor.codigo =:codigo and compra.dataCompra  "
					+ "between :dataInicial and :dataFinal  order by  compra.codigo DESC");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			query.setParameter("codigo", codigo);
				System.out.println("DAO codigo:"+codigo);
			 List<Compra> listaCompras = query.list();
System.out.println("listaCompras....."+listaCompras);
			return listaCompras;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}

}
