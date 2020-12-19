(ns aoc.day17
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]
            [clojure.test :as test]))

(def inputFile "src/aoc/day17.txt")

(defn parse
  [line y z w]
  (map (fn [ch x] (case ch
                    \. {:x x :y y :z z :w w :on false}
                    \# {:x x :y y :z z :w w :on true}
                    )) (seq line) (range (count line)))
  )

(defn getAt
  [space x y z w]

  (loop [remainingSpace space]
    (if (empty? remainingSpace)
      {:x x :y y :z z :w w :on false}
      (let [[head & tail] remainingSpace]
        (if (and (= (:x head) x) (= (:y head) y) (= (:z head) z) (= (:w head) w))
          head
          (recur tail)
          )
        )
      )
    )
  )


(defn getNeighbours
  [space position]

  (let [sx (:x position)
        sy (:y position)
        sz (:z position)
        sw (:w position)
        ]
    (filter some?
            (for [w (vector (dec sw) sw (inc sw))
                  z (vector (dec sz) sz (inc sz))
                  y (vector (dec sy) sy (inc sy))
                  x (vector (dec sx) sx (inc sx))
                  ]
              (if (and (= x sx) (= y sy) (= z sz) (= w sw))
                nil
                (getAt space x y z w)
                )
              )
            )
    )
  )

(defn expand
  [space]
  (distinct (mapcat (fn [position] (if (true? (:on position))
                                     (conj (getNeighbours space position) position)
                                     (list position)
                                     )
                      ) space))
  )


(defn nextValue
  [space position]
  (let [ne (getNeighbours space position)
        activeNe (count (filter #(true? (:on %1)) ne))
        isOn (:on position)
        ]
    (if (true? isOn)
      (if (or (= activeNe 2) (= activeNe 3))
        (into position {:on true})
        (into position {:on false})
        )
      (if (= activeNe 3)
        (into position {:on true})
        (into position {:on false})
        )
      )
    )
  )


(defn nextState
  [space]
  (let [ex (expand space)]
    (map (fn [position] (nextValue space position)) ex)
    )
  )


(defn countActive
  [space]
  (count
    (filter (fn [position] (true? (:on position))) space)
    )
  )


(defn printSpace
  [space]
  (let [sorted (sort-by (juxt :z :y :x) space)]
    (doseq [position sorted]
      (if (:on position)
        (println position)
        (print "")
        ))
    )
  )


(defn -main
  "Day 17: Ticket Translation"
  [& args]

  (def lines (u/readLines inputFile))
  (def spaceSize (count lines))

  (def startingSpace (mapcat (fn [line idx] (parse line idx 0 0)) lines (range (count lines))))

  (println
    (loop [space startingSpace
           iteration 0]
      (if (= iteration 6)
        (countActive space)
        (let [nextSpace (nextState space)]
          (recur nextSpace (inc iteration))
          )
        )
      )
    )

  )




