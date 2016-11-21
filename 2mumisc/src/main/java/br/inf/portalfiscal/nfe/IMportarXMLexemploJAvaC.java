/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.inf.portalfiscal.nfe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;  
    

  
    
public class IMportarXMLexemploJAvaC{    
    
    public static void main(String[] args) {    
    
        try {    
            String xml = lerXML("C:\\NFe42150382663337000143550010003425861000685174-procnfe.xml");  
              
            TNfeProc wNfe = getNFe(xml);  
            if (wNfe != null) {  
                
                info("| Destinatario[CNPJ]........: " + wNfe.getNFe().getInfNFe().getDest().getCNPJ());    
                info("| Destinatario[Nome]........: " + wNfe.getNFe().getInfNFe().getDest().getXNome());  
                info("| Emitente[CNPJ]............: " + wNfe.getNFe().getInfNFe().getEmit().getCNPJ());    
                info("| Emitente[Nome]............: " + wNfe.getNFe().getInfNFe().getEmit().getXNome());  
                  
                System.out.println(wNfe.getNFe().getInfNFe().getDet().size());  
                for(int i=0; i < wNfe.getNFe().getInfNFe().getDet().size(); i++){  
                    info("| Item[Nome]............: " + wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getXProd() +" Código do Produto " + 
                    wNfe.getNFe().getInfNFe().getDet().get(i).getProd().getCProd());      
                }  
                      
            }  
              
        } catch (Exception e) {    
            error(e.toString());    
        }    
    }    
        
      
       
    public static TNfeProc getNFe(String xml) throws Exception{        
        try {        
            JAXBContext context = JAXBContext.newInstance(TNfeProc.class);        
            Unmarshaller unmarshaller = context.createUnmarshaller();        
            TNfeProc nfe = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), TNfeProc.class).getValue();        
            return nfe;  
        } catch (JAXBException ex) {        
            throw new Exception(ex.toString());        
        }        
    }        
      
    private static String lerXML(String fileXML) throws IOException {    
        String linha = "";    
        StringBuilder xml = new StringBuilder();    
    
        BufferedReader in = new BufferedReader(new InputStreamReader(    
                new FileInputStream(fileXML)));    
        while ((linha = in.readLine()) != null) {    
            xml.append(linha);    
        }    
        in.close();    
    
        return xml.toString();    
    }    
    
  
    
    private static void info(String log) {    
        System.out.println("INFO: " + log);    
    }    
    
    private static void error(String log) {    
        System.out.println("ERROR: " + log);    
    }    
        
}    