(ns pradpi.xml.core
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml])
  (:import  [java.io ByteArrayInputStream]))

(defn zip-str
  "Convert an xml string to a zipper"
  [s]
  (zip/xml-zip
   (xml/parse (ByteArrayInputStream. (.getBytes s)))))

(defn tag-checker
  "Returns a predicate for checking if a node represents a specific tag"
  [tag]
  (fn [node] (= tag (:tag node))))
