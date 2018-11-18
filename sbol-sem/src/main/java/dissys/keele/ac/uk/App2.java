package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.sbolstandard.core2.AccessType;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.RestrictionType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SequenceConstraint;
import org.sbolstandard.core2.SequenceOntology;

/**
 * Hello world!
 *
 */
public class App2 
{//http://parts.igem.org/wiki/index.php?title=Part:BBa_S03839
    public static void main( String[] args ) throws Exception
    {
        SBOLDocument doc=new SBOLDocument();
        //doc.setDefaultURIprefix("http://www.keele.ac.uk/dissys");
        doc.setDefaultURIprefix("http://parts.igem.org");
        
        ComponentDefinition BBa_B0010=doc.createComponentDefinition("B0010",ComponentDefinition.DNA);
        BBa_B0010.addRole(SequenceOntology.TERMINATOR);
        
        ComponentDefinition BBa_B0012=doc.createComponentDefinition("B0012",ComponentDefinition.DNA);
        BBa_B0012.addRole(SequenceOntology.TERMINATOR);
        
        
        ComponentDefinition ptetR=doc.createComponentDefinition("ptetR",ComponentDefinition.DNA);
        ptetR.addRole(SequenceOntology.PROMOTER);
        
        ComponentDefinition BBa_B0034=doc.createComponentDefinition("B0034",ComponentDefinition.DNA);
        BBa_B0034.addRole(SequenceOntology.RIBOSOME_ENTRY_SITE);
        
        ComponentDefinition luxR=doc.createComponentDefinition("luxR",ComponentDefinition.DNA);
        luxR.addRole(SequenceOntology.CDS);
        
        ComponentDefinition BBa_B0015=doc.createComponentDefinition("B0015",ComponentDefinition.DNA);
        BBa_B0015.addRole(SequenceOntology.TERMINATOR);
        addComponent(BBa_B0015, BBa_B0010);
        addComponent(BBa_B0015, BBa_B0012);
                
        ComponentDefinition pluxR=doc.createComponentDefinition("pluxR",ComponentDefinition.DNA);
        pluxR.addRole(SequenceOntology.PROMOTER);
      
                
        ComponentDefinition BBa_F2620=doc.createComponentDefinition("BBa_F2620",ComponentDefinition.DNA);
        BBa_F2620.addRole(SequenceOntology.ENGINEERED_GENE);
                   
        Component cptetR=addComponent(BBa_F2620, ptetR);
        Component cBBa_B0034=addComponent(BBa_F2620, BBa_B0034);
        Component cluxR=addComponent(BBa_F2620, luxR);
        Component cBBa_B0015=addComponent(BBa_F2620, BBa_B0015);
        Component cpluxR=addComponent(BBa_F2620, pluxR);
        
        addSequenceConstraint(BBa_F2620, cptetR, cBBa_B0034);
        addSequenceConstraint(BBa_F2620, cBBa_B0034, cluxR);
        addSequenceConstraint(BBa_F2620, cluxR, cBBa_B0015);
        addSequenceConstraint(BBa_F2620, cBBa_B0015, cpluxR);
        
        
        ComponentDefinition gfp=doc.createComponentDefinition("gfp",ComponentDefinition.DNA);
        gfp.addRole(SequenceOntology.CDS);
        
        
        ComponentDefinition BBa_B0030=doc.createComponentDefinition("B0030",ComponentDefinition.DNA);
        BBa_B0030.addRole(SequenceOntology.RIBOSOME_ENTRY_SITE);
        
        
        ComponentDefinition BBa_S03839=doc.createComponentDefinition("BBa_S03839",ComponentDefinition.DNA);
        BBa_S03839.addRole(SequenceOntology.ENGINEERED_GENE);
                   
        Component cBBa_F2620=addComponent(BBa_S03839, BBa_F2620);
        Component cB0030=addComponent(BBa_S03839, BBa_B0030);
        
        Component cgfp=addComponent(BBa_S03839, gfp);
        
        addSequenceConstraint(BBa_S03839, cBBa_F2620, cB0030);
        addSequenceConstraint(BBa_S03839, cB0030, cgfp);
        
        
        
        
        
        
        
        doc.write(new File("output.rdf"));
        
        Model model = ModelFactory.createDefaultModel() ;
        
        InputStream is=new FileInputStream("output.rdf");
        model.read(is, RDFS.getURI());
        
        //model.read("output.rdf") ;
        
        Model ontModel=ModelFactory.createDefaultModel() ;
        InputStream ontIs=new FileInputStream("../sbol-owl/sbol.rdf");
        ontModel.read(ontIs, RDFS.getURI());
        
        //ontModel.read("/home/goksel/work/sbol-owl/sbol-owl/sbol.owl");
        
        model.add(ontModel);
        
        //save(model, "output_merged.rdf", "Turtle");
        save(model, "example2.rdf");
        
                       
    }
    
    private static Component  addComponent(ComponentDefinition cd, ComponentDefinition subCd) throws SBOLValidationException
    {
    	return cd.createComponent(subCd.getDisplayId() + "_comp", AccessType.PUBLIC, subCd.getIdentity());
    	
    }
    
    private static SequenceConstraint addSequenceConstraint(ComponentDefinition cd, Component subject, Component object) throws SBOLValidationException
    {
    	return cd.createSequenceConstraint (subject.getDisplayId() + "precedes" + object.getDisplayId(), RestrictionType.PRECEDES, subject.getIdentity(), object.getIdentity());
    	
    }

