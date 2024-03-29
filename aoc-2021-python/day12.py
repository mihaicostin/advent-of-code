import networkx as nx


def parse_input(file_name):
    G = nx.Graph()
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            nodes = line.strip().split("-")
            G.add_edge(nodes[0], nodes[1])
    return G


graph = parse_input("day12.txt")

print(graph.number_of_nodes())
print(graph.number_of_edges())


def find_paths(g, visited, current):
    if current == "end":
        return [visited + [current]]
    if current in visited and current.islower():
        return []
    all_paths = []
    for node in g.neighbors(current):
        paths = find_paths(g, visited + [current], node)
        non_empty_paths = list(filter(lambda p: len(p) != 0, paths))
        all_paths = all_paths + non_empty_paths
    return all_paths

paths = find_paths(graph, [], "start")
print(len(paths))


def find_paths_second(g, visited, single, visited_second, current):

    if current == "end":
        return [visited + [current]]

    if current in visited and current.islower():
        if current == single:
            if visited_second:
                return []
            else:
                visited_second = True
        else:
            return []

    all_paths = []
    for node in g.neighbors(current):
        paths = find_paths_second(g, visited + [current], single, visited_second, node)
        non_empty_paths = list(filter(lambda p: len(p) != 0, paths))
        all_paths = all_paths + non_empty_paths
    return all_paths


total_paths = []
for node in graph.nodes:
    if node.islower() and node != 'start' and node != 'end':
        p = find_paths_second(graph, [], node, False, "start")
        str_paths = list(map(lambda x: "|".join(x), p))
        total_paths = total_paths + str_paths

print(len(set(total_paths)))