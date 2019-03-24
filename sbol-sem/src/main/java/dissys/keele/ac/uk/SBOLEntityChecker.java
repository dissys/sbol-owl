package dissys.keele.ac.uk;

import java.util.Map;
import java.util.Map.Entry;

import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Thsi class is used to resolve IRIs of SBOL types and entities, whether they are defined in SBOL data model or using other namespaces.
 * @author gokselmisirli
 *
 */
public class SBOLEntityChecker implements OWLEntityChecker {
    private OWLDataFactory factory;
    public static final String NS = "http://sbols.org/v2";
    private Map<String,String> namespaces=null;
	
    /**
     * To resolve entities using the SBOL namespace only. 
     * @param factory
     */
    public SBOLEntityChecker(OWLDataFactory factory) {
        this.factory = factory;
    }
    
    /**
     * To resolve entities using the SBOL namespace and using the prefix/namespace mappings.
     * @param factory
     * @param namespaces
     */
    public SBOLEntityChecker(OWLDataFactory factory, Map<String,String> namespaces ) {
        this.factory = factory;
        this.namespaces=namespaces;
    }
    
    /**
     * Resolves IRIs for classes
     */
    public OWLClass getOWLClass(String name) {
        if (Character.isUpperCase(name.toCharArray()[0])) {
            return factory.getOWLClass(IRI.create(NS + "#" + name));
        }
        else {
            return null;
        }
    }
    
    /**
     * Resolves IRIs for object properties
     */
    public OWLObjectProperty getOWLObjectProperty(String name) {
        if (!Character.isUpperCase(name.toCharArray()[0])) {
        	IRI iri=getSBOLIRI(name);
            return factory.getOWLObjectProperty(iri);
        }
        else {
            return null;
        }
    }
    
    /**
     * Resolves IRIs for annotation properties
     */
    public OWLAnnotationProperty getOWLAnnotationProperty(String name) {
    	 if (!Character.isUpperCase(name.toCharArray()[0])) {
         	IRI iri=getSBOLIRI(name);
             return factory.getOWLAnnotationProperty(iri);
         }
         else {
             return null;
         }
    }

    /**
     * Resolves IRIs for data properties
     */
    public OWLDataProperty getOWLDataProperty(String name) {
    	 if (!Character.isUpperCase(name.toCharArray()[0])) {
         	IRI iri=getSBOLIRI(name);
             return factory.getOWLDataProperty(iri);
         }
         else {
             return null;
         }
    }

    /**
     * Resolves IRIs for data types
     */
    public OWLDatatype getOWLDatatype(String name) {
    	 if (!Character.isUpperCase(name.toCharArray()[0])) {
         	IRI iri=getSBOLIRI(name);
             return factory.getOWLDatatype(iri);
         }
         else {
             return null;
         }
    }

    /**
     * Resolves IRIs for individuals
     */
    public OWLNamedIndividual getOWLIndividual(String name) {
    	IRI iri=getIRI(name);
    	return factory.getOWLNamedIndividual(iri);    
    }
    
    private IRI getSBOLIRI(String name)
    {
    	return  IRI.create(NS + "#" + name);
    }
    
    private IRI getIRI(String name)
    {
    	boolean found=false;
    	if (namespaces!=null)
    	{
    		for (Entry<String, String> namespace:this.namespaces.entrySet())
    		{
    			String key=namespace.getKey() + ":";
    			if (name.startsWith(key))
    			{
    				name=name.substring(key.length()) ;
    				name=namespace.getValue() + name;
    				found=true;
    			}
    		}
    	}
    	if (found)
		{
    		return IRI.create(name);	
		}
    	else
    	{
    		return getSBOLIRI(name);
    	}
    }
    
  
}