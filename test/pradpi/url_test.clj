(ns pradpi.url-test
  (:require [clojure.test :refer :all]
            [pradpi.url :as url]))

(deftest query-test
  (testing "parsing a url with a query string"
   (let [uri "http://url.com?a=1&b=2"
         query (url/query uri)]
     (is (= "a=1&b=2" query))))
  (testing "parsing a url without a query string"
    (let [uri "http://url.com"
          query (url/query uri)]
      (is (empty? query)))))

(deftest rfc3986-test
  (testing "url encoding"
    (let [s "a=1,2,3"
          encoded (url/rfc3986 s)]
      (is (= "a%3D1%2C2%2C3", encoded))))
  (testing "percent encoding special characters"
    (let [s "a=1 2*3~"
          encoded (url/rfc3986 s)]
      (is (= "a%3D1%202%2A3~" encoded)))))

(deftest pairs-test
  (testing "creating a sorted map of key value pairs"
    (let [pairs ["lol=rofl" "name=brian" "Lol=haha"]
          sorted (url/pairs pairs)]
      (is (= {"Lol" "haha", "lol" "rofl", "name" "brian"} sorted)))))
