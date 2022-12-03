const {readFile} = require('fs/promises');
const inFile = "day3.txt";

const prio = letter => {
    if (letter.toLowerCase() === letter) {
        //lowercase
        return letter.charCodeAt(0) - "a".charCodeAt(0) + 1;
    } else {
        //uppercase
        return letter.charCodeAt(0) - "A".charCodeAt(0) + 27;
    }
}

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

//part1
parseInput(inFile)
    .then(lines => lines.map(line => {
        return [line.slice(0, line.length / 2), line.slice(line.length / 2, line.length)]
    }))
    .then(pairs => pairs
        .map(pair => pair[0].split('').filter(char => pair[1].indexOf(char) > -1))
        .map(r => r[0])
        .map(el => prio(el))
    )
    .then(list => list.reduce((prev,curr) => prev + curr, 0))
    .then(res => console.log('part 1',res));


//part 2
parseInput(inFile)
    .then(lines => lines.reduce( (prev,curr) => {
        if (prev[prev.length-1].length < 3) {
            prev[prev.length-1].push(curr)
        } else {
            prev.push([curr])
        }
        return prev;
    }, [[]]))
    .then(trioList => trioList.map(trio => {
        return trio[0].split('').filter(el => trio[1].indexOf(el) > -1 && trio[2].indexOf(el) > -1)[0]
    }))
    .then(ch => ch.map(c => prio(c)))
    .then(list => list.reduce((prev,curr) => prev + curr, 0))
    .then(res => console.log('part 2',res));
