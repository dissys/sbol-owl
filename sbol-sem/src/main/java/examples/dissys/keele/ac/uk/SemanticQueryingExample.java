package examples.dissys.keele.ac.uk;

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
import java.util.HashMap;

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

import dissys.keele.ac.uk.RDFMerger;
import dissys.keele.ac.uk.SemanticSBOL;
import dissys.keele.ac.uk.Util;

public class SemanticQueryingExample {
	private static String examplesFolder="../examples";
	private static String igemMainFolder = examplesFolder + "/igem";
	private static String igemDesignsFolder = igemMainFolder + "/designs";
	private static String igemOutputFolder = igemMainFolder + "/designs_chassis_sbolowl";
	private static String igemOutputFolder_withQuery = igemMainFolder + "/designs_chassis_sbolowl_withquery";

	public static void main(String[] args) throws Exception {

		Test(new File(igemOutputFolder + "/BBa_K316027.xml"));
	}

	public static void Test(File file) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {

		System.out.println("File:" + file);

		File ontFileUpdated = new File(igemOutputFolder_withQuery + "/" + file.getName());
		// if (!ontFileUpdated.exists()) {
		if (!file.exists())
		{
			System.out.println(file + " does not exist! Please execute SynBioHub_iGEM2018DistributionExample.java first to create required files");
			return;
		}
		FileUtils.copyFile(file, ontFileUpdated);

		try {
			HashMap<String, String> namespaces = new HashMap<String, String>();
			namespaces.put("igem", "https://synbiohub.org/public/igem/");
			SemanticSBOL semanticSBOL = new SemanticSBOL(ontFileUpdated, namespaces);
			
			semanticSBOL.addClass("BsubtilisPromoter",
					"BsubtilisPromoter EquivalentTo: Promoter and isMemberOf value igem:chassis_prokaryote_bsubtilis");
			// semanticSBOL.addSubClassing("BsubtilisPromoter", "ComponentDefinition");

			semanticSBOL.addClass("EcoliPromoter",
					"EcoliPromoter EquivalentTo: Promoter and isMemberOf value igem:chassis_prokaryote_ecoli");
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
			 * semanticSBOL.addProperty(URI.create(
			 * "https://synbiohub.org/public/igem/chassis_prokaryote_ecoli"),
			 * URI.create("https://synbiohub.org/public/igem/BBa_K143012"),
			 * URI.create("http://sbols.org/v2#member"));
			 */

			semanticSBOL.save();
			// semanticSBOL=new SemanticSBOL(ontFileUpdated);

			System.out.print("Is the file " + file + " consistent?... ");
			boolean isConsistent = semanticSBOL.isConsistent();
			System.out.println(isConsistent);
			if (isConsistent) {
				semanticSBOL.listSBOLEntities("BsubtilisPromoterContainer");
			}
			FileUtils.write(new File(igemOutputFolder_withQuery + "/.report.txt"),
					file.getName() + " isConsistent:" + isConsistent + "\r\n", true);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		/*
		 * } else { System.out.println("The file already exists. Delete to continue!" +
		 * file); }
		 */

		System.out.println("done!");
	}
}
