(defproject pradpi "0.2.1"
  :description "A library to simplify working with the Amazon Product Advertising API"
  :url "https://github.com/brianium/pradpi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [commons-codec/commons-codec "1.10"]
                 [clj-time "0.12.0"]
                 [naive-xml-reader "0.1.0"]
                 [http-kit "2.2.0"]]
  :profiles {:dev {:resource-paths ["test-resources"]}})
