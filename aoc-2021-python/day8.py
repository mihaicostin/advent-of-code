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
# input = [(["acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"], ["cdfeb", "fcadb", "cdfeb", "cdbaf"])]

# part 1
# 1, 4, 7, and 8 -> 2, 4, 3, 7
sum = 0
for sample in input:
    for nr in sample[1]:
        nr_len = len(nr)
        if nr_len == 2 or nr_len == 3 or nr_len == 4 or nr_len == 7:
            sum += 1
print(sum)


# part 2
#  0
# 1 2
#  3
# 4 5
#  6

def contains(sublist, main_list):
    for i in sublist:
        if i not in main_list:
            return False
    return True


def deduction(line):
    result = [''] * 10
    print(line)
    sorted_line = list(map(lambda x: sorted(x), line))

    for nr in sorted_line:
        if len(nr) == 2:
            result[1] = nr
        if len(nr) == 3:
            result[7] = nr
        if len(nr) == 4:
            result[4] = nr
        if len(nr) == 7:
            result[8] = nr
    sorted_line.remove(result[1])
    sorted_line.remove(result[7])
    sorted_line.remove(result[4])
    sorted_line.remove(result[8])

    for nr in sorted_line:
        if len(nr) == 5:
            if contains(result[7], nr):
                result[3] = nr
    sorted_line.remove(result[3])

    for nr in sorted_line:
        if len(nr) == 6:
            if contains(result[3], nr):
                result[9] = nr
    sorted_line.remove(result[9])

    for nr in sorted_line:
        if len(nr) == 6:
            if contains(result[7], nr):
                result[0] = nr
    sorted_line.remove(result[0])

    for nr in sorted_line:
        if len(nr) == 6:
            result[6] = nr
    sorted_line.remove(result[6])

    for nr in sorted_line:
        if len(nr) == 5:
            if contains(nr, result[9]):
                result[5] = nr
    sorted_line.remove(result[5])

    for nr in sorted_line:
        if len(nr) == 5:
            result[2] = nr
    sorted_line.remove(result[2])

    return list(map(lambda x: ''.join(x), result))


sum = 0
for sample in input:
    res = deduction(sample[0])
    out = list(map(lambda x: ''.join(sorted(x)), sample[1]))
    number = (res.index(out[0]) * 1000) + (res.index(out[1]) * 100) + (res.index(out[2]) * 10) + res.index(out[3])
    print(number)
    sum += number

print("result", sum)
