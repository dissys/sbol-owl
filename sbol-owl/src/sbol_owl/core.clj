(ns sbol-owl.core
  (:use [tawny.owl])
  (:refer-clojure :exclude [type,merge,sequence,hash,format] )
  (:use [sbol-owl.constants])
  )

(defontology sbol
	  :iri "http://sbols.org/v2"
	  :prefix "sbol:"
	  :comment "The OWL version of the SBOL data model"
	  :versioninfo "1.0"
)

(defclass Identified
  :label "Identified"
  :comment "Represents SBOL objects that can be identified uniquely using URIs."   
  :super PROVO_ENTITY
  )
   
(as-subclasses
  Identified
  (defclass TopLevel)
  (defclass Module)
  (defclass SequenceAnnotation)
  (defclass ComponentInstance)
  (defclass MapsTo)
  (defclass Location)
  (defclass SequenceConstraint)          
  (defclass Interaction)  
  (defclass Participation) 
  (defclass VariableComponent)
 )


(defclass TopLevel
 :label "TopLevel"
 :comment "Can be used to represent biological design components such as DNA, RNA and small molecules."
 )

(defoproperty access)
(defoproperty definition)
(defclass Access)
(defclass ComponentDefinition)
(defclass ComponentInstance
 :label "ComponentInstance"
 :comment "The ComponentInstance abstract class is inherited by SBOL classes that represent the usage or occurrence of a ComponentDefinition within a larger design (that is, another ComponentDefinition or ModuleDefinition)." 
 :subclass (owl-some access Access)
 :subclass (owl-some definition ComponentDefinition)
 )
 
(as-subclasses
  ComponentInstance
  :disjoint :cover
  (defclass Component)
  (defclass FunctionalComponent)
  )

(as-subclasses
  TopLevel
  :disjoint
  (defclass ComponentDefinition)
  (defclass ModuleDefinition)
  (defclass Sequence)
  (defclass Model)
  (defclass Collection)
  (defclass GenericTopLevel)
  (defclass Attachment)
  (defclass Implementation)
  (defclass CombinatorialDerivation)
  )


(as-subclasses
  Sequence
  :disjoint 
  (defclass NASequence)
  (defclass ProteinSequence)
  (defclass SMILES)
  )

 (defoproperty encoding)
 (defoproperty isSequenceOf)
 (defdproperty elements)
 (defclass DNA)
 (defclass RNA)
 (defclass Protein)
 (defclass SmallMolecule)
 
 (defclass NASequence
 :label "NASequence"
 :comment "Nucleic acid sequence" 
 :subclass (owl-some encoding SEQ_IUPAC_NA)
 ;if it is a sequence of a CD, then the CD can either be DNA or RNA
 :subclass (only isSequenceOf (owl-or DNA RNA))
 )
 
 (defclass ProteinSequence
 :label "ProteinSequence"
 :comment "Protein sequence" 
 :subclass (owl-some encoding SEQ_IUPAC_PROTEIN)
 :subclass (only isSequenceOf Protein)
 )
  
(defclass SMILES
:label "SMILES"
:comment "SMILES sequence" 
:subclass (owl-some encoding SEQ_SMILES)
:subclass (only isSequenceOf SmallMolecule)
)
 
;Classes for top level SBOL entities
(defclass Sequence
  :label "Sequence"
  :comment "Sequence"
  :subclass (owl-some elements :XSD_STRING)
  ;TODO :subclass (owl-some encoding (thing))
 )

(defclass ComponentDefinition
  :label "ComponentDefinition"
  :comment "Can be used to represent biological design components such as DNA, RNA and small molecules."  
  ;TODO  :subclass (owl-some type (thing))
 )

(defclass Collection
  :label "Collection"
  :comment "Groups together a set of TopLevel objects that have something in common."     
 )

(defclass Model
  :label "Model"
  :comment "Serves as a placeholder for an external computational model and provide additional meta-data to enable better reasoning about the contents of this model."     
  ;TODO :subclass (owl-some source URI)
  )

(defclass ModuleDefinition
  :label "ModuleDefinition"
  :comment "Represents a grouping of structural and functional entities in a biological design."     
 )

(defclass Attachment
  :label "Attachment"
  :comment "The purpose of the Attachment class is to serve as a general container for data files, especially experimental data files. It provides a means for linking files and metadata to SBOL designs."     
  ;TODO :subclass (owl-some source IRI)
 )

(defoproperty format
  :label "format"
  :comment "The format property is OPTIONAL and MAY contain a URI that specifies the format of the attached file. It is RECOMMENDED that this URI refer to a term from the EMBRACE Data and Methods (EDAM) ontology"
  :domain Attachment
  :characteristic :functional
 )

