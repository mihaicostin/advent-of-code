(ns aoc.day20
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.math.numeric-tower :as math]
            ))

(def inputFile "src/aoc/day20.txt")

(defn parseTile
  [lines tileSize]
  (let [[_ id] (re-matches #"Tile (\d+):" (nth lines 0))
        tileString (str/join (drop 1 lines))]
    {:uid (edn/read-string id), :size tileSize, :tileArray (seq (str/trim tileString))}))

(defn parseInput
  [lines tileSize]
  (loop [processingLines lines
         result []]
    (if (= 0 (count processingLines))
      result
      (let [nrLines (+ 2 tileSize)
            tileArray (take nrLines processingLines)
            remaining (drop nrLines processingLines)
            tile (parseTile tileArray tileSize)
            ]
        (recur remaining (conj result tile))))))


(defn getValueAt
  [tile x y]
  (let [size (:size tile)
        tileArray (:tileArray tile)]
    (nth tileArray (+ x (* y size)))))

(defn getFromXY
  [array w x y]
  (nth array (+ x (* y w))))

(defn transformFlipY
  [tile]
  (let [size (:size tile)]
    (for [y (range size)
          x (range size)]
      (getValueAt tile x (- (- size 1) y)))))

(defn transformFlipX
  [tile]
  (let [size (:size tile)]
    (for [y (range size)
          x (range size)]
      (getValueAt tile (- (- size 1) x) y))))


(defn transformRotateR
  [tile]
  (let [size (:size tile)]
    (for [y (range size)
          x (range size)]
      (getValueAt tile y (- (- size 1) x)))))

(defn transformRotateL
  [tile]
  (let [size (:size tile)]
    (for [y (range size)
          x (range size)]
      (getValueAt tile (- (- size 1) y) x))))


(defn transform
  [tile func]
  (let [tileSize (:size tile)
        uid (:uid tile)
        newTileArray (func tile)
        ]
    {:uid uid, :size tileSize :tileArray newTileArray}))


(defn flipX [tile] (transform tile transformFlipX))
(defn flipY [tile] (transform tile transformFlipY))
(defn rotateL [tile] (transform tile transformRotateL))
(defn rotateR [tile] (transform tile transformRotateR))


(defn allTrue
  [elements]
  (reduce #(and %1 %2) true elements))


(defn matchEdgeNorth
  [tileOne tileTwo]
  (let [size (:size tileOne)]
    (allTrue (for [x (range size)] (= (getValueAt tileOne x 0) (getValueAt tileTwo x (- size 1)))))))

(defn matchEdgeSouth
  [tileOne tileTwo]
  (let [size (:size tileOne)]
    (allTrue (for [x (range size)] (= (getValueAt tileOne x (- size 1)) (getValueAt tileTwo x 0))))))

(defn matchEdgeEast
  [tileOne tileTwo]
  (let [size (:size tileOne)]
    (allTrue (for [y (range size)] (= (getValueAt tileOne 0 y) (getValueAt tileTwo (- size 1) y))))))

(defn matchEdgeWest
  [tileOne tileTwo]
  (let [size (:size tileOne)]
    (allTrue (for [y (range size)] (= (getValueAt tileOne (- size 1) y) (getValueAt tileTwo 0 y))))))

(defn rotateR1 [tile] (rotateR tile))
(defn rotateR2 [tile] (rotateR (rotateR tile)))
(defn rotateR3 [tile] (rotateR (rotateR (rotateR tile))))


(defn itMatches
  [tile testTile edge]
  (if (= (:uid tile) (:uid testTile))
    nil
    (let [edgeTest (case edge
                     "N" matchEdgeNorth
                     "S" matchEdgeSouth
                     "E" matchEdgeEast
                     "W" matchEdgeWest)
          trPair (for [tr [identity flipX flipY]
                       rot [identity rotateR1 rotateR2 rotateR3]] [tr rot])]
      (loop [currentTrList trPair]
        (if (empty? currentTrList)
          nil
          (let [[[trans rotation] & tail] currentTrList
                t (rotation (trans testTile))]
            (if (edgeTest tile t)
              t
              (recur tail))))))))


(defn findMatch
  [tile tiles edge]
  (loop [tilesToSearch tiles]
    (if (empty? tilesToSearch)
      nil
      (let [[testingTile & tail] tilesToSearch
            match (itMatches tile testingTile edge)]
        (if (some? match)
          match
          (recur tail))))))


(defn findCorners
  [tiles]
  (filter some? (for [tile tiles]
                  (let [matches (for [edge ["N" "E" "S" "W"]] (findMatch tile tiles edge))
                        matchedEdges (count (filter some? matches))]
                    (if (= 2 matchedEdges)
                      (list tile (str/join (filter some? (map #(if (some? %1) %2 nil) matches ["N" "E" "S" "W"]))))
                      nil)))))

(defn multiplyCorners
  [inputTiles tileSize]

  (let [tilesArray (parseInput inputTiles tileSize)
        corners (map first (findCorners tilesArray))]
    (reduce #(* %1 (:uid %2)) 1 corners)))

(defn findFirstCorner
  [tiles]
  (first (first (filter #(= "ES" (last %)) (findCorners tiles)))))


(defn getByUid
  [tileArray uid]
  (filter #(= (:uid %1) uid) tileArray))

(defn dropByUid
  [tileArray uid]
  (filter #(not= (:uid %1) uid) tileArray))


(defn buildMap
  [tiles]
  (let [mapSize (int (Math/sqrt (count tiles)))
        indexes (for [y (range mapSize) x (range mapSize)] [x y])]
    (loop [[[x y] & remainingIndexes] indexes
           searchTiles tiles
           foundTiles []]
      (if (= nil x)
        foundTiles
        (if (and (= x 0) (= y 0))
          (let [corner (findFirstCorner tiles)]
            ;(println "found tile (corner) :" (:uid corner))
            (recur remainingIndexes (dropByUid searchTiles (:uid corner)) (conj foundTiles corner)))
          (if (= x 0)
            (let [matchTargetTile (getValueAt {:tileArray foundTiles :size mapSize} 0 (- y 1))
                  foundTile (findMatch matchTargetTile searchTiles "S")
                  remainingSearchTiles (dropByUid searchTiles (:uid foundTile))
                  ]
              ;(println "found tile (first on row " y ") :" (:uid foundTile))
              (recur remainingIndexes remainingSearchTiles (conj foundTiles foundTile)))
            (let [matchTargetTile (last foundTiles)
                  foundTile (findMatch matchTargetTile searchTiles "E")
                  remainingSearchTiles (dropByUid searchTiles (:uid foundTile))
                  ]
              ;(println "found tile on row " y " :" (:uid foundTile))
              (recur remainingIndexes remainingSearchTiles (conj foundTiles foundTile)))))))))


(defn trimBorders
  [imageMap]

  (let [tileSize (:size (first imageMap))
        borderlessTileSize (- tileSize 2)
        mapSize (int (Math/sqrt (count imageMap)))
        mapW (* mapSize borderlessTileSize)
        tileArray (for [y (range mapW)
                        x (range mapW)]
                    (let [tx (int (math/floor (/ x borderlessTileSize)))
                          ty (int (math/floor (/ y borderlessTileSize)))
                          inTileX (rem x borderlessTileSize)
                          inTileY (rem y borderlessTileSize)
                          tile (getFromXY imageMap mapSize tx ty)
                          ]
                      (getValueAt tile (inc inTileX) (inc inTileY))))
        ]
    {:uid 0, :size mapW :tileArray tileArray}))


(defn printTile
  [tileMap]
  (doseq [[idx el] (map list (range (* (:size tileMap) (:size tileMap))) (get tileMap :tileArray))]
    (if (= 0 (mod (+ idx 1) (:size tileMap)))
      (println el)
      (print el))))

(defn buildFullTile
  [inputLines tileSize]
  (let [tilesArray (parseInput inputLines tileSize)
        imageMap (buildMap tilesArray)
        ]
    (trimBorders imageMap)))


(def MP [[0 0]
         [1 1] [0 1] [-1 1] [-6 1] [-7 1] [-12 1] [-13 1] [-18 1]
         [-2 2] [-5 2] [-8 2] [-11 2] [-14 2] [-17 2]])


(defn patternFoundHere
  [imageTile monsterPattern x y]

  (allTrue (for [mIdx monsterPattern]
             (let [patternX (+ x (first mIdx))
                   patternY (+ y (last mIdx))]
               (if (and (>= patternX 0)
                        (< patternX (:size imageTile))
                        (>= patternY 0)
                        (< patternY (:size imageTile))
                        (= \# (getValueAt imageTile patternX patternY)))
                 true
                 false)))))


(defn addMonster
  [imageTile monsterPattern mx my]

  (for [y (range (:size imageTile))
        x (range (:size imageTile))]
    (loop [[index & tail] monsterPattern]
      (if (not (some? index))
        (getValueAt imageTile x y)
        (if (and (= x (+ mx (first index)))
                 (= y (+ my (last index))))
          \O
          (recur tail))))))

(defn addAllMonsters
  [imageTile monsterPattern positions]
  (println "monster positions" positions)
  (loop [tile imageTile
         [pos & tail] positions
         ]
    (if (not (some? pos))
      tile
      (recur (into tile {:tileArray (addMonster tile monsterPattern (first pos) (last pos))}) tail))))

(defn findMonsterLocations
  [imageTile monsterPattern]
  (filter some? (for [y (range (:size imageTile))
                      x (range (:size imageTile))]
                  (if (= \# (getValueAt imageTile x y))
                    (if (true? (patternFoundHere imageTile monsterPattern x y))
                      [x y]
                      nil)
                    nil))))


(defn findMonster
  [imageTile monsterPattern]

  (let [trPair (for [tr [identity flipX flipY] rot [identity rotateR1 rotateR2 rotateR3]] [tr rot])]
    (loop [currentTrList trPair]
      (if (empty? currentTrList)
        nil
        (let [ [[trans rotation] & tail] currentTrList
              searchTile (rotation (trans imageTile))
              monsterLocations (findMonsterLocations searchTile monsterPattern)]
          (if (not (empty? monsterLocations))
            (addAllMonsters searchTile monsterPattern monsterLocations)
            (recur tail)))))))

(defn countWaters
  [tileWithMonsters]
  (count (filter #(= % \#) (:tileArray tileWithMonsters)))
  )


(defn -main
  "Day 20: Jurassic Jigsaw"
  [& args]

  ;(println "part 1" (multiplyCorners (u/readLines inputFile) 10))

  (println "part 2")
  (println
    (let [lines (u/readLines inputFile)
          imageTile (buildFullTile lines 10)
          tileWithMonsters (findMonster imageTile MP)
          ]
      (printTile imageTile)
      (println "--------")
      (printTile tileWithMonsters)
      (countWaters tileWithMonsters))))