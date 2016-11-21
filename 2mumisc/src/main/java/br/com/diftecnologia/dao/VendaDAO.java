package br.com.diftecnologia.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.diftecnologia.domain.ItemVenda;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.domain.Venda;
import br.com.diftecnologia.util.HibernateUtil;

public class VendaDAO extends GenericDAO<Venda> {
	public void salvar(Venda venda, List<ItemVenda> itensVenda) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();

			sessao.save(venda);

			for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
				ItemVenda itemVenda = itensVenda.get(posicao);
				itemVenda.setVenda(venda);

				sessao.save(itemVenda);
				
				Produto produto = itemVenda.getProduto();
				double quantidade = (Double) (produto.getQuantidadeEstoque() - itemVenda.getQuantidade());
				if (quantidade >= 0) {
					produto.setQuantidadeEstoque((quantidade));
					sessao.update(produto);
				} else {
					throw new RuntimeException("Quantidade insuficiente em estoque");
				}
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

	
		public Long verificaNumeroRegistros(){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT count(numeroDocumentoFiscal) FROM Venda");
			Long numeroRegistros = (Long) query.uniqueResult();
System.out.println(numeroRegistros);
			return numeroRegistros;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
		
		public Long buscaUltimoNumeroDocumento(){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {

				Query query = sessao.createQuery("SELECT max(numeroDocumentoFiscal) FROM Venda");
				Long numeroDocumento = (Long) query.uniqueResult();
	System.out.println(numeroDocumento);
				return numeroDocumento;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}		
	}
		
	
		@SuppressWarnings("unchecked")
		public List<Venda>  buscaVendaDataProfissionalLogado(Timestamp dataAbertura,Long codigoFuncionario) {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			List <Venda> vendas = null;
			try {
				Query consulta = sessao.getNamedQuery("Venda.buscaVendaPorTerminalFuncionario");
				
				consulta.setTimestamp("dataAbertura", dataAbertura);
				System.out.println("Data da Abertura................."+dataAbertura);
				
				consulta.setLong("funcionarioLogado", codigoFuncionario);	
				System.out.println("Codigo Funcionario Logado ..............."+codigoFuncionario);
				
				
				vendas =  consulta.list();
				System.out.println("Venda................"+vendas);
				
			} catch (Exception erro) {
				System.err.println("Erro na busca de venda por periodo"+erro); 
			} finally {
				sessao.close();
			}
			return (List<Venda>) vendas;
		}

		@SuppressWarnings("unchecked")
		public List<Venda>  buscaVendaDataProfissionalLogadoAvista(Timestamp dataAbertura,Long codigoFuncionario) {
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			List <Venda> vendas = null;
			try {
				Query consulta = sessao.getNamedQuery("Venda.buscaVendaPorTerminalFuncionarioAvista");
				
				consulta.setTimestamp("dataAbertura", dataAbertura);
				System.out.println("Data da Abertura................."+dataAbertura);
				
				consulta.setLong("funcionarioLogado", codigoFuncionario);	
				System.out.println("Codigo Funcionario Logado ..............."+codigoFuncionario);
				
				
				vendas =  consulta.list();
				System.out.println("Venda................"+vendas);
				
			} catch (Exception erro) {
				System.err.println("Erro na busca de venda por periodo"+erro); 
			} finally {
				sessao.close();
			}
			return (List<Venda>) vendas;
		}
		@SuppressWarnings("unchecked")
		public List<Venda> listarUltimas1000Vendas(){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda  order by  venda.codigo DESC");
				query.setMaxResults(1000);
		
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasParceladas(){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda  where tipoPagamento_codigo = 2 order by  venda.codigo DESC");
						
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasParceladas(Date dataInicial, Date dataFinal){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.dataVenda  "
						+ "between :dataInicial and :dataFinal AND tipoPagamento_codigo = 2  order by  venda.codigo DESC " );
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
		
					
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasPorPeriodo(Date dataInicial, Date dataFinal){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.dataVenda  "
						+ "between :dataInicial and :dataFinal order by  venda.codigo DESC");
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
		
					
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasPorPeriodoVendedor(Date dataInicial, Date dataFinal,long codigo){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.funcionarioVenda.codigo =:codigo and venda.dataVenda  "
						+ "between :dataInicial and :dataFinal  order by  venda.codigo DESC");
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
				query.setParameter("codigo", codigo);
					System.out.println("DAO codigo:"+codigo);
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}
			}
		
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasPorPeriodoFuncionarioLog(Date dataInicial, Date dataFinal,long codigo){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.funcionario.codigo =:codigo and venda.dataVenda  "
						+ "between :dataInicial and :dataFinal  order by  venda.codigo DESC");
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
				query.setParameter("codigo", codigo);
					System.out.println("DAO codigo:"+codigo);
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasPorPeriodoFormaPagamento(Date dataInicial, Date dataFinal,long codigo){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.formaPagamento.codigo =:codigo and venda.dataVenda  "
						+ "between :dataInicial and :dataFinal  order by  venda.codigo DESC");
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
				query.setParameter("codigo", codigo);
					System.out.println("DAO codigo:"+codigo);
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
		
		@SuppressWarnings("unchecked")
		public List<Venda> listarVendasPorPeriodoTipoPagamento(Date dataInicial, Date dataFinal,long codigo){
			Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT venda FROM Venda venda WHERE venda.tipoPagamento.codigo =:codigo and venda.dataVenda  "
						+ "between :dataInicial and :dataFinal  order by  venda.codigo DESC");
				query.setParameter("dataInicial", dataInicial);
				query.setParameter("dataFinal", dataFinal);
				query.setParameter("codigo", codigo);
					System.out.println("DAO codigo:"+codigo);
				 List<Venda> listaVendas = query.list();
	System.out.println("listaVendas....."+listaVendas);
				return listaVendas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
}

