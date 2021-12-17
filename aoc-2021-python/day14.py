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


def generate_pair(pair, rules):
    for rule in rules:
        if rule[0] == pair:
            return [pair[0] + rule[1], rule[1] + pair[1]]
    return [pair]


# def generate(polymer, rules):
#     new_polymer = []
#     for idx, element in enumerate(polymer):
#         if idx < len(polymer) - 1:
#             new_polymer.append(element + generate_next(element + polymer[idx + 1], rules))
#         else:
#             new_polymer.append(element)
#     return new_polymer
#

def generate(poly_pairs, rules):
    new_poly_pairs = defaultdict(int)
    for pair in poly_pairs:
        current_count = poly_pairs[pair]
        res = generate_pair(pair, rules)
        for r in res:
            new_poly_pairs[r] += current_count
    return new_poly_pairs

input = parse_input("day14.txt")
print(input)
polymer = input[0] + "#"
rules = input[1]

poly_pairs = defaultdict(int)
for idx, element in enumerate(polymer):
    if idx < len(polymer) - 1:
        poly_pairs[element+polymer[idx + 1]] += 1

print(poly_pairs)

for i in range(0, 40):
    poly_pairs = generate(poly_pairs, rules)
    print(poly_pairs)

ch_count = defaultdict(int)
for p in poly_pairs:
    ch_count[p[0]] += poly_pairs[p]

print(ch_count)
print(max(ch_count.values()) - min(ch_count.values()))
