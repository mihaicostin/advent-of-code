const {readFile} = require('fs/promises');
const inFile = "day16.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

const INIT_POP_SIZE = 1_000_000;
const EVO_ITERATIONS = 250;
const OPEN_PROB = 0.9;
const MUTATE_PROB = 0.2;
const GENE_LEN = 31;


parseInput(inFile)
    .then(lines => {
            let items = lines.map(line => {
                let parts = line.split(";");
                let valvePattern = /Valve (\w+) has flow rate=(\d+)/;
                let v = parts[0].match(valvePattern);
                let dest = parts[1].replace("tunnels lead to valves", "")
                    .replace("tunnel leads to valve", "")
                    .split(",").map(el => el.trim())
                return {v: v[1], flow: parseInt(v[2]), tunnels: dest}
            });
            let genes = {};
            items.forEach(it => {
                genes[it.v] = it;
            })
            return genes;
        }
    )
    .then(genePool => {
        console.log(genePool);
        let population = [];
        console.time("genPop");
        for (let i = 0; i < INIT_POP_SIZE; i++) {
            population.push(genIndividual(genePool, GENE_LEN));
        }
        console.timeEnd("genPop");
        return {genePool, population};
    })
    .then(res => {

        let population = res.population;
        let genePool = res.genePool;
        population.sort((a, b) => b.fitness - a.fitness);


        let historicMax = 0;
        let maxChrom = undefined;
        let iteration = 0;

        console.log(iteration, population[0]);

        while (iteration < EVO_ITERATIONS) {
            let fittest = population.slice(0, population.length / 4);
            let newPop = [];
            while (newPop.length < fittest.length) {
                let p1 = getParent(fittest);
                let p2 = getParent(fittest);
                let offspring = crossover(genePool, p1, p2);
                if (offspring) {
                    // console.log(`p1 ${p1.fitness}, p2 ${p2.fitness}: offspring ${offspring.fitness}`);
                    newPop.push(offspring);
                }
            }

            //population = newPop;
            population = newPop.concat(fittest);
            for (let i = 0; i < INIT_POP_SIZE; i++) {
                population.push(genIndividual(genePool, GENE_LEN));
            }

            population.sort((a, b) => b.fitness - a.fitness);
            let best = population[0];
            if (historicMax < best.fitness) {
                historicMax = best.fitness;
                maxChrom = best;
            }
            console.log(iteration, best);
            iteration++;
        }

        console.log("historic max", maxChrom);
    })

const getParent = (pop) => {
   // let fitSum = pop.reduce((prev, current) => prev + current.fitness, 0);
   // let targetSum = rnd(fitSum);
   // let sum = 0;
   // for (const individual of pop) {
   //     sum += individual.fitness;
   //     if (sum >= targetSum) {
   //         return individual;
   //     }
   // }

    return getRandom(pop);
}


const rnd = (nr) => {
    return Math.floor(Math.random() * nr);
}

const getRandom = (array) => {
    return array[Math.floor(Math.random() * array.length)];
}

const getGene = (genePool, key) => {
    let searchFor = key;
    if (key.startsWith("o_")) {
        searchFor = key.replace("o_", "");
    }
    return genePool[searchFor];
}

const openValve = (v) => {
    return `o_${v}`;
}

const genIndividual = (genePool, size) => {
    let chromosome = [];
    let first = getGene(genePool, "AA");
    chromosome.push(first.v);

    while (chromosome.length < size) {
        let nextKey = getNextGeneKey(genePool, chromosome);
        chromosome.push(nextKey);
    }

    let fitness = calculateFitness(genePool, chromosome);
    return {val: chromosome, fitness: fitness};
}

const getNextGeneKey = (genePool, chromosome) => {
    let key = chromosome[chromosome.length - 1];
    let currentGene;
    if (key.startsWith("o_")) {
        //last is an action gene
        currentGene = getGene(genePool, key.replace("o_", ""));
    } else {
        currentGene = getGene(genePool, key);
    }
    let nextPossibles = currentGene.tunnels;

    if (!key.startsWith("o_")) {
        //maybe open this one
        if (currentGene.flow > 0 && chromosome.indexOf(openValve(currentGene.v)) === -1) {
            let prob = Math.random();
            if (prob < OPEN_PROB) {
                return openValve(currentGene.v);
            }
        }
    }
    return getRandom(nextPossibles);
}

const calculateFitness = (genePool, geneStr) => {
    let pres = 0;
    for (let idx = 0; idx < geneStr.length; idx++) {
        let g = geneStr[idx];
        if (g.startsWith("o_")) {
            let gene = getGene(genePool, g.replace("o_", ""));
            pres += (geneStr.length - idx - 1) * gene.flow;
        }
    }
    return pres;
}

const mutate = (genePool, geneStr) => {

    let mutated = geneStr.map(el => {
        let mutProb = Math.random();
        if (mutProb < MUTATE_PROB) {
            if (el.startsWith("o_")) {
                return undefined;
            }
        }
        return el;
    }).filter(el => el !== undefined);

    while (mutated.length < GENE_LEN) {
        let nextKey = getNextGeneKey(genePool, mutated);
        mutated.push(nextKey);
    }

    return mutated;
}


const crossover = (genePool, p1, p2) => {
    let offspring = [];
    let crossIdx = 1 + rnd(GENE_LEN - 2);
    let sub1 = p1.val.slice(0, crossIdx);
    let sub2 = p2.val.slice(crossIdx);

    let g1 = sub1[sub1.length - 1];
    let g2 = sub2[0];
    if (g2.startsWith('o_')) {
        return null; //won't join at 'open' action.
    }

    let gene1 = getGene(genePool, g1);

    if (gene1.tunnels.indexOf(g2) === -1) {
        return null; //can't link
    }

    offspring = offspring.concat(sub1);
    for (const el of sub2) {
        if (el.startsWith("o_") && sub1.indexOf(el) !== -1) {
            //already in p1, invalid gene
            continue;
        }
        offspring.push(el);
    }

    offspring = mutate(genePool, offspring);

    let fitness = calculateFitness(genePool, offspring);
    return {val: offspring, fitness};
}


//const pick