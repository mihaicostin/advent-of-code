(ns aoc.day20
  (:require [aoc.utilitybelt :as u]
            [clojure.edn :as edn]
            [clojure.string :as str]
            ))

(def inputFile "src/aoc/day20.txt")

(defn parseTile
  [lines]
  (let [[_ id] (re-matches #"Tile (\d+):" (nth lines 0))
        tileString (str/join (drop 1 lines))]
    {:uid (edn/read-string id), :size 10, :tileArray (seq (str/trim tileString))}))

(defn parseInput
  [lines]
  (loop [processingLines lines
         result []]
    (if (= 0 (count processingLines))
      result
      (let [tileArray (take 12 processingLines)
            remaining (drop 12 processingLines)
            tile (parseTile tileArray)
            ]
        (recur remaining (conj result tile))))))


(defn getValueAt
  [tile x y]
  (let [size (:size tile)
        tileArray (:tileArray tile)
        ]
    (nth tileArray (+ x (* y size)))))


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
                      tile
                      nil)))))



(defn printTile
  [tileMap]
  (doseq [[idx el] (map list (range 100) (get tileMap :tileArray))]
    (if (= 0 (mod (+ idx 1) 10))
      (println el)
      (print el)
      )
    )
  )


(defn -main
  "Day 20: Jurassic Jigsaw"
  [& args]

  (println "part 1"
    (let [lines (u/readLines inputFile)
          tilesArray (parseInput lines)
          corners (findCorners tilesArray)
          ]
      (reduce #(* %1 (:uid %2)) 1 corners)))



  )