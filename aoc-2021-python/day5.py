import numpy as np

lines = []
max_size = 0
with open("day5.txt") as f:
    file_lines = f.readlines()
    for line in file_lines:
        splits = line.split("->")
        start = splits[0].strip().split(",")
        end = splits[1].strip().split(",")
        sx = int(start[0])
        sy = int(start[1])
        ex = int(end[0])
        ey = int(end[1])
        max_size = max(max_size, sx, ex, sy, ey)
        lines.append([sx, sy, ex, ey])

surface = np.zeros((max_size + 1, max_size + 1))

intersection_points = 0
for line in lines:
    [x1, y1, x2, y2] = line
    if x1 == x2:
        # same X, vertical line, inc all points on the line
        for y in range(min(y1, y2), max(y1, y2) + 1):
            surface[x1][y] += 1
            if surface[x1][y] == 2:
                intersection_points += 1
    else:
        if y1 == y2:
            # same Y, horizontal line
            for x in range(min(x1, x2), max(x1, x2) + 1):
                surface[x][y1] += 1
                if surface[x][y1] == 2:
                    intersection_points += 1

print("part 1", intersection_points)

# part 2
surface = np.zeros((max_size + 1, max_size + 1))
intersection_points = 0


def points(x1, y1, x2, y2):
    result = []
    if x2 < x1:
        return points(x2, y2, x1, y1)
    else:
        y = y1
        for x in range(x1, x2 + 1):
            result.append([x, y])
            if y1 < y2:
                y = y + 1
            else:
                y = y - 1
        return result


for line in lines:
    [x1, y1, x2, y2] = line
    if x1 == x2:
        # same X, vertical line, inc all points on the line
        for y in range(min(y1, y2), max(y1, y2) + 1):
            surface[x1][y] += 1
            if surface[x1][y] == 2:
                intersection_points += 1
    else:
        if y1 == y2:
            # same Y, horizontal line
            for x in range(min(x1, x2), max(x1, x2) + 1):
                surface[x][y1] += 1
                if surface[x][y1] == 2:
                    intersection_points += 1
        else:
            # diagonal line
            for p in points(x1, y1, x2, y2):
                surface[p[0]][p[1]] += 1
                if surface[p[0]][p[1]] == 2:
                    intersection_points += 1

print("part 2", intersection_points)
