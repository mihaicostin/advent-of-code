import numpy as np

def to_numbers(file_name):
    with open(file_name) as f:
        file_lines = f.readlines()
    numbers_str = file_lines[0].split(",")
    return list(map(lambda x: int(x.strip()), numbers_str))


input = to_numbers("day7.txt")
#input = [16,1,2,0,4,2,7,1,2,14]
numbers = np.array(input)

mean = np.median(numbers)
sum = 0
for nr in input:
    sum += abs(nr - mean)

print(sum)