package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.FormaPagamentoDAO;
import br.com.diftecnologia.domain.FormaPagamento;

@ManagedBean
@ViewScoped
public class FormaPagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private FormaPagamento formaPagamento;
	private List<FormaPagamento> formasPagamento;

	public void novo() {
		try {
			formaPagamento = new FormaPagamento();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova forma de pagamento!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
			formasPagamento = formaPagamentoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as unidades de medida!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();

			formaPagamentoDAO.merge(formaPagamento);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogoPagamento').hide();");
			Messages.addGlobalInfo("Forma de pagamento salva com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar a forma de pagamento!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		formaPagamento = (FormaPagamento) evento.getComponent().getAttributes().get("formaSelecionada");
	}

	public void excluir(ActionEvent evento) {
		try {
			formaPagamento = (FormaPagamento) evento.getComponent().getAttributes().get("formaSelecionada");

			FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
			formaPagamentoDAO.excluir(formaPagamento);

			listar();

			Messages.addGlobalInfo("Forma de pagamento removida com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover a forma de pagamento selecionada!");
			erro.printStackTrace();
		}
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<FormaPagamento> getFormasPagamento() {
		return formasPagamento;
	}

	public void setFormasPagamento(List<FormaPagamento> formasPagamento) {
		this.formasPagamento = formasPagamento;
	}

}
