package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.InventarioDAO;
import br.com.diftecnologia.dao.ProdutoDAO;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.Inventario;
import br.com.diftecnologia.domain.Produto;


@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class InventarioBean implements Serializable {
private Inventario inventario;
private Produto produto;
private List<Produto> produtos;
public Integer quantidadeAtualizada, quantidadeEstoque;
public String observacao;
@ManagedProperty(value="#{autenticacaoBean}")
private AutenticacaoBean autenticacaoBean;

public void editar(ActionEvent evento){
	inventario= new Inventario();
	produto = new Produto();
	try{
		produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");
	}catch(RuntimeException erro){
		Messages.addFlashGlobalError("Ocorreu um erro ao tentar selecionar o item!");
		erro.printStackTrace();
	}
}
@PostConstruct
public void listarProdutos() {
	try {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtos = produtoDAO.listar();
	} catch (RuntimeException erro) {
		Messages.addGlobalError("Ocorreu um erro ao tentar listar os produtos!");
		erro.printStackTrace();
	}
}
public void salvar() {
	ProdutoDAO produtoDAO= new ProdutoDAO();
	try {
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo());
		
		InventarioDAO inventarioDAO= new InventarioDAO();
	//	inventario.setCodigo(produto.getCodigo());
		inventario.setCodigoBarras(produto.getCodigoBarras());
		inventario.setDataModificacao(new Date());
		inventario.setDescricao(produto.getDescricao());
		inventario.setFuncionario(funcionario);
		inventario.setMotivo(observacao);
		inventario.setNovaQuantidade(produto.getQuantidadeEstoque().intValue());
        inventario.setQuantidadeAtual(quantidadeAtualizada);
        
		
	
		

		produto = produtoDAO.buscar(produto.getCodigo());
		produto.setQuantidadeEstoque((double) inventario.getQuantidadeAtual());
		
		
		produtoDAO.merge(produto);
		inventarioDAO.merge(inventario);
		
		listarProdutos();	
		Messages.addGlobalInfo("Produto salvo com sucesso!");
	} catch (RuntimeException erro) {
		Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o produto!");
		erro.printStackTrace();
	}
}

public Inventario getInventario() {
	return inventario;
}

public void setInventario(Inventario inventario) {
	this.inventario = inventario;
}


public Produto getProduto() {
	return produto;
}


public void setProduto(Produto produto) {
	this.produto = produto;
}

public Integer getQuantidadeAtualizada() {
	return quantidadeAtualizada;
}

public void setQuantidadeAtualizada(Integer quantidadeAtualizada) {
	this.quantidadeAtualizada = quantidadeAtualizada;
}

public Integer getQuantidadeEstoque() {
	return quantidadeEstoque;
}

public void setQuantidadeEstoque(Integer quantidadeEstoque) {
	this.quantidadeEstoque = quantidadeEstoque;
}
public String getObservacao() {
	return observacao;
}
public void setObservacao(String observacao) {
	this.observacao = observacao;
}

public List<Produto> getProdutos() {
	return produtos;
}

public void setProdutos(List<Produto> produtos) {
	this.produtos = produtos;
}
public AutenticacaoBean getAutenticacaoBean() {
	return autenticacaoBean;
}
public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
	this.autenticacaoBean = autenticacaoBean;
}


}
