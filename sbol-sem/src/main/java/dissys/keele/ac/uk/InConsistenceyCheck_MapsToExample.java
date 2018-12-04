package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.sbolstandard.core2.*;

public class InConsistenceyCheck_MapsToExample {

	public static void main(String[] args) throws URISyntaxException, SBOLValidationException, IOException, SBOLConversionException {

		//http://sbols.org/MapsToExample/fc1_def/component1 is private and should be public since it is referred to as the remote
		   
		RDFMerger.combine("examples/mapsto_inconsistent.rdf","../sbol-owl/sbol.rdf", "examples/mapsto_sbolowl_inconsistent.rdf");
		
		System.out.println("done!");
	}
	
	
}
