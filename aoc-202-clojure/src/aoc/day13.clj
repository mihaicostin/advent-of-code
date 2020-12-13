(ns aoc.day13
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(def inputFile "src/aoc/day13.txt")






(defn -main
  "Day 13: Shuttle Search"
  [& args]

  (def lines (u/readLines inputFile))

  (def ts (edn/read-string (first lines)))
  (def intervals (map #(edn/read-string %1) (filter #(not (= %1 "x")) (str/split (last lines) #","))))

  (println ts "," intervals)

  (def departure
    (first
      (loop [target ts]
        (println "TS:" target)
        (let [found (filter some?
                            (for [bus intervals]
                              (if (integer? (/ target bus))
                                (list target bus)
                                nil
                                )
                              )
                            )]
          (if (not (empty? found))
            found
            (recur (inc target))
            )
          )
        )
      )
    )

  (println (* (- (first departure) ts) (last departure)))

  )
