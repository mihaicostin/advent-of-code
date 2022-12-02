(ns aoc.day19
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            ))

(def inputFile "src/aoc/day19.txt")

(defn parseRule
  [line]
  (let [[ruleKey ruleDefStr] (str/split line #":")
        options (case ruleKey
                  ;update for part2
                  "8" ["42" "42 8"]
                  "11" ["42 31" "42 11 31"]
                  (str/split ruleDefStr #"\|"))
        rules (for [option options]
                (filter #(not (empty? %1)) (map #(str/trim (str/replace %1 #"\"" "")) (str/split option #" "))))
        ]
    {ruleKey rules}
    ))


(defn parseLine
  [line]
  (if (> (.indexOf line ":") 0)
    {:rule (parseRule line)}
    {:message (str/trim line)}
    ))

(defn parseInput
  [lines]
  (reduce (fn [result line]
            (let [parsed (parseLine line)
                  rulesMap (first result)
                  messages (last result)
                  ]
              (if (some? (get parsed :rule))
                (list (into rulesMap (get parsed :rule)) messages)
                (list rulesMap (conj messages (get parsed :message)))))) '({} []) lines))

(defn expandRule
  [ruleMap ruleKey depth]
  (if (or (= ruleKey "a") (= ruleKey "b"))
    ruleKey
    (let [ruleDef (get ruleMap ruleKey)]
      (reduce (fn [result alternatives]
                (let [expandedRules (map (fn [rule]
                                           (if (= ruleKey rule)
                                             (if (= depth 0)
                                               ""
                                               (expandRule ruleMap rule (dec depth)))
                                             (expandRule ruleMap rule depth))) alternatives)
                      expanded (str/join expandedRules)
                      ]
                  (if (empty? result)
                    expanded
                    (str/join ["(" (str/join "|" [result expanded]) ")"])
                    ))) "" ruleDef)
      )))

(defn verify
  [message regex]
  (some? (re-matches (re-pattern regex) message))
  )


(defn -main
  "Day 19: Monster Messages"
  [& args]

  (println
    (let [lines (u/readLines inputFile)
          rulesAndMessages (parseInput lines)
          rules (first rulesAndMessages)
          messages (last rulesAndMessages)
          pattern (expandRule rules "0" 10)
          ]
      (count (filter true? (for [msg messages] (verify msg pattern)))))))