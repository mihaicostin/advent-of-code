(ns aoc.day8
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day8.txt")


(defn process
  [instructions position processed acc]

  (if (not (= (some #{position} processed) nil))
    (println "found it: " acc)

    (let [[instr arg] (str/split (nth instructions position) #" ")]
      (case instr
        "acc" (process instructions (+ position 1) (conj processed position) (+ acc (edn/read-string arg)))
        "nop" (process instructions (+ position 1) (conj processed position) acc)
        "jmp" (process instructions (+ position (edn/read-string arg)) (conj processed position) acc)
        )
      )
    )

  )

(defn -main
  "Day 8: Handheld Halting"
  [& args]

  (def lines (u/readLines inputFile))
  (println
    (process lines 0 () 0))
  )


