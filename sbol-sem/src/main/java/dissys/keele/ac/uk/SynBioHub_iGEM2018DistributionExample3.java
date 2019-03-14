package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.sbolstandard.core2.AccessType;
import org.sbolstandard.core2.Collection;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.RestrictionType;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.SequenceConstraint;
import org.sbolstandard.core2.SequenceOntology;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class SynBioHub_iGEM2018DistributionExample3 {
	private static String igemMainFolder = Util.examplesFolder + "/igem";
	private static String igemDesignsFolder = igemMainFolder + "/designs";
	private static String igemOutputFolder = igemMainFolder + "/designs_chassis_sbolowl";
	private static String igemOutputFolder_withQuery = igemMainFolder + "/designs_chassis_sbolowl_withquery";

	public static void main(String[] args) throws Exception {

		// createFiles();
		// checkForConsistencies();
		FindBacillusDesignsThatContainOnlyBSubtilisPromoters();
	}

	private static void FindBacillusDesignsThatContainOnlyBSubtilisPromoters()
			throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {

		File designs = new File(igemOutputFolder);
		File[] files = designs.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (!file.getName().startsWith(".")) {
				//if (file.getName().equals("BBa_K316027.xml")) {
					System.out.println("File:" + file.getName());

					File ontFileUpdated = new File(igemOutputFolder_withQuery + "/" + file.getName());
					if (!ontFileUpdated.exists()) {
						FileUtils.copyFile(file, ontFileUpdated);
						
						try {
							SemanticSBOL semanticSBOL = new SemanticSBOL(ontFileUpdated);
							// SemanticSBOL semanticSBOL=new SemanticSBOL(file);
							semanticSBOL.addClass("BsubtilisPromoter",
									"BsubtilisPromoter EquivalentTo: Promoter and isMemberOf value chassis_prokaryote_bsubtilis");
							// semanticSBOL.addSubClassing("BsubtilisPromoter", "ComponentDefinition");

							semanticSBOL.addClass("EcoliPromoter",
									"EcoliPromoter EquivalentTo: Promoter and isMemberOf value chassis_prokaryote_ecoli");
							// semanticSBOL.addSubClassing("EcoliPromoter", "ComponentDefinition");

							// semanticSBOL.addClass("BsubtilisPromoterContainer",
							// "BsubtilisPromoterContainer EquivalentTo: ComponentDefinition and (component
							// some (definition some (BsubtilisPromoter or BsubtilisPromoterContainer)))");
							semanticSBOL.addClass("BsubtilisPromoterContainer",
									"BsubtilisPromoterContainer EquivalentTo: ComponentDefinition and component some (definition some (BsubtilisPromoter or BsubtilisPromoterContainer))");

							semanticSBOL.MakeDisjoint("BsubtilisPromoter", "EcoliPromoter");

							// This line makes the ontology inconsistent. Since the a promoter is both for
							// ecoli and bsubtilis.
							/*
							 semanticSBOL.addProperty(URI.create(
							  "https://synbiohub.org/public/igem/chassis_prokaryote_ecoli"),
							  URI.create("https://synbiohub.org/public/igem/BBa_K143012"),
							  URI.create("http://sbols.org/v2#member"));
							 */

							semanticSBOL.save();
							// semanticSBOL=new SemanticSBOL(ontFileUpdated);

							System.out.print("Is the file " + file + " consistent?... ");
							boolean isConsistent = semanticSBOL.isConsistent();
							System.out.println(isConsistent);
							FileUtils.write(new File(igemOutputFolder_withQuery + "/.report.txt"),file.getName() + " isConsistent:" + isConsistent + "\r\n", true);
						} catch (Exception e) {
							System.out.println("Error: " + e.getMessage());
						}
					}
					else
					{
						System.out.println("Skipped" + file);
					}
					// semanticSBOL.listSBOLEntities("BsubtilisPromoterContainer");
					/*
					 * if (!isConsistent) { FileUtils.copyFile(ontFileUpdated, new
					 * File(ontFileUpdated.getPath() + ".inconsistent.xml")); }
					 */
				/*	break;
				}*/
			}
			
		}
		System.out.println("done!");
	}

	private static void checkForConsistencies() throws OWLOntologyCreationException {
		File designs = new File(igemOutputFolder);
		File[] files = designs.listFiles();
		Arrays.sort(files);

		for (File file : files) {
			if (!file.getName().startsWith(".")) {
				OWLOntologyManager m = OWLManager.createOWLOntologyManager();
				OWLOntology o = m.loadOntologyFromOntologyDocument(file);
				Reasoner hermit = new Reasoner(o);
				System.out.print("Checking if the design in " + file.getName() + " is consistent: ");
				if (!hermit.isConsistent()) {
					System.out.println("------ Inconsistent");
				} else

				{
					System.out.println("Consistent");
				}
			}
		}
	}

	private static void createFiles()
			throws MalformedURLException, IOException, SBOLValidationException, SBOLConversionException {
		Util.createFolder(Util.examplesFolder);
		Util.createFolder(igemMainFolder);
		Util.createFolder(igemOutputFolder);
		Util.createFolder(igemDesignsFolder);
		Util.createFolder(igemOutputFolder_withQuery);

		String bSubtilisPartsFile = Util.downloadFile(
				"https://synbiohub.org/public/igem/chassis_prokaryote_bsubtilis/1/chassis_prokaryote_bsubtilis.xml",
				igemMainFolder);
		String eColiPartsFile = Util.downloadFile(
				"https://synbiohub.org/public/igem/chassis_prokaryote_ecoli/1/chassis_prokaryote_ecoli.xml",
				igemMainFolder);
		String distributionFile = Util.downloadFile(
				"https://synbiohub.org/public/iGEM_Distributions/iGEM_2018_Plate1/1/iGEM_2018_Plate1.xml",
				igemMainFolder);
		SBOLDocument doc = SBOLReader.read(distributionFile);
		int numberOfDesigns = 0;
		for (Collection col : doc.getCollections()) {
			for (URI uri : col.getMemberURIs()) {
				if (!uri.toString().contains("iGEM_Distributions")) {
					String uriTemp = uri.toString();
					String partName = uriTemp.substring(uriTemp.lastIndexOf("/") + 1);
					String newUri = uri.toString() + "/1/" + partName + ".xml";
					Util.downloadFile(newUri, igemDesignsFolder);
					numberOfDesigns++;
					System.out.println("Processed " + uri);
				}
			}
		}
		System.out.println("Created the initial files! Processed " + numberOfDesigns + " parts.");
		String bSubtilisPartsNoCDFile = igemMainFolder + "/chassis_prokaryote_bsubtilis_nocd.xml";
		String eColiPartsNoCDFile = igemMainFolder + "/chassis_prokaryote_ecoli_nocd.xml";

		// Remove Component Definitions and only keep the Collection entities which
		// includes memberships of parts.
		Util.removeComponentDefinitions(bSubtilisPartsFile, bSubtilisPartsNoCDFile);
		Util.removeComponentDefinitions(eColiPartsFile, eColiPartsNoCDFile);

		// For each SBOL file, merge it with the list of ecoli parts, bsubtilis parts
		// and SBOL-OWL
		File designs = new File(igemDesignsFolder);
		for (File file : designs.listFiles()) {
			if (!file.getName().startsWith(".")) {
				ArrayList<String> files = new ArrayList();
				files.add(file.getPath());
				files.add(bSubtilisPartsNoCDFile);
				files.add(eColiPartsNoCDFile);
				files.add("../sbol-owl/sbol.rdf");
				String outputFile = igemOutputFolder + "/" + file.getName();
				if (!new File(outputFile).exists()) {
					System.out.println("Created:" + outputFile);
					RDFMerger.combine(files, outputFile);
				} else {
					System.out.println("File exists:" + outputFile);

				}
			}
		}

		System.out.println("Combined designs with SBOL-OWL and chassis information!");

	}

}
