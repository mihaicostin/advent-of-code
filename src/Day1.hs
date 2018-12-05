module Day1(
        solveDay1_1,
        solveDay1_2
        ) where

import System.Environment
import qualified Data.Text    as Text
import qualified Data.Text.IO as Text
import Data.Text.Read

num :: Text.Text -> Int
num = either (error . show) fst . signed decimal

-- Day 1.1 puzzle
solveDay1_1 :: IO ()
solveDay1_1 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args) )
    print (sum (map num fileLines))

-- Day 1.2 puzzle
solveDay1_2 :: IO ()
solveDay1_2 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args))
    print ((listSum [] (cycle (map num fileLines)) ))

listSum :: [Int] -> [Int] -> [Int]
listSum acc (x1:x2:xs) =  if (elem x1 acc) then [x1] else x1:(listSum (x1:acc) (x1+x2:xs))
listSum acc (x:xs) = x:xs
listSum acc [] = []
