module Day3(
        solveDay3_1,
        ) where

import System.Environment
import qualified Data.Text    as Text
import qualified Data.Text.IO as Text
import Data.Text.Read
import Data.List
import qualified Data.Map as M
import Text.Regex.Posix


-- Day 3.1 puzzle
solveDay3 :: IO ()
solveDay3 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args) )
    let fabrics = process fileLines
    print (countInches fabrics)
    print (getUniqueClaim fabrics)

data Fabric = Fabric{ fid :: String, fx::Integer, fy:: Integer, fw:: Integer, fh::Integer} deriving (Show)

process :: [Text.Text] -> [Fabric]
process lines = (map (\e -> (getResult ((Text.unpack e) =~ "#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)" :: (String,String,String,[String])))) lines)
getResult (_ , _ , _ , [id, x, y, w, h]) = Fabric id (read x :: Integer) (read y :: Integer) (read w :: Integer) (read h :: Integer)

countInches fabrics = length (M.filter (>1) (M.fromListWith (\c1 c2 -> c1 + c2) (concat (map points fabrics))))
points fab = [ ( (x,y) , 1) | x <- [(fx fab)..(fx fab + fw fab - 1)], y <- [(fy fab)..(fy fab + fh fab - 1)]]

-- Day 3.2 puzzle

getUniqueClaim fabrics = (map fid fabrics) \\ (concat (map snd (M.assocs (M.filter (\e -> (length e) > 1) (M.fromListWith (\c1 c2 -> c1 ++ c2) (concat (map pointToFab fabrics)))))))
pointToFab fab = [ ( (x,y) , [fid fab]) | x <- [(fx fab)..(fx fab + fw fab - 1)], y <- [(fy fab)..(fy fab + fh fab - 1)]]