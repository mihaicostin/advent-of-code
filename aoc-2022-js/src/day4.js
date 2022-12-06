const {readFile} = require('fs/promises');
const inFile = "day4.txt";


const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

//part1
parseInput(inFile)
    .then(lines => lines.map(line => {
        let pairs = line.trim().split(",");
        let pair1 = pairs[0].split("-");
        let pair2 = pairs[1].split("-");
        let pair1Start = parseInt(pair1[0]);
        let pair1End = parseInt(pair1[1]);
        let pair2Start = parseInt(pair2[0]);
        let pair2End = parseInt(pair2[1]);
        if (pair1Start <= pair2Start && pair1End >= pair2End) {
            return 1;
        }
        if (pair2Start <= pair1Start && pair2End >= pair1End) {
            return 1;
        }
        return 0;
    }))
    .then(list => list.reduce((prev,curr) => prev + curr, 0))
    .then(res => console.log("part1", res));


//part 2
parseInput(inFile)
    .then(lines => lines.map(line => {
        let pairs = line.trim().split(",");
        let pair1 = pairs[0].split("-");
        let pair2 = pairs[1].split("-");
        let s1 = parseInt(pair1[0]);
        let e1 = parseInt(pair1[1]);
        let s2 = parseInt(pair2[0]);
        let e2 = parseInt(pair2[1]);
        if (s1 <= s2 && s2 <= e1) {
            return 1;
        }
        if (s1 <= e2 && e2 <= e1) {
            return 1;
        }
        if (s2 <= s1 && s1 <= e2) {
            return 1;
        }
        if (s2 <= e1 && e1 <= e2) {
            return 1;
        }
        return 0;
    }))
    .then(list => list.reduce((prev,curr) => prev + curr, 0))
    .then(res => console.log("part2", res));