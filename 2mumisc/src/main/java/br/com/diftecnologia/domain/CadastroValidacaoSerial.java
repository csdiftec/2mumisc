package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cadastro_validacao_serial")
@NamedQueries({
		@NamedQuery(name = "CadastroValidacaoSerial.buscaUltimoRegistro", query = "SELECT cadastroValidacaoSerial FROM CadastroValidacaoSerial cadastroValidacaoSerial") })
public class CadastroValidacaoSerial extends GenericDomain {

	private static final long serialVersionUID = 1L;
	
	@Column(name="status_da_validacao", columnDefinition = "boolean default false", nullable = false)
	private Boolean statusValidacao= false;
	
	public Boolean getStatusValidacao() {
		return statusValidacao;
	}
	public void setStatusValidacao(Boolean statusValidacao) {
		this.statusValidacao = statusValidacao;
	}
	
	
}
