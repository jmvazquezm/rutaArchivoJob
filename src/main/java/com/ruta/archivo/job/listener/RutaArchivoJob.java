package com.ruta.archivo.job.listener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ruta.archivo.job.service.RutaArchivoImpl;

public class RutaArchivoJob extends JobExecutionListenerSupport   {

	private static final Logger LOG = LoggerFactory.getLogger(RutaArchivoJob.class);
	
	@Autowired
	RutaArchivoImpl rutaArchivoImpl;
	

	@Override	
	public void afterJob(JobExecution jobExecution) {

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			
			
			
			rutaArchivoImpl.descargaArchivoRepositorio();
			
			rutaArchivoImpl.subirArchivoS3();
			
			//rutaArchivoImpl.delete();
			
			
			
				

			} 
			
			

		else if (jobExecution.getStatus() == BatchStatus.FAILED) {

			LOG.info("¡EL JOB FINALIZÓ CON ERROR!");
		}
	}
}

	
	

	


