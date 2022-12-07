const {readFile} = require('fs/promises');
const inFile = "day7.txt";

const parseInput = async file => {
    let content = await readFile(file, "utf-8");
    return content
        .split("\n")
        .map(el => el.trim());
}

class Node {

    constructor({name, size, parent, isFolder}) {
        this.name = name;
        this.isFolder = isFolder
        this.size = size || 0;
        this.parent = parent;
        this.children = []
    }

    getChild(dirName) {
        return this.children.find(ch => ch.name === dirName);
    }

    addFile(fileName, size) {
        this.children.push(new Node({name: fileName, size, parent: this, isFolder: false}));
        //this.size += size;
    }

    addFolder(name) {
        this.children.push(new Node({name, parent: this, isFolder: true}))
    }

    updateSize() {
        this.children.forEach(ch => {
            if (ch.isFolder) {
                ch.updateSize();
            }
        });
        this.size = this.children.map(ch => ch.size).reduce((prev,curr) => prev + curr, 0);
    }

    getAllFolders() {
        let all = [];
        this.children.forEach(ch => {
            if (ch.isFolder) {
                all.push(ch);
                all.push(...ch.getAllFolders())
            }
        })
        return all;
    }
}


const parseTree = (lines) => {
    let root = new Node({name: "/", isFolder: true})

    lines.reduce((currentDir, line) => {
        if (line.startsWith("$")) {
            if (line.startsWith("$ cd")) {
                let elems = line.split(" ");
                let targetDir = elems[2];
                if (targetDir === "/") {
                    return root;
                } else if (targetDir === "..") {
                    return currentDir.parent;
                } else {
                    return currentDir.getChild(targetDir);
                }
            } else {
                //it's a 'ls'
                return currentDir;
            }
        } else {
            //we have some listings
            let elems = line.split(" ");
            if (elems[0] === "dir") {
                currentDir.addFolder(elems[1]);
            } else {
                currentDir.addFile(elems[1], parseInt(elems[0]))
            }
            return currentDir;
        }
    }, root);
    root.updateSize();
    return root;
}

//part 1
parseInput(inFile)
    .then(lines => parseTree(lines))
    .then(root => {
        return root.getAllFolders()
            .filter(ch => ch.size <=100000)
            .map(ch => ch.size)
            .reduce((prev,curr) => prev + curr, 0);
    })
    .then(res => console.log("part 1", res));

//part 2
parseInput(inFile)
    .then(lines => parseTree(lines))
    .then(root => {
        //70000000
        //30000000
        let usedSpace = root.size;
        let unUsedSpace = (70000000 - usedSpace);
        let requiredSpace = 30000000 - unUsedSpace;

        return root.getAllFolders()
            .sort((a,b) => a.size - b.size)
            .find(file => file.size >= requiredSpace).size;
    })
    .then(res => console.log(res));