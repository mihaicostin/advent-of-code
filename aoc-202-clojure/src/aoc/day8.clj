(ns aoc.day8
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day8.txt")


(defn process
  [instructions position processed acc flipIndex]

  (if (>= position (count instructions))
    (list '1 acc)

    (if (not (= (some #{position} processed) nil))
      (list '-1 acc)

      (let [[instr arg] (str/split (nth instructions position) #" ")]
        (if (= flipIndex position)
          (case instr
            "acc" (process instructions (+ position 1) (conj processed position) (+ acc (edn/read-string arg)) flipIndex)
            "jmp" (process instructions (+ position 1) (conj processed position) acc flipIndex)
            "nop" (process instructions (+ position (edn/read-string arg)) (conj processed position) acc flipIndex)
            )
          (case instr
            "acc" (process instructions (+ position 1) (conj processed position) (+ acc (edn/read-string arg)) flipIndex)
            "nop" (process instructions (+ position 1) (conj processed position) acc flipIndex)
            "jmp" (process instructions (+ position (edn/read-string arg)) (conj processed position) acc flipIndex)
            )
          )
        )
      )
    )

  )

(defn -main
  "Day 8: Handheld Halting"
  [& args]

  (def lines (u/readLines inputFile))

  (def cc (count lines))
  (print
    (filter
      #(let [[head tail] %1] (= head 1))
      (for [idx (range cc)]
        (if (or (str/includes? (nth lines idx) "jmp") (str/includes? (nth lines idx) "nop"))
          (process lines 0 () 0 idx)
          '()
          )
        )))

  )


