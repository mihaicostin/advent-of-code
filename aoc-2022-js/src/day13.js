const {readFile} = require('fs/promises');
const inFile = "day13.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

const cmp = (msg1, msg2) => {
    for (let i = 0; i < Math.max(msg1.length, msg2.length); i++) {
        let l = msg1[i];
        let r = msg2[i];
        if (l === undefined && r !== undefined) {
            //left ran out of items
            return -1;
        } else if (l !== undefined && r === undefined) {
            //right ran out of items
            return +1;
        } else if (!Number.isInteger(l) && !Number.isInteger(r)) {
            //they are both lists
            let res = cmp(l, r);
            if (res === 0) {
                continue;
            } else {
                return res;
            }
        } else if (Number.isInteger(l) && Number.isInteger(r)) {
            //they are both integers
            if (l === r) {
                continue;
            } else {
                return l - r;
            }
        } else if (Number.isInteger(l)) {
            //one is integer, the other is list
            let res = cmp([l], r);
            if (res === 0) {
                continue;
            } else {
                return res;
            }
        } else if (Number.isInteger(r)) {
            let res = cmp(l, [r]);
            if (res === 0) {
                continue;
            } else {
                return res;
            }
        }
    }
    return 0;
}

const parse = (msg) => {
    return eval(msg);
}


parseInput(inFile)
    .then(lines => {
        return lines.reduce((prev, current) => {
            if (current !== '') {
                let lastVal = prev[prev.length - 1];
                if (lastVal.left === undefined) {
                    lastVal.left = parse(current);
                } else if (lastVal.right === undefined) {
                    lastVal.right = parse(current);
                } else {
                    prev.push({left: parse(current)});
                }
            }
            return prev;
        }, [{}]);
    })
    .then(res => {
        let sum = 0;
        res.forEach((el, idx) => {
            let c = cmp(el.left, el.right);
            if (c <= 0) {
                sum += (idx + 1);
            }
            console.log(`index ${idx} = ${c}`);
        });
        console.log("p1", sum);
    });


parseInput(inFile)
    .then(lines => {
        return lines
            .filter(line => line.length > 0)
            .map(line => {
                return parse(line)
            });
    })
    .then(res => {
        res.push([[2]]);
        res.push([[6]]);
        let resStr = res.sort(cmp).map(el => JSON.stringify(el));
        let idx1 = resStr.indexOf('[[2]]') + 1;
        let idx2 = resStr.indexOf('[[6]]') + 1;
        console.log("p2", idx1, idx2, idx1 * idx2);
    });