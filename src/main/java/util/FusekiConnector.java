package util;

import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;

public class FusekiConnector {
	private Dataset ds; 
	private FusekiServer server;
	
	public FusekiConnector() {
		this.ds = DatasetFactory.createTxnMem();
		this.server = FusekiServer.create()
			    .add("/ds",ds)
			    .build();
	}
	
	public void startServer() {
		this.server.start();
	}
	
	public void stopServer() {
		server.stop() ;
	}

	public Dataset getDs() {
		return ds;
	}

	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	public FusekiServer getServer() {
		return server;
	}

	public void setServer(FusekiServer server) {
		this.server = server;
	}
}
