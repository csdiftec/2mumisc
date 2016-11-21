package br.com.diftecnologia.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.domain.AberturaOcorrencia;
import br.com.diftecnologia.util.HibernateUtil;

public class AberturaOcorrenciaDAO extends GenericDAO<AberturaOcorrencia> {

	public Long verificaExistenciaOcorrencia(long codigo){
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT count(aberturaOcorrencia) FROM AberturaOcorrencia aberturaOcorrencia "
					+ "where aberturaOcorrencia.caixa.codigo = :codigo");
			query.setParameter("codigo", codigo);
			Long numeroRegistros = (Long) query.uniqueResult();
System.out.println(numeroRegistros);
			return numeroRegistros;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

		}
	@SuppressWarnings("unchecked")
	public List<AberturaOcorrencia> listarOcorrenciasAbertas() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
			try {
				Query query = sessao.createQuery("SELECT aberturaOcorrencia FROM AberturaOcorrencia aberturaOcorrencia "
						+ "where aberturaOcorrencia.status =true");
				List<AberturaOcorrencia> listaOcorrenciasAbertas = (List<AberturaOcorrencia>) query.list();
	System.out.println(listaOcorrenciasAbertas);
				return listaOcorrenciasAbertas;
			} catch (RuntimeException erro) {

				throw erro;
			} finally {
				sessao.close();
			}

			}
	
	public Long  buscarUltimaOcorenciaAberta() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {
			Query query = sessao.createQuery("SELECT max(aberturaOcorrencia.codigo)  FROM AberturaOcorrencia aberturaOcorrencia");
				Long codigoOcorrencia = (Long) query.uniqueResult();
System.out.println("Codigo ocorrencia DAO..........:"+codigoOcorrencia);
			return codigoOcorrencia;
		} catch (RuntimeException erro) {

			throw erro;
		} finally {
			sessao.close();
		}

	}
	}