(defoproperty built
  :label "built"
  :comment "The built property is OPTIONAL and MAY contain a URI that MUST refer to a TopLevel object that is either a ComponentDefinition or ModuleDefinition. This ComponentDefinition or ModuleDefinition is intended to describe the actual physical structure and/or functional behavior of the Implementation."
  :domain Implementation
  :range  (owl-or ComponentDefinition ModuleDefinition)
  :characteristic :functional
 )

(defdproperty size
  :label "size"
  :comment "The size property is OPTIONAL and MAY contain a long indicating the file size in bytes."
  :domain Attachment
  :range :XSD_LONG
  :characteristic :functional
 )

(defdproperty hash
  :label "hash"
  :comment "The hash property is OPTIONAL and MAY contain a SHA-1 hash of the file contents represented as a hexadecimal digest."
  :domain Attachment
  :range :XSD_STRING
  :characteristic :functional
 )

(defclass Implementation
  :label "Implementation"
  :comment "An Implementation represents an instance of a synthetic biological construct, and describes the build phase of a design-built-test-learn workflow. Importantly, an Implementation can be associated with a laboratory sample that was already built, or that is to be built in the future. An Implementation can also represent virtual and simulated instances. An Implementation may be linked back to its original design (either a ModuleDefinition or ComponentDefinition) using the wasDerivedFroms property inherited from the Identified superclass. An Implementation may also link to a ModuleDefinition or ComponentDefinition that specifies its realized structure and/or function."     
 )

(defclass CombinatorialDerivationStrategy
  :label "CombinatorialDerivationStrategy"
  :comment "Specifies strategy types for combinatorial derivations."
 )

(defclass enumerate
  :label "enumerate"
  :comment "A user SHOULD derive all possible ComponentDefinition objects specified by the CombinatorialDerivation." 
 )

(defclass sample
  :label "sample"
  :comment "A user SHOULD derive a subset of all possible ComponentDefinition objects specified by CombinatorialDerivation. The manner in which this subset is chosen is for the user to decide."
 )

(as-subclasses
  CombinatorialDerivationStrategy
  :disjoint :cover
  (defclass enumerate)
  (defclass sample)
 )

(defclass VariableOperator
  :label "VariableOperator"
  :comment "Specifies the operator types for combinatorial derivations."
 )

(defclass zeroOrOne
  :label "zeroOrOne"
  :comment "No more than one Component in the derived ComponentDefinition SHOULD have a wasDerivedFroms property that refers to the template Component." 
 )

(defclass one
  :label "one"
  :comment "Exactly one Component in the derived ComponentDefinition SHOULD have a wasDerivedFroms property that refers to the template Component."
 )

(defclass zeroOrMore
  :label "zeroOrMore"
  :comment "Any number of Component objects in the derived ComponentDefinition MAY have wasDerivedFroms properties that refer to the template Component."
 )

(defclass oneOrMore
  :label "oneOrMore"
  :comment "At least one Component in the derived ComponentDefinition SHOULD have a wasDerivedFroms property that refers to the template Component."
 )

(as-subclasses
  VariableOperator
  :disjoint :cover
  (defclass zeroOrOne)
  (defclass one)
  (defclass zeroOrMore)
  (defclass oneOrMore) 
 )

(defoproperty template
  :label "template"
  :comment "The template property is REQUIRED and MUST contain a URI that refers to a ComponentDefinition. This ComponentDefinition is expected to serve as a template for the derivation of new ComponentDefinition objects."
  :domain CombinatorialDerivation
  :range ComponentDefinition
  :characteristic :functional
 )

(defoproperty strategy
  :label "strategy"
  :comment "Indicates combinatorial derivation strategy."
  :domain CombinatorialDerivation
  :range CombinatorialDerivationStrategy
  :characteristic :functional
 )

(defclass VariableComponent)

(defoproperty variable
  :label "variable"
  :comment "The variable property is REQUIRED and MUST contain a URI that refers to a template Component in the template ComponentDefinition."
  :domain VariableComponent
  :range Component
  :characteristic :functional
 )

(defoproperty variant
  :label "variant"
  :comment "The variants property is OPTIONAL and MAY contain zero or more URIs that each refer to a ComponentDefinition. This property specifies individual ComponentDefinition objects to serve as options when deriving a new Component from the template Component."
  :domain VariableComponent
  :range ComponentDefinition
 )

(defoproperty operator
  :label "operator"
  :comment "The operator property is REQUIRED and has a data type of URI. This property specifies how many Component objects SHOULD be derived from the template Component during the derivation of a new ComponentDefinition."
  :domain VariableComponent
  :range VariableOperator
  :characteristic :functional
 )

