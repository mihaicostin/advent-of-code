(ns aoc.day9
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day9.txt")

(defn isSum
  [target numbers]
  (some #{true} (for [a numbers
                      b numbers]
                  (= (+ a b) target)
                  ))
  )


(defn checkNumber
  [numbers idx]

  (let [target (nth numbers idx)
        prev (take 25 (drop (- idx 25) numbers))
        valid (isSum target prev)]
    (if (= valid true)
      (checkNumber numbers (+ idx 1))
      target
      )
    )

  )

(defn findList
  [target numbers idx length]

  (let [sublist (take length (drop (- idx length) numbers))
        sum (reduce + sublist)
        ]
    (if (= sum target)
      sublist
      (if (> sum target)
        '()
        (findList target numbers idx (+ 1 length))
        )
      )
    )

  )


(defn -main
  "Day 9: Encoding Error"
  [& args]

  (def lines (u/readLines inputFile))
  (def numbers (map #(edn/read-string %) lines))

  (println "all numbers" numbers)

  (def targetNumber (checkNumber numbers 25))

  (println "invalid number" targetNumber)

  (println "xmas weakness"
    (let [result
          (sort
            (first (filter #(> (count %1) 1)
                           (for [idx (range (count numbers))]
                             (findList targetNumber numbers idx 1))
                           )))]
      (+ (first result) (last result))
      ))
  )
