(ns aoc.day16
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

(def inputFile "src/aoc/day16.txt")


(defn parseRule
  [line]
  (let [[_ rule min1 max1 min2 max2] (re-matches #"([\w ]+): (\d+)-(\d+) or (\d+)-(\d+)" line)]
    ;(println "parsed rule" rule min1 max1 min2 max2)
    {:rule rule
     :min1 (edn/read-string min1)
     :max1 (edn/read-string max1)
     :min2 (edn/read-string min2)
     :max2 (edn/read-string max2)
     }
    )
  )

(defn parseTicket
  [line]
  (into [] (map #(edn/read-string %1) (str/split line #",")))
  )


;type: R, Y, T
(defn parseInput
  [lines collectingType collectedRules collectedTickets]

  (if (empty? lines)
    (list collectedRules collectedTickets)
    (let [[head & tail] lines]
      (case head
        "your ticket:" (parseInput tail "T" collectedRules collectedTickets)
        "nearby tickets:" (parseInput tail "T" collectedRules collectedTickets)
        "" (parseInput tail collectingType collectedRules collectedTickets)
        (case collectingType
          "R" (parseInput tail collectingType (conj collectedRules (parseRule head)) collectedTickets)
          "T" (parseInput tail collectingType collectedRules (conj collectedTickets (parseTicket head)))
          )
        )
      )
    )
  )


(defn isContained
  [value min1 max1 min2 max2]
  (or (and (<= min1 value) (<= value max1)) (and (<= min2 value) (<= value max2)))
  )

(defn isValidForRule
  [value rule]
  (isContained value (:min1 rule) (:max1 rule) (:min2 rule) (:max2 rule))
  )

(defn allValidForRule
  [values rule]

  (loop [valuesToCheck values]
    (if (empty? valuesToCheck)
      true
      (let [[value & tail] valuesToCheck]
        (if (not (isValidForRule value rule))
          false
          (recur tail)
          )
        )
      )
    )
  )



(defn invalidNumbers
  [ticket rules]
  (filter #(>= %1 0)
          (for [number ticket]
            (loop [rulesToCheck rules]
              (if (empty? rulesToCheck)
                number
                (let [[rule & tail] rulesToCheck]
                  (if (isValidForRule number rule)
                    -1
                    (recur tail)
                    )
                  )
                )
              )
            )
          )

  )


(defn getTicketsValueForIndex
  [tickets index]
  (into [] (for [ticket tickets]
             (nth ticket index)
             ))
  )


(defn -main
  "Day 16: Ticket Translation"
  [& args]

  (def lines (u/readLines inputFile))

  (println "1. err rate = "
           (let [[rules [myTicket & tickets]] (parseInput lines "R" [] [])]
             (reduce + (reduce (fn [a el] (into a el)) [] (for [ticket tickets] (invalidNumbers ticket rules))))
             )
           )
  (println

    (let [[rules [myTicket & tickets]] (parseInput lines "R" [] [])
          validTickets (conj (filter #(empty? (invalidNumbers %1 rules)) tickets) myTicket)
          fieldCount (count (first validTickets))
          ]
      (def validRulesMapping
        (for [index (range fieldCount)]
          (let [values (getTicketsValueForIndex validTickets index)
                validRules (filter #(allValidForRule values %1) rules)]
            [index (map :rule validRules)]
            )
          )
        )

      (def mappings
        (loop [rules validRulesMapping
               result []]
          (let [pair (first (filter #(= 1 (count (last %1))) rules))
                idx (first pair)
                rule (first (last pair))
                newRules (remove #(empty? (last %1)) (map (fn [el] (vector (first el) (remove #{rule} (last el)))) rules))
                ]
            (if (empty? newRules)
              result
              (recur newRules (into result [[rule idx]]))
              )
            )
          ))

      (def departures (filter #(str/starts-with? (first %1) "departure") mappings))

      (println departures)
      (println myTicket)

      (reduce #(* %1 (nth myTicket (last %2) )) 1 departures)
      )

    )
  )








