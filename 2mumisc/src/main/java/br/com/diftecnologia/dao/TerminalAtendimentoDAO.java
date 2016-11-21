package br.com.diftecnologia.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.TerminalAtendimento;
import br.com.diftecnologia.util.HibernateUtil;

public class TerminalAtendimentoDAO extends GenericDAO<TerminalAtendimento> {
	
	public Long buscarQuantidadeTerminalAberto() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT count(terminalAtendimento) FROM TerminalAtendimento terminalAtendimento"
					+ " where terminalAtendimento.status =0");
			Long terminalAtendimento = (Long) query.uniqueResult();
			System.out.println("Terminal DAO: "+terminalAtendimento);
			return  terminalAtendimento;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}
	public long ContarTerminaisAberto() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			long terminalAtendimento;
			Query query = sessao.createQuery("SELECT count(terminalAtendimento) FROM TerminalAtendimento terminalAtendimento"
					+ " where terminalAtendimento.status =0 ");
			   terminalAtendimento = (long) query.uniqueResult();
			
				  System.out.println("Terminal DAO ContaTERMINAIS...: "+terminalAtendimento);
			
			
			return  terminalAtendimento;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}
	
	public byte buscarTerminalAberto() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			byte terminalAtendimento = 1;
			Query query = sessao.createQuery("SELECT min(terminalAtendimento.numeroTerminal) FROM TerminalAtendimento terminalAtendimento"
					+ " where terminalAtendimento.status =0 ");
			   terminalAtendimento = (byte) query.uniqueResult();
			  
			System.out.println("Terminal DAO: "+terminalAtendimento);
			return  terminalAtendimento;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}
	}
/*	public byte verificaSituacaoCaixa(byte numeroTerminal) {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			byte terminalAtendimento;
			Query query = sessao.createQuery("SELECT min(terminalAtendimento.numeroTerminal) FROM TerminalAtendimento terminalAtendimento"
					+ " where terminalAtendimento.status =0 ");
			   terminalAtendimento = (byte) query.uniqueResult();
			System.out.println("Numero terminal DAO: "+terminalAtendimento);
			return  terminalAtendimento;
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}*/


