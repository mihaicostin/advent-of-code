(ns aoc.day2
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            ))

(def inputFile "src/aoc/day2.txt" )

(defn readLines
  [input]
  (with-open [rdr (clojure.java.io/reader input)]
    (reduce conj [] (line-seq rdr))
    )
  )

(defn isValid
  [passLine]

  (let [[_ min max letter value] (re-matches #"(\d+)-(\d+) ([a-z]): ([a-z]+)" passLine)]

    (def full (count value))
    (def nonLetter (count (str/replace value letter "")))
    (def letters (- full nonLetter))

    (and (<= (edn/read-string min) letters) (>= (edn/read-string max) letters))
  )
  )


( defn -main
  "Day 2: Password Philosophy"
  [& args]

  (print (count (filter isValid (readLines inputFile))))

  )



