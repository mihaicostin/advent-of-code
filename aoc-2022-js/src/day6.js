const {readFile} = require('fs/promises');
const inFile = "day6.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content.trim();
}

const findMessage = (line, winLen) => {
    let index = 0;
    let msg = "";
    while (index + winLen < line.length) {
        msg = line.slice(index, index + winLen);
        let set = new Set();
        for (let i=0; i<winLen; i++) {
            set.add(msg.charAt(i))
        }
        if (set.size === msg.length) {
            break;
        }
        index++;
    }
    return index + winLen;
}

//part 1
parseInput(inFile)
    .then(line => findMessage(line, 4))
    .then(res => console.log("part1", res));

//part 2
parseInput(inFile)
    .then(line => findMessage(line, 14))
    .then(res => console.log("part 2",res));