	public static void save(Model rdfModel, String filePath) throws IOException, FileNotFoundException {
		save(rdfModel, filePath,getDefaultFormat());
	}	
	  
	public static void save(Model rdfModel, String filePath, String format) throws IOException, FileNotFoundException {
		if (format == null || format.length() == 0) {
			format = getDefaultFormat();
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(filePath));
			rdfModel.write(stream, format);
		} 
		finally {
			if (stream != null) {
				stream.close();
				stream = null;
			}
		}
	}

	
	public static String getDefaultFormat() {
		return "RDF/XML-ABBREV";
	}
}

/*
 * 
 PREFIX  sybio: <http://w3id.org/synbio/ont#>
SELECT ?ComponentDefinition`
WHERE {
   ?ComponentDefinition sbol:component ?Component
}
 * */
//To return CDs with child components
//ComponentDefinition and (component some owl:Thing)  

//To return CDs with child DNA components
//ComponentDefinition and (component some (definition some DNA))

// Terminator formed of other Terminators
//Terminator and (component some (definition some Terminator))

//To return parents with Promoter parts
//ComponentDefinition and (component some (definition some Promoter))


//I can't execute 
//There may be other components that are part of the Terminator. I have to use a closed world assumption
//Terminator and (component only (definition some Terminator))


//Individual reasoning:
//Find designs including pluxR
//DNA and (component some (definition value pluxR))

//Create a new class called pluxR_Entity:
//Now we can search for entities that contain pluxR or its parents
//Transitive
/*
DNA and (
  (component some (definition value pluxR))
  or 
  (component some (definition some pluxR_Entity))
)
*/

//To find out sequence constraints with the precedes relationship
//SequenceConstraint and (restriction value precedes) 

//Step 1:
//Component and (isSubjectOf some (SequenceConstraint))
//Step 2:
//Component and (isSubjectOf some (restriction value precedes))
//Step 3
//Component and (isSubjectOf some ((restriction value precedes) and (object some Component)))
//Step 3
//Component and (isSubjectOf some ((restriction value precedes) and (object some (definition value pluxR))))
//Step 4 Create a pluxRPreceder class and execute recursively
/*
Component and (isSubjectOf some ((restriction value precedes) and 
((object some (definition value pluxR))   or (object some pluxRPreceder))

))
*/

//Step5 Now we link it to the ComponentDefinition BBa_F2620
/*
Component and (isComponentOf value BBa_F2620) and (isSubjectOf some ((restriction value precedes) and 
((object some (definition value pluxR))   or (object some pluxRPreceder))

))

*/

//Step 6: Let's try to find all components that come after ptetR
//Create the ptetRFollower Class
/*Component and (isComponentOf value BBa_F2620) and (isObjectOf some ((restriction value precedes) and 
((subject some (definition value ptetR))   or (subject some ptetRFollower))

))

*/

//Step 7: correct

/*
  DNA and (
  (component some (isComponentOf value BBa_S03839)) or 
  (isDefinitionOf some 
    (Component
     and (isComponentOf some BBa_S03839_node))) or (isDefinitionOf some (isComponentOf value BBa_S03839)))
     
 **/
//Step 7_wrong:
//Let's define the class BBa_S03839_node to find all nodes in the design recursively, including itself
/*DNA and 

(
(isDefinitionOf some (isComponentOf value BBa_S03839)) or
(isDefinitionOf some (Component and (isComponentOf some BBa_S03839_node)))

)

and 

(
(isDefinitionOf some (isComponentOf value BBa_S03839)) or
(isDefinitionOf some (Component and (isComponentOf some BBa_S03839_node))) or
(component some (isComponentOf value BBa_S03839))

)
*/

//Step 8:
//Update ptetRFollower to find followers in BBa_S03839
/*
Component and (isComponentOf some BBa_S03839_node) and (isObjectOf some ((restriction value precedes) and 
((subject some (definition value ptetR))   or (subject some ptetRFollower))

))
*/

//step 9: gfp was not in the list
//Let's define the ptetTParent class first
/*DNA and (
(component some (definition value ptetR))
or 

(component some (definition some ptetRParent))
)
*/

//step 10:
//Let's find out all the components followed by ptetR in all sub components of BBa_S03839
/*


Component and (isComponentOf some BBa_S03839_node) and (isObjectOf some ((restriction value precedes) and 
((subject some (definition value ptetR))   or (subject some ptetRFollower) or (subject some (definition some ptetRParent)))

))

*/




//DEL: Promoter and (sequenceConstraint some ()
//Works: SequenceConstraint and (restriction some owl:Thing) 

//DNA and (isDefinitionOf some (isComponentOf value BBa_J24677))
/*DNA and 

(
(isDefinitionOf some (isComponentOf value BBa_J24677)) or
(isDefinitionOf some BBa_J24677_node)

)*/




/*Not working:
 DNA
 and ((component some (isComponentOf value BBa_S03839)) or (isDefinitionOf some 
    (Component
     and (isComponentOf some BBa_S03839_node))) or (isDefinitionOf some (isComponentOf value BBa_S03839)))
 and ((isDefinitionOf some 
    (Component
     and (isComponentOf some BBa_S03839_node))) or (isDefinitionOf some (isComponentOf value BBa_S03839)))
     
  
 */


		

