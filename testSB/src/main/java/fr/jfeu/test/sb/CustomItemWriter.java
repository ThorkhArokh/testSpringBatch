package fr.jfeu.test.sb;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.couchbase.client.CouchbaseClient;

public class CustomItemWriter implements ItemWriter {

	static Logger logger = Logger.getLogger(CustomItemWriter.class);

	public void write(List list) throws Exception {

		//ArrayList<URI> nodes = new ArrayList<URI>();

	    // Add one or more nodes of your cluster (exchange the IP with yours)
	    //nodes.add(URI.create("http://192.168.59.103:49155/pools"));

	    // Try to connect to the client
	    //CouchbaseClient client = null;
//	    try {
//	      client = new CouchbaseClient(nodes, "default", "");
//	    } catch (Exception e) {
//	    	logger.error("Error connecting to Couchbase: " + e.getMessage());
//	    }

		for (Object object : list) {
			CustomPojo cp = (CustomPojo)object;
			logger.info("writing Pojo : " + cp.getStr());
			
			// Set your first document with a key of "hello" and a value of "couchbase!"
		    int timeout = 0; // 0 means store forever
		    //client.set(String.valueOf(Math.random()), timeout, cp.getStr());
		}
		
		// Shutdown the client
	    //client.shutdown();

	}

}
