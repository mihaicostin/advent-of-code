(ns aoc.day18
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            ))

(def inputFile "src/aoc/day18.txt")

(def precedence {"(" 0 "+" 2 "*" 1})


(defn isNumber
  [token]
  (let [nr (try (edn/read-string token) (catch Exception _ nil))]
    (if (number? nr)
      nr
      nil
      )
    )

  )


(defn toRPN
  [tokens]

  ;op stack, output queue
  (let [[s q]
        (reduce
          (fn [result token]
            (if (some? (isNumber token))
              (list (first result) (conj (last result) (isNumber token)))
              (case token
                "(" (list (conj (first result) token) (last result))
                ")" (let [opStack (first result)
                          outQueue (last result)
                          ]
                      (loop [stack opStack
                             queue outQueue
                             ]
                        (let [[head & tail] stack]
                          (if (= head "(")
                            (list tail queue)
                            (recur tail (conj queue head))
                            )
                          )
                        )
                      )
                ("*" "+" "-" "/") (let [opStack (first result)
                                        outQueue (last result)
                                        ]
                                    (loop [stack opStack
                                           queue outQueue
                                           ]
                                      (if (or (empty? stack) (< (get precedence (peek stack)) (get precedence token)))
                                        (list (conj stack token) queue)
                                        (let [[head & tail] stack]
                                          (recur tail (conj queue head))
                                          )
                                        )
                                      )
                                    )
                )
              )
            )

          (list () [])
          tokens
          )
        ]
    (into q s)
    )
  )


(defn evalRPN
  [tokens]
  (loop [remainingTokens tokens
         processingStack '()]
    (if (empty? remainingTokens)
      (last processingStack)
      (let [[currentToken & tokenTail] remainingTokens]
        (case currentToken
          "+" (let [[op1 op2 & tail] processingStack]
                (recur tokenTail (conj tail (+ op1 op2)))
                )
          "*" (let [[op1 op2 & tail] processingStack]
                (recur tokenTail (conj tail (* op1 op2)))
                )
          (recur tokenTail (conj processingStack currentToken))
          )
        )
      )
    )
  )



(defn aocEval
  [elements]

  (loop [tokens elements
         accumulator (list 0)
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




(defn evaluate1
  [expression]

  (let [str1 (str/replace expression #"\(" "( ")
        str2 (str/replace str1 #"\)" " )")
        elements (str/split str2 #" ")]
    (aocEval elements)
    )
  )

(defn evaluate2
  [expression]

  (let [str1 (str/replace expression #"\(" "( ")
        str2 (str/replace str1 #"\)" " )")
        elements (filter #(not (= "" %1)) (str/split str2 #" "))]
    (evalRPN (toRPN elements))
    )
  )



(defn -main
  "Day 18: Operation Order"
  [& args]

  (println
    (let [lines (u/readLines inputFile)]
      (reduce + (map evaluate2 lines))
      )
    )
  )