package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="cadastro_serial")
@NamedQueries({
	@NamedQuery(name = "CadastroSerial.buscaUltimoRegistro",
			query = "SELECT cadastroSerial FROM CadastroSerial cadastroSerial order by cadastroSerial.codigo desc")
})
public class CadastroSerial extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	

	@Column(name="data_Vencimento", nullable = false)
	private String dataVencimento;
	
@Transient
	private String dataProximoVencimentoRegistrada;

	public String getDataVencimento() {
		return dataVencimento;
	}


	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}


	public String getDataProximoVencimentoRegistrada() {
		return dataProximoVencimentoRegistrada;
	}


	public void setDataProximoVencimentoRegistrada(String dataProximoVencimentoRegistrada) {
		this.dataProximoVencimentoRegistrada = dataProximoVencimentoRegistrada;
	}
	
}

