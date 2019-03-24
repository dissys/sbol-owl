package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;

/**
 * Provides methods to merge RDF files.
 * @author gokselmisirli
 *
 */
public class RDFMerger {

	public static void main(String[] args) throws IOException {
		if (args != null && args.length == 3) 
		{
			combine(args[0], args[1], args[2]);
		} 
		else {
			System.out.println("usage: sbolowl_file sboldesign_file merged_file");
			System.out.println("   sbolowl_file: The RDF version of the SBOL-OWL ontology");
			System.out.println("   sboldesign_file: An SBOL file including genetic circuit designs");
			System.out.println("   merged_file: The output file name");
		}
	}

	/**
	 * Merges the first two RDF files, and creates the third file as the output. 
	 * @param file1
	 * @param file2
	 * @param file3
	 * @throws IOException
	 */
	public static void combine(String file1, String file2, String file3) throws IOException {
		Model model = ModelFactory.createDefaultModel();

		InputStream is = new FileInputStream(file1);
		model.read(is, RDFS.getURI());

		Model ontModel = ModelFactory.createDefaultModel();
		InputStream ontIs = new FileInputStream(file2);
		ontModel.read(ontIs, RDFS.getURI());

		model.add(ontModel);

		save(model, file3);
	}
	
	/**
	 * Merges the RDF files given in the array and a single output file. 
	 * @param inputFiles
	 * @param outputFile
	 * @throws IOException
	 */
	public static void combine(ArrayList<String> inputFiles, String outputFile) throws IOException {
		Model model = ModelFactory.createDefaultModel();

		InputStream is = new FileInputStream(inputFiles.get(0));
		model.read(is, RDFS.getURI());

		for (int i=1;i<inputFiles.size();i++)
		{
			Model ontModel = ModelFactory.createDefaultModel();
			InputStream ontIs = new FileInputStream(inputFiles.get(i));
			ontModel.read(ontIs, RDFS.getURI());
			model.add(ontModel);
		}
		save(model, outputFile);
	}

	/**
	 * Saves an RDF model to a file
	 * @param rdfModel
	 * @param filePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void save(Model rdfModel, String filePath) throws IOException, FileNotFoundException {
		save(rdfModel, filePath, getDefaultFormat());
	}

	/**
	 * Saves an RDF model to a file using a serialisation format.
	 * @param rdfModel
	 * @param filePath
	 * @param format
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void save(Model rdfModel, String filePath, String format) throws IOException, FileNotFoundException {
		if (format == null || format.length() == 0) {
			format = getDefaultFormat();
		}
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(new File(filePath));
			rdfModel.write(stream, format);
		} finally {
			if (stream != null) {
				stream.close();
				stream = null;
			}
		}
	}

	/**
	 * Default format is RDF/XML-ABBREV
	 * @return
	 */
	public static String getDefaultFormat() {
		return "RDF/XML-ABBREV";
	}
}
