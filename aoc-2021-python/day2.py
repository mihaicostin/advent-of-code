depth = 0
horizontal = 0
with open("day2.txt") as f:
    lines = f.readlines()
    for line in lines:
        elems = line.split(" ")
        com = elems[0]
        amount = int(elems[1])
        if com == "forward":
            horizontal += amount
        if com == "down":
            depth += amount
        if com == "up":
            depth -= amount
    print(depth * horizontal)

# part 2

depth = 0
horizontal = 0
aim = 0
with open("day2.txt") as f:
    lines = f.readlines()
    for line in lines:
        elems = line.split(" ")
        com = elems[0]
        amount = int(elems[1])
        if com == "forward":
            horizontal += amount
            depth += (aim * amount)
        if com == "down":
            aim += amount
        if com == "up":
            aim -= amount
    print(depth * horizontal)
