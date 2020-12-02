(ns aoc.utilitybelt
  (:gen-class))


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