;Defined in two places in purpose, without defining the property, we can't use them! And the properties refer to this class in domain and range properties.
(defclass VariableComponent
   :label "VariableComponent"
   :comment "The VariableComponent class can be used to specify a choice of ComponentDefinition objects for any new 35 Component derived from a template Component in the template ComponentDefinition."     
   :subclass (owl-some variable Component)
   :subclass (owl-some operator VariableOperator) 
  )

(defoproperty variantCollection
  :label "variantCollection"
  :comment "This property is OPTIONAL and MAY contain zero or more URIs that each refer to a Collection. The members property of each Collection referred to in this way MUST NOT be empty. This property enables the convenient specification of existing groups of ComponentDefinition objects to serve as options when deriving a new Component from the template Component."
  :domain VariableComponent
  :range Collection
 )

(defoproperty variableComponent
  :label "variableComponent"
  :comment "The variableComponent property is OPTIONAL and MAY contain a set of VariableComponent objects."
  :domain CombinatorialDerivation
  :range VariableComponent
 )

(defoproperty variantDerivation
  :label "variantDerivation"
  :comment "The variantDerivations property is OPTIONAL and MAY contain zero or more URIs that each refer to a CombinatorialDerivation. This property enables the convenient specification of ComponentDefinition objects derived in accordance with another CombinatorialDerivation to serve as options when deriving a new Component from the template Component. "
  :domain VariableComponent
  :range CombinatorialDerivation
 )

(defclass CombinatorialDerivation
  :label "CombinatorialDerivation"
  :comment "The purpose of the CombinatorialDerivation class is to specify combinatorial genetic designs without having to specify every possible design variant. For example, a CombinatorialDerivation can be used to specify a library of reporter gene variants that include different promoters and RBSs without having to specify a ComponentDefinition for every possible combination of promoter, RBS, and CDS in the library. ComponentDefinition objects that realize a CombinatorialDerivation can be derived in accordance with the class properties template, variableComponents, and strategy."     
  :subclass (owl-some template ComponentDefinition)
 )
                        
(defclass Component
  :label "Component"
  :comment "Component"
 )

(defclass FunctionalComponent
  :label "FunctionalComponent"
  :comment "FunctionalComponent"
  :subclass (owl-some direction Direction)
 )

(defclass SequenceAnnotation
  :label "SequenceAnnotation"
  :comment "The SequenceAnnotation class describes one or more regions of interest on the Sequence objects referred to by its parent ComponentDefinition. In addition, SequenceAnnotation objects can describe the substructure of their parent ComponentDefinition through association with the Component objects contained by this ComponentDefinition."
  :subclass (owl-some location Location)
 )

(defclass Location
  :label "Location"
  :comment "Extended by the Range, Cut, and GenericLocation classes."
 )

(as-subclasses
  Location
  :disjoint :cover
  (defclass Range)
  (defclass Cut)
  (defclass GenericLocation)
 )

(defclass Range
  :label "Range"
  :comment "A Range object specifies a region via discrete, inclusive start and end positions that correspond to indices for characters in the elements String of a Sequence." 
  :subclass (owl-some start :XSD_INTEGER )
  :subclass (owl-some end :XSD_INTEGER)
  ;TODO Ask Phil:subclass (some start (>< 0 8) )
 )

(defclass Cut
  :label "Cut"
  :comment "The Cut class has been introduced to enable the specification of a region between two discrete positions. This specification is accomplished using the at property, which specifies a discrete position that that corresponds to the index of a character in the elements String of a Sequence." 
  :subclass (owl-some at :XSD_INTEGER)
 )

(defclass GenericLocation
  :label "GenericLocation"
  :comment "While the Range and Cut classes are best suited to specifying regions on Sequence objects with IUPAC encodings, the GenericLocation class is included as a starting point for specifying regions on Sequence objects with different encoding properties and potentially nonlinear structure. This class can also be used to set the orientation of a SequenceAnnotation and any associated Component when their parent ComponentDefinition is a partial design that lacks a Sequence." 
 )

(defclass Orientation
  :label "Orientation"
  :comment "Provides values for the orientation of a Sequence entity."
 )

(as-subclasses
  Orientation
  :disjoint :cover
  (defclass inline)
  (defclass reverseComplement)
 )

(defclass inline
  :label "inline"
  :comment "The region specified by this Location is on the elements of a Sequence." 
 )

(defclass reverseComplement
  :label "reverseComplement"
  :comment "The region specified by this Location is on the reverse-complement translation of the elements of a Sequence. The exact nature of this translation depends on the encoding of the Sequence" 
 )

(defclass SequenceConstraint
  :label "SequenceConstraint"
  :comment "The SequenceConstraint class can be used to assert restrictions on the relative, sequence-based positions of pairs of Component objects contained by the same parent ComponentDefinition. The primary purpose of this class is to enable the specification of partially designed ComponentDefinition objects, for which the precise positions or orientations of their contained Component objects are not yet fully determined. Each SequenceConstraint includes the restriction, subject, and object properties."
  :subclass (owl-some restriction Restriction)
  :subclass (owl-some subject Component)
  :subclass (owl-some object Component)
  )

