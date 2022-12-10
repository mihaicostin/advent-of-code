const {readFile} = require('fs/promises');
const inFile = "day9.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

const touching = ({hx, hy, tx, ty}) => {
    return Math.abs(hx - tx) <= 1 && Math.abs(hy - ty) <= 1;
}


const moveTail = ({hx, hy, tx, ty}) => {

    if (touching({hx, hy, tx, ty})) {
        return {tx, ty}
    }

    //same row
    if (hy === ty) {
        if (hx < tx) {
            tx--;
        } else if (hx > tx) {
            tx++;
        }
    }
    //same column
    else if (hx === tx) {
        if (hy < ty) {
            ty--;
        } else if (hy > ty) {
            ty++;
        }
    } else {
        if (tx < hx && ty < hy) {
            tx++;
            ty++;
        }
        else if (tx > hx && ty > hy) {
            tx--;
            ty--;
        }
        else if (tx < hx && ty > hy) {
            tx++;
            ty--;
        }
        else if (tx > hx && ty < hy) {
            tx--;
            ty++;
        }
    }

    return {tx, ty};
}

//part 1
console.time('part1')
parseInput(inFile)
    .then(lines => {
        let hx = 0, hy = 0, tx =0 , ty = 0;
        let positions = new Set();
        positions.add(`x${tx}y${ty}`);

        lines.forEach(line => {
            let elems = line.split(" ");
            let dir = elems[0];
            let amount = parseInt(elems[1]);
            while (amount > 0) {
                switch (dir) {
                    case 'L':
                        hx--;
                        break;
                    case 'R':
                        hx++;
                        break;
                    case 'U':
                        hy++;
                        break;
                    case 'D':
                        hy--;
                }
                let newPos = moveTail({hx, hy, tx, ty});
                tx = newPos.tx;
                ty = newPos.ty;
                positions.add(`x${tx}y${ty}`);
                amount--;
            }
        });
        return positions.size;
    })
    .then(res => {
        console.log("part1", res);
        console.timeEnd('part1');
    });


console.time('part2')
parseInput(inFile)
    .then(lines => {
        let hx = 0, hy = 0;
        let positions = new Set();
        let ropeX = [hx, hx, hx, hx, hx, hx, hx, hx, hx, hx]
        let ropeY = [hy, hy, hy, hy, hy, hy, hy, hy, hy, hy]
        positions.add(`x${hx}y${hy}`);

        lines.forEach(line => {
            let elems = line.split(" ");
            let dir = elems[0];
            let amount = parseInt(elems[1]);

            while (amount > 0) {
                switch (dir) {
                    case 'L':
                        ropeX[0]--;
                        break;
                    case 'R':
                        ropeX[0]++;
                        break;
                    case 'U':
                        ropeY[0]++;
                        break;
                    case 'D':
                        ropeY[0]--;
                }

                for (let idx = 0; idx < ropeX.length - 1; idx++) {
                    let newPos = moveTail({hx: ropeX[idx], hy: ropeY[idx], tx: ropeX[idx+1], ty: ropeY[idx+1]});
                    ropeX[idx+1] = newPos.tx;
                    ropeY[idx+1] = newPos.ty;
                }

                positions.add(`x${ropeX[ropeX.length-1]}y${ropeY[ropeY.length-1]}`);
                amount--;
            }
        });
        return positions.size;
    })
    .then(res => {
        console.log("part2", res);
        console.timeEnd('part2');
    });

