def parse_input(file_name):
    result = []
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            line_nr = list(map(lambda x: int(x), line.strip()))
            result.append(line_nr)
    return result


octopuses = parse_input("day11.txt")


def increment_energy(octopuses):
    for y in range(0, len(octopuses)):
        for x in range(0, len(octopuses[y])):
            octopuses[y][x] += 1


def valid_idx(matrix, x, y):
    return (x >= 0) and (y >= 0) and (y < len(matrix)) and (x < len(matrix[y]))


def flash(octopuses):
    flashes = 0
    for y in range(0, len(octopuses)):
        for x in range(0, len(octopuses[y])):
            if octopuses[y][x] > 9:
                flashes += 1
                octopuses[y][x] = -1
                for y_idx in [y - 1, y, y + 1]:
                    for x_idx in [x - 1, x, x + 1]:
                        if not (y_idx == y and x_idx == x) and valid_idx(octopuses, x_idx, y_idx) and octopuses[y_idx][x_idx] != -1:
                            octopuses[y_idx][x_idx] += 1
    return flashes


def set_to_zero(octopuses):
    for y in range(0, len(octopuses)):
        for x in range(0, len(octopuses[y])):
            if octopuses[y][x] == -1:
                octopuses[y][x] = 0


def all_flashed(octopuses):
    for line in octopuses:
        for octo in line:
            if octo != 0:
                return False
    return True


total_flashes = 0
idx = 0
while True:
    idx += 1
    increment_energy(octopuses)
    step_flashes = 0
    while True:
        step_flashes = flash(octopuses)
        total_flashes += step_flashes
        if step_flashes == 0:
            break
    set_to_zero(octopuses)
    if all_flashed(octopuses):
        print("Part 2. after nr of iterations: ", idx)
        break

print("part 1", total_flashes)