(defoproperty subject
  :label "subject"
  :comment "The subject property is REQUIRED and MUST contain a URI that refers to a Component contained by the same parent ComponentDefinition that contains the SequenceConstraint."
  :domain SequenceConstraint 
  :range Component
  :characteristic :functional
 )

(defoproperty isSubjectOf
  :label "isSubjectOf"
  :comment "isSubjectOf"
  :domain Component 
  :range SequenceConstraint
  :inverse subject
 )

(defoproperty object
  :label "object"
  :comment "The object property is REQUIRED and MUST contain a URI that refers to a Component contained by the same parent ComponentDefinition that contains the SequenceConstraint. This Component MUST NOT be the same Component that the SequenceConstraint refers to via its subject property."
  :domain SequenceConstraint 
  :range Component
  :characteristic :functional
 )

(defoproperty isObjectOf
  :label "isObjectOf"
  :comment "isObjectOf"
  :domain  Component
  :range SequenceConstraint
  :inverse object
 )

(defclass Restriction
  :label "Restriction"
  :comment "Specifies relative positions of sub components in a design." 
  )

(defoproperty restriction
  :label "restriction"
  :comment "The restriction property is REQUIRED and has a data type of URI. This property MUST indicate the type of structural restriction on the positions, orientations, or structural identities of the subject and object Component objects in relation to each other."
  :domain SequenceConstraint 
  :range Restriction
  :characteristic :functional
 )

(as-subclasses
  Restriction
  :disjoint :cover
  (defclass precedes)
  (defclass sameOrientationAs)
  (defclass oppositeOrientationAs)
  (defclass differentFrom)
 )

(defclass precedes
  :label "precedes"
  :comment "The position of the subject Component MUST precede that of the object Component. If each one is associated with a SequenceAnnotation, then the SequenceAnnotation associated with the subject Component MUST specify a region that starts before the region specified by the SequenceAnnotation associated with the object Component." 
 )

(defclass sameOrientationAs
  :label "sameOrientationAs"
  :comment "The subject and object Component objects MUST have the same orientation. If each one is associated with a SequenceAnnotation, then the orientation URIs of the Location objects of the first SequenceAnnotation MUST be among those of the second SequenceAnnotation, and vice versa." 
 )

(defclass oppositeOrientationAs
  :label "oppositeOrientationAs"
  :comment "The subject and object Component objects MUST have opposite orientations. If each one is associated with a SequenceAnnotation, then the orientation URIs of the Location objects of one SequenceAnnotation MUST NOT be among those of the other SequenceAnnotation."
 )

(defclass differentFrom
  :label "differentFrom"
  :comment "The definition property of the subject Component MUST NOT refer to the same ComponentDefinition as that of the object Component." 
 )

(defoproperty refinement)
(defoproperty local)
(defoproperty remote)
(defclass Refinement)
(defclass public)
(defclass private)

(defclass MapsTo
 :label "MapsTo"
 :comment "MapsTo"
 :subclass (owl-some refinement Refinement)
 :subclass (owl-some local ComponentInstance)
 
 :subclass (owl-some remote ComponentInstance)
 :subclass (owl-only remote (owl-some access public))
 )

(defclass Refinement
  :label "Refinement"
  :comment "Not represented in SBOL directly. It is used in the OWL representation to enforce choosing a refinement using one of its subclasses."
 )

(as-subclasses
  Refinement
  :disjoint :cover
  (defclass useRemote)
  (defclass useLocal)
  (defclass verifyIdentical)
  (defclass merge)
 )

(defclass useLocal
  :label "useLocal"
  :comment "In the context of the ComponentDefinition or ModuleDefinition that contains the owner of the MapsTo, all references to the remote ComponentInstance MUST dereference to the local ComponentInstance instead." 
 )

(defclass useRemote
  :label "useRemote"
  :comment "Indicates that all references to the local ComponentInstance MUST dereference to the remote ComponentInstance instead." 
 )

(defclass verifyIdentical 
  :label "verifyIdentical"
  :comment "Indicates that the definition properties of the local and remote ComponentInstance objects MUST refer to the same ComponentDefinition." 
 )

(defclass merge
  :label "merge"
  :comment "In the context of the ComponentDefinition or ModuleDefinition that contains the owner of the MapsTo, all references to the local ComponentInstance or the remote ComponentInstance MUST dereference to both objects." 
 )

(defclass RoleIntegration
  :label "RoleIntegration"
  :comment "Not represented in SBOL directly. It is used in the OWL representation to enforce choosing a role integration using one of its subclasses."
 )

