(ns aoc.day14
  (:gen-class)
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]))

(def inputFile "src/aoc/day14.txt")


(defn parseLine
  [line]

  (def splits (str/split line #" = "))

  (if (str/starts-with? (first splits) "mask")
    {:mask (last splits)}

    (let [sp1 (first splits)
          [_ idx] (re-matches #"mem\[(\d+)\]" sp1)
          index (edn/read-string idx)
          value (edn/read-string (last splits))
          ]
      {:index index :value value}
      )

    )
  )


(defn getValueAt
  [array index def]
  (if (< index (count array))
    (nth array index)
    def
    )
  )


(defn applyMask
  [value mask]

  (let [binaryArray (into [] (str/reverse (Long/toBinaryString value)))
        maskArray (into [] (str/reverse mask))
        maskLen (count maskArray)
        valueLen (count binaryArray)
        size (max maskLen valueLen)
        ]
    (def resultArray
      (for [idx (range size)]
        (let [b (getValueAt binaryArray idx 0)
              m (getValueAt maskArray idx "X")]
              (if (= m \X)
                b
                m)
          )
        ))
    (Long/parseLong (str/join (reverse resultArray)) 2)
    )
  )


(defn process
  [lines memoMap mask]

  (if (empty? lines)
    memoMap
    (let [[head & tail] lines
          instr (parseLine head)
          ]
      (if (contains? instr :mask)
        (process tail memoMap (get instr :mask))
        (process tail (into memoMap {(get instr :index) (applyMask (get instr :value) mask)}) mask)
        )
      )
    )
  )

(defn -main
  "Day 14: Docking Data"
  [& args]

  (def lines (u/readLines inputFile))

  (def memo (process lines {} ""))
  (println
    (reduce (fn [s [k v]] (+ s v)) 0 memo)
    )
  )





