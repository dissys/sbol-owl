package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SequenceOntology;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        SBOLDocument doc=new SBOLDocument();
        doc.setDefaultURIprefix("http://www.keele.ac.uk/dissys");
        ComponentDefinition comp=doc.createComponentDefinition("cds1",ComponentDefinition.DNA);
        comp.addRole(SequenceOntology.CDS);
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
        save(model, "output_merged.rdf");
        
                       
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
