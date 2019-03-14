package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

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

public class SynBioHub_iGEM2018DistributionExample2 {
	public static void main(String[] args) throws Exception {
		//createFiles();
		//checkForConsistencies();
		FindBacillusDesignsThatContainOnlyBSubtilisPromoters();
		
			
			
	}
	

	private static void FindBacillusDesignsThatContainOnlyBSubtilisPromoters() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException
	{
	
		File designs=new File ("examples/igem/designs_chassis_sbolowl");
		for (File file:designs.listFiles())		
		{
			if (!file.getName().startsWith("."))
			
				if (file.getName().equals("BBa_K316027.xml"))
				{
					File ontFileUpdated=new File(file.getPath() + ".updated.xml");
					FileUtils.copyFile(file, ontFileUpdated);
					OWLOntologyManager m=OWLManager.createOWLOntologyManager();
			        OWLOntology o=m.loadOntologyFromOntologyDocument(ontFileUpdated);
			        //Reasoner hermit=new Reasoner(o);
			        //System.out.println("Is the design consistent: " + hermit.isConsistent());
			        
			        OWLEntityChecker checker = new SBOLEntityChecker(m.getOWLDataFactory());
			        OWLDataFactory df = m.getOWLDataFactory();
			        
			       // ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, "BsubtilisPromoter SubClassOf: isMemberOf value chassis_prokaryote_bsubtilis");
			        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, "BsubtilisPromoter EquivalentTo: isMemberOf value chassis_prokaryote_bsubtilis");
			        parser.setOWLEntityChecker(checker);
			        OWLAxiom axiom =  parser.parseAxiom();
			        System.out.println("Axiom = " + axiom);
			        
			        OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#BsubtilisPromoter"));
			        OWLClass clsB = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#ComponentDefinition"));
			        OWLAxiom axiomSubClass = df.getOWLSubClassOfAxiom(clsA, clsB);
			        
			        //https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java
			        //shouldAddAxioms
			        AddAxiom addAxiom = new AddAxiom(o, axiom);
			        m.applyChange(addAxiom);
			        addAxiom = new AddAxiom(o, axiomSubClass);
			        m.applyChange(addAxiom);
			        m.saveOntology(o);
			        
			        m=OWLManager.createOWLOntologyManager();
			        o=m.loadOntologyFromOntologyDocument(ontFileUpdated);
			        Reasoner  hermit=new Reasoner(o);
			        System.out.println("Is the design consistent: " + hermit.isConsistent());
			        
			        //OWLClassExpression cls = parser.parseClassExpression();
			        System.out.println("Class = " + clsA);
			        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(clsA, true);
			        System.out.println("The Individuals of the class : ");
			    
			        for (OWLNamedIndividual i : instances.getFlattened()) {
			              System.out.println(i.getIRI().getFragment());             
			              }
			        
			        /*ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(df, "BsubtilisPromoter EquivalentTo: isMemberOf value chassis_prokaryote_bsubtilis");
			        parser.setOWLEntityChecker(checker);
			        OWLAxiom axiom =  parser.parseAxiom();
			        System.out.println("Axiom = " + axiom);
			        
			        OWLClass clsA = df.getOWLClass(IRI.create(SBOLEntityChecker.NS + "#BsubtilisPromoter"));
			        //https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/Examples.java
			        //shouldAddAxioms
			        AddAxiom addAxiom = new AddAxiom(o, axiom);
			        m.applyChange(addAxiom);
			       // m.saveOntology(o);
			        
			        System.out.println("Is the design consistent: " + hermit.isConsistent());
			        
			        //OWLClassExpression cls = parser.parseClassExpression();
			        System.out.println("Class = " + clsA);
			        NodeSet<OWLNamedIndividual> instances = hermit.getInstances(clsA, true);
			        System.out.println("The Individuals of the class : ");
			    
			        for (OWLNamedIndividual i : instances.getFlattened()) {
			              System.out.println(i.getIRI().getFragment());             
			              }
			              */
			        
			        System.out.println("done!");  

				}
			}
	}
	
	
	private static void checkForConsistencies() throws OWLOntologyCreationException {
		File designs=new File ("examples/igem/designs_chassis_sbolowl");
		for (File file:designs.listFiles())		
		{
			if (!file.getName().startsWith("."))
			{
				OWLOntologyManager m=OWLManager.createOWLOntologyManager();
		        OWLOntology o=m.loadOntologyFromOntologyDocument(new File("examples/igem/del/BBa_K316027.xml"));
		        Reasoner hermit=new Reasoner(o);
		        System.out.print("Checking if the design in " + file.getName() + " is consistent: ");
		        if (!hermit.isConsistent())
		        {
		        	System.out.println("------ Inconsistent");
		        }
		        else
		        {
		        	System.out.println("Consistent");
		        }
			}
		}
		//	RDFMerger.combine("examples/igem/designs/BBa_K316027.xml","../sbol-owl/sbol.rdf", "examples/igem/del_BBa_K316027_sbolowl.xml");  
      	
      		
	}

	private static void createFiles() throws MalformedURLException, IOException, SBOLValidationException, SBOLConversionException {
		String distFile = "examples/igem/iGEM_2018_Plate1.xml";
		get("https://synbiohub.org/public/igem/chassis_prokaryote_bsubtilis/1/chassis_prokaryote_bsubtilis.xml");
		get("https://synbiohub.org/public/igem/chassis_prokaryote_ecoli/1/chassis_prokaryote_ecoli.xml");
		get("https://synbiohub.org/public/iGEM_Distributions/iGEM_2018_Plate1/1/iGEM_2018_Plate1.xml");
		SBOLDocument doc = SBOLReader.read(distFile);
		for (Collection col : doc.getCollections()) {
			for (URI uri : col.getMemberURIs()) {
				if (!uri.toString().contains("iGEM_Distributions")) {
					String uriTemp = uri.toString();//.substring(0, uri.toString().lastIndexOf("/"));
					String partName = uriTemp.substring(uriTemp.lastIndexOf("/") + 1);
					String newUri = uri.toString() + "/1/" + partName + ".xml";
					get(newUri, "designs");
					System.out.println(uri);
				}
			}
		}
		System.out.println("Created the initial files!");
	
		removeComponentDefinitions("examples/igem/chassis_prokaryote_bsubtilis.xml", "examples/igem/chassis_prokaryote_bsubtilis_nocd.xml");
		removeComponentDefinitions("examples/igem/chassis_prokaryote_ecoli.xml", "examples/igem/chassis_prokaryote_ecoli_nocd.xml");
		//Check for consistencies first
		File designs=new File ("examples/igem/designs");
		for (File file:designs.listFiles())		
		{
			if (!file.getName().startsWith("."))
			{
				ArrayList<String> files=new ArrayList();
				files.add(file.getPath());
				files.add("examples/igem/chassis_prokaryote_bsubtilis_nocd.xml");
				files.add("examples/igem/chassis_prokaryote_ecoli_nocd.xml");
				files.add("../sbol-owl/sbol.rdf");
				String outputFile="examples/igem/designs_chassis_sbolowl/" + file.getName();
				if (!new File(outputFile).exists())
				{
					System.out.println("File:" + outputFile);
					RDFMerger.combine(files, outputFile);
				}
				else
				{
					System.out.println("Skipping file:" + outputFile);
					
				}
			}				
		}
		
		System.out.println("Combined the designs with the chassis information!");
		
	}

	private static void removeComponentDefinitions(String file1, String file2) throws SBOLValidationException, IOException, SBOLConversionException
	{
		if (!new File(file2).exists())
		{
		SBOLDocument doc = SBOLReader.read(file1);
		ArrayList<ComponentDefinition> compDefs=new ArrayList<ComponentDefinition>();
		for (ComponentDefinition compDef:doc.getComponentDefinitions())
		{
			compDefs.add(compDef);
		}
		for (ComponentDefinition compDef:compDefs)
		{
			doc.removeComponentDefinition(compDef);
		}
		doc.write(new File(file2));
		}
		
	}
	
	private static void get(String url, String subDir) throws MalformedURLException, IOException {
		String fileName = "examples/igem/";
		if (subDir != null) {
			fileName += subDir + "/";
		}
		fileName += url.substring(url.lastIndexOf("/") + 1);
		if (!new File(fileName).exists()) {
			InputStream in = new URL(url).openStream();
			String data = null;
			try {
				data = IOUtils.toString(in);
				data=data.replaceAll("/1\"", "\"");
			} finally {
				IOUtils.closeQuietly(in);
			}
			FileUtils.write(new File(fileName), data);
		}
	}

	private static void get(String url) throws MalformedURLException, IOException {
		get(url, null);

	}

}