(as-subclasses
  RoleIntegration
  :disjoint :cover
  (defclass overrideRoles)
  (defclass mergeRoles)
 )

(defclass overrideRoles
  :label "overrideRoles"
  :comment "Indicates that in the context of a Component, ignore any roles given for the included subComponentDefinition. Instead use only the set of zero or more roles given for this Component."
 )


(defclass mergeRoles
  :label "mergeRoles"
  :comment "Indicates to use the union of the two sets: both the set of zero or more roles given for this Component as well as the set of zero or more roles given for the included subComponentDefinition."
 )

(defclass private
  :label "private"
  :comment "Indicates that a ComponentInstance MUST NOT be referred to by remote MapsTo objects."
 )

(defclass Access
  :label "Access"
  :comment "Not represented in SBOL directly. It is used in the OWL representation to enforce choosing an access type using one of its subclasses."
 )

(as-subclasses
  Access
  :disjoint :cover
  (defclass public)
  (defclass private)
 )

(defclass public
  :label "public"
  :comment "Indicates that a ComponentInstance MAY be referred to by remote MapsTo objects."
 )

(defclass private
  :label "private"
  :comment "Indicates that a ComponentInstance MUST NOT be referred to by remote MapsTo objects."
 )

(defclass Direction
  :label "Direction"
  :comment "Not represented in SBOL directly. It is used in the OWL representation to enforce choosing a direction type using one of its subclasses."
 )

(as-subclasses
  Direction
  :disjoint :cover
  (defclass in)
  (defclass out)
  (defclass inout)
  (defclass none)  
 )

(defclass in
  :label "in"
  :comment "Indicates that the FunctionalComponent is an input."
 )

(defclass out
  :label "out"
  :comment "Indicates that the FunctionalComponent is an output."
 )

(defclass inout
  :label "inout"
  :comment "Indicates that the FunctionalComponent is both an input and output."
 )

(defclass none
  :label "none"
  :comment "Indicates that the FunctionalComponent is neither an input or output."
 )

(defclass Interaction
  :label "Interaction"
  :comment "Provides more detailed description of how the FunctionalComponent objects of a ModuleDefinition are intended to work together."
  ;TODO: owl-some type URI
 )

(defoproperty participant)
(defclass Participation
 :label "Participation"
 :comment "Each Participation represents how a particular FunctionalComponent behaves in its parent Interaction."
 :subclass (owl-some participant FunctionalComponent)
 )

(defclass Module
 :label "Module"
 :comment "Represents the usage or occurrence of a ModuleDefinition within a larger design."
 :subclass (owl-some definition ModuleDefinition)
 )

(defclass SBOLVocabulary
  :label "SBOLVocabulary"
  :comment "Not represented in SBOL directly. It is used to group SBOL specific terms."
 )

(as-subclasses
  SBOLVocabulary
  :disjoint :cover
  (defclass Access)
  (defclass Orientation)
  (defclass Refinement)
  (defclass RoleIntegration)  
  (defclass Direction)    
  (defclass Restriction)    
  (defclass CombinatorialDerivationStrategy) 
  (defclass VariableOperator) 
 )


(defoproperty persistentIdentity
  :label "persistentIdentity"
  :comment "It is used to refer to a set of SBOL objects that are different versions of each other."
  :domain Identified
  :characteristic :functional
 )
 
(defoproperty role
  :label "role"
  :comment "Points to a URI that represents the role. In the context of a ComponentDefinition, aan SO term for a promoter."
  :domain (owl-or ComponentDefinition Component SequenceAnnotation Participation ModuleDefinition)
 )

(defoproperty type
  :label "type"
  :comment "Points to a URI that represents the type."
  :domain (owl-or ComponentDefinition Interaction)
 )

(defdproperty displayId
  :label "displayId"
  :comment "The displayId property is an OPTIONAL identifier with a data type of String. This property is intended to be an intermediate between name and identity that is machine-readable, but more human-readable than the full URI of an identity."
  :domain Identified
  :range :XSD_STRING
  :characteristic :functional
 )

(defdproperty version
  :label "version"
  :comment "The version property is OPTIONAL and has a data type of String. This property can be used to compare two SBOL objects with the same persistentIdentity."
  :domain Identified
  :range :XSD_STRING 
  :characteristic :functional
 )
   
(defdproperty elements
  :label "elements"
  :comment "The elements property is a REQUIRED String of characters that represents the constituents of a biological or chemical molecule. For example, these characters could represent the nucleotide bases of a molecule of DNA, the amino acid residues of a protein, or the atoms and chemical bonds of a small molecule."
  :domain Sequence
  :range :XSD_STRING 
  :characteristic :functional
 )

