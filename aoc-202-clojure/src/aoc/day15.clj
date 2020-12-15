(ns aoc.day15
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

(defn findIndex
  [history value]

  (loop [values history]
    (if (empty? values)
      nil
      (let [[head & tail] values]
        (if (= value (last head))
          (first head)
          (recur tail)
          )
        )
      )

    )
  )


(defn nextNumber
  [history]

  (let [[lastSpoken & tail] history
        lastSeenIndex (findIndex tail (last lastSpoken))
        ]
    (if (some? lastSeenIndex)
      (- (first lastSpoken) lastSeenIndex)
      0
      )
    )
  )


(defn generate
  [history stopAt]

  (let [lastSpoken (first history)
        lastIndex (first lastSpoken)
        ]

    (if (= lastIndex stopAt)
      lastSpoken
      (generate (conj history [(inc lastIndex) (nextNumber history)]) stopAt)
      )

    )

  )



(defn -main
  "Day 15: Rambunctious Recitation"
  [& args]

  (def input [10 16 6 0 1 17])
  (def indexedInput (map vector (range (count input)) input))
  (def history (reverse indexedInput))

  (println
    (generate history (dec 2020))
    )

  )




