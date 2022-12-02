(ns aoc.day19_test
  (:require [clojure.test :refer :all]
            [aoc.day19 :refer :all]
            [clojure.string :as str]))


(def testInput ["0: 4 1 5"
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


(def testInputPart2 "42: 9 14 | 10 1\n9: 14 27 | 1 26\n10: 23 14 | 28 1\n1: \"a\"\n11: 42 31\n5: 1 14 | 15 1\n19: 14 1 | 14 14\n12: 24 14 | 19 1\n16: 15 1 | 14 14\n31: 14 17 | 1 13\n6: 14 14 | 1 14\n2: 1 24 | 14 4\n0: 8 11\n13: 14 3 | 1 12\n15: 1 | 14\n17: 14 2 | 1 7\n23: 25 1 | 22 14\n28: 16 1\n4: 1 1\n20: 14 14 | 1 15\n3: 5 14 | 16 1\n27: 1 6 | 14 18\n14: \"b\"\n21: 14 1 | 1 14\n25: 1 1 | 1 14\n22: 14 14\n8: 42\n26: 14 22 | 1 20\n18: 15 15\n7: 14 5 | 1 21\n24: 14 1\n\nabbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa\nbbabbbbaabaabba\nbabbbbaabbbbbabbbbbbaabaaabaaa\naaabbbbbbaaaabaababaabababbabaaabbababababaaa\nbbbbbbbaaaabbbbaaabbabaaa\nbbbababbbbaaaaaaaabbababaaababaabab\nababaaaaaabaaab\nababaaaaabbbaba\nbaabbaaaabbaaaababbaababb\nabbbbabbbbaaaababbbbbbaaaababb\naaaaabbaabaaaaababaa\naaaabbaaaabbaaa\naaaabbaabbaaaaaaabbbabbbaaabbaabaaa\nbabaaabbbaaabaababbaabababaaab\naabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba")



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
      (is (= "a((aa|bb)(ab|ba)|(ab|ba)(aa|bb))b" (expandRule (first input) "0" 10)))
      ))
  )

(deftest testVerify
  (testing "verify message"
    (let [input (parseInput testInput)]
      (is (= true (verify "ababbb" (expandRule (first input) "0" 10))))
      (is (= true (verify "abbbab" (expandRule (first input) "0" 10))))
      (is (= false (verify "bababa" (expandRule (first input) "0" 10))))
      (is (= false (verify "aaabbb" (expandRule (first input) "0" 10))))
      (is (= false (verify "aaaabbb" (expandRule (first input) "0" 10))))
      ))
  )

(deftest testVerifyPart2
  (testing "verify message for part 2"
    (let [input (parseInput (str/split testInputPart2 #"\n"))
          rulex (expandRule (first input) "0" 10)]
      (println rulex)
      (is (= true (verify "aaabbbbbbaaaabaababaabababbabaaabbababababaaa" rulex)))
      ))
  )




(run-tests `aoc.day19_test)
