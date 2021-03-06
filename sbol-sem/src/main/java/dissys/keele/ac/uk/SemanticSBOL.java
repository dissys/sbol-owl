package dissys.keele.ac.uk;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import com.clarkparsia.owlapi.explanation.BlackBoxExplanation;
import com.clarkparsia.owlapi.explanation.HSTExplanationGenerator;


/**
 * Provides a programmatic access to OWL queries using SBOL-OWL. The constructor requires an SBOL file that is merged with the RDF version of SBOL-OWL. 
 * the RDFMerger can be used to programmatically merge these files. This class was based on examples from https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java
 * @author gokselmisirli
 *
 */
public class SemanticSBOL {

	private OWLEntityChecker checker;
	private OWLDataFactory df;
	private OWLOntology o;
	private OWLOntologyManager m;
	private Reasoner reasoner;
	private ReasonerFactory factory;
    
	/**
	 * Creates a default SemanticSBOL instance. Individuals are not included in queries.
	 * @param sbolFileWithSBOLOWL The SBOL file with the RDF content of SBOL-OWL
	 * @throws OWLOntologyCreationException
	 */
	public SemanticSBOL(File sbolFileWithSBOLOWL) throws OWLOntologyCreationException {
		this(sbolFileWithSBOLOWL,null);		
	}
	
	/**
	 * Creates a Semantic instance to include individuals in queries.
	 * @param sbolFileWithSBOLOWL The SBOL file with the RDF content of SBOL-OWL
	 * @param namespaces A HashMap with key/value pairs. Key: Namespace prefix, Value: Namespace. E.g.: igem, https://synbiohub.org/public/igem/
	 * @throws OWLOntologyCreationException
	 */
	public SemanticSBOL(File sbolFileWithSBOLOWL, Map<String,String> namespaces) throws OWLOntologyCreationException {
		m = OWLManager.createOWLOntologyManager();
		o = m.loadOntologyFromOntologyDocument(sbolFileWithSBOLOWL);
		
		checker = new SBOLEntityChecker(m.getOWLDataFactory(),namespaces);
		df = m.getOWLDataFactory();
		m.setSilentMissingImportsHandling(true);
		
	}

	/**
	 *
	 * @param className Name of the query to add, in the form of an OWL class
	 * @param classDef The query in Manchester syntax. E.g. "BsubtilisPromoter EquivalentTo: Promoter and isMemberOf value igem:chassis_prokaryote_bsubtilis"
	 */
	public void addClass(String className, String classDef) {
		ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, classDef);
		parser.setOWLEntityChecker(checker);
		OWLAxiom axiom = parser.parseAxiom();
		System.out.println("Axiom = " + axiom);

		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + className));
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		m.applyChange(addAxiom);

		// shouldAddAxioms
	}

	/**
	 * Makes one class subclass of another
	 * @param subClass The subclass
	 * @param parentClass The parent class
	 */
	public void addSubClassing(String subClass, String parentClass) {
		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + subClass));
		OWLClass clsB = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + parentClass));
		OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsB);
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		m.applyChange(addAxiom);
	}

	/**
	 * Saves the ontology using the default file used
	 * @throws OWLOntologyStorageException
	 */
	public void save() throws OWLOntologyStorageException {
		m.saveOntology(o);
	}

	/*public boolean isConsistent() throws OWLOntologyStorageException {
		if (reasoner == null) {
			reasoner = new Reasoner(o);
		}
		return reasoner.isConsistent();
	}*/

	/**
	 * Checks whether the SBOL data are consistent or not
	 * @return
	 * @throws OWLOntologyStorageException
	 */
	public boolean isConsistent() throws OWLOntologyStorageException {
		if (reasoner == null) {
			factory = new Reasoner.ReasonerFactory() {
				protected OWLReasoner createHermiTOWLReasoner(org.semanticweb.HermiT.Configuration configuration,
						OWLOntology ontology) {
					configuration.throwInconsistentOntologyException = false;
					return new Reasoner(configuration, ontology);
				}
			};

			Configuration configuration = new Configuration();
			configuration.throwInconsistentOntologyException = false;
			reasoner = (Reasoner) factory.createReasoner(o, configuration);
		}
		return reasoner.isConsistent();
	}

	/**
	 * If SBOL data are not consistent, this method lists explanations. It is based on the example from https://github.com/phillord/hermit-reasoner/blob/master/examples/org/semanticweb/HermiT/examples/Explanations.java
	 */
	public void printInconsistencies() {
		if (!reasoner.isConsistent()) {
			System.out.println("Computing explanations for the inconsistency...");

			BlackBoxExplanation exp = new BlackBoxExplanation(o, factory, reasoner);
			HSTExplanationGenerator multExplanator = new HSTExplanationGenerator(exp);
			// Now we can get explanations for the inconsistency \

			IRI identified = IRI.create("http://sbols.org/v2#Identified");
			OWLClass cls = df.getOWLClass(identified);
			Set<Set<OWLAxiom>> explanations = multExplanator.getExplanations(cls);

			for (Set<OWLAxiom> explanation : explanations) {
				System.out.println("------------------");
				System.out.println("Axioms causing the inconsistency: ");
				for (OWLAxiom causingAxiom : explanation) {
					System.out.println(causingAxiom);
				}
				System.out.println("------------------");
			}
		} else {
			System.out.println("Data are consistent");
		}
	}

	/**
	 * Lists SBOL entities for given type.
	 * @param className The type of SBOL entities
	 */
	public void listSBOLEntities(String className) {
		OWLClass cls = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + className));

		System.out.println("Class = " + cls);
		NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
		System.out.println("The Individuals of the class : ");

		for (OWLNamedIndividual i : instances.getFlattened()) {
			System.out.println(i.getIRI().getFragment());
		}
	}

	/**
	 * Makes two classes (or queries) disjoint
	 * @param class1 The first class or query
	 * @param class2 The second class or query
	 */
	public void makeDisjoint(String class1, String class2) {
		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + class1));
		OWLClass clsB = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + class2));
		OWLDisjointClassesAxiom disjointClassesAxiom = df.getOWLDisjointClassesAxiom(clsA, clsB);
		m.addAxiom(o, disjointClassesAxiom);
	}

	/**
	 * Adds a tuple for a given SBOL entity
	 * @param subject The SBOL entity
	 * @param object The value
	 * @param property The property
	 */
	public void addProperty(URI subject, URI object, URI property) {
		OWLNamedIndividual sub = df.getOWLNamedIndividual(IRI.create(subject.toString()));
		OWLNamedIndividual obj = df.getOWLNamedIndividual(IRI.create(object.toString()));
		OWLObjectProperty p = df.getOWLObjectProperty(IRI.create(property.toString()));
		OWLObjectPropertyAssertionAxiom propertyAssertion = df.getOWLObjectPropertyAssertionAxiom(p, sub, obj);
		m.addAxiom(o, propertyAssertion);
	}
}
