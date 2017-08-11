(ns sbol-owl.dcterms
  (:use [tawny.owl])
  )


(defontology dcterms
	:iri "http://dublincore.org/documents/dcmi-terms/"
	:prefix "dcterms:"
	:comment "Imported terms from DCMI Metata Terms"
	:versioninfo "2012-06-04"
 )
 
(defdproperty title
  :label "title"
  "comment" "A name given to the resource"
  :range :XSD_STRING
 )
 
 
(defdproperty description
  :label "description"
  :comment "A description given to the resource"
  :range :XSD_STRING
 )
 

