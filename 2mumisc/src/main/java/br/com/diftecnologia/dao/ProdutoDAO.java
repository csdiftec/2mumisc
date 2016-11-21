package br.com.diftecnologia.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.util.HibernateUtil;

public class ProdutoDAO extends GenericDAO<Produto> {
	
	public Produto verificaProdutoExiste(String codigoBarras,String descricao) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Produto produto = null;
		try {
			Query consulta = sessao.getNamedQuery("Produto.verificaCodigoBarraComDescricao");
			consulta.setString("codigoBarras", codigoBarras);
			consulta.setString("descricao", descricao);
			produto = (Produto) consulta.uniqueResult();
			System.out.println(produto);
		} catch (Exception erro) {
			System.out.println("Não existe este produto no estoque"); 
		} finally {
			sessao.close();
		}
		return produto;
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> listaProdutoEmEstoque() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query consulta = sessao.getNamedQuery("Produto.listaProdutoEmEstoque");
			List<Produto> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> listaTodosProdutoEmEstoque() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query consulta = sessao.getNamedQuery("Produto.listaTodosProdutoEmEstoque");
			List<Produto> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> listarProdutoEmEstoqueNaQtdCritica() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query consulta = sessao.getNamedQuery("Produto.listaProdutoEmEstoqueNaQtdCritica");
			List<Produto> resultado = consulta.list();
			return resultado;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}
	}
	
}
