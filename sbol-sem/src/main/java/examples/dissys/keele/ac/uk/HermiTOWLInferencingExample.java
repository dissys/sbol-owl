package examples.dissys.keele.ac.uk;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class HermiTOWLInferencingExample {

	public static void main(String[] args) throws Exception {
    	OWLOntologyManager m=OWLManager.createOWLOntologyManager();
        OWLOntology o=m.loadOntologyFromOntologyDocument(new File("../examples/popsreceiver_sbolowl_withqueries.rdf"));
        
        Reasoner hermit=new Reasoner(o);
        
        System.out.println("Is the design consistent: " + hermit.isConsistent());
        
        OWLDataFactory df = m.getOWLDataFactory();
        
        OWLClass cls = df.getOWLClass(IRI.create("http://sbols.org/v2#"+ "ptetRParent"));
        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(cls, true);
        System.out.println("The Individuals of the class : ptetRParent");
    
        for (OWLNamedIndividual i : instances.getFlattened()) {
              System.out.println(i.getIRI().getFragment());             
              }
	
        System.out.println("The types of the individual : ptetR");
        OWLNamedIndividual ind=df.getOWLNamedIndividual(IRI.create("http://parts.igem.org/ptetR"));
        NodeSet<OWLClass> types=hermit.getTypes (ind, false);
        for (OWLClass i : types.getFlattened()) {
            System.out.println(i.getIRI().getFragment());             
            }
	}
	
	

}