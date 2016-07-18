(ns pradpi.xml-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pradpi.xml.error :as err]))

(defn open-file
  "Create a File object from the named file"
  [file-name]
  (io/file
   (io/resource
    file-name)))

(defn read-file
  "Reads the contents of the named file into a string"
  [file-name]
  (slurp (open-file file-name)))

(deftest errors
  (testing "parsing errors from xml"
    (let [content (read-file "ItemLookupErrorResponse.xml")
          expected {:code "RequestExpired" :message "So expired!"}
          no-errors "<ItemLookupResponse><Nope></Nope></ItemLookupResponse>"]
      (is (err/has-errors content))
      (is (= expected (first (err/errors content))))
      (is (not (err/has-errors no-errors))))))
