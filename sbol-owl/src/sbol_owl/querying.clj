(ns sbol-owl.querying
    (:use [tawny.owl])
    (:refer-clojure :exclude [type,merge,sequence] )
    (:use [sbol-owl.core])  
    (:require [tawny
             [polyglot]
             [reasoner :as r]
             [pattern :as p]])

  )

(import '(java.io FileInputStream File FileOutputStream))       

(import '(org.apache.jena.vocabulary RDFS))
;(import '(org.apache.jena.rdf.model Model ))
(import '(org.apache.jena.rdf.model ModelFactory))
(import '(org.semanticweb.owlapi.model OWLOntology PrefixManager SetOntologyID OWLOntologyID))



(defn saveRdfXml[model filePath]
  (.write model (new FileOutputStream (new File filePath)) "RDF/XML-ABBREV")
  ;(.write model (new FileOutputStream (new File filePath)) "RDF/XML")
  ;(.write model (new FileOutputStream (new File filePath)) "Turtle")  
  (println "Wrote the model")  
  )

(defn mergeSBOL_OWL [sbolFilePath owlFilePath mergeFilePath]
  (let [
       RDF_BASE_URI "http://www.bacillondex.org"
       stream (FileInputStream. (File. sbolFilePath))
       model (ModelFactory/createDefaultModel)
       ontModel (ModelFactory/createDefaultModel)
       ontStream (FileInputStream. (File. owlFilePath))
       
       
       ]  
    
  (println (str "Reading file from " sbolFilePath))
  (.read model stream (RDFS/getURI))
  
  (println (str "Reading file from " owlFilePath))
  (.read ontModel ontStream (RDFS/getURI))
  
  (.add model ontModel)
  
  (println (str "Saving file to " owlFilePath))
  (saveRdfXml ontModel mergeFilePath)
  
  ))

(def localOntology)

(defn loadOntology [filePath]
  (def localOntology (.loadOntologyFromOntologyDocument (owl-ontology-manager) (File. filePath))))

   

(defn printReasoning [label inferredClasses]
  (println label " classes:")
  (doseq [subclass inferredClasses]
    (println (.getIRI subclass))))


 ;(defontology queryontology
;	  :iri "http://keele.ac.uk/dissys"
;	  :prefix "dissys:"
;	  :versioninfo "1.0"
;)
 
 
  
   (defclass ComponentInstance2
 :label "ComponentInstance"
 :comment "ComponentInstance" 
 :equivalent (owl-and ComponentDefinition DNA)
 
 )
   
    (save-ontology sbol "query.rdf" :rdf)   
  
    
    

(defn example1 [] 
  ;(remove-ontology-maybe   (.getOntologyID localOntology))
  ;(remove-ontology-maybe   (.getOntologyID sbol))
  ;(println  (.getOntologyID sbol))
  (println "Removing...")
  (remove-ontology-maybe  (new OWLOntologyID (iri (str "http://sbols.org/v2"))))
  (println "done!")
 
  
 
   
 ; (defclass ex1
 ;   :equivalent  (ComponentDefinition and (component some (definition some DNA)))
 ;) 
  
 
;(defclass ComponentInstance2
; :label "ComponentInstance"
; :comment "ComponentInstance" 
; :equivalent (owl-and ComponentDefinition  DNA)
 
; )
  
    
  (mergeSBOL_OWL "../sbol-sem/output.rdf" "sbol.rdf" "example1.rdf")
  (mergeSBOL_OWL "query.rdf" "example1.rdf" "example1_merged.rdf")
  
   (println "merged!")
    (loadOntology "example1_merged.rdf") 
      (println "loaded!")
 ;    (defclass ComponentInstance2
 ;:label "ComponentInstance"
 ;:comment "ComponentInstance" 
 ;)
   


     
 ;   (owl-class localOntology "ex1"
 ;     ;:equivalent  (ComponentDefinition and (component some (definition some DNA)))
 ;     :equivalent  (ComponentDefinition and sbol DNA)      
 ;     )
   
   (r/reasoner-factory :jfact)
    (printReasoning "ComponentInstance2" (r/isubclasses (iri ( str "http://keele.ac.uk/dissys/ComponentInstance2"))))  
    

    
  
  
  )
        
        
        
 