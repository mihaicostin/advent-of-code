(ns aoc.day19_test
  (:require [clojure.test :refer :all]
            [aoc.day19 :refer :all]
            ))


(def testInput  ["0: 4 1 5"
                  "1: 2 3 | 3 2"
                  "2: 4 4 | 5 5"
                  "3: 4 5 | 5 4"
                  "4: \"a\""
                  "5: \"b\""
                  "ababbb"
                  "bababa"
                  "abbbab"
                  "aaabbb"
                  "aaaabbb"])


(deftest testParsing
  (testing "parse input"
    (is (= '({"0" (("4" "1" "5")),
              "1" (("2" "3") ("3" "2")),
              "2" (("4" "4") ("5" "5")),
              "3" (("4" "5") ("5" "4")),
              "4" (("a")),
              "5" (("b"))}
             ["ababbb" "bababa" "abbbab" "aaabbb" "aaaabbb"]) (parseInput testInput)))
    )
  )

(deftest testExpandRule
  (testing "turn into regex"
    (let [input (parseInput testInput)]
    (is (= "a((aa|bb)(ab|ba)|(ab|ba)(aa|bb))b" (expandRule (first input) "0")))
    ))
  )

(deftest testVerify
  (testing "verify message"
    (let [input (parseInput testInput)]
    (is (= true (verify "ababbb" (expandRule (first input) "0"))))
    (is (= true (verify "abbbab" (expandRule (first input) "0"))))
    (is (= false (verify "bababa" (expandRule (first input) "0"))))
    (is (= false (verify "aaabbb" (expandRule (first input) "0"))))
    (is (= false (verify "aaaabbb" (expandRule (first input) "0"))))
    ))
  )





(run-tests `aoc.day19_test)
