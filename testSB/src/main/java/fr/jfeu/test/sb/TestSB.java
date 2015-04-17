package fr.jfeu.test.sb;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestSB implements CommandLineRunner {
	
	//java -jar testSB-0.1.0.jar --spring.config.location=file:/Users/dom/
	
	static Logger logger = Logger.getLogger(TestSB.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		logger.info("Start testSB");
		SpringApplication.run(TestSB.class, args);
		
//		SpringApplication app = new SpringApplication(TestSB.class);
//        app.setWebEnvironment(false);
//        
//        app.run(args);
	}

	public void run(String... arg0) throws Exception {
		System.out.println("Launch testSB");
		logger.info("Launch testSB");
		
	}

}
