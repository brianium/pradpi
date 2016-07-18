(ns pradpi.xml.error
  (:require [pradpi.xml.core :refer :all]
            [clojure.zip :as zip]))

(def ^:private is-error (tag-checker :Error))

(defn error-nodes
  "Get errors nodes from the xml content"
  [s]
  (->> (zip-str s)
       zip/children
       (filter is-error)))

(defn has-errors
  "Check if the xml content has errors"
  [s]
  (not (empty? (error-nodes s))))

(def ^:private is-code (tag-checker :Code))
(def ^:private is-message (tag-checker :Message))

(defn code
  "Extract the error code from an error entry"
  [entry]
  (some->> entry
           :content
           (some #(when (is-code %) %))
           :content
           first))

(defn message
  "Extract the error message from an entry"
  [entry]
  (some->> entry
           :content
           (some #(when (is-message %) %))
           :content
           first))

(defn error
  "Converts an xml entry to an error map"
  [entry]
  (hash-map :code (code entry)
            :message (message entry)))

(defn errors
  "Converts an xml string into error objects"
  [s]
  (->> (error-nodes s)
      (map error)))
