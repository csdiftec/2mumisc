package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.TipoPagamentoDAO;
import br.com.diftecnologia.domain.TipoPagamento;

@ManagedBean
@ViewScoped
public class TipoPagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private TipoPagamento tipoPagamento;
	private List<TipoPagamento> tiposPagamento;

	public void novo() {
		try {
			tipoPagamento = new TipoPagamento();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo tipo de pagamento!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
			tiposPagamento = tipoPagamentoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os tipos de pagamento!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();

			tipoPagamentoDAO.merge(tipoPagamento);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");

			Messages.addGlobalInfo("Tipo de pagamento salvo com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar o tipo de pagamento!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		tipoPagamento = (TipoPagamento) evento.getComponent().getAttributes().get("tipoSelecionado");
	}

	public void excluir(ActionEvent evento) {
		try {
			tipoPagamento = (TipoPagamento) evento.getComponent().getAttributes().get("tipoSelecionado");

			TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
			tipoPagamentoDAO.excluir(tipoPagamento);

			listar();

			Messages.addGlobalInfo("Tipo de pagamento removido com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o tipo de pagamento selecionado!");
			erro.printStackTrace();
		}
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public List<TipoPagamento> getTiposPagamento() {
		return tiposPagamento;
	}

	public void setTiposPagamento(List<TipoPagamento> tiposPagamento) {
		this.tiposPagamento = tiposPagamento;
	}

}
