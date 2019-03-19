package examples.dissys.keele.ac.uk;

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

import dissys.keele.ac.uk.RDFMerger;

/***
 * Checks if example files from the SBOL test suite are inconsistent or not using SBOL-OWL.
 * Example files were downloaded from https://github.com/SynBioDex/SBOLTestSuite/tree/master/InvalidFiles
 * @author gokselmisirli
 *
 */
public class SBOLSpecInconsistentFiles {
	private static String rulesDirectory = "../examples/validationrules";
	private static String rules_sbolowl_Directory = "../examples/validationrules_sbolowl";

	public static void main(String[] args) throws Exception {
		createFiles();
		checkForConsistencies();
		System.out.println("done!");
	}

	private static void createFiles() throws OWLOntologyCreationException, IOException {
		int invalidRDF = 0;
		int validRDF = 0;
		File designs = new File(rulesDirectory);
		File[] files = designs.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (file.getName().startsWith("sbol")) {
				File newFile = new File(rules_sbolowl_Directory + "/" + file.getName());
				String content = FileUtils.readFileToString(file);
				content = content.replaceAll("/1.0\"", "\"");
				content = content.replaceAll("/1\"", "\"");
				FileUtils.write(newFile, content);
				try {
					RDFMerger.combine(newFile.getPath(), "../sbol-owl/sbol.rdf", newFile.getPath());
					validRDF++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					invalidRDF++;
					continue;
				}
			}
		}
		System.out.println("...Finished creating files!");
		System.out.println(invalidRDF + " invalid RDF files were not processed");
		System.out.println(validRDF + " valid RDF files were processed");
	}

	private static void checkForConsistencies() throws OWLOntologyCreationException, IOException {
		int count = 0;
		int invalidatedOnlyUsinglibSBOLj = 0;
		int invalidatedUsingSBOLOWL = 0;
		int invalidatedRDF = 0;

		File designs_sbolowl = new File(rules_sbolowl_Directory);

		File[] files = designs_sbolowl.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (file.getName().startsWith("sbol")) {
				count = count + 1;

				System.out.print(file.getName() + ":");
				OWLOntology o = null;
				try {
					OWLOntologyManager m = OWLManager.createOWLOntologyManager();
					o = m.loadOntologyFromOntologyDocument(file);
				} catch (Exception e) {
					invalidatedRDF=invalidatedRDF +1;
					continue;
				}
				try {
					Reasoner hermit = new Reasoner(o);
					System.out.print("IsConsistent:");
					if (!hermit.isConsistent()) {
						System.out.println("Inconsistent");
						invalidatedUsingSBOLOWL++;
					} else {
						System.out.println("Consistent");
						try {
							SBOLReader.read(file);
						} catch (Exception e) {
							System.out.println(e.getMessage());
							invalidatedOnlyUsinglibSBOLj++;
						}
					}
				} catch (Exception e) {
					System.out.println("Error:" + e.getMessage());
				}
			}
		}
		System.out.println("SBOLOWL invalidated:" + invalidatedUsingSBOLOWL);
		System.out.println("libSBOLj invalidated:" + invalidatedOnlyUsinglibSBOLj);
	}
}
