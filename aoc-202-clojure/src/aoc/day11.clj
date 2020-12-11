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


(defn getFirstMapValueAt
  [x y w h values xFun yFun]

  (loop [lx x
         ly y]
    (def current (getMapValueAt (xFun lx) (yFun ly) w h values))
    (case current
      \L \L
      \# \#
      nil nil
      (recur (xFun lx) (yFun ly))
      )
    )
  )

(defn stepSecondPhase
  [input w h]

  (into [] (for [y (range h)
                 x (range w)]
             (let [current (getMapValueAt x y w h input)
                   s1 (getFirstMapValueAt x y w h input dec dec)
                   s2 (getFirstMapValueAt x y w h input identity dec)
                   s3 (getFirstMapValueAt x y w h input inc dec)
                   s4 (getFirstMapValueAt x y w h input inc identity)
                   s5 (getFirstMapValueAt x y w h input inc inc)
                   s6 (getFirstMapValueAt x y w h input identity inc)
                   s7 (getFirstMapValueAt x y w h input dec inc)
                   s8 (getFirstMapValueAt x y w h input dec identity)
                   neighbours (filter some? [s1 s2 s3 s4 s5 s6 s7 s8])
                   occupied (count (filter #(= \# %) neighbours))
                   ]

               (case current
                 \. \.
                 \L (if (= occupied 0) \# \L)
                 \# (if (>= occupied 5) \L \#)
                 current
                 )
               ))
        )
  )

(defn processSecondPhase
  [input w h]

  (loop [seats input]
    (def nextStage (stepSecondPhase seats w h))

    (if (= seats nextStage)
      seats
      (recur nextStage)
      )
    )
  )





(defn -main
  "Day 11: Seating System"
  [& args]

  (def lines (u/readLines inputFile))
  (def h (count lines))
  (def w (count (first lines)))

  (def allChars (reduce #(into %1 (seq %2)) [] lines))

  (println "part 1 - total occupied " (count (filter #(= \# %) (process allChars w h))))

  (println "part 2 - total occupied " (count (filter #(= \# %) (processSecondPhase allChars w h))))




  )
