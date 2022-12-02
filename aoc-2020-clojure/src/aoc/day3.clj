(ns aoc.day3
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day3.txt")


(defn getValueAt
  [x y w values]
  (nth values (+ x (* y w)))
  )

(defn path
  [inputList w h x y sx sy]

  (conj

    (if (< y (- h sy))
      (if (< (+ x sx) w) (path inputList w h (+ x sx) (+ y sy) sx sy)
                         (path inputList w h (- (+ x sx) w) (+ y sy) sx sy))
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

  (println (* (count (filter #(= % \#) (path forestList w h 0 0 1 1)))
              (count (filter #(= % \#) (path forestList w h 0 0 3 1)))
              (count (filter #(= % \#) (path forestList w h 0 0 5 1)))
              (count (filter #(= % \#) (path forestList w h 0 0 7 1)))
              (count (filter #(= % \#) (path forestList w h 0 0 1 2)))

              )
           )
  )