(defoproperty encoding
  :label "encoding"
  :comment "The encoding property is REQUIRED and has a data type of URI. This property MUST indicate how the elements property of a Sequence MUST be formed and interpreted."
  :domain Sequence
  :characteristic :functional
 )

(defoproperty sequence
  :label "sequence"
  :comment "The sequences property is OPTIONAL and MAY include a set of URIs that refer to Sequence objects. These objects define the primary structure of the ComponentDefinition"
  :domain ComponentDefinition
  :range Sequence
 )

(defoproperty isSequenceOf
  :label "isSequenceOf"
  :comment "Inverse of the sequence property"
  :domain Sequence
  :range ComponentDefinition
  :inverse sequence
  :characteristic :functional
 )

(defoproperty component
  :label "component"
  :comment "The components property is OPTIONAL and MAY specify a set of Component objects that are contained by the ComponentDefinition. The set of relations between Component and ComponentDefinition objects is strictly acyclic."
  :domain (owl-or ComponentDefinition SequenceAnnotation)
  :range Component
 )

(defoproperty isComponentOf
  :label "isComponentOf"
  :comment "isComponentOf"
  :domain Component
  :range (owl-or ComponentDefinition SequenceAnnotation)
  :inverse component
  :characteristic :functional
 )

(defoproperty sequenceAnnotation
  :label "sequenceAnnotation"
  :comment "The sequenceAnnotations property is OPTIONAL and MAY contain a set of SequenceAnnotation objects. Each SequenceAnnotation specifies and describes a potentially discontiguous region on the Sequence objects referred to by the ComponentDefinition."
  :domain ComponentDefinition
  :range SequenceAnnotation
 )

(defoproperty location
  :label "location"
  :comment "The location property is REQUIRED to specify one or more Location objects that indicate which elements of a Sequence are described by the SequenceAnnotation."
  :domain SequenceAnnotation
  :range Location
 )

(defoproperty orientation
  :label "orientation"
  :comment "Indicates the type of a component's orientation. All subclasses of Location share this property, which can be used to indicate how the region specified by the SequenceAnnotation and any associated double-stranded Component is oriented on the elements of a Sequence from their parent ComponentDefinition"
  :domain Location
  :range Orientation
  :characteristic :functional
 )

(defdproperty start
  :label "start"
  :comment "Specifies the inclusive starting position of the Range."
  :domain Range
  :range :XSD_INTEGER
  :characteristic :functional
 )

(defdproperty end
  :label "end"
  :comment "Specifies the inclusive ending position of the Range."
  :domain Range
  :range :XSD_INTEGER
  :characteristic :functional
 )

(defdproperty at
  :label "at"
  :comment "specifies a discrete position that that corresponds to the index of a character in the elements String of a Sequence."
  :domain Cut
  :range :XSD_INTEGER
  :characteristic :functional
 )

(defoproperty sequenceConstraint
  :label "sequenceConstraint"
  :comment "The sequenceConstraints property is OPTIONAL and MAY contain a set of SequenceConstraint objects. These objects describe any restrictions on the relative, sequence-based positions and/or orientations of the Component objects contained by the ComponentDefinition. For example, the ComponentDefinition of a gene might specify that the position of its promoter Component precedes that of its CDS Component. This is particularly useful when a ComponentDefinition lacks a Sequence and therefore cannot specify the precise, sequence-based positions of its Component objects using SequenceAnnotation objects."
  :domain ComponentDefinition
  :range SequenceConstraint
 )

(defoproperty access
  :label "access"
  :comment "The access property is a REQUIRED URI that indicates whether the ComponentInstance can be referred to remotely by a MapsTo on another ComponentInstance or Module contained by a different parent ComponentDefinition or ModuleDefinition (one that does not contain this ComponentInstance)"
  :domain ComponentInstance
  :range Access
  :characteristic :functional
 )

(defoproperty mapsTo
  :label "mapsTo"
  :comment "The mapsTo property is OPTIONAL and MAY contain a MapsTo object that refers to and links together a ComponentInstance object (both Component objects and FunctionalComponent objects) within a larger design."
  :domain ComponentInstance
  :range MapsTo
 )

(defoproperty roleIntegration
  :label "roleIntegration"
  :comment "A roleIntegration specifies the relationship between a Component instance’s own set of roles and the set of roles on the included sub-ComponentDefinition."
  :domain Component
  :range RoleIntegration
  :characteristic :functional
 )

(defoproperty direction
  :label "direction"
  :comment "Each FunctionalComponent MUST specify via the direction property whether it serves as an input, output, both, or neither for its parent ModuleDefinition object."
  :domain FunctionalComponent
  :range Direction
  :characteristic :functional
 )

