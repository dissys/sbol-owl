## Ontology
The SBOL-OWL ontology provides a set of controlled terms that are used to describe genetic circuit designs using SBOL. Terms are included for the followings.

- Descriptions of SBOL entities (e.g. "[ComponentDefinition](http://sbolstandard.org/v2#ComponentDefinition)") that are exchanged electronically. Constraints and validation rules that are enforced on SBOL entities are also captured as part of the ontology.
- SBOL entities (e.g. "[TopLevel](http://sbolstandard.org/v2#TopLevel)" that are not serialised but are used to group different SBOL entities. SBOL-OWL exposes these entities to semantic reasoning tools via parent-child relationships.
- Terms (e.g. "[inline](http://sbolstandard.org/v2#inline)") that are used to restrict values of SBOL entities.
- Properties (e.g. "[sequence](http://sbolstandard.org/v2#sequence)") linking SBOL entities to accepted values.
- Metadata terms (e.g. "[Promoter](http://sbolstandard.org/v2#Promoter)") for commonly used descriptions of design entities. Such terms may require the use of several SBOL entities and properties.

### Browse
[Browse the SBOL-OWL terms via an HTML page.](https://dissys.github.io/sbol-owl/sbol-owl.html)

### Download
SBOL-OWL is available in different formats.

- [OWL file](https://dissys.github.io/sbol-owl/sbol.owl)
- [RDF file](https://dissys.github.io/sbol-owl/sbol.rdf)
- [OMN file](https://dissys.github.io/sbol-owl/sbol.omn) (Manchester Syntax)

### Semantic reasoning
SBOL-OWL can be combined with any SBOL document to query genetic circuit designs using semantic queries. The semanticSBOL Java application has been developed to facilitate this process. The resulting files can then be submitted to existing reasoners for semantic inferencing.

#### Using the semanticSBOL Java library to merge SBOL and SBOL-OWL files
usage: sbolowl_file sboldesign_file merged_file

- sbolowl_file: The RDF version of the SBOL-OWL ontology
- sboldesign_file: An SBOL file including genetic circuit designs
- merged_file: The output file name

Example using the command line:
```
java -cp semanticSBOL-1.0-SNAPSHOT-jar-with-dependencies.jar dissys.keele.ac.uk.RDFMerger sbol.rdf mapsto.rdf mapsto_sbolowl_consistent.rdf
```

Example using the programmatic access:
```
RDFMerger.combine("../examples/circuit_0x78_environment_md.xml","../sbol-owl/sbol.rdf", "../examples/circuit_0x78_environment_md_sbolowl.rdf");  
```    	
    	
[Click here](https://github.com/dissys/sbol-owl/tree/master/sbol-sem/src/main/java/examples/dissys/keele/ac/uk) to access other examples.

#### Some of the important methods for programmatic access to semanticSBOL

Constructing an SemanticSBOL object:
```
SemanticSBOL semanticSBOL = new SemanticSBOL(sbolFile_with_SBOLOWL);
```		

Checking the consistency of SBOL files: 
```
semanticSBOL.isConsistent()
```

Adding a semantic query:
```
semanticSBOL.addClass("BsubtilisPromoter", "BsubtilisPromoter EquivalentTo: Promoter and isMemberOf value igem:chassis_prokaryote_bsubtilis");
```

Adding custom namespace declarations for semantic queries:
```
HashMap<String, String> namespaces = new HashMap<String, String>();
namespaces.put("igem", "https://synbiohub.org/public/igem/");
SemanticSBOL semanticSBOL = new SemanticSBOL(ontFileUpdated, namespaces);
```

Adding subclass definitions:
```
semanticSBOL.addSubClassing("BsubtilisPromoter", "ComponentDefinition");
```

Defining semantic queries with no shared SBOL entities:
```
semanticSBOL.makeDisjoint("BsubtilisPromoter", "EcoliPromoter");
```
					
Listing SBOL entities for a semantic query:
```
semanticSBOL.listSBOLEntities("BsubtilisPromoterContainer");
```		

Describing inconsistencies:
```
semanticSBOL.printInconsistencies();
```			
#### Download sbol-sem
- [Click here](https://dissys.github.io/sbol-owl/semanticSBOL-1.0-SNAPSHOT-jar-with-dependencies.jar) to download the standalone semanticSBOL Java library as a single file including all the dependencies.
- [Click here](https://dissys.github.io/sbol-owl/semanticSBOL-1.0-SNAPSHOT.jar) to download the semanticSBOL Java library only. Dependencies are not included.
