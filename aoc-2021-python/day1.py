# count the number of times a depth measurement increases from the previous measurement.
# (There is no measurement before the first measurement.)

count = 0
with open("day1.txt") as f:
    lines = f.readlines()
    prev = -1
    for line in lines:
        if (int(line) > prev) & (prev != -1):
            count = count + 1
        prev = int(line)
print(count)

# part 2

secondCount = 0


def window_sum(array, idx):
    if idx > 1:
        return array[idx] + array[idx - 1] + array[idx - 2]
    return -1


with open("day1.txt") as f:
    lines = f.readlines()
    numbers = list(map(lambda el: int(el), lines))
    for idx, val in enumerate(numbers):
        if idx > 2:
            a = window_sum(numbers, idx - 1)
            b = window_sum(numbers, idx)
            if b > a:
                secondCount += 1

print(secondCount)