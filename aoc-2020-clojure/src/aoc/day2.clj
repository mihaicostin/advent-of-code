(ns aoc.day2
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day2.txt" )

(defn isValid
  [passLine]

  (let [[_ min max letter value] (re-matches #"(\d+)-(\d+) ([a-z]): ([a-z]+)" passLine)]

    (def pos1 (- (edn/read-string min) 1))
    (def pos2 (- (edn/read-string max) 1))
    (def ch (seq value))
    (def l (first letter))

    (or (and (= (nth (seq value) pos1) l) (not= (nth (seq value) pos2) l))
        (and (not= (nth (seq value) pos1) l) (= (nth (seq value) pos2) l))))
  )


( defn -main
  "Day 2: Password Philosophy"
  [& args]

  (print (count (filter isValid (u/readLines inputFile))))

  )



