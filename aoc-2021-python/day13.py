import numpy as np


def parse_input(file_name):
    dots = []
    folds = []
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            if len(line.strip()) > 0:
                coordinates = line.strip().split(",")
                if len(coordinates) == 2:
                    dot = (int(coordinates[0]), int(coordinates[1]))
                    dots.append(dot)
                else:
                    fold_instr = line.replace("fold along", "").split("=")
                    fold = (fold_instr[0].strip(), int(fold_instr[1]))
                    folds.append(fold)
        return dots, folds


def fold_x(p, value):
    new_p = np.zeros((len(p), value))
    for y in range(0, len(p)):
        for x in range(0, value):
            new_p[y][x] = p[y][x] or p[y][2*value+1 - x -1]
    return new_p

def fold_y(p, value):
    new_p = np.zeros((value, len(p[0])))
    for y in range(0, value):
        for x in range(0, len(p[0])):
            new_p[y][x] = p[y][x] or p[2*value+1 - y -1][x]
    return new_p

input = parse_input("day13.txt")
# print(input[0])
folds = input[1]
width = 0
height = 0
for f in folds:
    if f[0] == 'x' and width == 0:
        width = f[1] * 2 + 1
    if f[0] == 'y' and height == 0:
        height = f[1] * 2 + 1

print(width, height)

paper = np.zeros((height, width))
for x in range(0, width):
    for y in range(0, height):
        if (x, y) in input[0]:
            paper[y][x] = 1

for f in folds:
    (axis, val) = f
    if axis == 'x':
        paper = fold_x(paper, val)
    if axis == 'y':
        paper = fold_y(paper, val)


#print("result", np.sum(paper)) #after first iteration.
print(paper) # and then to excel..
