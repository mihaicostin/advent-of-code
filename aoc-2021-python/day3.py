import numpy as np


def compress(array):
    cnt = 0
    for nr in array:
        if nr == 1:
            cnt += 1
        if nr == 0:
            cnt -= 1
    if cnt >= 0:
        return 1
    else:
        return 0


num_len = 12
readings = np.zeros((1000, num_len))


with open("day3.txt") as f:
    lines = f.readlines()
    for ly, line in enumerate(lines):
        for rx, ch in enumerate(line.strip()):
            readings[ly][rx] = int(ch)

gamma_str = ""
epsilon_str = ""
for idx in range(num_len):
    result = compress(readings[:, idx])
    inverse = result * (-1) + 1
    gamma_str += str(result)
    epsilon_str += str(inverse)

gamma = int(gamma_str, 2)
epsilon = int(epsilon_str, 2)
print(gamma * epsilon)


#part 2

oxygen_readings = np.copy(readings)
for idx in range(num_len):
    if len(oxygen_readings) > 1:
        col = oxygen_readings[:, idx]
        most_freq = compress(col)
        oxygen_readings = oxygen_readings[np.where(col == most_freq)]
o_result = oxygen_readings.dot(1 << np.arange(oxygen_readings.shape[-1] - 1, -1, -1))

co2_readings = np.copy(readings)
for idx in range(num_len):
    if len(co2_readings) > 1:
        col = co2_readings[:, idx]
        most_freq = compress(col) * (-1) + 1
        co2_readings = co2_readings[np.where(col == most_freq)]
co2_result = co2_readings.dot(1 << np.arange(co2_readings.shape[-1] - 1, -1, -1))

print(o_result * co2_result)