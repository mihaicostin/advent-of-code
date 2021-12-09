
def parse_input(file_name):
    result = []
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            parts = line.strip().split("|")
            input = parts[0].strip().split(" ")
            output = parts[1].strip().split(" ")
            result.append((input, output))
    return result

input = parse_input("day8.txt")

#part 1
# 1, 4, 7, and 8 -> 2, 4, 3, 7
sum = 0
for sample in input:
    for nr in sample[1]:
        nr_len = len(nr)
        if nr_len == 2 or nr_len == 3 or nr_len == 4 or nr_len == 7:
            sum += 1

print(sum)
