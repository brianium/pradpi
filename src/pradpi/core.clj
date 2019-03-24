(ns pradpi.core
  (:require [pradpi.url :as url]
            [pradpi.hmac :as hmac]
            [pradpi.group :as group]
            [naive-xml-reader.core :refer [read-xml]]
            [org.httpkit.client :as http]))

(def default-config
  {:host "webservices.amazon.com"
   :path "/onca/xml"
   :version "2013-08-01"})

(defn- create-params
  "Creates params for product advertising api"
  [operation config params]
  {:pre [(every? config #{:associate-tag :key-id})
         (map? params)]}
  (let [merged-config (merge default-config config)
        version (:version merged-config)]
    (-> (assoc params "Service" "AWSECommerceService")
        (assoc "Operation" operation)
        (assoc "AssociateTag" (:associate-tag config))
        (assoc "AWSAccessKeyId" (:key-id config))
        (assoc "Version" version))))

(defn- handle-response
  "Unpack a an xml response into a hash map"
  [resp params]
  (future (let [{body :body} @resp]
            (read-xml body (group/xml-config params)))))

(defn request
  "Perform a request against the Amazon Product Advertising API"
  ([operation protocol config params]
   {:pre [(not (nil? (:secret config)))]}
   (let [merged-config (merge default-config config)
         host (:host merged-config)
         path (:path merged-config)
         query (create-params operation merged-config params)
         uri (url/create (str protocol "://" host path) query)
         signed (hmac/signed uri (:secret config))
         response (http/get signed merged-config)]
     (handle-response response params)))
  ([operation config params] (request "http" operation config params)))

(defn item-lookup
  "Perform an ItemLookup operation"
  ([protocol config params] (request "ItemLookup" protocol config params))
  ([config params] (item-lookup "http" config params)))
