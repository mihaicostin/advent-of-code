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

print(max_size)
print(lines)
surface = np.zeros((max_size + 1, max_size + 1))

intersection_points = 0
for line in lines:
    if line[0] == line[2]:
        # same X, vertical line, inc all points on the line
        for y in range(min(line[1], line[3]), max(line[1], line[3]) + 1):
            surface[line[0]][y] += 1
            if surface[line[0]][y] == 2:
                intersection_points += 1
    else:
        if line[1] == line[3]:
            # same Y, horizontal line
            for x in range(min(line[0], line[2]), max(line[0], line[2]) + 1):
                surface[x][line[1]] += 1
                if surface[x][line[1]] == 2:
                    intersection_points += 1

print(intersection_points)