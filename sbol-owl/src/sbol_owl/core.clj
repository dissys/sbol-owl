(ns sbol-owl.core
  (:use [tawny.owl])
  (:require [sbol-owl.dcterms])
  )

(defontology sbol
	  :iri "http://sbolstandard.org/v2"
	  :prefix "sbol:"
	  :comment "The OWL version of the SBOL data model"
	  :versioninfo "1.0"
)

 (owl-import sbol-owl.dcterms/dcterms)
 
(defclass Identified
  :label "Identified"
  :comment "Represents SBOL objects that can be identified uniquely using URIs."   
  :super
    (owl-some sbol-owl.dcterms/title :XSD_STRING)
    (owl-some sbol-owl.dcterms/description :XSD_STRING)
;:subclass (owl-some sbol-owl.dcterms/description (iri(str "http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral")))
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
 )


(defclass TopLevel
 :label "TopLevel"
 :comment "Can be used to represent biological design components such as DNA, RNA and small molecules." 
 )

(defclass ComponentInstance
 :label "ComponentInstance"
 :comment "ComponentInstance" 
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
 )

;Classes for top level SBOL entities
(defclass Sequence
  :label "Sequence"
  :comment "Sequence"     
 )

(defclass ComponentDefinition
  :label "ComponentDefinition"
  :comment "Can be used to represent biological design components such as DNA, RNA and small molecules."     
 )

(defclass Collection
  :label "Collection"
  :comment "Groups together a set of TopLevel objects that have something in common."     
 )

(defclass Model
  :label "Model"
  :comment "Serves as a placeholder for an external computational model and provide additional meta-data to enable better reasoning about the contents of this model."     
 )

(defclass ModuleDefinition
  :label "ModuleDefinition"
  :comment "Represents a grouping of structural and functional entities in a biological design."     
 )

;Classes for entities that a=used within top level SBOL entities


(defclass Component
  :label "Component"
  :comment "Component"
 )

(defclass FunctionalComponent
  :label "FunctionalComponent"
  :comment "FunctionalComponent"
 )

(defclass SequenceAnnotation
  :label "SequenceAnnotation"
  :comment "SequenceAnnotation"
 )

(defclass Location
  :label "Location"
  :comment "Extended by the Range, Cut, and GenericLocation classes"
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
  :comment "Range" 
 )

(defclass Cut
  :label "Cut"
  :comment "Cut" 
 )


(defclass GenericLocation
  :label "GenericLocation"
  :comment "GenericLocation" 
 )



(defclass Orientation
  :label "Orientation"
  :comment "Orientation"
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
  :comment "SequenceConstraint"
 )

(defclass MapsTo
 :label "MapsTo"
 :comment "MapsTo"
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
 )

(defclass Participation
 :label "Participation"
 :comment "Participation"
 )

(defclass Module
 :label "Module"
 :comment "Represents the usage or occurrence of a ModuleDefinition within a larger design."
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
 )



;Properties
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


;type is a reserved in Clojure. It can'be expressed using "defoproperty type"
(object-property sbol "type"
  :label "type"
  :comment "Points to a URI that represents the type."
  :domain (owl-or ComponentDefinition Interaction)
 )

(defdproperty displayId
  :label "displayId"
  :comment "A human-readable identifier"
  :domain Identified
  :range :XSD_STRING
 )

(defdproperty version
  :label "version"
  :comment "version"
  :domain Identified
  :range :XSD_STRING 
 )
   

(defdproperty elements
  :label "elements"
  :comment "elements"
  :domain Sequence
  :range :XSD_STRING 
 )

(defoproperty encoding
  :label "encoding"
  :comment "encoding"
  :domain Sequence
 )

(defoproperty sequence
  :label "sequence"
  :comment "sequence"
  :domain ComponentDefinition
  :range Sequence
 )

