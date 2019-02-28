package dissys.keele.ac.uk;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class SBOLSpecInconsistentFiles {
	public static void main(String[] args) throws Exception {

		checkForConsistencies();
		System.out.println("done!");
	}

	private static void checkForConsistencies() throws OWLOntologyCreationException, IOException {
		File designs = new File("examples/invalidfiles");
		int count = 0;
		int invalidatedOnlyUsinglibSBOLj = 0;
		int invalidatedUsingSBOLOWL = 0;
		int invalidRDF=0;
		File[] files=designs.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (file.getName().startsWith("sbol")) {
				count++;
				File newFile = new File("examples/invalidfiles_sbolowl/" + file.getName());
				String content = FileUtils.readFileToString(file);
				content = content.replaceAll("/1.0\"", "\"");
				content = content.replaceAll("/1\"", "\"");
				File updatedSBOLfile = new File("examples/invalidfiles/updated" + file.getName());
				FileUtils.write(updatedSBOLfile, content);
				try {
					OWLOntology o =null;
					try
					{
						System.out.print(file.getName() + ":");
						RDFMerger.combine(updatedSBOLfile.getPath(), "../sbol-owl/sbol.rdf", newFile.getPath());
						OWLOntologyManager m = OWLManager.createOWLOntologyManager();
						o = m.loadOntologyFromOntologyDocument(newFile);
					}
					catch (Exception e)
					{
						System.out.println(e.getMessage());
						invalidRDF++;
						continue;
					}
					Reasoner hermit = new Reasoner(o);
					System.out.print("IsConsistent:");
					if (!hermit.isConsistent()) {
						System.out.println("Inconsistent");
						invalidatedUsingSBOLOWL++;
					} else {
						System.out.println("Consistent");
						try {
							SBOLDocument doc = SBOLReader.read(file);
						} catch (Exception e) {
							System.out.println(e.getMessage());
							invalidatedOnlyUsinglibSBOLj++;
							//e.printStackTrace();
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	System.out.println ("SBOLOWL invalidated:" + invalidatedUsingSBOLOWL);
	System.out.println ("libSBOLj invalidated:" + invalidatedOnlyUsinglibSBOLj);
	
	}
}

//sbol-10101: OK - Invalid RDF

// 11406: OK - SBOL-OWL Validated since object and subject are disjoint
// 11412: * Cannot validate since the hello restriction type can be any of the other four, as long as it is not defined to be disjoint, it can't invalidate the design'
// 13013: Check this with Chris: It seems ok.
// 11604: