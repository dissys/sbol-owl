package examples.dissys.keele.ac.uk;

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

import dissys.keele.ac.uk.RDFMerger;


public class SynBioHubExamples 
{   public static void main(String[] args ) throws Exception
    {
       
      	RDFMerger.combine("../examples/circuit_0x78_environment_md.xml","../sbol-owl/sbol.rdf", "../examples/circuit_0x78_environment_md_sbolowl.rdf");  
    	System.out.println("done!");
    }
}
    
    
  

/*
circuit_0x78_Module:
ModuleDefinition
and (isDefinitionOf some 
   (Module
    and ((isModuleOf some circuit_0x78_Module) or (isModuleOf value circuit_0x78_environment_md))))

circuit_0x78_Interaction:
Interaction and (isInteractionOf some circuit_0x78_Module)

To include the the most entity:
{circuit_0x78_environment_md} or (ModuleDefinition
 and (isDefinitionOf some 
    (Module
     and ((isModuleOf some circuit_0x78_Module) or (isModuleOf value circuit_0x78_environment_md)))))
     
*/


//Remove provo
//Tested with https://openprovenance.org/services/view/validator and prov0 visualiser at http://provoviz.org/


//ComponentDefinition and isDefinitionOf some (FunctionalComponent and (isParticipantOf some Participation))

//Interaction and (participation  some (Participation and participant some (FunctionalComponent and definition value TetR_protein)))
//Works: Interaction and (participation  some (Participation and (participant some (FunctionalComponent and (definition some ComponentDefinition)))))
//Works: Interaction and (participation  some (Participation and (participant some (FunctionalComponent and (definition value TetR_protein_gm)))))
//Testing: Interaction and ( (participation  some (Participation and (participant some (FunctionalComponent and (definition value TetR_protein_gm)))))
//or (isInteractionOf some ModuleDefinition and (isModuleOf some ModuleDefinition and )) )

