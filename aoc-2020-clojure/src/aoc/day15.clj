(ns aoc.day15
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))


(defn nextNumber
  [lastSpoken historyMap]

  (let [lastSpokenValue (first lastSpoken)
        lastSpokenIndex (last lastSpoken)
        lastSeenIndex (get historyMap lastSpokenValue)
        ]
    (if (some? lastSeenIndex)
      (- lastSpokenIndex lastSeenIndex)
      0
      )
    )
  )


(defn generate
  [initialHistoryMap initialLastSpoken stopAt]

  (loop [history initialHistoryMap
         lastSpoken initialLastSpoken
         ]
    (let [lastValue (first lastSpoken)
          lastIndex (last lastSpoken)
          ]

      (if (= lastIndex stopAt)
        lastSpoken
        (recur (into history {lastValue lastIndex}) [(nextNumber lastSpoken history) (inc lastIndex)] )
        )
      )
    )

  )



(defn -main
  "Day 15: Rambunctious Recitation"
  [& args]

  (def input [10 16 6 0 1 17])
  (def indexedInput (map vector (range (dec (count input))) input))


  (def historyMap (reduce (fn [m el] (into m {(last el) (first el)})) {} indexedInput))


  (def history (reverse indexedInput))

  (println (generate historyMap [(last input) (dec (count input))] (dec 30000000)))


  )




