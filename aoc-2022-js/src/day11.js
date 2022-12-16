
const divs = [3, 5, 2, 13, 11, 17, 19, 7];

class Item {
    constructor(worry) {
        this.divisibles = divs.map(d => worry % d);
        console.log("item created", this.divisibles);
    }

    operation(opStr) {
        let elems = opStr.split(" ");
        let op = elems[0];
        if (op === "+") {
            let val = parseInt(elems[1]);
            this.divisibles = this.divisibles.map((el, idx) => (el + val) % divs[idx]);
        }
        if (op === "*") {
            let val = parseInt(elems[1]);
            this.divisibles = this.divisibles.map((el, idx) => (el * val) % divs[idx])
        }
        if (op === "pow") {
            this.divisibles = this.divisibles.map((el, idx) => (el * el) % divs[idx])
        }
    }

    isDivisibleBy(nr) {
        return this.divisibles[divs.indexOf(nr)] === 0;
    }

}

class Monkey {

    constructor({items, operation, divisibleBy, throwTo}) {
        this.items = items.map(w => new Item(w));
        this.divisibleBy = divisibleBy;
        this.throwTo = throwTo;
        this.operation = operation;
        this.inspected = 0;
    }

    test(item) {
        return item.isDivisibleBy(this.divisibleBy);
    }

    inspect(item) {
        this.inspected++;
        item.operation(this.operation);
        //part 1
        //item.worry = Math.floor(item.worry / 3);
        if (this.test(item)) {
            return {to: this.throwTo[0], item: item};
        } else {
            return {to: this.throwTo[1], item: item}
        }
    }

    add(item) {
        this.items.push(item);
    }

    getFirst() {
        return this.items.shift();
    }

}


const monkeys = [
    new Monkey({
        items: [99, 67, 92, 61, 83, 64, 98],
        operation: "* 17",
        divisibleBy: 3,
        throwTo: [4, 2]
    }),
    new Monkey({
        items: [78, 74, 88, 89, 50],
        operation: "* 11",
        divisibleBy: 5,
        throwTo: [3, 5]
    }),
    new Monkey({
        items: [98, 91],
        operation: "+ 4",
        divisibleBy: 2,
        throwTo: [6, 4]
    }),
    new Monkey({
        items: [59, 72, 94, 91, 79, 88, 94, 51],
        operation: "pow",
        divisibleBy: 13,
        throwTo: [0, 5]
    }),
    new Monkey({
        items: [95, 72, 78],
        operation: "+ 7",
        divisibleBy: 11,
        throwTo: [7, 6]
    }),
    new Monkey({
        items: [76],
        operation: "+ 8",
        divisibleBy: 17,
        throwTo: [0, 2]
    }),
    new Monkey({
        items: [69, 60, 53, 89, 71, 88],
        operation: "+ 5",
        divisibleBy: 19,
        throwTo: [7, 1]
    }),
    new Monkey({
        items: [72, 54, 63, 80],
        operation: "+ 3",
        divisibleBy: 7,
        throwTo: [1, 3]
    })
]

let round = 0;
while (round < 10000) {
    for (let monkey of monkeys) {
        let item = monkey.getFirst();
        while (item !== undefined) {
            let res = monkey.inspect(item);
            monkeys[res.to].add(res.item);
            item = monkey.getFirst();
        }
    }
    round++;
}

console.time("part2");
monkeys.sort((a, b) => b.inspected - a.inspected)
console.log(monkeys);
console.log(monkeys[0].inspected * monkeys[1].inspected);
console.timeEnd("part2");