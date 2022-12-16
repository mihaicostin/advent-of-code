const {readFile} = require('fs/promises');
const inFile = "day15.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

const range = (sx, sy, bx, by) => {
    let xDist = Math.abs(sx - bx);
    let yDist = Math.abs(sy - by);
    return xDist + yDist;
}

const intersects = (p1, p2) => {
    if (p1.start >= p2.start && p1.start <= p2.end) {
        return true;
    }
    if (p2.start >= p1.start && p2.start <= p1.end) {
        return true;
    }
    return false;
}

const merge = (p1, p2) => {
    return {start: Math.min(p1.start, p2.start), end: Math.max(p1.end, p2.end)};
}

const collapse = (intervals) => {
    return intervals.reduce((prev, current) => {
        let match = prev.find(it => intersects(it, current));
        if (match !== undefined) {
            let n = merge(match, current);
            match.start = n.start;
            match.end = n.end;
        } else {
            prev.push(current);
        }
        return prev;
    }, []);
}

//part 1
parseInput(inFile)
    .then(lines => {
        return lines.map(line => {
            let pattern = /(-?\d+)/g;
            let [sx, sy, bx, by] = line.match(pattern).map(el => parseInt(el));
            return {sx, sy, bx, by}
        })
    })
    .then(points => processLine(points, 2_000_000))
    .then(res => {
        let sum = res.reduce((prev, current) => {
            return prev + Math.abs(current.end - current.start) + 1;
        }, 0);
        console.log("part1", sum - 1); //account for the beacon already present on the line.
    });


//part 2
parseInput(inFile)
    .then(lines => {
        return lines.map(line => {
            let pattern = /(-?\d+)/g;
            let [sx, sy, bx, by] = line.match(pattern).map(el => parseInt(el));
            return {sx, sy, bx, by}
        })
    })
    .then(points => {
        let allLines = [];
        for (let y = 0; y <= 4_000_000; y++) {
            let intervals = processLine(points, y);
            if (intervals.length === 1) {
                let it = intervals[0];
                if (it.start > 0 || it.end < 4_000_000) {
                    console.log("possible", y, it.start, it.end);
                }
            }
            if (intervals.length === 2) {
                console.log("2!", y, intervals);
            }

            allLines.push(intervals);
        }
        return allLines;
    })
    .then(res => {
        //console.log(res);
        console.log(3385922 * 4000000 + 2671045);
    });

const processLine = (points, targetY) => {
    let intervals = points.map(p => {
        let maxDistance = range(p.sx, p.sy, p.bx, p.by);
        let yDistanceToTarget = Math.abs(targetY - p.sy);
        if (yDistanceToTarget <= maxDistance) {
            let xMax = Math.abs(maxDistance - yDistanceToTarget);
            let start = p.sx - xMax;
            let end = p.sx + xMax;
            return {start, end}
        }
    }).filter(el => el !== undefined);
    intervals = collapseAll(intervals);
    return intervals;
}

const collapseAll = (intervals) => {
    let oldLen = intervals.length;
    let collapsed = true;
    while (collapsed) {
        intervals = collapse(intervals);
        collapsed = intervals.length !== oldLen;
        oldLen = intervals.length;
    }
    return intervals;
}

