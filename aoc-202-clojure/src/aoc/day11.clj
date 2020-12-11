(ns aoc.day11
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day11.txt")


(defn getMapValueAt
  [x y w h values]

  (if (or (>= x w) (>= y h) (< x 0) (< y 0))
    nil
    (nth values (+ x (* y w)))
    )

  )


(defn nextStep
  [input w h]

  (into [] (for [y (range h)
                 x (range w)]
             (let [current (getMapValueAt x y w h input)
                   s1 (getMapValueAt (dec x) (dec y) w h input)
                   s2 (getMapValueAt x (dec y) w h input)
                   s3 (getMapValueAt (inc x) (dec y) w h input)
                   s4 (getMapValueAt (inc x) y w h input)
                   s5 (getMapValueAt (inc x) (inc y) w h input)
                   s6 (getMapValueAt x (inc y) w h input)
                   s7 (getMapValueAt (dec x) (inc y) w h input)
                   s8 (getMapValueAt (dec x) y w h input)
                   neighbours (filter some? [s1 s2 s3 s4 s5 s6 s7 s8])
                   occupied (count (filter #(= \# %) neighbours))
                   ]

               ;(println "current" current [s1 s2 s3 s4 s5 s6 s7 s8] neighbours occupied)
               (case current
                 \. \.
                 \L (if (= occupied 0) \# \L)
                 \# (if (>= occupied 4) \L \#)
                 current
                 )
               ))
        )
  )


(defn process
  [input w h]

  (def nextStage (nextStep input w h))
  (if (= input nextStage)
    input
    (process nextStage w h)
    )

  )


(defn -main
  "Day 11: Seating System"
  [& args]

  (def lines (u/readLines inputFile))
  (def h (count lines))
  (def w (count (first lines)))

  (def allChars (reduce #(into %1 (seq %2)) [] lines))

  (def equilibrium (process allChars w h))

  (println "part 1 - total occupied " (count (filter #(= \# %) equilibrium)))

  )
