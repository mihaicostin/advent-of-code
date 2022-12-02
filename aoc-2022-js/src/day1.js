const {readFile} = require('fs/promises');
const inFile = "day1.txt";

const countTheElves = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .reduce((prev, current) => {
            if (current.trim().length === 0) {
                prev.push(0);
            } else {
                prev[prev.length - 1] += parseInt(current);
            }
            return prev;
        }, [0]);

}

const max = async list => {
    return list
        .reduce((max, current) => {
            return max > current ? max : current;
        }, 0);
}

//part 1
countTheElves(inFile)
    .then(list => max(list))
    .then(res => console.log(res));

//part 2
countTheElves(inFile)
    .then(list => list.sort())
    .then(list => list.slice(list.length - 3, list.length))
    .then(list => list.reduce((prev, current) => prev + current, 0))
    .then(res => console.log(res));