# fish = [3,4,3,1,2]
# days = 18

# part 1
# days = 80

# part 2
days = 256
schools = [0 for x in range(9)]

with open("day6.txt") as f:
    file_lines = f.readlines()
    numbers_str = file_lines[0].split(",")
    fish = list(map(lambda x: int(x.strip()), numbers_str))
    for x in fish:
        schools[x] += 1

new_fish = []
for day in range(days):
    new_school = [0 for x in range(9)]
    for d in range(0, 8):
        new_school[d] = schools[d + 1]
    new_school[6] += schools[0]
    new_school[8] += schools[0]
    schools = new_school

print(schools)
print(sum(schools))
