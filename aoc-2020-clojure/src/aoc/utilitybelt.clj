(ns aoc.utilitybelt
  (:gen-class)
  (:require [clojure.set :as s]
            ))


(defn readFile
  "read an input file as a long string"
  [file]
  (def values (slurp file))
  (read-string (str "[" values "]"))
  )

(defn readLines
  "read a file as an array "
  [input]
  (with-open [rdr (clojure.java.io/reader input)]
    (reduce conj [] (line-seq rdr))
    )
  )

(defn intersect
  [l1 l2]
  (s/intersection (into #{} l1) (into #{} l2) )
)
