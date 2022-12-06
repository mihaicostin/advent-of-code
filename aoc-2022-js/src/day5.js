const {readFile} = require('fs/promises');
const inFile = "day5.txt";


const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

//         [J]         [B]     [T]
//         [M] [L]     [Q] [L] [R]
//         [G] [Q]     [W] [S] [B] [L]
// [D]     [D] [T]     [M] [G] [V] [P]
// [T]     [N] [N] [N] [D] [J] [G] [N]
// [W] [H] [H] [S] [C] [N] [R] [W] [D]
// [N] [P] [P] [W] [H] [H] [B] [N] [G]
// [L] [C] [W] [C] [P] [T] [M] [Z] [W]
//  1   2   3   4   5   6   7   8   9

const crates = [
    'LNWTD',
    'CPH',
    'WPHNDGMJ',
    'CWSNTQL',
    'PHCN',
    'THNDMWQB',
    'MBRJGSL',
    'ZNWGVBRT',
    'WGDNPL'
]

const moveP1 = (crates, from, to, quantity) => {
    let srcCrateIdx = from - 1;
    let destCrateIdx = to - 1;
    let src = crates[srcCrateIdx];
    let dest = crates[destCrateIdx];

    let last = src.slice(src.length - quantity, src.length).split('').reverse().join('');
    let a = src.slice(0, src.length - quantity);
    let b = dest + "" + last;

    crates[srcCrateIdx] = a;
    crates[destCrateIdx] = b;

    return crates;
}

const moveP2 = (crates, from, to, quantity) => {
    let srcCrateIdx = from - 1;
    let destCrateIdx = to - 1;
    let src = crates[srcCrateIdx];
    let dest = crates[destCrateIdx];

    let last = src.slice(src.length - quantity, src.length);
    let a = src.slice(0, src.length - quantity);
    let b = dest + "" + last;

    crates[srcCrateIdx] = a;
    crates[destCrateIdx] = b;

    return crates;
}


const getMsg = (crates) => {
    return crates.map(line => line.charAt(line.length - 1)).join("")
}

const parseLine = (line) => {
    let elements = line.split(" ");
    let q = parseInt(elements[1]);
    let src = parseInt(elements[3]);
    let dst = parseInt(elements[5]);
    return {q, src, dst};
}

//part 1
parseInput(inFile).then(lines => lines.reduce((prev, line) => {
    let p = parseLine(line);
    return moveP1(prev, p.src, p.dst, p.q);
}, [...crates]))
    .then(res => getMsg(res))
    .then(res => console.log("part1", res));

//part 2
parseInput(inFile).then(lines => lines.reduce((prev, line) => {
    let p = parseLine(line);
    return moveP2(prev, p.src, p.dst, p.q);
}, [...crates]))
    .then(res => getMsg(res))
    .then(res => console.log("part2", res));