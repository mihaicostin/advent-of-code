(ns aoc.day9
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day9.txt")

(defn isSum
  [target numbers]
  (some #{true} (for [a numbers
                      b numbers]
                  (= (+ (edn/read-string a) (edn/read-string b)) target)
                  ))
  )


(defn checkNumber
  [numbers idx]

  (let [target (get numbers idx)
        prev (take 25 (drop (- idx 25) numbers))
        valid (isSum (edn/read-string target) prev)]
    (if (= valid true)
      (checkNumber numbers (+ idx 1))
      target
      )
    )

  )



(defn -main
  "Day 9: Encoding Error"
  [& args]

  (def lines (u/readLines inputFile))

  (print
    (checkNumber lines 25)
    )
  )


