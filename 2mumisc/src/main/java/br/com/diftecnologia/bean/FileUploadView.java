/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.diftecnologia.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.primefaces.model.UploadedFile;

import br.com.diftecnologia.dao.CompraDAO;
import br.com.diftecnologia.dao.FormaPagamentoDAO;
import br.com.diftecnologia.dao.FornecedorDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.TipoPagamentoDAO;
import br.com.diftecnologia.domain.Compra;
import br.com.diftecnologia.domain.FormaPagamento;
import br.com.diftecnologia.domain.Fornecedor;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.TipoPagamento;
import br.inf.portalfiscal.nfe.TNfeProc;

@ViewScoped
@ManagedBean
public class FileUploadView implements Serializable {
	private static final long serialVersionUID = 1L;
	private UploadedFile file;
	private Fornecedor cnpjFornecedor;
	TNfeProc wNfe;
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public void LerArquivoXml() throws Exception {
		try {
			if (file != null) {
				String enderecoArquivo = "C://DifTecnologia-ImportarNota/" + file.getFileName();
				String xml = lerXML(enderecoArquivo);
				wNfe = getNFe(xml);
				criarDiretorioImportacao();
				verificaFornecedorPorCnpj();
				if (cnpjFornecedor != null) {
					if (wNfe != null) {
						System.out.println(wNfe.getNFe().getInfNFe().getEmit().getXNome());
						System.out.println(wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
						System.out.println(wNfe.getNFe().getInfNFe().getId());
						System.out.println(wNfe.getNFe().getInfNFe().getIde().getDEmi());
						System.out.println(wNfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF());
						inserirCabecalhoCompra();
						/*
						 * for (int i = 0; i <
						 * wNfe.getNFe().getInfNFe().getDet().size(); i++) {
						 * System.out.println(
						 * "\n|----------------------------------------------------------------------------------------------------------------------------------------------"
						 * ); System.out.println( "\n| Código: " +
						 * wNfe.getNFe().getInfNFe().getDet().get(i).getProd().
						 * getCEAN() + "\n"); System.out.println(
						 * "| Item[Nome]: " +
						 * wNfe.getNFe().getInfNFe().getDet().get(i).getProd().
						 * getXProd() + "\n"); System.out.println(
						 * "| Unidade de Medida: " +
						 * wNfe.getNFe().getInfNFe().getDet().get(i).getProd().
						 * getUCom() + "\n"); System.out.println(
						 * "| Qtd. de Item: " +
						 * wNfe.getNFe().getInfNFe().getDet().get(i).getProd().
						 * getQCom() + "\n"); System.out.println(
						 * "| Valor do Item: " +
						 * wNfe.getNFe().getInfNFe().getDet().get(i).getProd().
						 * getVUnCom()); }
						 */
					}
				} else {
					System.out.println("Fornecedor não cadastrado!");
					System.out.println("Informar ao usuario para cadastradar o fornecedor.");
				}
			}
		} catch (IOException e) {
			System.out.println("Erro na leitura doarquivo xml-Metodo lerArquivoXML " + e);
		}
	}
	
	public void inserirCabecalhoCompra()throws Exception{
		String enderecoArquivo = "C://DifTecnologia-ImportarNota/" + file.getFileName();
		String xml = lerXML(enderecoArquivo);
		wNfe = getNFe(xml);
		
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
		TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
		FornecedorDAO fornecedorDAO = new FornecedorDAO();
		
		Funcionario funcionario = funcionarioDAO.buscar(1L);		
		FormaPagamento formaPagamento = formaPagamentoDAO.buscar(1L);
		TipoPagamento tipoPagamento = tipoPagamentoDAO.buscar(1L);
		Fornecedor fornecedor = fornecedorDAO.verificarCnpj(wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
		System.out.println("Valor do xml:" + wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
		
		Long codigoFornecedor = fornecedor.getCodigo();
		System.out.println("Código do fornecedor:"+codigoFornecedor);
		fornecedor= fornecedorDAO.buscar(codigoFornecedor);
		System.out.println("Objeto Fornecedor:"+fornecedor);
		
		
		Date date = (Date)formatter.parse(wNfe.getNFe().getInfNFe().getIde().getDEmi());
		
		Compra compra = new Compra();
		compra.setValorTotal(new BigDecimal("182.35"));
		compra.setNumeroDocumentoFiscal(wNfe.getNFe().getInfNFe().getId());
		compra.setDataCompra(date);
		compra.setFuncionario(funcionario);
		compra.setFormaPagamento(formaPagamento);
		compra.setTipoPagamento(tipoPagamento);
		compra.setFornecedor(fornecedor);
		
		CompraDAO compraDAO = new CompraDAO();
		compraDAO.merge(compra);
		System.out.println("Salvo!");
	}
	
	
	
	
	public void verificaFornecedorPorCnpj() {
		try {
			FornecedorDAO fornecedorDAO = new FornecedorDAO();
			cnpjFornecedor = fornecedorDAO.verificarCnpj(wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
			if (cnpjFornecedor != null) {
				System.out.println("---------------------------------");
				System.out.println("Fornecedor Cadastrado");
				System.out.println("CNPJ:" + wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
				System.out.println(cnpjFornecedor);
				System.out.println("---------------------------------");
			}
		} catch (RuntimeException e) {
			System.out.println("Erro no metodo verificaFornecedorPorCnpj " + e);
		}

	}

	private static String lerXML(String fileXML) throws IOException {
		String linha = "";
		StringBuilder xml = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileXML)));
		while ((linha = in.readLine()) != null) {
			xml.append(linha);
		}
		in.close();

		return xml.toString();
	}
@PostConstruct
	public void criarDiretorioImportacao() {
		try {
			// Aqui cria o diretorio caso não exista
			File file = new File("C:/DifTecnologia-ImportarNota/");
			file.mkdirs();
			
			File theDir = new File("C:\\DifTecnologia-ImportarNota01");
			if (!theDir.exists()) theDir.mkdir();
			System.out.println("-----------------------------------DiretorioCriado-------------------------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static TNfeProc getNFe(String xml) throws Exception {
		try {
			JAXBContext context = JAXBContext.newInstance(TNfeProc.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TNfeProc nfe = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), TNfeProc.class).getValue();
			return nfe;
		} catch (JAXBException ex) {
			throw new Exception(ex.toString());
		}
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
