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


(defn generateValues
  [bitArray]

  (if (empty? bitArray)
    [[]]
    (let [[head & tail] bitArray
          other (generateValues tail)
          ]
      (if (= head \X)
        (into (map #(into [ \0 ] %1 ) other) (map #(into [ \1 ] %1 ) other))
        (map #(into [ head ] %1 ) other)
        )
      )

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
          (case m
            \X \X
            \1 \1
            b
            )
          )
        ))
    (def indexes (generateValues (reverse resultArray)))
    (map #(Long/parseLong (str/join %1) 2) indexes)
    )
  )


(defn writeToMemo
  [memoMap indexes value]
  (reduce (fn [map idx] (into map {idx value})) memoMap indexes)
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
        (process tail (writeToMemo memoMap (applyMask (get instr :index) mask) (get instr :value)) mask)
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





