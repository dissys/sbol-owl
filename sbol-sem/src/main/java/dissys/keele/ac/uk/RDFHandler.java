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

public class RDFHandler {

	public static void combine(String file1, String file2, String file3) throws IOException
	{
	        Model model = ModelFactory.createDefaultModel() ;
	        
	        InputStream is=new FileInputStream(file1);
	        model.read(is, RDFS.getURI());
	        
	        //model.read("output.rdf") ;
	        
	        Model ontModel=ModelFactory.createDefaultModel() ;
	        InputStream ontIs=new FileInputStream(file2);
	        ontModel.read(ontIs, RDFS.getURI());
	        
	        //ontModel.read("/home/goksel/work/sbol-owl/sbol-owl/sbol.owl");
	        
	        model.add(ontModel);
	        
	        //save(model, "output_merged.rdf", "Turtle");
	        save(model, file3);    
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
