(ns aoc.day10
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            [clojure.math.combinatorics :as combo]
            ))

(def inputFile "src/aoc/day10.txt")

(defn calculateDiffs
  [prev elements results]
  (if (empty? elements)
    results
    (let [[current & rest] elements]
      (calculateDiffs current rest (conj results (- current prev)))
      )
    )
  )


(defn splitIntoLists
  [prevElements prev elements results]
  (if (empty? elements)
    (conj results (conj prevElements prev))
    (let [[current & rest] elements]
      (if (= 3 (- current prev))
        (splitIntoLists [] current rest (conj results (conj prevElements prev)))
        (splitIntoLists (conj prevElements prev) current rest results)
        )
      )
    )
  )

(defn validDifs
  [l]
  (let [difs (calculateDiffs (first l) l [])]
    (empty? (filter #(> % 3) difs))
    )
  )


(defn countCombinations
  [oneList]
  (if (< (count oneList) 3)
    1
    (count
      (filter #(and
                 (<= 2 (count %))
                 (= (first oneList) (first %))
                 (= (last oneList) (last %))
                 (validDifs %)
                 )

              (combo/subsets oneList)

              )
      )
    )

  )



(defn -main
  "Day 9: Encoding Error"
  [& args]

  (def lines (u/readLines inputFile))
  (def numbers (map #(edn/read-string %) lines))
  (def sortedNr (sort numbers))

  (println "all:" sortedNr)


  (def result
    (reduce #(let [[sum1 sum3] %1]
               (if (= %2 1)
                 [(+ sum1 1) sum3]
                 [sum1 (+ 1 sum3)]
                 )
               ) [0 0] (calculateDiffs 0 sortedNr []))
    )

  ;add another 3 for the last one
  (println "part 1 result:"
           (* (nth result 0) (+ 1 (nth result 1)))
           )

  (println "lists"
           (splitIntoLists [] 0 sortedNr []))

  ; split at 3 diff, get combinations for sublists, multiply.

  (print
    (reduce * 1
            (map countCombinations (splitIntoLists [] 0 sortedNr []))
            )
    )
  )
