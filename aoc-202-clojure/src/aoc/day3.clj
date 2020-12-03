(ns aoc.day3
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day3.txt" )


(defn getValueAt
  [x y w values]
  (nth values (+ x (* y w)))
  )

(defn printPath
  [inputList w h ]
  (loop [x 0 y 0
         result []]
    (when (< y h)

      (if (= \# (getValueAt x y w inputList))
        (conj result 0)
        (conj result 1))

      (if (<= (+ x 3) w) (recur (+ x 3) (+ y 1) result)
                         (recur (- (+ x 3) w) (+ y 1) result))

    ))
  )


(defn path
  [inputList w h x y]

  (conj

    (if (< y (- h 1))
      (if (< (+ x 3) w) (path inputList w h (+ x 3) (+ y 1))
                         (path inputList w h (- (+ x 3) w) (+ y 1)))
      )

    (getValueAt x y w inputList)

    )
  )


(defn -main
  "Day 3: Toboggan Trajectory"
  [& args]

  (def lines (u/readLines inputFile))
  (def h (count lines))
  (def w (count (first lines)))
  (def forestList (mapcat #(seq %) lines))

  (println w h forestList)

  (def slide (path forestList w h 0 0))

  (print (count (filter #(= % \#) slide)))

  )



