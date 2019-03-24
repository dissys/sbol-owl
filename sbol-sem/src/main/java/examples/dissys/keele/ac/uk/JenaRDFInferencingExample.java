package examples.dissys.keele.ac.uk;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.*;
import org.apache.jena.vocabulary.RDF;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;

public class JenaRDFInferencingExample {

	public static void main(String[] args) throws URISyntaxException, SBOLValidationException, IOException, SBOLConversionException {
		Model schema = FileManager.get().loadModel("../sbol-owl/sbol.rdf");
		Model data = FileManager.get().loadModel("../examples/popsreceiver.rdf");
		InfModel infmodel = ModelFactory.createRDFSModel(schema, data);

		Resource ptetR = infmodel.getResource("http://parts.igem.org/ptetR");
		System.out.println("ptetR has types:");
		printStatements(infmodel, ptetR, RDF.type, null);
	}
	
	public static void   printStatements(Model m, Resource s, Property p, Resource o) {
	    for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) {
	        Statement stmt = i.nextStatement();
	        System.out.println(" - " + PrintUtil.print(stmt));
	        	        
	    }
	}
}
