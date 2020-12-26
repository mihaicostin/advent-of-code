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
      (is (= (seq "123456789") (:tileArray (rotateR (rotateR3 inputTile))))))))

(deftest testAllTrue
  (testing "allTrue function"
    (is (true? (allTrue '(true true true true))))
    (is (= false (allTrue '(true false true true))))))


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
      (is (= nil (itMatches tileOne tileTwo "E")))
      (is (= nil (itMatches tileOne tileTwo "N")))
      (is (= nil (itMatches tileOne tileTwo "W"))))))

(deftest testTrimBorders
  (testing "trim borders"
    (is (= (seq "2367230167760112")
           (:tileArray (trimBorders [
                                     {:uid 1, :size 4, :tileArray (seq "1234123412341234")}
                                     {:uid 2, :size 4, :tileArray (seq "4567567890123456")}
                                     {:uid 3, :size 4, :tileArray (seq "1234567890123456")}
                                     {:uid 4, :size 4, :tileArray (seq "9999876831246315")}
                                     ]))))))

(deftest findMonsterTest
  (testing "find monster pattern"
    (let [mp [[0 0] [1 1] [0 1] [-1 1] [-3 1]]
          inputTileArray (seq "#....#.#...####...#.#..#.#..###.#.###...#.##..#...##.#..#..##...##...#.#####...##....#......#..####.")
          inputTile {:uid 0, :size 10, :tileArray inputTileArray}
          ]
      (is (= "#....#.#...####...#.#..#.O..###.O.OOO...#.##..#...##.#..#..##...O#...#.O#OOO...##....#......#..####." (str/join (:tileArray (findMonster inputTile mp))))))))


(def INPUT_TILES
  (str/split
    "Tile 2311:\n..##.#..#.\n##..#.....\n#...##..#.\n####.#...#\n##.##.###.\n##...#.###\n.#.#.#..##\n..#....#..\n###...#.#.\n..###..###\n\nTile 1951:\n#.##...##.\n#.####...#\n.....#..##\n#...######\n.##.#....#\n.###.#####\n###.##.##.\n.###....#.\n..#.#..#.#\n#...##.#..\n\nTile 1171:\n####...##.\n#..##.#..#\n##.#..#.#.\n.###.####.\n..###.####\n.##....##.\n.#...####.\n#.##.####.\n####..#...\n.....##...\n\nTile 1427:\n###.##.#..\n.#..#.##..\n.#.##.#..#\n#.#.#.##.#\n....#...##\n...##..##.\n...#.#####\n.#.####.#.\n..#..###.#\n..##.#..#.\n\nTile 1489:\n##.#.#....\n..##...#..\n.##..##...\n..#...#...\n#####...#.\n#..#.#.#.#\n...#.#.#..\n##.#...##.\n..##.##.##\n###.##.#..\n\nTile 2473:\n#....####.\n#..#.##...\n#.##..#...\n######.#.#\n.#...#.#.#\n.#########\n.###.#..#.\n########.#\n##...##.#.\n..###.#.#.\n\nTile 2971:\n..#.#....#\n#...###...\n#.#.###...\n##.##..#..\n.#####..##\n.#..####.#\n#..#.#..#.\n..####.###\n..#.#.###.\n...#.#.#.#\n\nTile 2729:\n...#.#.#.#\n####.#....\n..#.#.....\n....#..#.#\n.##..##.#.\n.#.####...\n####.#.#..\n##.####...\n##..#.##..\n#.##...##.\n\nTile 3079:\n#.#.#####.\n.#..######\n..#.......\n######....\n####.#..#.\n.#...#.##.\n#.#####.##\n..#.###...\n..#.......\n..#.###..."
    #"\n"))


(deftest testCorners
  (testing "find the corners"
    (is (= 20899048083289 (multiplyCorners INPUT_TILES 10)))))


(def MONSTER_IMG ".####...#####..#...###..#####..#..#.#.####..#.#..#.#...#.###...#.##.O#..#.O.##.OO#.#.OO.##.OOO##..#O.#O#.O##O..O.#O##.##...#.#..##.##...#..#..###.##.#..#.#..#..##.#.#...###.##.....#...###.#...#.####.#.#....##.#..#.#.##...#..#....#..#...####..#.##...###..#.#####..#....#.##.#.#####....#.....##.##.###.....#.##..#.#...#...###..####....##..#.##...#.##.#.#.###...##.###.#..####...##..#...#.###...#.##...#.##O###..O##.#OO.###OO##..OOO##...O#.O..O..O.#O##O##.####.#..##.########..#..##.#.#####..#.#...##..#....#....##..#.#########..###...#.....#..##...###.###..###....##.#...##.##.#")
(def NO_MONSTER_IMG (str/replace MONSTER_IMG #"O" "#"))

(deftest testLocations
  (testing "test monster locations"
    (is (true? (patternFoundHere {:uid 1, :size 24, :tileArray (seq NO_MONSTER_IMG)} MP 20 2)  ))
    (is (true? (patternFoundHere {:uid 1, :size 24, :tileArray (seq NO_MONSTER_IMG)} MP 19 16)  ))
    (is (= [[20 2] [19 16]] (findMonsterLocations {:uid 1, :size 24, :tileArray (seq NO_MONSTER_IMG)} MP)))
    ))

(deftest testMonsterImg_noTr
  (testing "build image with monsters from direct input"
    (is (= MONSTER_IMG (str/join (:tileArray (findMonster {:uid 1, :size 24, :tileArray (seq NO_MONSTER_IMG)} MP)))))))


(deftest testCountWater
  (testing "count water tiles"
    (is (= 273 (countWaters (findMonster (buildFullTile INPUT_TILES 10) MP))))))

(deftest testMonsterImg
  (testing "build image with monsters"
    (is (= MONSTER_IMG (str/join (:tileArray (findMonster (buildFullTile INPUT_TILES 10) MP)))))))


(run-tests `aoc.day20_test)
