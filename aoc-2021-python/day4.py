import numpy as np

boards = []
with open("day4.txt") as f:
    lines = f.readlines()
    numbers_str = lines[0].split(",")
    numbers = list(map(lambda x: int(x), numbers_str))
    current_board = []
    for line in (lines[1:-1]):
        if line.strip():
            board_line_str = list(filter(lambda x: x.strip(), line.strip().split(" ")))
            board_line = list(map(lambda x: [int(x), 0], board_line_str))
            current_board.append(board_line)
        else:
            if current_board:
                boards.append(current_board)
                current_board = []
    # for ly, line in enumerate(lines):
    #     for rx, ch in enumerate(line.strip()):
    #         readings[ly][rx] = int(ch)


def check_marked(array):
    for el in array:
        if el[1] == 0:
            return False
    return True


def mark_number(board, nr):
    for board_row in board:
        for idx, el in enumerate(board_row):
            if el[0] == nr:
                el[1] = 1
                return check_marked(np.array(board)[:, idx]) | check_marked(board_row)


def sum_unmarked(board):
    sum = 0
    for board_row in board:
        for el in board_row:
            if el[1] == 0:
                sum += el[0]
    return sum


def run_bingo(numbers, boards):
    for bingo_nr in numbers:
        for board in boards:
            bingo = mark_number(board, bingo_nr)
            if bingo:
                result = sum_unmarked(board) * bingo_nr
                return result


print(run_bingo(numbers, boards))
