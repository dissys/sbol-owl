package examples.dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.sbolstandard.core2.*;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import dissys.keele.ac.uk.RDFMerger;
import dissys.keele.ac.uk.SemanticSBOL;

public class InConsistenceyCheck_MapsToExample {

	public static void main(String[] args) throws URISyntaxException, SBOLValidationException, IOException, SBOLConversionException, OWLOntologyCreationException, OWLOntologyStorageException {

		//http://sbols.org/MapsToExample/fc1_def/component1 is private and should be public since it is referred to as the remote
		String inconsistentFile="../examples/mapsto_sbolowl_inconsistent.rdf";
		String consistentFile="../examples/mapsto_sbolowl_consistent.rdf";
		
		RDFMerger.combine("../examples/mapsto_inconsistent.rdf","../sbol-owl/sbol.rdf", inconsistentFile);
		System.out.println("Created the files!");
		SemanticSBOL semanticSBOL=new SemanticSBOL(new File(inconsistentFile));				
        boolean isConsistent=semanticSBOL.isConsistent();
        System.out.println("File: " +  inconsistentFile + "   Is the file consistent? - " + isConsistent);
        semanticSBOL.printInconsistencies();
		
         semanticSBOL=new SemanticSBOL(new File(consistentFile));				
         isConsistent=semanticSBOL.isConsistent();
         semanticSBOL.printInconsistencies();
 		
        System.out.println("File: " +  consistentFile + "   Is the file consistent? - " + isConsistent);
        
        System.out.println("done!");
	}
	
	
}
