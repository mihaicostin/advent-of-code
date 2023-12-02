
map = {
    "one": '1',
    "1": '1',
    "two": '2',
    "2": '2',
    "three": '3',
    "3": '3',
    "four": '4',
    "4": '4',
    "five": '5',
    "5": '5',
    "six": '6',
    "6": '6',
    "seven": '7',
    "7": '7',
    "eight": '8',
    "8": '8',
    "nine": '9',
    "9": '9'
}


def lines():
    with open("day1.txt") as f:
        lines = f.readlines()
        return lines


def find_nr(buf):
    for key in map:
        if key in buf:
            return map[key]
    return ''


def find_start(row):
    buf = ""
    for el in row:
        buf += el
        nr = find_nr(buf)
        if len(nr) > 0:
            return nr
    return ""


def find_end(row):
    buf = ""
    for el in reversed(row):
        buf = el+buf
        nr = find_nr(buf)
        if len(nr) > 0:
            return nr
    return ""


def main():
    rows = lines()
    sum = 0
    for row in rows:
        print(row)
        nr = find_start(row) + "" + find_end(row)
        print("->",nr)
        sum += int(nr)
    print("sum =", sum)


if __name__ == "__main__":
    main()
