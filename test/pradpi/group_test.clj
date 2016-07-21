(ns pradpi.group-test
  (:require [clojure.test :refer :all]
            [pradpi.group :as group]))

(deftest xml-config
  (testing "default :list-paths"
    (let [config (group/xml-config {})]
      (is (= [[:items :item]] (:list-paths config)))))
  
  (testing "response group with offers"
    (let [config (group/xml-config {:ResponseGroup "Offers"})]
      (is (= [[:items :item]
              [:items :item :offers :offer]] (:list-paths config))))))
