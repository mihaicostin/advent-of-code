(ns aoc.day20_test
  (:require [clojure.test :refer :all]
            [aoc.day20 :refer :all]
            [clojure.string :as str]))


(deftest transformArray
  (testing "transform"
    (let [inputTile {:uid 123, :size 3, :tileArray (seq "123456789")}]
      (is (= (seq "741852963") (:tileArray (rotateR inputTile))))
      (is (= (seq "369258147") (:tileArray (rotateL inputTile))))
      (is (= (seq "321654987") (:tileArray (flipX inputTile))))
      (is (= (seq "789456123") (:tileArray (flipY inputTile))))
      (is (= (seq "123456789") (:tileArray (rotateR (rotateR3 inputTile)))))
      )))

(deftest testAllTrue
  (testing "allTrue function"
    (is (true? (allTrue '(true true true true))))
    (is (= false (allTrue '(true false true true))))
    ))


(deftest matchTile
  (testing "match tile"
    (let [tileOne {:uid 123, :size 3, :tileArray (seq "123456789")}
          tileTwo {:uid 456, :size 3, :tileArray (seq "111222789")}
          ]
      (is (some? (itMatches tileOne tileTwo "S")))
      (is (some? (itMatches tileOne (rotateR tileTwo) "S")))
      (is (some? (itMatches tileOne (rotateL (flipX tileTwo)) "S")))
      (is (some? (itMatches tileOne (rotateL (rotateL (flipX tileTwo))) "S")))
      (is (some? (itMatches tileOne (rotateL (rotateL (flipY tileTwo))) "S")))
      (is (=  nil (itMatches tileOne tileTwo "E")))
      (is (=  nil (itMatches tileOne tileTwo "N")))
      (is (=  nil (itMatches tileOne tileTwo "W")))
      )))


(run-tests `aoc.day20_test)
