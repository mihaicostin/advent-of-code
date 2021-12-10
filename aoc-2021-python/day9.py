def parse_input(file_name):
    result = []
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            line_nr = list(map(lambda x: int(x), line.strip()))
            # line_nr = [-1] + line_nr + [-1]
            result.append(line_nr)
        # empty_row = [-1] * len(result[0])
        # result = [empty_row] + result + [empty_row]
    return result


def valid_idx(matrix, x, y):
    return (x >= 0) and (y >= 0) and (y < len(matrix)) and (x < len(matrix[y]))


def is_local_min(matrix, y, x):
    val = matrix[y][x]
    for ny in [y - 1, y, y + 1]:
        for nx in [x - 1, x, x + 1]:
            if (not (nx == x and ny == y)) and valid_idx(matrix, nx, ny):
                if val >= matrix[ny][nx]:
                    return False
    return True


input = parse_input("day9.txt")

sum = 0
for y in range(0, len(input)):
    for x in range(0, len(input[y])):
        if is_local_min(input, y, x):
            risk = 1 + input[y][x]
            sum += risk

print("part 1", sum)


def visit(matrix, visited_points, global_visited, current):
    x = current[1]
    y = current[0]
    current_val = matrix[y][x]
    if x < 0 or y < 0 or y >= len(matrix) or x >= len(matrix[y]):
        return []
    if current in visited_points or current in global_visited:
        return []
    if current_val == 9:
        return []
    visited_points.append(current)
    for target_point in [(y - 1, x), (y + 1, x), (y, x - 1), (y, x + 1)]:
        nx = target_point[1]
        ny = target_point[0]
        if valid_idx(matrix, nx, ny):
            if matrix[ny][nx] > current_val and matrix[ny][nx] != 9:
                visited_points = visited_points + visit(matrix, visited_points, global_visited, (ny, nx))
    return list(set(visited_points))


gv = []
sizes = []
for y in range(0, len(input)):
    for x in range(0, len(input[y])):
        if is_local_min(input, y, x):
            visited_basin = visit(input, [], gv, (y, x))
            sizes.append(len(visited_basin))
            gv = gv + visited_basin

vals = sorted(sizes, reverse=True)
result = vals[0] * vals[1] * vals[2]
print("part 2", result)
