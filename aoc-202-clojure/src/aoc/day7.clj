(ns aoc.day7
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set :as s]
            [aoc.utilitybelt :as u]
            ))

(def inputFile "src/aoc/day7.txt")

(defn cleanBag
  [fullNameBag]
  (def trimmed (str/trim (str/replace (str/replace (str/replace fullNameBag #"(bags)" "") #"(bag)" "") #"\." "")))
  trimmed
  ;(let [[_ nr bagColor] (re-matches #"(\d+) ([a-z ]+)" trimmed)]
  ;  (if (= bagColor nil)
  ;    trimmed
  ;    bagColor)
  ;  )

  )

(defn processLine
  [bagLine]
  (let [[head & tail] (str/split bagLine #",")]
    (let [[container another] (str/split head #"contain")]
      (def contBags (map cleanBag (conj tail another)))
      (list (cleanBag container) contBags)
      )
    )
  )

;(defn collectBags
;  [bagList searchForBags result]
;  (if (empty? searchForBags)
;    result
;
;    (let [ keysList (reduce #(let [[key tail] %2]
;                             (println "key, tail" key tail)
;                             (def found (u/intersect tail searchForBags))
;                             (if (empty? found)
;                               %1
;                               (conj %1 key))
;                             ) () bagList)]
;      (println "keylist" keysList)
;    (collectBags bagList keysList (distinct (concat result keysList))))
;
;    )
;  )

(defn getBag
  [bagList key]
  (let [[found] (filter #(let [[head tail] %1] (= head key)) bagList)]
    (let [[k values] found]
      values)
    )
  )


(defn collectBags
  [bagList bag]

  (let [bags (getBag bagList bag)]
    (println bags (= '("no other") bags))
    (if (or (empty? bags) (= '("no other") bags))
      1
      (+ 1 (reduce #(let [[_ number bagColor] (re-matches #"(\d+) ([a-z ]+)" %2)]
                      (println "number, color" number bagColor)
                      (if (= number nil)
                        %1
                        (+ %1 (* (edn/read-string number) (collectBags bagList bagColor)))
                        )
                      ) 0 bags))
      )
    )
  )


(defn -main
  "Day 7: Handy Haversacks "
  [& args]

  (def lines (u/readLines inputFile))

  (def bagList (reduce #(conj %1 (processLine %2)) '() lines))

  (println (- (collectBags bagList "shiny gold") 1))

  )
