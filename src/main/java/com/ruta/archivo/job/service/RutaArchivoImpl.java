package com.ruta.archivo.job.service;

import java.io.BufferedInputStream;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ecs.model.Resource;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ruta.archivo.job.properties.IClaseProperties;





@Service
public class RutaArchivoImpl implements IRutaArchivo, Runnable{
	
	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoImpl.class);
	
	
	@Autowired
	IClaseProperties properties;
	
	
	
	@Value("${ruta.repositorio}")
	private String rRepositorio;
	
	@Value("${ruta.local}")
	private String rLocal;
	
	@Value("${AWSKeyId}")
	private String AWSKeyId;
	
	@Value("${AWSKey}")
	private String AWSKey;
	
	
	//***Para subir un objeto a S3***
	  private  String bucketName = "bucket-prueba-archivo";	  
	  private  String keyName = "TSP_SE_19112020.xml";	
	  private  String s3Url = "";	
	  public static final String ARCHIVO_PROPIEDADES = "C:/tmp/properties/pruebaDatos.properties";
	  
	  
	
	  
	 
	@Override
	public void descargaArchivoRepositorio() {
		
		
		//************Para prueba**************
		try {
			LOG.info("INICIA CARGA DE ARCHIVO");
			Properties propiedades = properties.leerPropiedades(ARCHIVO_PROPIEDADES);			
			LOG.info("SE CARGO ARCHIVO DE CONFIGURACIONES");
			LOG.info("RUTA_LOCAL: " + rLocal);
			LOG.info("RUTA_REPOSITORIO: " + rRepositorio);
			//LOG.info("AWSKEYID: " + AWSKeyId);
			//LOG.info("AWSKEY: " + AWSKey);			
			
		} catch (IOException e) {
			LOG.info("Pruebaaa fallida");
		}
			
		//****************************************
		
		
		File file = new File(rRepositorio); 			 
		if (file.equals(null)){ 
			LOG.error("RUTA REPOSITORIO ES NULA");
		}else {			
			LOG.info("INICIO DE DESCARGA DEL ARCHIVO: " + new Date());
			run();
			new Thread().start();			
		}
		
		
	
		
	}

	
	@Override
	public void subirArchivoS3() {
						
			//************************************************************************
			//****SUBIR UN OBJETO A S3****
			
			 BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AWSKeyId, AWSKey);
		        AmazonS3Client s3client = new AmazonS3Client(awsCredentials);
		     
		        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		        s3client.setRegion(usEast1);
		        try {
		         
		           LOG.info("SUBIENDO OBJETO A S3 DESDE UN ARCHIVO " + new Date());
		            File file = new File(rLocal);
		            s3client.putObject(new PutObjectRequest(
		                    bucketName, keyName, file));
		            s3client.setObjectAcl(bucketName, keyName, CannedAccessControlList.PublicRead);
		            s3Url = s3client.getResourceUrl(bucketName, keyName);
		            LOG.info("S3 Url:       " + s3Url);
		            LOG.info("FINALIZA LA CARGA DE OBJETO A S3" + new Date());

		        } catch (AmazonServiceException ase) {
		            LOG.error("Se ha detectado una AmazonServiceException" +
		        "La solicitud a Amazon S3 pero fue rechazada con una respuesta de error.");
		            LOG.info("Mensaje Error:    " + ase.getMessage());
		            LOG.info("HTTP Status Code: " + ase.getStatusCode());
		            LOG.info("AWS Error Code:   " + ase.getErrorCode());
		            LOG.info("Request ID:       " + ase.getRequestId());
		            LOG.info("Error Type:       " + ase.getErrorType());
		        } catch (AmazonClientException ace) {
		            LOG.error("Se ha detectado una AmazonClientException" +
		        "Encontro un error interno al comunicarse con S3.");
		            LOG.info("Error Message: " + ace.getMessage());
		        }
			
			
			//******
		        
		    //***************************    
			
			
			
		
	}
	
	@Override
	public void delete() {
		
		File dFile = new File(rLocal);
		
		if(dFile.exists()) {
			LOG.info("ELIMINANDO ARCHIVO..." +  new Date()) ;
			dFile.delete();
			LOG.info("FUE ELIMINADO " +  new Date()) ;
		}else {
			LOG.error("NO ELIMINO EL ARCHIVO");
		}
	
		
		
	}
	
	@Override
	public void run() {
		try {
			URL url = new URL(rRepositorio);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();			
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(rLocal);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];			
			int read = 0;			
			while((read = in.read(buffer, 0, 1024)) >= 0) {
				bout.write(buffer, 0, read);				
			}
			bout.close();
			in.close();
			LOG.info("FINALIZA LA DESCARGA CORRECTAMENTE!!: " + new Date());
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}		
	}
	
}
