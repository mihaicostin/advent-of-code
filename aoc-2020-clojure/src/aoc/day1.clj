(ns aoc.day1
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]))

(defn -main
  "Day 1: Report Repair"
  [& args]

  (def input (u/readFile "src/aoc/day1.txt") )

  (def result (for [a input
                    b input
                    c input
                    :let [ y (* a b c)]
                    :when (== (+ a b c) 2020)]
      y))

  (println result)
  )



