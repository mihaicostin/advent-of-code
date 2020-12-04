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
        (collect rest (merge current (toMap line)) result))
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

    ;byr (Birth Year) - four digits; at least 1920 and at most 2002.
    (contains? passportMap "byr")
    (let [y (edn/read-string (get passportMap "byr"))]
      (and (not (nil? y)) (>= y 1920) (<= y 2002)))

    ;iyr (Issue Year) - four digits; at least 2010 and at most 2020.
    (contains? passportMap "iyr")
    (let [y (edn/read-string (get passportMap "iyr"))]
      (and (not (nil? y)) (>= y 2010) (<= y 2020)))

    ;eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
    (contains? passportMap "eyr")
    (let [y (edn/read-string (get passportMap "eyr"))]
      (and (not (nil? y)) (>= y 2020) (<= y 2030)))

    ;hgt (Height) - a number followed by either cm or in:
    ;If cm, the number must be at least 150 and at most 193.
    ;If in, the number must be at least 59 and at most 76.
    (contains? passportMap "hgt")
    (let [[_ val units] (re-matches #"([0-9]+)(cm|in)" (get passportMap "hgt"))]
      (cond
        (= units "cm") (let [y (edn/read-string val)] (and (not (nil? y)) (>= y 150) (<= y 193)))
        (= units "in") (let [y (edn/read-string val)] (and (not (nil? y)) (>= y 59) (<= y 76)))
        :else false
        )
      )

    ;hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
    (contains? passportMap "hcl")
    (let [[_ val] (re-matches #"#([0-9a-f]{6})" (get passportMap "hcl"))] (not (nil? val)))

    ;ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
    (contains? passportMap "ecl")
    (let [[_ val] (re-matches #"(amb|blu|brn|gry|grn|hzl|oth)" (get passportMap "ecl"))] (not (nil? val)))

    ;pid (Passport ID) - a nine-digit number, including leading zeroes.
    (contains? passportMap "pid")
    (let [[_ val] (re-matches #"([0-9]{9})" (get passportMap "pid"))] (not (nil? val)))
    ))


(defn -main
  "Day 4: Passport Processing"
  [& args]

  (def lines (u/readLines inputFile))
  (def processedLines (collect lines {} []))

  (print "valid:"
         (count
           (filter isValid processedLines)
           )
         )

  )


