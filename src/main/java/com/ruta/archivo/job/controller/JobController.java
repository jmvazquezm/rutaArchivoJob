package com.ruta.archivo.job.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruta.archivo.job.listener.RutaArchivoJob;



@RestController
public class JobController {
	
	private static final Logger LOG = LoggerFactory.getLogger(JobController.class);
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job processJob;
	
	/*@Autowired
	RutaArchivoJob rutaArchivoJob;*/
	
	
	
	
	 @RequestMapping("/rutaArchivo")
	 //@Scheduled(fixedRate = 5000)
	 @Scheduled(cron = "0 0/05 * * * MON-FRI")//Se ejecuta cada 5 min. L-V
	// @Scheduled(cron = "0 0 9-23 * 11 MON-FRI")//Se ejecuta cada hora en mes de Noviembre de L-V
	    public String handle() throws Exception {
		 
		 		LOG.info("INICIO DE LA TAREA: " + new Date());
	            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
	                    .toJobParameters();
	            jobLauncher.run(processJob, jobParameters);
	 
	        return "SDASDADAFD";
	    }

}
