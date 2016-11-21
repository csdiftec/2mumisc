package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;

import br.com.diftecnologia.dao.UnidadeMedidaDAO;
import br.com.diftecnologia.domain.UnidadeMedida;

@ManagedBean
@ViewScoped
public class UnidadeMedidaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private UnidadeMedida unidade;
	private List<UnidadeMedida> unidades;

	public void novo() {
		try {
			unidade = new UnidadeMedida();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova unidade de medida!");
			erro.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			UnidadeMedidaDAO unidadeMedidaDAO = new UnidadeMedidaDAO();
			unidades = unidadeMedidaDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar as unidades de medida!");
			erro.printStackTrace();
		}
	}

	public void salvar() {
		try {
			UnidadeMedidaDAO unidadeMedidaDAO = new UnidadeMedidaDAO();

			unidadeMedidaDAO.merge(unidade);

			novo();
			listar();
			 RequestContext.getCurrentInstance().execute("PF('dialogo').hide();");
Messages.addGlobalInfo("Unidade de medida salva com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar a unidade de medida!");
			erro.printStackTrace();
		}
	}

	public void editar(ActionEvent evento) {
		unidade = (UnidadeMedida) evento.getComponent().getAttributes().get("unidadeSelecionada");
	}

	public void excluir(ActionEvent evento) {
		try {
			unidade = (UnidadeMedida) evento.getComponent().getAttributes().get("unidadeSelecionada");

			UnidadeMedidaDAO unidadeMedidaDAO = new UnidadeMedidaDAO();
			unidadeMedidaDAO.excluir(unidade);

			listar();

			Messages.addGlobalInfo("Unidade de medida removida com sucesso!");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover a unidade de medida selecionada!");
			erro.printStackTrace();
		}
	}

	public UnidadeMedida getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}

	public List<UnidadeMedida> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeMedida> unidades) {
		this.unidades = unidades;
	}

}
