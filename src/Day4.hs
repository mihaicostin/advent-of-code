module Day4(
        solveDay4,
        ) where

import System.Environment
import qualified Data.Text    as Text
import qualified Data.Text.IO as Text
import Data.Text.Read
import Data.List
import qualified Data.Map as M
import Text.Regex.Posix
import Data.Time
import Data.List.Split

-- Day 4.1 puzzle
solveDay4 :: IO ()
solveDay4 = do
    args <- getArgs
    fileLines <- fmap Text.lines (Text.readFile (head args) )
    let lines = process fileLines
    print "Multiply the id by the minute:"
    print (getPart1Results (proc "" "" (sortBy (\(a,_) (b,_) -> compare a b) lines) ))
    print (getPart2Results (proc "" "" (sortBy (\(a,_) (b,_) -> compare a b) lines) ))

process lines = (map (\e -> (getResult ((Text.unpack e) =~ "\\[(.+)\\] (.+)" :: (String,String,String,[String])))) lines)
getResult (_ , _ , _ , [time, action]) = (time, action)


proc gId prevDate ((date, 'G':ac):xs) = proc (getGuardId ac) date xs
proc gId prevDate ((date, 'f':ac):xs) = (gId, []) : proc gId date xs
proc gId prevDate ((date, 'w':ac):xs) = (gId, [(getMin prevDate)..(getMin date)-1]) : proc gId date xs
proc gId prevDate [] = []

getGuardId action = (splitOn " " action)!!1

getMin date = read ((splitOn ":" ((splitOn " " date)!!1))!!1) :: Int

getPart1Results = head . sortBy (\(_,a,_) (_,b,_) -> compare b a) . countMinutes . M.assocs  . M.fromListWith (++)

countMinutes (x:xs) = (fst x, length (snd x), mostCommon (snd x)) : countMinutes xs
countMinutes [] = []

mostCommon = snd . maximum . map (\xs -> (length xs, head xs)) . group . sort

--

getPart2Results = head . sortBy (\(_,(a,_)) (_, (b,_)) -> compare b a) . countTimes . M.assocs  . M.fromListWith (++)

countTimes (x:xs) = (fst x, mostCommonWithLength (snd x)) : countTimes xs
countTimes [] = []

mostCommonWithLength =  maximum . map (\xs -> (length xs, head xs)) . group . sort