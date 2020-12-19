(ns aoc.day18
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            ))

(def inputFile "src/aoc/day18.txt")

(defn isNumber
  [token]
  (let [nr (try (edn/read-string token) (catch Exception _ nil))]
    (if (number? nr)
      nr
      nil
      )
    )

  )


(defn aocEval
  [elements]

  (loop [tokens elements
         accumulator (list 0 )
         fnToApply (list +)
         ]
    (if (empty? tokens)
      (peek accumulator)
      (let [[head & tt] tokens
            tail (into [] tt)
            token (str/trim head)
            nrToken (isNumber token)]

        (if (some? nrToken)
          (let [op1 (peek accumulator)
                op2 nrToken
                f (peek fnToApply)
                newAcc (pop accumulator)
                newFn (pop fnToApply)
                ]
            (recur tail (conj newAcc (f op1 op2)) newFn))

          (case token
            "+" (recur tail accumulator (conj fnToApply +))
            "*" (recur tail accumulator (conj fnToApply *))
            "" (recur tail accumulator fnToApply)
            "(" (recur tail (conj accumulator 0) (conj fnToApply +))
            ")" (let [f (peek fnToApply)
                      op1 (peek accumulator)
                      opAcc (pop accumulator)
                      op2 (peek opAcc)
                      newAcc (pop opAcc)
                      newFun (pop fnToApply)
                      ]
                  (recur tail (conj newAcc (f op1 op2)) newFun)
                  )

            )
          )
        )
      )
    )
  )




(defn evaluate
  [expression]

  (let [str1 (str/replace expression #"\(" "( ")
        str2 (str/replace str1 #"\)" " )")
        elements (str/split str2 #" ")]
    (aocEval elements)
    )
  )



(defn -main
  "Day 18: Operation Order"
  [& args]

  (println
    (let [lines (u/readLines inputFile)]
      (reduce + (map evaluate lines))
      )
    )
  )