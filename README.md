## Ontology
The SBOL-OWL ontology provides a set of controlled terms that are used to describe genetic circuit designs using SBOL. Terms are included for the followings.

- Descriptions of SBOL entities (e.g. "[ComponentDefinition](http://sbolstandard.org/v2#ComponentDefinition)") that are exchanged electronically. Constraints and validation rules that are enforced on SBOL entities are also captured as part of the ontology.
- SBOL entities (e.g. "[TopLevel]http://sbolstandard.org/v2#TopLevel)" that are not serialised but are used to group different SBOL entities. SBOL-OWL exposes these entities to semantic reasoning tools via parent-child relationships.
- Terms (e.g. "[inline](http://sbolstandard.org/v2#inline)") that are used to restrict values of SBOL entities.
- Properties (e.g. "[sequence](http://sbolstandard.org/v2#sequence)") linking SBOL entities to accepted values.
- Metadata terms (e.g. "[Promoter](http://sbolstandard.org/v2#Promoter)") for commonly used descriptions of design entities. Such terms may require the use of several SBOL entities and properties.

### Browse
[Browse the SBOL-OWL terms via an HTML page.](https://dissys.github.io/sbol-owl/sbol-owl.htm)

### Download
SBOL-OWL is available in different formats.

- [OWL file](https://dissys.github.io/sbol-owl/sbol.owl)
- [RDF file](https://dissys.github.io/sbol-owl/sbol.rdf)
- [OMN file - Manchester Syntax] (https://dissys.github.io/sbol-owl/sbol.omn)

### Semantic reasoning
SBOL-OWL can be combined with any SBOL document to query genetic circuit designs using semantic queries. The sbolowl-sem Java application has been developed to facilitate this process. The resulting files can then be submitted to existing reasoners for semantic inferencing.

#### Using the sbolowl-sem Java library
usage: sbolowl_file sboldesign_file merged_file

- sbolowl_file: The RDF version of the SBOL-OWL ontology
- sboldesign_file: An SBOL file including genetic circuit designs
- merged_file: The output file name

Example:
```
java -cp sbolowl-sem-1.0-SNAPSHOT-jar-with-dependencies.jar dissys.keele.ac.uk.RDFMerger sbol.rdf mapsto.rdf mapsto_sbolowl_consistent.rdf
```

[Click here](https://github.com/dissys/sbol-owl/tree/master/sbol-sem/examples) to access other examples.

#### Download sbol-sem
- [Click here](https://dissys.github.io/sbol-owl/sbolowl-sem-1.0-SNAPSHOT-jar-with-dependencies.jar) to download the standalone sbol-sem Java library as a single file including all the dependencies.
- [Click here](https://dissys.github.io/sbol-owl/sbolowl-sem-1.0-SNAPSHOT.jar) to download the sbol-sem Java library only. Dependencies are not included.
