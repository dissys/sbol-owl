(ns sbol-owl.constants
    (:use [tawny.owl])
  )

(defn toIri[iriString]
  (print iriString)
  (iri (str iriString))
  )

(def CD_DNA (toIri "http://www.biopax.org/release/biopax-level3.owl#DnaRegion"))
(def CD_RNA (toIri "http://www.biopax.org/release/biopax-level3.owl#RnaRegion"))
(def CD_PROTEIN (toIri "http://www.biopax.org/release/biopax-level3.owl#Protein"))
(def CD_SMALLMOLECULE (toIri "http://www.biopax.org/release/biopax-level3.owl#SmallMolecule"))
(def CD_COMPLEX (toIri "http://www.biopax.org/release/biopax-level3.owl#Complex"))


(def SO_PROMOTER (toIri "http://identifiers.org/so/SO:0000167"))
(def SO_RBS (toIri "http://identifiers.org/so/SO:0000139"))
(def SO_CDS (toIri "http://identifiers.org/so/SO:0000316"))
(def SO_TERMINATOR (toIri "http://identifiers.org/so/SO:0000141"))
(def SO_GENE (toIri "http://identifiers.org/so/SO:0000704"))
(def SO_OPERATOR (toIri "http://identifiers.org/so/SO:0000057"))
(def SO_ENGINEEREDGENE (toIri "http://identifiers.org/so/SO:0000280"))
(def SO_MRNA (toIri "http://identifiers.org/so/SO:0000234"))



;(defn testabc []
;  (println "start")
;  (toIri "http://identifiers.org/so/SO:0000167")
;  )