(defoproperty local
  :label "local"
  :comment "This REQUIRED property has a data type of URI and is used to refer to the ComponentInstance contained by the “higher level” ComponentDefinition or ModuleDefinition. This local ComponentInstance MUST be contained by the ComponentDefinition or ModuleDefinition that contains the ComponentInstance or Module that owns the MapsTo."
  :domain MapsTo
  :range ComponentInstance
  :characteristic :functional
 )

(defoproperty remote
  :label "remote"
  :comment "This REQUIRED property has a data type of URI and is used to refer to the ComponentInstance contained by the “lower level” ComponentDefinition or ModuleDefinition. This remote ComponentInstance MUST be contained by the ComponentDefinition or ModuleDefinition that is the definition of the ComponentInstance or Module that owns the MapsTo. Lastly, the access property of the remote ComponentInstance MUST be set to “public."
  :domain MapsTo
  :range ComponentInstance
  :characteristic :functional
 )

(defoproperty refinement
  :label "refinement"
  :comment "The refinement property is REQUIRED and has a data type of URI. Each MapsTo object MUST specify the rela- 27 tionship between its local and remote ComponentInstance objects."
  :domain MapsTo
  :range Refinement
  :characteristic :functional
 )

(defoproperty definition
  :label "definition"
  :comment "The definition property is a REQUIRED URI that refers to the ComponentDefinition of the ComponentInstance. As described in the previous section, this ComponentDefinition effectively provides information about the types and roles of the ComponentInstance."
  :domain (owl-or ComponentInstance Module)
  :range (owl-or ComponentDefinition ModuleDefinition)
  :characteristic :functional
 )

(defoproperty isDefinitionOf
  :label "isDefinitionOf"
  :comment "Inverse of the definition property"
  :inverse definition
 )

(defoproperty participation
  :label "participation"
  :comment "The participation property is an OPTIONAL and MAY contain a Participation object, which identifies the roles that its referenced FunctionalComponent plays in the Interaction"
  :domain Interaction
  :range Participation
 )

(defoproperty participant
  :label "participant"
  :comment "The participant property MUST specify precisely one FunctionalComponent object that plays the designated role in its parent Interaction object."
  :domain Participation
  :range FunctionalComponent
  :characteristic :functional
 )

(defoproperty member
  :label "member"
  :comment "Contains a reference to a top level entity"
  :domain Collection
  :range TopLevel
 )

(defoproperty source
  :label "source"
  :comment "The source property is REQUIRED and MUST contain a URI reference to the source file for a model."
  :domain (owl-or Model Attachment)
  :characteristic :functional
  )

(defoproperty language
  :label "language"
  :comment "The language property is REQUIRED and MUST contain a URI that specifies the language in which the model is implemented. It is RECOMMENDED that this URI refer to a term from the EMBRACE Data and Methods (EDAM) ontology."
  :domain Model
  :characteristic :functional
 )

(defoproperty framework
  :label "framework"
  :comment "The framework property is REQUIRED and MUST contain a URI that specifies the framework in which the model is implemented. It is RECOMMENDED this URI refer to a term from the modeling framework branch of the SBO when possible."
  :domain Model
  :characteristic :functional
 )

(defoproperty model
  :label "model"
  :comment "The model property is OPTIONAL and MAY be used to specify a set of URI references to Model objects."
  :domain ModuleDefinition
  :range Model
 )

(defoproperty interaction
  :label "interaction"
  :comment "The interaction property is OPTIONAL and MAY be used specify a set of Interaction objects within the ModuleDefinition."
  :domain ModuleDefinition
  :range Interaction
 )

(defoproperty functionalComponent
  :label "functionalComponent"
  :comment "The property is OPTIONAL and MAY be used specify a set of FunctionalComponent objects contained by the ModuleDefinition."
  :domain ModuleDefinition
  :range FunctionalComponent
 )

(defoproperty module
  :label "module"
  :comment "The property is OPTIONAL and MAY be used specify a set of Module objects contained by the ModuleDefinition. Note that the set of relations between Module and ModuleDefinition objects is strictly acyclic."
  :domain ModuleDefinition
  :range Module
 )

