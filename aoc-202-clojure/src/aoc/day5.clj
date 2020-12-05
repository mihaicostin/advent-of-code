(ns aoc.day5
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day5.txt")


(defn decode
  [value min max lk uk]

  (def middle (/ (+ min (- max 1)) 2))

  (let [[head & tail] value]
    (if (empty? tail)
      (cond
        (= head lk) min
        (= head uk) max
        )
      (if (= head lk)
        (decode tail min middle lk uk)
        (decode tail (+ middle 1) max lk uk)
        ))
    )

  )


(defn toBoardingPass
  [lineStr]

  (def row (decode (seq (subs lineStr 0 7)) 0 127 \F \B) )
  (def col (decode (seq (subs lineStr 7 10)) 0 7 \L \R) )
  (def id (+ (* row 8) col))

  {:row row, :col col, :id id, :line lineStr}

  )

(defn has
  [col elem]
  (not (= (some #{elem} col) nil))
  )


(defn -main
  "Day 5: Binary Boarding"
  [& args]

  (def lines (u/readLines inputFile))

  (def ids (distinct (sort (map #(get (toBoardingPass %1) :id) lines))))

  (println
    (filter
      #(and (not (has ids (+ % 1))) (has ids (+ % 2) ))
      ids)
    )

  )


