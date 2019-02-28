package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.Rule.ParserException;
import org.apache.jena.vocabulary.RDFS;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.sbolstandard.core2.AccessType;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.RestrictionType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SequenceConstraint;
import org.sbolstandard.core2.SequenceOntology;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

//https://sourceforge.net/p/owlapi/mailman/message/24568179/
public class SynBioHubExamplesDel 
{   public static void main(String[] args ) throws Exception
    {
       
      //	RDFMerger.combine("examples/igem/designs/BBa_K316027.xml","../sbol-owl/sbol.rdf", "examples/igem/del_BBa_K316027_sbolowl.xml");  
      	
      	OWLOntologyManager m=OWLManager.createOWLOntologyManager();
        OWLOntology o=m.loadOntologyFromOntologyDocument(new File("examples/igem/del/BBa_K316027.xml"));
        
        Reasoner hermit=new Reasoner(o);
        
        System.out.println("Is the design consistent: " + hermit.isConsistent());
        OWLEntityChecker checker = new SBOLEntityChecker(m.getOWLDataFactory());
        OWLDataFactory df = m.getOWLDataFactory();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, "TestClassGM EquivalentTo: definition some Promoter");
        parser.setOWLEntityChecker(checker);
        OWLAxiom axiom =  parser.parseAxiom();
        System.out.println("Axiom = " + axiom);
        
        OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#TestClassGM"));
        //https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java
        //shouldAddAxioms
        AddAxiom addAxiom = new AddAxiom(o, axiom);
        m.applyChange(addAxiom);
       // m.saveOntology(o);
        //OWLClassExpression cls = parser.parseClassExpression();
        System.out.println("Class = " + clsA);
        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(clsA, true);
        System.out.println("The Individuals of the class : ");
    
        for (OWLNamedIndividual i : instances.getFlattened()) {
              System.out.println(i.getIRI().getFragment());             
              }
        
        /*
        String def="TestClass EquivalentTo: sbol:Promoter";
        OWLClassExpression cls = parseClassExpression(def,o,m);
        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(cls, true);
        System.out.println("The Individuals of the class : ");
    
        for (OWLNamedIndividual i : instances.getFlattened()) {
              System.out.println(i.getIRI().getFragment());             
              }
        /*
        
        
        /*OWLDataFactory df = m.getOWLDataFactory();
        OWLClass cls = df.getOWLClass(IRI.create("http://sbols.org/v2#"+ "ptetRParent"));
        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(cls, true);
        System.out.println("The Individuals of the class : ");
    
        for (OWLNamedIndividual i : instances.getFlattened()) {
              System.out.println(i.getIRI().getFragment());             
              }
	
*/
    	System.out.println("done!");
    }

public static OWLClassExpression parseClassExpression(String classExpressionString, OWLOntology rootOntology,OWLOntologyManager m)
        throws ParserException {
    OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager()
            .getOWLDataFactory();
    // Set up the real parser
    ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(
            dataFactory, classExpressionString);
    parser.setDefaultOntology(rootOntology);
    // Specify an entity checker that wil be used to check a class
    // expression contains the correct names.
    
    Set<OWLOntology> importsClosure = new HashSet<OWLOntology>();//rootOntology.getImportsClosure();
    ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
    BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(m,
            importsClosure, shortFormProvider);
    
    OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
    parser.setOWLEntityChecker(entityChecker);
    // Do the actual parsing
    return parser.parseClassExpression();
}

}



/*

OWLClassExpression classExpression = parser
.parseClassExpression(classExpressionString);
NodeSet<OWLClass> superClasses = reasoner
.getSuperClasses(classExpression, direct);

*/

    
    
 