(defoproperty component
  :label "component"
  :comment "component"
  :domain (owl-or ComponentDefinition SequenceAnnotation)
  :range Component
 )

(defoproperty sequenceAnnotation
  :label "sequenceAnnotation"
  :comment "sequenceAnnotation"
  :domain ComponentDefinition
  :range SequenceAnnotation
 )

(defoproperty location
  :label "location"
  :comment "Contains a URI reference of a Location object."
  :domain SequenceAnnotation
  :range Location
 )

(defoproperty orientation
  :label "orientation"
  :comment "Indicates the type of a component's orientation."
  :domain Location
  :range Orientation
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
  :comment "sequenceConstraint"
  :domain ComponentDefinition
  :range SequenceConstraint
 )

(defoproperty access
  :label "access"
  :comment "access"
  :domain ComponentInstance
  :range Access
 )

(defoproperty mapsTo
  :label "mapsTo"
  :comment "mapsTo"
  :domain ComponentInstance
  :range MapsTo
 )


(defoproperty roleIntegration
  :label "roleIntegration"
  :comment "roleIntegration"
  :domain Component
  :range RoleIntegration
 )

(defoproperty direction
  :label "direction"
  :comment "direction"
  :domain FunctionalComponent
  :range Direction
 )

(defoproperty local
  :label "local"
  :comment "local"
  :domain MapsTo
  :range ComponentInstance
 )

(defoproperty remote
  :label "remote"
  :comment "remote"
  :domain MapsTo
  :range ComponentInstance
 )

(defoproperty refinement
  :label "refinement"
  :comment "refinement"
  :domain MapsTo
  :range Refinement
 )

(defoproperty definition
  :label "definition"
  :comment "definition"
  :domain (owl-or ComponentInstance Module)
  :range ComponentDefinition
 )


(defoproperty participation
  :label "participation"
  :comment "participation"
  :domain Interaction
  :range Participation
 )

(defoproperty participant
  :label "participant"
  :comment "participant"
  :domain Participation
  :range FunctionalComponent
 )

(defoproperty member
  :label "member"
  :comment "Contains a reference to a top level entity"
  :domain Collection
  :range TopLevel
 )

(defoproperty source
  :label "source"
  :comment "Contains a URI reference to the source file for a model."
  :domain Model
  :characteristic :functional
  )

(defoproperty language
  :label "language"
  :comment "contain a URI that specifies the language in which the model is implemented."
  :domain Model
  :characteristic :functional
 )

(defoproperty framework
  :label "framework"
  :comment "Contains a URI that specifies the framework in which the model is implemented."
  :domain Model
  :characteristic :functional
 )


(defoproperty model
  :label "model"
  :comment "Contains a URI that references a Model object."
  :domain ModuleDefinition
  :range Model
 )


(defoproperty interaction
  :label "interaction"
  :comment "Contains a URI that references an Interaction object."
  :domain ModuleDefinition
  :range Interaction
 )


(defoproperty functionalComponent
  :label "functionalComponent"
  :comment "Contains a URI that references a FunctionalComponentobject."
  :domain ModuleDefinition
  :range FunctionalComponent
 )


(defoproperty module
  :label "module"
  :comment "Contains a URI that references a Module object."
  :domain ModuleDefinition
  :range Module
 )





   

 ;TODO Add wasDerivedFrom property from PROV-O
 ;TODO Add data types for datatype properties
 
  (defn save []
   (save-ontology sbol "sbol.omn" :omn) 
   (save-ontology sbol "sbol.owl" :owl)   
)
  
  
    

     (defn testing []
   (print "test")
)
  
  ;http://homepages.cs.ncl.ac.uk/phillip.lord/download/tawny/icbo_2015/2015_lisbon.html#(180)
  ;http://homepages.cs.ncl.ac.uk/phillip.lord/take-wing/take_wing.html
  ;https://github.com/phillord/tawny-pizza/blob/master/src/pizza/pizza.clj
  