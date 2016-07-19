# pradpi

A Clojure library designed to simplify working with the Amazon Product Advertising API

## Installation

Add the following dependency to your `project.clj` file:

```
[pradpi "0.1.0"]
```

## Usage

```clojure
(ns my-amazon.app)

;; minimum config map needed for requests
(def config {:associate-tag "my-tag"
             :key-id "my-id"
             :secret "my-secret"})

;; ItemLookup operation 
(def result (item-lookup config {:IdType "UPC"
                                 :ItemId "862583000163"
                                 :SearchIndex "Toys"}))

;; results are futures containing response as a map
(def realized @result);
```

## Functions

All functions have the signature `[protocol config params]` or `[config params]` to default to `http`

The `config` param contains AWS credentials. Any additional properties in this map will be forwarded
to the underlying [http kit client](http://www.http-kit.org/client.html).

Currently there is one specialized function of `item-lookup`. `item-lookup` just wraps the core `request`
function. `request` has the signature `[operation protocol config params]`

## License

Copyright © 2016 Brian Scaturro

Distributed under the Eclipse Public License, the same as Clojure
