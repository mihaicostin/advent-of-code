const {readFile} = require('fs/promises');
const inFile = "day10.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

parseInput(inFile)
    .then(lines => {
        let valIdx = [20, 60, 100, 140, 180, 220];
        let cycle = 1;
        let signal = 1;
        let sum = 0;
        lines.forEach(line => {
            console.log('signal', cycle, signal);
            if (valIdx.indexOf(cycle) > -1) {
                console.log("searched for", cycle, signal, signal * cycle, sum)
                sum += (signal * cycle);
            }

            if (line.trim() === "noop") {
                cycle++;
            } else {
                let elems = line.split(" ");
                let add = parseInt(elems[1]);
                cycle++;
                if (valIdx.indexOf(cycle) > -1) {
                    console.log("searched for", cycle, signal, signal * cycle, sum)
                    sum += (signal * cycle);
                }
                cycle++;
                signal += add;
            }
        });
        return sum;
    })
    .then(res => console.log(res));

const initScreen = (w, h) => {
    let sc = [];
    for (let j = 0; j < h; j++) {
        let row = [];
        for (let i = 0; i < w; i++) {
            row.push(" ");
        }
        sc.push(row);
    }
    return sc;
}


const addToScreen = (screen, x, y, val) => {
    screen[y][x] = val;
}

const process = (cycle, regX, screen) => {
    let x = (cycle - 1) % 40;
    let y = Math.ceil(cycle / 40) - 1;

    if (x === regX || x === regX - 1 || x === regX + 1) {
        addToScreen(screen, x, y, "#");
    } else {
        addToScreen(screen, x, y, ".");
    }
}

parseInput(inFile)
    .then(lines => {
        let cycle = 1;
        let signal = 1;
        let screen = initScreen(40, 6);

        lines.forEach(line => {

            process(cycle, signal, screen);

            if (line.trim() === "noop") {
                cycle++;
            } else {
                let elems = line.split(" ");
                let add = parseInt(elems[1]);
                cycle++;

                process(cycle, signal, screen);

                cycle++;
                signal += add;
            }
        });
        return screen;
    })
    .then(res => {
        for (let i = 0; i < 6; i++) {
            console.log(JSON.stringify(res[i]));
        }
    });