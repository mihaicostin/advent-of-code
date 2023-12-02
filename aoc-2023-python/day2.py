import re


def lines(file):
    with open(file) as f:
        lines = f.readlines()
        return lines


def extract_nr(word):
    nrs = re.findall(r'\d+', word)
    return int(nrs[0])


def extract_word(word):
    return re.findall(r'\D+', word)


# 12 red cubes, 13 green cubes, and 14 blue
def part1():
    rows = lines("day2.txt")
    sum = 0
    for row in rows:
        game_info = row.split(":")
        game_id = extract_nr(game_info[0].strip())
        sets = game_info[1].strip().split(";")
        valid = True
        for s in sets:
            draws = s.strip().split(",")
            for draw in draws:
                nr = extract_nr(draw)
                color = extract_word(draw.strip())[0].strip()
                if color == "red" and nr > 12:
                    valid = False
                if color == "green" and nr > 13:
                    valid = False
                if color == "blue" and nr > 14:
                    valid = False
        if valid:
            sum += game_id
    print(sum)


def part2():
    rows = lines("day2.txt")
    sum = 0
    for row in rows:
        game_info = row.split(":")
        game_id = extract_nr(game_info[0].strip())
        sets = game_info[1].strip().split(";")
        power = {"red":0, "blue": 0, "green": 0}
        for s in sets:
            draws = s.strip().split(",")
            for draw in draws:
                nr = extract_nr(draw)
                color = extract_word(draw.strip())[0].strip()
                power[color] = max(power[color], nr)
        sum += (power["red"] * power["blue"] * power["green"])
    print("part2:", sum)


def main():
    part1()
    part2()


if __name__ == "__main__":
    main()
