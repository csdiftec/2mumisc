package br.com.diftecnologia.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.diftecnologia.util.HibernateUtil;

@ViewScoped
@ManagedBean
public class BackupBean {
	Date date = new Date();
	 SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
	   String dataAtual = String.valueOf(sd.format(date));
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		
	public void backupteste01() {
		try {
			Runtime rt = Runtime.getRuntime();
			String[] commands = { "C:/xampp/mysql/bin/mysqldump.exe",
					"mysqldump -v-v-v -u root -p --databases victoria > 'mydb_dump02.sql' " };
			Process proc = rt.exec(commands);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println(exc);
		}
	}
	public boolean backup() {
		
        String executeCmd = "C:/xampp/mysql/bin/mysqldump.exe mysqldump -u root  -p --add-drop-database -B victoria -r > mydb_dump02.sql ";
        Process runtimeProcess;
        try {
 
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
 
            if (processComplete == 0) {
                System.out.println("Backup created successfully");
                return true;
            } else {
                System.out.println("Could not create the backup");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
        return false;
    }
	public void backupTeste() {
		 criarDiretorioDeBackup();
		try {
			backupUnidade();
			backupPessoa();
		
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}
	public void backupUnidade() {
	

			Query query = sessao.createSQLQuery("SELECT * INTO OUTFILE 'C:/DifTecnologia-ImportarNota/Backup/backup"+dataAtual+"/unidade_medida.csv' FIELDS TERMINATED BY ';' ENCLOSED BY '' LINES TERMINATED BY '\n'"
					+ " FROM unidade_medida");
		query.uniqueResult();

	}
	public void backupPessoa() {
	
		try {
		Query query01 = sessao.createSQLQuery("SELECT * INTO OUTFILE 'C:/DifTecnologia-ImportarNota/Backup/backup"+dataAtual+"/pessoa.csv' FIELDS TERMINATED BY ';' ENCLOSED BY '' LINES TERMINATED BY '\n'"
				+ " FROM pessoa");
	query01.uniqueResult();
		} catch (RuntimeException erro) {
			throw erro;
		} finally {
			sessao.close();
		}

	}
public void criarDiretorioDeBackup(){
	
	   
		File fileBackup = new File("C://DifTecnologia-ImportarNota/Backup/backup"+dataAtual);
		System.out.println("Arquivo criado................"+fileBackup);

		File theDir = new File("C://DifTecnologia-ImportarNota/Backup/backup"+dataAtual);
		if (!theDir.exists()) theDir.mkdir();

		
	}
}