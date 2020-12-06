(ns aoc.day6
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day6.txt")


(defn collect
  [lines current result]

  (if (empty? lines)
    (conj result current)
    (let [[line & rest] lines]
      (println "line, rest: " line rest)
      (if (str/blank? line)
        (collect rest '() (conj result current))
        (collect rest (distinct (concat current (seq line))) result))
      )
    )
  )




(defn -main
  "Day 6: Binary Boarding"
  [& args]

  (def lines (u/readLines inputFile))

  (def lists (collect lines '() '()))

  (print (reduce + (map count lists )))

  )
