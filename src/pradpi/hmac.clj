(ns pradpi.hmac
  (:require [clojure.java.io :refer [as-url]]
            [clojure.string :refer [split join replace]]
            [clj-time.format :as format]
            [clj-time.core :as time])
  (:import [java.net URLEncoder]
           [javax.crypto Mac]
           [javax.crypto.spec SecretKeySpec]
           [org.apache.commons.codec.binary Base64])
  (:refer-clojure :exclude [replace]))

(def ^:const algo "HmacSHA256")

(defn- query
  "Get the query portion of the given url"
  [url]
  (->> url
       as-url
       (.getQuery)))

(defn- raw-hmac
  "Returns a byte array of the string signed with the given key"
  [to-sign key]
  (let [bytes (.getBytes key "UTF-8")
        secret (SecretKeySpec. bytes algo)
        mac (doto (Mac/getInstance algo) (.init secret))]
    (.doFinal mac to-sign)))

(defn- rfc3986
  "Apply rfc3986 encoding to the given string"
  [str]
  (-> (URLEncoder/encode str)
      (replace "+" "%20")
      (replace "*" "%2A")
      (replace "%7E" "~")))

(defn- now
  "Gets a UTC timestamp for the time right now"
  []
  (let [now (time/now)
        formatter (format/formatters :date-time-no-ms)]
    (format/unparse formatter now)))

(defn- pairs
  "Takes a vector of pair strings of the form key=value and returns a byte sorted map"
  [pairstrs]
  (->> (map #(apply hash-map (split % #"=")) pairstrs)
       (apply merge)
       (reduce-kv #(assoc %1 %2 (rfc3986 %3)) {})
       (into (sorted-map))))

(defn- canonical
  "Get the canonical string to sign for an amazon request"
  [url]
  (-> (query url)
      (split #"&")
      (pairs)
      (#(map (fn [[k v]](str k "=" v)) %))
      (#(join "&" %))))

(defn- signable
  "Returns a string that is ready for signature"
  [method url]
  (let [u (as-url url)]
    (join "\n" [method
                (.getHost u)
                (.getPath u)
                (canonical url)])))

(defn- sign
  "Signs a request with the given key"
  [method url key]
  (-> (signable method url)
      (.getBytes "UTF-8")
      (raw-hmac key)
      (#(.encode (Base64.) %))
      (String.)
      (rfc3986)))

(defn signed
  "Get a signed url for use in the ad API. Adds Timestamp, and Signature query params"
  ([method url key timestamp]
   (let [stamped (str url "&Timestamp=" timestamp)
        signature (sign method stamped key)]
    (str stamped "&Signature=" signature)))
  ([url key] (signed "GET" url key (now))))
