
def hex_to_bin(hex_char):
    return "{0:04b}".format(int('0x'+hex_char, 0))


def parse_input(file_name):
    bits = ""
    with open(file_name) as f:
        file_lines = f.readlines()
        for line in file_lines:
            for ch in line.strip():
                bits += hex_to_bin(ch)
    return bits


def read_header(array, index):
    version = array[index] + array[index + 1] + array[index + 2]
    type_id = array[index+3] + array[index + 4] + array[index + 5]
    v = int(version, 2)
    id = int(type_id, 2)
    return index + 6, v, id


def read_literal(array, index):
    idx = index
    bits = ''
    cnt = True
    while cnt:
        cnt = array[idx] == '1'
        bits += ''.join(array[idx + 1 : idx + 5])
        idx = idx + 5
    return idx, int(bits, 2)


def to_decimal(array):
    bits = ''.join(array)
    return int(bits, 2)


def read_package(input, idx):
    if idx + 6 > len(input):
        return -1
    header = read_header(input, idx)
    idx, version, h_id = header
    bit_len = 0
    pack_nr = 0
    value = -1
    if h_id == 4:
        r = read_literal(input, idx)
        idx = r[0]
        value = r[1]
    else:
        len_type_id = input[idx]
        idx = idx + 1
        if len_type_id == '0':
            bit_len = to_decimal(input[idx: idx + 15])
            idx = idx + 15
        else:
            pack_nr = to_decimal(input[idx: idx + 11])
            idx = idx + 11
    return {"idx": idx, "version": version, "next_len": bit_len, "pack_nr": pack_nr, "value": value}


input = parse_input('day16.txt')
print("input size", len(input))
res = read_package(input, 0)
i = res['idx']
sum_v = res['version']
while res['idx'] < len(input):
    res = read_package(input, res['idx'])
    if res == -1:
        break;
    sum_v += res['version']

print(sum_v)