import numpy as np

points = {')': 3, ']': 57, '}': 1197, '>': 25137}
points_part2 = {')': 1, ']': 2, '}': 3, '>': 4}


def parse_input(file_name):
    with open(file_name) as f:
        file_lines = f.readlines()
        return list(map(lambda x: x.strip(), file_lines))


def get_points(illegal_char):
    return points[illegal_char]


def closing(ch):
    if ch == '(': return ')'
    if ch == '[': return ']'
    if ch == '<': return '>'
    if ch == '{': return '}'


input = parse_input("day10.txt")

stash = []

score = 0
score_part2 = []
valid_lines = []
for line in input:
    valid_line = True
    stash = []
    for ch in line:
        if ch == '<' or ch == '(' or ch == '[' or ch == '{':
            stash.append(ch)
        else:
            found = stash.pop()
            if (ch == '>' and found != '<') or (ch == '}' and found != '{') or (ch == ']' and found != '[') or (
                    ch == ')' and found != '('):
                score += get_points(ch)
                valid_line = False
                break
    if valid_line:
        valid_lines.append(line)
        print("st", stash)
        auto_score = 0
        while len(stash) > 0:
            ch = closing(stash.pop())
            auto_score = auto_score * 5 + points_part2[ch]
        score_part2.append(auto_score)

print("part1", score)
print("part2", np.median(np.array(score_part2)))
