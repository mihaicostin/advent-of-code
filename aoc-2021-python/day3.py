import numpy as np


def compress(array):
    cnt = 0
    for nr in array:
        if nr == 1:
            cnt += 1
        if nr == 0:
            cnt -= 1
    if cnt > 0:
        return 1
    else:
        return 0


readings = np.zeros((12, 1000))

with open("day3.txt") as f:
    lines = f.readlines()
    for ly, line in enumerate(lines):
        for rx, ch in enumerate(line.strip()):
            readings[rx][ly] = int(ch)

readingsLen = len(readings)

gammaStr = ""
epsilonStr = ""
for col in readings:
    result = compress(col)
    inverse = result * (-1) + 1
    gammaStr += str(result)
    epsilonStr += str(inverse)

gamma = int(gammaStr, 2)
epsilon = int(epsilonStr, 2)
print(gamma * epsilon)
