(ns aoc.day4
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day4.txt")


(defn addEntry
  [m entry]
  (let [[k v] (str/split entry #":")]
    (assoc m k v)
    )
  )

(defn toMap
  [line]
  (def chunks (str/split line #" "))
  (reduce addEntry {} chunks)
  )


(defn collect
  [lines current result]

  (if (empty? lines)
    result
    (let [[line & rest] lines]
      (if (str/blank? line)
        (collect rest {} (conj result current))
        (collect rest (merge current (toMap line) ) result))
      )
    )
  )


;byr (Birth Year)
;iyr (Issue Year)
;eyr (Expiration Year)
;hgt (Height)
;hcl (Hair Color)
;ecl (Eye Color)
;pid (Passport ID)
;cid (Country ID)

(defn isValid
  [passportMap]
  (and
    (contains? passportMap "byr")
    (contains? passportMap "iyr")
    (contains? passportMap "eyr")
    (contains? passportMap "hgt")
    (contains? passportMap "hcl")
    (contains? passportMap "ecl")
    (contains? passportMap "pid")
    ))


(defn -main
  "Day 4: Passport Processing"
  [& args]

  (def lines (u/readLines inputFile))
  (def processedLines (collect lines {} []))

  ;(doseq [line processedLines]
  ;  (println "Line: " line))

  (print "valid:"
    (count
      (filter isValid  processedLines)
      )
  )

  )


