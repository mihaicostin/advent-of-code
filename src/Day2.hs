module Day2(
        solveDay2_1,
        solveDay2_2
        ) where

import System.Environment
import qualified Data.Text    as Text
import qualified Data.Text.IO as Text
import Data.Text.Read
import Data.List

num :: Text.Text -> Int
num = either (error . show) fst . signed decimal

-- Day 2.1 puzzle
solveDay2_1 :: IO ()
solveDay2_1 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args) )
    print (uncurry (*) (foldr (\x y -> (fst y + (if (elem 2 x) then 1 else 0), snd y + (if (elem 3 x) then 1 else 0)) ) (0,0) (fmap count fileLines)))

count :: Text.Text -> [Int]
count txt = filter (\e -> e==2 || e==3) (map snd (countChars (Text.unpack txt)))

countChars :: Ord a => [a] -> [(a, Int)]
countChars = map (\x -> (head x,length x)) . group . sort


-- Day 2.2 puzzle
solveDay2_2 :: IO ()
solveDay2_2 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args) )
    print (map (\e -> similar (fst e) (snd e))(cartProd (map Text.unpack fileLines) (map Text.unpack fileLines)))

cartProd xs ys = [(x,y) | x <- xs, y <- ys, (length (similar x y)) == ((length x) - 1)]

similar :: String -> String -> String
similar (x:xs) (y:ys) = if (x == y) then x:similar xs ys else similar xs ys
similar [] [] = []