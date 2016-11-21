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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.omnifaces.util.Messages;
import org.primefaces.model.UploadedFile;

import br.com.diftecnologia.dao.CompraDAO;
import br.com.diftecnologia.dao.FormaPagamentoDAO;
import br.com.diftecnologia.dao.FornecedorDAO;
import br.com.diftecnologia.dao.FuncionarioDAO;
import br.com.diftecnologia.dao.ItemCompraDAO;
import br.com.diftecnologia.dao.ProdutoDAO;
import br.com.diftecnologia.dao.TipoPagamentoDAO;
import br.com.diftecnologia.dao.UnidadeMedidaDAO;
import br.com.diftecnologia.domain.Compra;
import br.com.diftecnologia.domain.FormaPagamento;
import br.com.diftecnologia.domain.Fornecedor;
import br.com.diftecnologia.domain.Funcionario;
import br.com.diftecnologia.domain.ImportarNFE;
import br.com.diftecnologia.domain.ItemCompra;
import br.com.diftecnologia.domain.Produto;
import br.com.diftecnologia.domain.TipoPagamento;
import br.com.diftecnologia.domain.UnidadeMedida;
import br.inf.portalfiscal.nfe.TNfeProc;

@ManagedBean
@ViewScoped
public class ImportarNfeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value="#{autenticacaoBean}")
	private AutenticacaoBean autenticacaoBean;
	private UploadedFile file;
	private Fornecedor cnpjFornecedor;
	public short quantidade, porcentagemDoDesconto;
	public BigDecimal valorParcial,
	valorDoDesconto = BigDecimal.ZERO,
	valor= BigDecimal.ZERO;
	Double valorAtualizadoEstoque;
	public long codigoCompra, codigoBarras;
	public Integer  quantidadeCritica;
	TNfeProc wNfe;
	public String descricao;
	public Boolean situacao=true;
	public Double quantidadeEstoque;
	private ImportarNfeBean importarNfe ;
	private ImportarNFE importar ;
	private Produto produto;
	public boolean idNFeImportada;
	String enderecoArquivo=null;
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private String CPFformatado, nomeFantasia;
	

	public void LerArquivoXml() throws Exception {
		try {
		
			if (file != null) {
//String 	sistemaOperacional = System.getProperty("os.name");


			System.out.println(System.getProperty("os.name")); 
//			if(sistemaOperacional.equals("Linux")){
//				enderecoArquivo = ""+System.getProperty("user.home")+"/DifTecnologia-ImportarNota/" + file.getFileName();
//				System.out.println(enderecoArquivo);
//			}else if(sistemaOperacional.equals("Windows")){
				 enderecoArquivo = "C://DifTecnologia-ImportarNota/" + file.getFileName();
				System.out.println(enderecoArquivo);
	//		}
			

			
				String xml = lerXML(enderecoArquivo);
				wNfe = getNFe(xml);
				criarDiretorioImportacao();
				verificaFornecedorPorCnpj();
				verificaDuplicidadeNFe();
				
				
				
				if (cnpjFornecedor != null) {
					if(idNFeImportada!= true){
					if (wNfe != null) {
						System.out.println(wNfe.getNFe().getInfNFe().getEmit().getXNome());
						System.out.println(wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
						System.out.println(wNfe.getNFe().getInfNFe().getId());
						System.out.println(wNfe.getNFe().getInfNFe().getIde().getDEmi());
						System.out.println(wNfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF());
				
						inserirCabecalhoCompra();
						verificaProdutoPorCodigoBarraComDescricao();
						Messages.addGlobalInfo("Importação realizada!");
					}
				
						}else{
					Messages.addGlobalError("Nota Fiscal Já cadastrada!");
				} }else {
					Messages.addGlobalError("Ação permitida, fornecedor não cadastrado.");
				}
			}
		} catch (IOException e) {
			System.out.println("Erro na leitura do arquivo xml-Metodo lerArquivoXML " + e);
			Messages.addGlobalError("Erro na leitura do arquivo");
		}
	}
	
	
	
	public void verificaProdutoPorCodigoBarraComDescricao()throws Exception{
		String enderecoArquivo = "C://DifTecnologia-ImportarNota/" + file.getFileName();
		String xml = lerXML(enderecoArquivo);
		wNfe = getNFe(xml);
		
		for (int i = 0; i < wNfe.getNFe().getInfNFe().getDet().size(); i++){
		 System.out.println("\n|------------------------------------Topo------------------------------------"); 
		 System.out.println("\n| Código: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getCEAN() + "\n"); 
		 System.out.println("| Item[Nome]: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd(). getXProd() + "\n");
		 System.out.println("| Unidade de Medida: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getUCom() + "\n");
		 System.out.println("| Qtd. de Item: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getQCom() + "\n"); 
		 System.out.println("| Valor do Item: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getVUnCom());
		
		 System.out.println("\n|-----------------------------------------------------------------------------"); 
			try {
				String codigo = wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getCEAN();
				ProdutoDAO produtoDAO = new ProdutoDAO();
				 produto = produtoDAO.verificaProdutoExiste(codigo,wNfe.getNFe().getInfNFe().getDet().get(i).getProd(). getXProd());

				 codigoBarras= Long.parseLong(wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getCEAN());
					descricao= wNfe.getNFe().getInfNFe().getDet().get(i).getProd(). getXProd() ;
					quantidadeCritica = 0;
					quantidadeEstoque = Double.parseDouble(wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getQCom());
					situacao =true;
					valor=new BigDecimal(wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getVUnCom());
					salvarItemCompra();
				if(produto!=null){
					
					
					 atualizaDadosProduto();
					 System.out.println("Atualiza o valor");
					}else {					
					salvarProduto();	
					
					
				}
				

			} catch (Exception e) {
			 System.out.println("Erro:Método 'verificaProdutoPorCodigoBarra()' retorna null"); 
		 }
		  }
	}
public void salvarItemCompra(){
	CompraDAO compraDAO = new CompraDAO();
	 Compra compra = compraDAO.buscarUltimaCompra();
	 
	 ItemCompra itemCompra= new ItemCompra();
	 
	 itemCompra.setPorcentagemDoDesconto((short) 0);
	 itemCompra.setQuantidade(quantidadeEstoque);
	 itemCompra.setValorComDesconto(valor);
	 itemCompra.setValorDoDesconto(valorDoDesconto);
	 itemCompra.setValorParcial(valor);
	 itemCompra.setProduto(produto);
	
	 itemCompra.setValorParcial(valor);
	 itemCompra.setCompra(compra);
	 ItemCompraDAO itemCompraDAO = new ItemCompraDAO();
	 itemCompraDAO.merge(itemCompra);
	 System.out.println("Item da compra salvo:");
	 
	 
}
	
	public void salvarProduto(){
		
		Produto produto= new Produto();
		FuncionarioDAO funcionarioDAO= new FuncionarioDAO();
	
		UnidadeMedidaDAO unidadeDAO=new UnidadeMedidaDAO();
	
		UnidadeMedida unidadeMedida= unidadeDAO.buscar(1L);	
		Funcionario funcionario =funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo());

		produto.setCodigoBarras(codigoBarras);
		produto.setDescricao(descricao);
		produto.setFuncionario(funcionario);
		produto.setQuantidadeCritica(quantidadeCritica);
		produto.setQuantidadeEstoque(quantidadeEstoque);
		produto.setSituacao(situacao);
		produto.setUnidadeMedida(unidadeMedida);
		produto.setValor(valor);
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoDAO.merge(produto);
		System.out.println(produto);
		
	}
	
	public void atualizaDadosProduto() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		Produto produto = produtoDAO.verificaProdutoExiste(String.valueOf(codigoBarras),descricao);
	   valorAtualizadoEstoque = (double) (produto.getQuantidadeEstoque()+quantidadeEstoque);
	   produto.setQuantidadeEstoque(valorAtualizadoEstoque);
	   produtoDAO.merge(produto);
		
		System.out.println("Atualiza dados do produto,valor:"+ produto.getQuantidadeEstoque());
	}
	
	public void inserirCabecalhoCompra()throws Exception{
	//	String enderecoArquivo = "C://DifTecnologia-ImportarNota/" + file.getFileName();
		String xml = lerXML(enderecoArquivo);
		wNfe = getNFe(xml);
		
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
		TipoPagamentoDAO tipoPagamentoDAO = new TipoPagamentoDAO();
		FornecedorDAO fornecedorDAO = new FornecedorDAO();
		
		Funcionario funcionario = funcionarioDAO.buscar(autenticacaoBean.getFuncionarioLogado().getCodigo()); 
		System.out.println("Funcionario Logado no cadastro venda..... "+autenticacaoBean.getFuncionarioLogado().getCodigo());
	
		FormaPagamento formaPagamento = formaPagamentoDAO.buscar(1L);
		TipoPagamento tipoPagamento = tipoPagamentoDAO.buscar(1L);
		//formatar cnpj
		String cnpj =wNfe.getNFe().getInfNFe().getEmit().getCNPJ();
		String bloco1 = cnpj.substring(0, 2);
		String bloco2 = cnpj.substring(2, 5);
		String bloco3 = cnpj.substring(5, 8);
		String bloco4 = cnpj.substring(8, 12);
		String bloco5 = cnpj.substring(12, 14);
		cnpj = bloco1+"."+bloco2+"."+bloco3+"/"+bloco4+"-"+bloco5;
		System.out.println("Inserir cabeçalho da compra:..............................."+cnpj);				
		Fornecedor fornecedor = fornecedorDAO.verificarCnpj(cnpj);
		System.out.println("Valor do xml:" + wNfe.getNFe().getInfNFe().getEmit().getCNPJ());
		
		Long codigoFornecedor = fornecedor.getCodigo();
		
		fornecedor= fornecedorDAO.buscar(codigoFornecedor);
	
		
		
		Date date = (Date)formatter.parse(wNfe.getNFe().getInfNFe().getIde().getDEmi());
		Double  totalNF = Double.parseDouble(wNfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF());
		Compra compra = new Compra();
		compra.setValorTotal(new BigDecimal(totalNF));
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
//			System.out.println("076.256.686-77");
//			
//			String  cnpj = "25.589.196/0001-89";
//			cnpj = cnpj.replace("/", "");
//			cnpj = cnpj.replace(".", "");
//			cnpj = cnpj.replace("-", "");
//				
//				System.err.println("-----------------------------------------------"+cnpj);
			String cnpj =wNfe.getNFe().getInfNFe().getEmit().getCNPJ();
			String bloco1 = cnpj.substring(0, 2);
			String bloco2 = cnpj.substring(2, 5);
			String bloco3 = cnpj.substring(5, 8);
			String bloco4 = cnpj.substring(8, 12);
			String bloco5 = cnpj.substring(12, 14);
			cnpj = bloco1+"."+bloco2+"."+bloco3+"/"+bloco4+"-"+bloco5;
			System.out.println(cnpj);						
			FornecedorDAO fornecedorDAO = new FornecedorDAO();
			cnpjFornecedor = fornecedorDAO.verificarCnpj(cnpj);
			
			if (cnpjFornecedor != null) {
				System.out.println("---------------------------------");
				System.out.println("Fornecedor Cadastrado");
				System.out.println(cnpj);
				
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
	
		File file = new File("C://DifTecnologia-ImportarNota/");
		System.out.println("Windows................"+file);
	
//			file = new File(""+System.getProperty("user.home")+"/DifTecnologia-ImportarNota/");
//			System.out.println("ubuntu.........."+file);
//			file.mkdirs();
		File theDir = new File("C://DifTecnologia-ImportarNota");
		if (!theDir.exists()) theDir.mkdir();
	
		   System.out.println(file.getAbsolutePath() + " - " + file.exists());
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
	
	public Boolean verificaDuplicidadeNFe() throws IOException {

		try {

			if (file != null) {

	
				String xml = lerXML(enderecoArquivo);
				TNfeProc wNfe = getNFe(xml);

				CompraDAO compraDAO = new CompraDAO();
				 List<Compra> idNFe = compraDAO.buscarPorIdNFe(wNfe.getNFe().getInfNFe().getId());
				 
				System.out.println(idNFe);
				if(!idNFe.isEmpty()){
				System.out.println("Id da nota cadastrada no sistema: "+ idNFe);	
				idNFeImportada = true;
				}else{
					System.out.println("Nota Fiscal não cadastrada no sistema!"+ idNFe);	
					idNFeImportada = false;
				}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	
return idNFeImportada;
	}
	
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}




	public ImportarNfeBean getImportarNfe() {
		return importarNfe;
	}


	public void setImportarNfe(ImportarNfeBean importarNfe) {
		this.importarNfe = importarNfe;
	}



	public Produto getProduto() {
		return produto;
	}



	public void setProduto(Produto produto) {
		this.produto = produto;
	}



	public String getCPFformatado() {
		return CPFformatado;
	}

	public void setCPFformatado(String cPFformatado) {
		CPFformatado = cPFformatado;
	}

	
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public ImportarNFE getImportar() {
		return importar;
	}

	public void setImportar(ImportarNFE importar) {
		this.importar = importar;
	}

	public AutenticacaoBean getAutenticacaoBean() {
		return autenticacaoBean;
	}

	public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
		this.autenticacaoBean = autenticacaoBean;
	}
	
}
