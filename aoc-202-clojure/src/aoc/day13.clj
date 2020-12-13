(ns aoc.day13
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

(def inputFile "src/aoc/day13.txt")


(defn part1
  [ts intervals]
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

  (* (- (first departure) ts) (last departure))

  )



(defn findTs
  [startTimestamp increment processing]

  (loop [timestamp startTimestamp]

    (println "TS:" timestamp)
    (let [found
          (filter some? (for [bus processing]
                          (if (= (last bus) 0)
                            0
                            (if (integer? (/ (+ timestamp (first bus)) (last bus)))
                              (list timestamp bus)
                              nil
                              )
                            )
                          )
                  )
          ]
      (if (= (count found) (count processing))
        (list timestamp (math/lcm increment (last (last processing))))
        (recur (+ timestamp increment))
        )
      )
    )

  )



(defn -main
  "Day 13: Shuttle Search"
  [& args]

  (def lines (u/readLines inputFile))

  (def ts (edn/read-string (first lines)))
  (def intervals (filter #(some? (last %1)) (map-indexed vector (map #(if (= %1 "x") nil (edn/read-string %1)) (str/split (last lines) #",")))))

  (println ts "," intervals)

  (def len (count intervals))

  (def processing (take 1 intervals))

  (println (findTs 0 1 processing))

  (println
    (loop [processingLen 1
           increment 1
           ts 0]
      (let [processing (take processingLen intervals)
            found (findTs ts increment processing)]
        (if (= processingLen len)
          found
          (recur (inc processingLen) (last found) (first found))
          )
        )
      )
    )

  )