(as-subclasses
  ComponentDefinition
  :disjoint :cover
  (defclass DNA)
  (defclass Protein)
  (defclass SmallMolecule)
  (defclass RNA)
  (defclass Complex)
 )

 (defclass DNA
 :label "DNA"
 :comment "DNA component definition" 
 :subclass (owl-some type CD_DNA)
 )
 
 (defclass Protein
 :label "Protein"
 :comment "Protein component definition" 
 :subclass (owl-some type CD_PROTEIN)
 )
  
 (defclass SmallMolecule
 :label "SmallMolecule"
 :comment "SmallMolecule component definition" 
 :subclass (owl-some type CD_SMALLMOLECULE)
 )
 
 (defclass RNA
 :label "RNA"
 :comment "RNA component definition" 
 :subclass (owl-some type CD_RNA)
 )
  
 (defclass Complex
 :label "Complex"
 :comment "Complex component definition" 
 :subclass (owl-some type CD_COMPLEX)
 )
  
  ;Metadata classes
  (as-subclasses
  DNA
  :disjoint 
  (defclass Promoter)
  (defclass Operator)
  (defclass CDS)
  (defclass RBS)
  (defclass Terminator)
  (defclass Gene)
  (defclass EngineeredGene)
 )
  
 (defclass Promoter
 :label "Promoter"
 :comment "Promoter DNA component"
 :equivalent (owl-some role SO_PROMOTER))

  (defclass RBS
 :label "RBS"
 :comment "RBS DNA component"
 :equivalent (owl-some role SO_RBS))
  
  (defclass Operator
 :label "Operator"
 :comment "Operator DNA component"
 :equivalent (owl-some role SO_OPERATOR)
 )
  
 (defclass CDS
 :label "CDS"
 :comment "CDS DNA component"
 :equivalent (owl-some role SO_CDS)
 )
 
 (defclass Terminator
 :label "Terminator"
 :comment "Terminator DNA component"
 :equivalent (owl-some role SO_TERMINATOR)
 )            
       
 (defclass Gene
 :label "Gene"
 :comment "Gene DNA component"
 :equivalent (owl-some role SO_GENE)
 )    
       
 (defclass EngineeredGene
 :label "EngineeredGene"
 :comment "EngineeredGene DNA component"
 :equivalent (owl-some role SO_ENGINEEREDGENE)
 )
         
 (defclass mRNA
 :label "mRNA"
 :comment "mRNA RNA component"
 :equivalent (owl-some role SO_MRNA)
 :subclass RNA
 )
     
 (defclass Effector
 :label "Effector"
 :comment "Effector small molecule"
 :equivalent (owl-some role CHEBI_EFFECTOR)
 :subclass SmallMolecule
 )
      

(defn addExternalTerm[externalIRI]
  (owl-class externalIRI)
  (individual externalIRI :type  (owl-class externalIRI))
 )

;DNA Component Definition roles
(addExternalTerm SO_PROMOTER)
(addExternalTerm SO_RBS)
(addExternalTerm SO_CDS)
(addExternalTerm SO_TERMINATOR)
(addExternalTerm SO_GENE)
(addExternalTerm SO_OPERATOR)
(addExternalTerm SO_ENGINEEREDGENE)

;RNA Component Definition roles
(addExternalTerm SO_MRNA)

;Component Definition types
(addExternalTerm CD_DNA)
(addExternalTerm CD_RNA)
(addExternalTerm CD_PROTEIN)
(addExternalTerm CD_SMALLMOLECULE)
(addExternalTerm CD_COMPLEX)

;To use the some relationship for individuals
(individual (toIri "http://sbols.org/v2#private")  :type (owl-class private))
(individual (toIri "http://sbols.org/v2#public") :type (owl-class public))
(individual (toIri "http://sbols.org/v2#useLocal") :type (owl-class useLocal))
(individual (toIri "http://sbols.org/v2#useRemote") :type (owl-class useRemote))
(individual (toIri "http://sbols.org/v2#verifyIdentical") :type (owl-class verifyIdentical))
(individual (toIri "http://sbols.org/v2#merge") :type (owl-class merge))
(individual (toIri "http://sbols.org/v2#inline") :type (owl-class inline))
(individual (toIri "http://sbols.org/v2#reverseComplement") :type (owl-class reverseComplement))
(individual (toIri "http://sbols.org/v2#in") :type (owl-class in))
(individual (toIri "http://sbols.org/v2#out") :type (owl-class out))
(individual (toIri "http://sbols.org/v2#inout") :type (owl-class inout))
(individual (toIri "http://sbols.org/v2#none") :type (owl-class none))
(individual (toIri "http://sbols.org/v2#enumerate") :type (owl-class enumerate))
(individual (toIri "http://sbols.org/v2#sample") :type (owl-class sample))
(individual (toIri "http://sbols.org/v2#zeroOrOne") :type (owl-class zeroOrOne))
(individual (toIri "http://sbols.org/v2#one") :type (owl-class one))
(individual (toIri "http://sbols.org/v2#zeroOrMore") :type (owl-class zeroOrOne))
(individual (toIri "http://sbols.org/v2#oneOrMore") :type (owl-class oneOrMore))

(defn save []
 (save-ontology sbol "sbol.omn" :omn) 
 (save-ontology sbol "sbol.owl" :owl)   
 (save-ontology sbol "sbol.rdf" :rdf)
 )
