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

public class ConsistenceyCheck_MapsToExample {

	public static void main(String[] args) throws URISyntaxException, SBOLValidationException, IOException, SBOLConversionException {
		SBOLDocument doc = new SBOLDocument();

		doc.setDefaultURIprefix("http://sbols.org/MapsToExample/");
		doc.setComplete(true);
		doc.setCreateDefaults(true);
		
		String version = "";
		
		ModuleDefinition md1 = doc.createModuleDefinition("md1", version);
		ComponentDefinition fc1_def = doc.createComponentDefinition("fc1_def", version, ComponentDefinition.DNA);
		ComponentDefinition fc2_def = doc.createComponentDefinition("fc2_def", version, ComponentDefinition.DNA);
		FunctionalComponent fc1 = md1.createFunctionalComponent(
					"fc1", AccessType.PUBLIC, "fc1_def", version, DirectionType.NONE);
		FunctionalComponent fc2 = md1.createFunctionalComponent(
				"fc2", AccessType.PUBLIC, "fc2_def", version, DirectionType.NONE);
				
		ComponentDefinition cd = doc.createComponentDefinition("cd", version, ComponentDefinition.DNA);
		fc1_def.createComponent("component1", AccessType.PUBLIC, "cd");
		
		fc1.createMapsTo("mapsTo", RefinementType.USELOCAL, "fc2", "component1");
		
		doc.write(new File("examples/mapsto.rdf"));
		   
		RDFMerger.combine("examples/mapsto.rdf","../sbol-owl/sbol.rdf", "examples/mapsto_sbolowl_consistent.rdf");
		
		System.out.println("done!");
	}
	
	
}
