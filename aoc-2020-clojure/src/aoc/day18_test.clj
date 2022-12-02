(ns aoc.day18_test
  (:require [clojure.test :refer :all]
            [aoc.day18 :refer :all]
            ))


(deftest addition
  (testing "math part 1"
    (is (= 7 (evaluate1 "2 + 5")))
    (is (= 12 (evaluate1 "2 + 2 * 3 ")))
    (is (= 12 (evaluate1 "   2   +   2   *   3 ")))
    (is (= 51 (evaluate1 "  1 + (2 * 3) + (4 * (5 + 6)) ")))
    (is (= 71 (evaluate1 " 1 + 2 * 3 + 4 * 5 + 6 ")))
    (is (= 26 (evaluate1 " 2 * 3 + (4 * 5) ")))
    (is (= 437 (evaluate1 " 5 + (8 * 3 + 9 + 3 * 4 * 3) ")))
    (is (= 12240 (evaluate1 " 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    (is (= 13632 (evaluate1 " ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 ")))
    )
  )

(deftest parseToRPN
  (testing "RPN"
    (is (= [2 5 "+"] (toRPN ["2" "+" "5"])))
    )
  )

(deftest strangeMath
  (testing "math part two"
    (is (= 7 (evaluate2 "2 + 5")))
    (is (= 51 (evaluate2 "1 + (2 * 3) + (4 * (5 + 6))")))
    (is (= 46 (evaluate2 " 2 * 3 + (4 * 5) ")))
    (is (= 1445 (evaluate2 " 5 + (8 * 3 + 9 + 3 * 4 * 3) ")))
    (is (= 669060 (evaluate2 " 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    (is (= 23340 (evaluate2 " ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 ")))
    )
  )



(run-tests `aoc.day18_test)
