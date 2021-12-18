import networkx as nx


def parse_input(file_name):
    G = nx.DiGraph()
    with open(file_name) as f:
        file_lines = f.readlines()
        # p2
        file_lines = list(map(lambda l: l.strip() + increment_right(l.strip()), file_lines))
        file_lines = file_lines \
                     + increment_down(file_lines, 1) \
                     + increment_down(file_lines, 2) \
                     + increment_down(file_lines, 3) \
                     + increment_down(file_lines, 4)

        for y, line in enumerate(file_lines):
            line_strip = line.strip()
            for x, v in enumerate(line_strip):
                if x + 1 < len(line_strip):
                    G.add_edge(f'{y}|{x}', f'{y}|{x + 1}', risk=int(line_strip[x + 1]))
                if y + 1 < len(file_lines):
                    G.add_edge(f'{y}|{x}', f'{y + 1}|{x}', risk=int(file_lines[y + 1][x]))
                if x > 0:
                    G.add_edge(f'{y}|{x}', f'{y}|{x-1}', risk=int(line_strip[x-1]))
                if y > 0:
                    G.add_edge(f'{y}|{x}', f'{y-1}|{x}', risk=int(file_lines[y-1][x]))
    return G


def increment(line, val):
    return ''.join(list(map(lambda l: str(int(l) + val) if int(l) + val < 10 else str(int(l) + val - 9), line)))


def increment_right(line):
    new_l = increment(line, 1) + increment(line, 2) + increment(line, 3) + increment(line, 4)
    return ''.join(new_l)


def increment_down(lines, val):
    new_lines = []
    for l in lines:
        new_lines.append(increment(l.strip(), val))
    return new_lines


graph = parse_input("day15.txt")
pp = nx.shortest_path(graph, source="0|0", target="499|499", weight="risk")
sum = 0
for idx, p in enumerate(pp):
    if idx + 1 < len(pp):
        sum += graph.get_edge_data(p, pp[idx + 1])["risk"]
print(sum)

