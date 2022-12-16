const {readFile} = require('fs/promises');
const inFile = "day14.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

// 0    - empty space
// 1    - rock
//-1    - sand
//undefined = void
const initMap = (minX, maxX, maxY) => {
    let caveMap = [];
    for (let y = 0; y <= maxY; y++) {
        let row = [];
        for (let x = minX; x <= maxX; x++) {
            row.push(0);
        }
        caveMap.push(row);
    }
    return caveMap;
}

function* range(start, end) {
    for (let i = Math.min(start, end); i <= Math.max(start, end); i++) {
        yield i;
    }
}

const printMap = (map) => {
    for (let r = 0; r < map.length; r++) {
        console.log(JSON.stringify(map[r]));
    }
}

const isVoid = (caveMap, x, y) => {
    return caveMap[y] === undefined || caveMap[y][x] === undefined;
}

const isRock = (map, x, y) => {
    return map[y][x] === 0;
}

const isNotBlocked = (map, x, y) => {
    return isVoid(map, x, y) || isRock(map, x, y);
}

const placeSand = (map, x, y) => {
    map[y][x] = 2;
}

//true = void, false = rested
const moveSand = (caveMap, x, y) => {

    //falls down one step if possible
    //if blocked => one step down and to the left.
    //if that tile is blocked => one step down and to the right
    //if that also blocked => rest

    if (isVoid(caveMap, x, y)) {
        return true;
    }

    if (isNotBlocked(caveMap, x, y + 1)) {
        return moveSand(caveMap, x, y + 1);
    }

    if (isNotBlocked(caveMap, x - 1, y + 1)) {
        return moveSand(caveMap, x - 1, y + 1);
    }

    if (isNotBlocked(caveMap, x + 1, y + 1)) {
        return moveSand(caveMap, x + 1, y + 1);
    }

    //all 3 directions are blocked. this will rest here.
    placeSand(caveMap, x, y);
    return {x, y};
}

parseInput(inFile)
    .then(lines => {
        let maxX = 0;
        let minX = Infinity;
        let maxY = 0;
        let mapLines = lines.map(line => {
            let points = line.split("->");
            return points.map(point => {
                let xy = point.trim().split(",");
                let p = {x: parseInt(xy[0]), y: parseInt(xy[1])};
                if (p.x > maxX) {
                    maxX = p.x;
                }
                if (p.x < minX) {
                    minX = p.x;
                }
                if (p.y > maxY) {
                    maxY = p.y;
                }
                return p;
            });
        });
        minX = 0;
        return {mapLines, minX, maxX, maxY};
    })
    .then(res => {
        let caveMap = initMap(0 - 2 * res.maxY, res.maxX + 2 * res.maxY, res.maxY + 2);
        let lines = res.mapLines;
        lines.forEach(points => {
            for (let i = 0; i < points.length - 1; i++) {
                let point = points[i];
                let nextPoint = points[i + 1];

                let px = point.x - res.minX;
                let py = point.y;

                let npx = nextPoint.x - res.minX;
                let npy = nextPoint.y;

                if (px === npx) {
                    //fill the y
                    for (let y of range(py, npy)) {
                        caveMap[y][px] = 1;
                    }
                } else if (py === npy) {
                    //fill the x
                    for (let x of range(px, npx)) {
                        caveMap[py][x] = 1;
                    }
                } else {
                    console.error("diagonal line?!");
                }
            }
        });
        let lastRow = caveMap[caveMap.length - 1];
        caveMap[caveMap.length - 1] = lastRow.map(_ => 1);

        return {caveMap, minX: res.minX};
    })
    .then(res => {
        let sy = 0;
        let sx = 500 - res.minX;

        console.log(sx, sy);
        //printMap(res.caveMap);

        // let theVoid = false;
        // while (!theVoid) {
        //     theVoid = moveSand(res.caveMap, sx, sy);
        // }

        let p = {};
        while (p.x !== sx || p.y !== sy) {
            p = moveSand(res.caveMap, sx, sy);
            console.log("rested", p)
        }

        console.log("DONE");
        let cnt = 0;
        res.caveMap.forEach(row => row.forEach(point => {
            if (point === 2) {
                cnt++
            }
        }))
        console.log("res", cnt);

    });



