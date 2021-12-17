from collections import defaultdict


def parse_input(file_name):
    with open(file_name) as f:
        file_lines = f.readlines()
        initial_value = file_lines[0].strip()
        del file_lines[0]
        transformations = []
        for line in file_lines:
            strip_line = line.strip()
            if len(strip_line) > 0:
                parts = strip_line.split("->")
                insert = parts[0].strip()
                transformations.append((insert, parts[1].strip()))
        return initial_value, transformations


def generate_next(pair, rules):
    for rule in rules:
        if rule[0] == pair:
            return rule[1]
    return ""


def generate(polymer, rules):
    new_polymer = []
    for idx, element in enumerate(polymer):
        if idx < len(polymer) - 1:
            new_polymer.append(element + generate_next(element + polymer[idx + 1], rules))
        else:
            new_polymer.append(element)
    return new_polymer


input = parse_input("day14.txt")
print(input)

polymer = input[0]
rules = input[1]

for i in range(0, 10):
    polymer = generate(polymer, rules)
    polymer = ''.join(polymer)

ch_count = defaultdict(int)
for ch in polymer:
    ch_count[ch] += 1

print(ch_count)
print(max(ch_count.values()) - min(ch_count.values()))
