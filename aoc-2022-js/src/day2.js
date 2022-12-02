const {readFile} = require('fs/promises');

const inFile = "day2.txt";

const p1 = "ABC"
const p2 = "XYZ"

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim().split(" "));
}

//part 1
parseInput(inFile)
    .then(list => list.map(game => {
        let idx1 = p1.indexOf(game[0]);
        let idx2 = p2.indexOf(game[1]);
        let score = (idx2 + 1);
        if (idx1 === idx2) {
            return score + 3;
        }
        if (idx1 < idx2) {
            if (idx1 === 0 && idx2 === 2) {
                return score;
            } else {
                return score + 6;
            }
        }
        if (idx1 > idx2) {
            if (idx1 === 2 && idx2 === 0) {
                return score + 6;
            } else {
                return score;
            }
        }
    }))
    .then(res => res.reduce((prev, curr) => prev + curr, 0))
    .then(res => console.log("part1", res));


//part 2
parseInput(inFile)
    .then(list => list.map(game => {
        let idx1 = p1.indexOf(game[0]);
        let idx2 = p2.indexOf(game[1]);

        if (idx2 === 0) {
            //lose
            return 0 + ((idx1 === 0 ? 2 : (idx1 - 1)) + 1);
        }
        if (idx2 === 1) {
            //draw
            return 3 + (idx1 + 1);
        }
        if (idx2 === 2) {
            //win
            return 6 + ((idx1 === 2 ? 0 : (idx1 + 1)) + 1);
        }
    }))
    .then(res => res.reduce((prev, curr) => prev + curr, 0))
    .then(res => console.log("part2", res));


