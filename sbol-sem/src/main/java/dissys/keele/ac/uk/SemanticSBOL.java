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

public class SemanticSBOL {

	private OWLEntityChecker checker;
	private OWLDataFactory df;
	private OWLOntology o;
	private OWLOntologyManager m;
	private Reasoner reasoner;
	ReasonerFactory factory;

	public SemanticSBOL(File sbolFile) throws OWLOntologyCreationException {
		m = OWLManager.createOWLOntologyManager();
		o = m.loadOntologyFromOntologyDocument(sbolFile);
		// Reasoner hermit=new Reasoner(o);
		// System.out.println("Is the design consistent: " + hermit.isConsistent());

		checker = new SBOLEntityChecker(m.getOWLDataFactory());
		df = m.getOWLDataFactory();
		m.setSilentMissingImportsHandling(true);
	}
	
	public SemanticSBOL(File sbolFile, Map<String,String> namespaces) throws OWLOntologyCreationException {
		m = OWLManager.createOWLOntologyManager();
		o = m.loadOntologyFromOntologyDocument(sbolFile);
		// Reasoner hermit=new Reasoner(o);
		// System.out.println("Is the design consistent: " + hermit.isConsistent());

		checker = new SBOLEntityChecker(m.getOWLDataFactory());
		df = m.getOWLDataFactory();
	}

	public void addClass(String className, String classDef) {
		ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, classDef);
		parser.setOWLEntityChecker(checker);
		OWLAxiom axiom = parser.parseAxiom();
		System.out.println("Axiom = " + axiom);

		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + className));
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		m.applyChange(addAxiom);

		// https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java
		// shouldAddAxioms
	}

	public void addSubClassing(String subClass, String parentClass) {
		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + subClass));
		OWLClass clsB = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + parentClass));
		OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsB);
		AddAxiom addAxiom = new AddAxiom(o, axiom);
		m.applyChange(addAxiom);
	}

	public void save() throws OWLOntologyStorageException {
		m.saveOntology(o);
	}

	/*public boolean isConsistent() throws OWLOntologyStorageException {
		if (reasoner == null) {
			reasoner = new Reasoner(o);
		}
		return reasoner.isConsistent();
	}*/

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

	// Based on the example from
	// https://github.com/phillord/hermit-reasoner/blob/master/examples/org/semanticweb/HermiT/examples/Explanations.java
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

	public void listSBOLEntities(String className) {
		OWLClass cls = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + className));

		System.out.println("Class = " + cls);
		NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);
		System.out.println("The Individuals of the class : ");

		for (OWLNamedIndividual i : instances.getFlattened()) {
			System.out.println(i.getIRI().getFragment());
		}
	}

	public void MakeDisjoint(String class1, String class2) {
		OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + class1));
		OWLClass clsB = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#" + class2));
		OWLDisjointClassesAxiom disjointClassesAxiom = df.getOWLDisjointClassesAxiom(clsA, clsB);
		m.addAxiom(o, disjointClassesAxiom);
	}

	public void addProperty(URI subject, URI object, URI property) {
		OWLNamedIndividual sub = df.getOWLNamedIndividual(IRI.create(subject.toString()));
		OWLNamedIndividual obj = df.getOWLNamedIndividual(IRI.create(object.toString()));
		OWLObjectProperty p = df.getOWLObjectProperty(IRI.create(property.toString()));
		OWLObjectPropertyAssertionAxiom propertyAssertion = df.getOWLObjectPropertyAssertionAxiom(p, sub, obj);
		m.addAxiom(o, propertyAssertion);
	}
}
