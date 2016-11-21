package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.DespesasDAO;
import br.com.diftecnologia.domain.Despesas;

@ManagedBean
@ViewScoped
public class DespesasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Despesas despesa;
	private List<Despesas> despesas;

	public void novo() {
		try {
			despesa = new Despesas();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova despesa!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			DespesasDAO despesaDAO = new DespesasDAO();
			despesas = despesaDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as despesas!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			DespesasDAO despesaDAO = new DespesasDAO();

			despesaDAO.merge(despesa);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
			Messages.addGlobalInfo("Despesa salva com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar a despesa!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		despesa = (Despesas) evento.getComponent().getAttributes().get("despesaSelecionada");
	}

	public void excluir(ActionEvent evento) {
		try {
			despesa = (Despesas) evento.getComponent().getAttributes().get("despesaSelecionada");

			DespesasDAO despesaDAO = new DespesasDAO();
			despesaDAO.excluir(despesa);

			listar();

			Messages.addGlobalInfo("Despesa removida com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover a despesa selecionada!");
			erro.printStackTrace();
		}
	}

	public Despesas getDespesas() {
		return despesa;
	}

	public void setDespesas(Despesas despesa) {
		this.despesa = despesa;
	}

	public List<Despesas> getDespesass() {
		return despesas;
	}

	public void setDespesass(List<Despesas> despesas) {
		this.despesas = despesas;
	}

	

}
