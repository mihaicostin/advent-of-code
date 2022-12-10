const {readFile} = require('fs/promises');
const inFile = "day8.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

const isMax = (array, max) => {
    return array.find(el => el >= max) === undefined;
}

const getHigherIdx = (array, max) => {
    return array.map((el, idx) => el >= max ? idx : -1).filter(el => el !== -1);
}

const getColumn = (map, index) => {
    let col = [];
    for (let i = 0; i < map.length; i++) {
        col.push(map[i][index]);
    }
    return col;
}


//part 1
parseInput(inFile)
    .then(lines => {
        let map = [];
        lines.forEach(line => {
            let row = line.split('').map(ch => parseInt(ch))
            map.push(row);
        });
        return map;
    })
    .then(map => {
        let w = map[0].length;
        let h = map.length;
        let visible = h * 2 + (w - 2) * 2;
        for (let i = 1; i < w - 1; i++) {
            for (let j = 1; j < h - 1; j++) {
                let current = map[i][j];
                let col = getColumn(map, j);
                let row = map[i];
                if (isMax(row.slice(0, j), current) || isMax(row.slice(j + 1, w), current) ||
                    isMax(col.slice(0, i), current) || isMax(col.slice(i + 1, h), current)) {
                    visible++;
                }
            }
        }
        return visible;
    })
    .then(res => console.log("part 1", res));

//part 2
parseInput(inFile)
    .then(lines => {
        let map = [];
        lines.forEach(line => {
            let row = line.split('').map(ch => parseInt(ch))
            map.push(row);
        });
        return map;
    })
    .then(map => {
        let w = map[0].length;
        let h = map.length;
        let maxScore = 0;

        for (let i = 1; i < w - 1; i++) {
            for (let j = 1; j < h - 1; j++) {
                let current = map[i][j];
                let col = getColumn(map, j);
                let row = map[i];
                let score = 1;

                let prevIdxRow = getHigherIdx(row.slice(0, j), current);
                let l = prevIdxRow.length === 0 ? j : (j - prevIdxRow.pop());

                let nextInRow = getHigherIdx(row.slice(j + 1, w), current);
                let r = nextInRow.length === 0 ? (w - j - 1) : (nextInRow[0] + 1);

                let prevIdxCol = getHigherIdx(col.slice(0, i), current);
                let t = prevIdxCol.length === 0 ? i : (i - prevIdxCol.pop());

                let nextIdxCol = getHigherIdx(col.slice(i + 1, h), current);
                let b = nextIdxCol.length === 0 ? (h - i - 1) : (nextIdxCol[0] + 1);

                score = l * r * t * b;
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }
        return maxScore;
    })
    .then(res => console.log("part 2", res));

