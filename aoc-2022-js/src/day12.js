const {readFile} = require('fs/promises');
const inFile = "day12.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

class Point {
    constructor({x, y}) {
        this.x = x;
        this.y = y;
    }
}

class Node {
    constructor({x, y, val}) {
        this.costToGetHere = Infinity; //how much it took to get here
        this.distanceToEnd = undefined; //how much do we think it will take to get to end
        this.fromNode = undefined;
        this.x = x;
        this.y = y;
        this.val = val;
    }

    estimatedCost() {
        return this.costToGetHere + this.distanceToEnd;
    }

    updateDistanceToEnd(end) {
        this.distanceToEnd = Math.abs(end.x - this.x) + Math.abs(end.y - this.y);
    }
}

const neighbours = (land, node) => {
    let x = node.x;
    let y = node.y;
    let res = [];
    res.push(getNode(land, {x: x, y: y - 1}));
    res.push(getNode(land, {x: x, y: y + 1}));
    res.push(getNode(land, {x: x - 1, y: y}));
    res.push(getNode(land, {x: x + 1, y: y}));
    return res.filter(r => r !== undefined);
}

const getNode = (land, point) => {
    if (land[point.y] === undefined) {
        return undefined;
    }
    return land[point.y][point.x];
}

const canPass = (from, to) => {
    return from.val.charCodeAt(0) + 1 >= to.val.charCodeAt(0);
}

const getAllPossibleStarts = (land) => {
    let st = [];
    land.forEach(row => {
        row.forEach(el => {
            if (el.val === 'a') {
                st.push(el);
            }
        })
    });
    return st;
}

const resetMap = (land) => {
    land.forEach(row => row.forEach(node => {
        this.costToGetHere = Infinity; //how much it took to get here
        this.distanceToEnd = undefined; //how much do we think it will take to get to end
        this.fromNode = undefined;
    }))
}


function calculatePath({land, start, end}) {
    let st = getNode(land, start);
    let len = undefined;
    let openList = [];
    let closedList = [];
    land.map(row => {
        return row.map(node => node.updateDistanceToEnd(end));
    });
    st.costToGetHere = 0;

    openList.push(st);

    while (openList.length > 0) {
        let currentNode = openList.sort((a, b) => b.estimatedCost() - a.estimatedCost()).pop();
        if (currentNode.x === end.x && currentNode.y === end.y) {
            len = currentNode.costToGetHere;
            break;
        } else {
            closedList.push(currentNode);
            let nodes = neighbours(land, currentNode);
            for (let nd of nodes) {
                if (closedList.indexOf(nd) !== -1 || !canPass(currentNode, nd)) {
                    //you shall not pass
                    continue;
                }
                let score = currentNode.costToGetHere + 1;
                let isBestScore = false;

                if (openList.indexOf(nd) === -1) {
                    //first time here
                    isBestScore = true;
                    openList.push(nd);
                } else {
                    if (score < nd.costToGetHere) {
                        isBestScore = true;
                    }
                }
                if (isBestScore) {
                    nd.fromNode = currentNode;
                    nd.costToGetHere = score;
                }
            }
        }
    }
    return len;
}

console.time("part2");
parseInput(inFile)
    .then(lines => {
        let start, end;
        let land = lines.map((line, y) => {
            return line.split('').map((ch, x) => {
                let val = undefined;
                if (ch === 'S') {
                    start = {x, y};
                    val = 'a';
                } else if (ch === 'E') {
                    end = {x, y}
                    val = 'z';
                } else {
                    val = ch;
                }
                return new Node({x, y, val});
            });
        });
        return {land, start, end};
    })
    .then(res => {
        let land = res.land;
        let allPossibleStarts = getAllPossibleStarts(land);
        let min = Infinity;
        for (let start of allPossibleStarts) {
            let dist = calculatePath({land, start: start, end: res.end});
            if (dist < min) {
                min = dist;
            }
            resetMap(land);
        }
        return min;
    })
    .then(res => {
        console.log(res);
        console.timeEnd("part2");
    });
