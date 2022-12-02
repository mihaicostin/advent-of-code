(ns aoc.day6
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day6.txt")

;part 1
;(defn collect
;  [lines current result]
;
;  (if (empty? lines)
;    (conj result current)
;      (println "line, rest: " line rest)
;      (if (str/blank? line)
;        (collect rest '() (conj result current))
;        (collect rest (distinct (concat current (seq line))) result))
;      )
;    )
;  )

;part 2
(defn collect
  [lines current result]

  (if (empty? lines)
    (conj result current)
    (let [[line & rest] lines]
      (if (str/blank? line)
        (collect rest '() (conj result current))
        (collect rest (conj current (set (seq line))) result)
        )
      )
    )
  )




(defn -main
  "Day 6: Custom Customs "
  [& args]

  (def lines (u/readLines inputFile))

  (def lists (collect lines '() []))

  (print (reduce + (map count (map #(reduce s/intersection %) lists))))

  )
