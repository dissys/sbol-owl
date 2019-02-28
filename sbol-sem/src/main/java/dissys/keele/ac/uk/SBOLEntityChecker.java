package dissys.keele.ac.uk;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class SBOLEntityChecker implements OWLEntityChecker {
    private OWLDataFactory factory;
    public static final String NS = "http://sbols.org/v2";

    public SBOLEntityChecker(OWLDataFactory factory) {
        this.factory = factory;
    }
    


    public OWLClass getOWLClass(String name) {
        if (Character.isUpperCase(name.toCharArray()[0])) {
            return factory.getOWLClass(IRI.create(NS + "#" + name));
        }
        else {
            return null;
        }
    }
    
    public OWLObjectProperty getOWLObjectProperty(String name) {
        if (!Character.isUpperCase(name.toCharArray()[0])) {
            return factory.getOWLObjectProperty(IRI.create(NS + "#" + name));
        }
        else {
            return null;
        }
    }
    
    public OWLAnnotationProperty getOWLAnnotationProperty(String name) {
        return null;
    }

    public OWLDataProperty getOWLDataProperty(String name) {
        return null;
    }

    public OWLDatatype getOWLDatatype(String name) {
        return null;
    }

    public OWLNamedIndividual getOWLIndividual(String name) {
        if (name.startsWith("chassis"))
        {
        	return factory.getOWLNamedIndividual(IRI.create("https://synbiohub.org/public/igem/" + name));
        }
        else
        {
        	return factory.getOWLNamedIndividual(IRI.create(NS + "#" + name));
        }      
    }
    
}