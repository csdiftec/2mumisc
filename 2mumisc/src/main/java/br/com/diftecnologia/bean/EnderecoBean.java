package br.com.diftecnologia.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;

import br.com.diftecnologia.dao.CidadeDAO;
import br.com.diftecnologia.dao.EnderecoDAO;
import br.com.diftecnologia.dao.EstadoDAO;
import br.com.diftecnologia.domain.Cidade;
import br.com.diftecnologia.domain.Endereco;
import br.com.diftecnologia.domain.Estado;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class EnderecoBean  implements Serializable {
	private Endereco endereco;
	private Estado estado;
	private List<Cidade> cidades;
	private List<Estado> estados;
	private List<Endereco> enderecos;
	
	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	
	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	
	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@PostConstruct
	public void listar() {
		try {
			EnderecoDAO enderecoDAO = new EnderecoDAO();
			enderecos = enderecoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os endereços");
			erro.printStackTrace();
		}
	}
	
	
	public void novo() {
		try{
			endereco = new Endereco();
			estado = new Estado();
			
			listarEstados();

			
			cidades = new ArrayList<>();
			
		}catch (RuntimeException erro){
			Messages.addGlobalError("Occorreu um erro ao gerar uma nova cidade.");
		}
	}
	
	private void listarEstados() {
		try {
			EstadoDAO estadoDAO = new EstadoDAO();
			estados = estadoDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os estados!");
			erro.printStackTrace();
		}
	}
	public void salvar(){
		EnderecoDAO enderecoDAO =  new EnderecoDAO();
		enderecoDAO.salvar(endereco);
		Messages.addGlobalInfo("Endereço salvo com sucesso.");
	}
	
	public void popular() {
		try {
			if (estado != null) {
				CidadeDAO cidadeDAO = new CidadeDAO();
				cidades = cidadeDAO.buscarPorEstado(estado.getCodigo());
			} else {
				cidades = new ArrayList<>();
			}
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar filtrar as cidades");
			erro.printStackTrace();
		}
	}
	
}
