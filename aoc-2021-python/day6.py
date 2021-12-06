
days = 80
with open("day6.txt") as f:
    file_lines = f.readlines()
    numbers_str = file_lines[0].split(",")
    fish = list(map(lambda x: int(x.strip()), numbers_str))

# fish = [3,4,3,1,2]
# days = 18

new_fish = []
for day in range(days):
    for idx, f in enumerate(fish):
        if f == 0:
            fish[idx] = 6
            new_fish.append(8)
        else:
            fish[idx] = f - 1
    fish = fish + new_fish
    new_fish = []

print(len(fish))