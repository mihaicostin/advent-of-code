(ns aoc.utilitybelt
  (:gen-class))


(defn readFile
  "read an input file as a long string"
  [file]
  (def values (slurp file))
  (read-string (str "[" values "]"))
  )