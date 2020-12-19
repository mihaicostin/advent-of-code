(ns aoc.day18_test
  (:require [clojure.test :refer :all]
            [aoc.day18 :refer :all]
            ))


(deftest addition
  (testing "additon"
    (is (= 7 (evaluate "2 + 5")))
    (is (= 12 (evaluate "2 + 2 * 3 ")))
    (is (= 12 (evaluate "   2   +   2   *   3 ")))
    (is (= 51 (evaluate "  1 + (2 * 3) + (4 * (5 + 6)) ")))
    (is (= 71 (evaluate " 1 + 2 * 3 + 4 * 5 + 6 ")))
    (is (= 26 (evaluate " 2 * 3 + (4 * 5) ")))
    (is (= 437 (evaluate " 5 + (8 * 3 + 9 + 3 * 4 * 3) ")))
    (is (= 12240 (evaluate " 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
    (is (= 13632 (evaluate " ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 ")))
    )
  )

(run-tests `aoc.day18_test)
