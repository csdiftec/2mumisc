package br.com.diftecnologia.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="terminal_atendimento")
public class TerminalAtendimento  extends GenericDomain {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="numero_terminal", length = 2, nullable = false)
	private Byte numeroTerminal;
	
	public boolean status;
	
	@Transient
	@Column(name="titulo_status", length = 2, nullable = false)
	private String tituloStatus;
		
	public Byte getNumeroTerminal() {
		return numeroTerminal;
	}
	
	public void setNumeroTerminal(Byte numeroTerminal) {
		this.numeroTerminal = numeroTerminal;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getTituloStatus() {
		return tituloStatus;
	}

	public void setTituloStatus(String tituloStatus) {
		this.tituloStatus = tituloStatus;
	}

	@Override
	public String toString() {
		return "TerminalAtendimento [numeroTerminal=" + numeroTerminal + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((numeroTerminal == null) ? 0 : numeroTerminal.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminalAtendimento other = (TerminalAtendimento) obj;
		if (numeroTerminal == null) {
			if (other.numeroTerminal != null)
				return false;
		} else if (!numeroTerminal.equals(other.numeroTerminal))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
}
