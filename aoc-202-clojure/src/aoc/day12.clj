(ns aoc.day12
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(def inputFile "src/aoc/day12.txt")


(defn decode
  [instruction]
  (let [[_ letter number] (re-matches #"([LRNSEWF])(\d+)" instruction)]
    (list letter number)
    )
  )


(defn changeDirection
  [current changeDirection amount]

  (let [angle (case current
                "E" 0
                "N" 90
                "W" 180
                "S" 270)
        sign (case changeDirection
               "L" +
               "R" -)
        newValue (sign angle amount)
        newAngle (if (>= newValue 360)
                   (- newValue 360)
                   (if (< newValue 0)
                     (+ newValue 360)
                     newValue
                     )
                   )
        ]
    (case newAngle
      0 "E"
      90 "N"
      180 "W"
      270 "S"
      )
    )
  )


(defn moveShip
  [x y direction instructions]

  (if (empty? instructions)

    (list x y direction)

    (let [[head & tail] instructions
          [letter nrValue] (decode head)
          value (edn/read-string nrValue)
          ]

      (case letter
        "N" (moveShip x (- y value) direction tail)
        "S" (moveShip x (+ y value) direction tail)
        "W" (moveShip (- x value) y direction tail)
        "E" (moveShip (+ x value) y direction tail)

        "F" (moveShip x y direction (into [(str/join [direction value])] tail))
        "L" (moveShip x y (changeDirection direction letter value) tail)
        "R" (moveShip x y (changeDirection direction letter value) tail)
        )

      )

    )

  )


(defn -main
  "Day 12: Rain Risk"
  [& args]

  (def lines (u/readLines inputFile))

  (println (moveShip 0 0 "E" lines))

  )
