def hex_to_bin(hex_char):
    return "{0:04b}".format(int('0x' + hex_char, 0))


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
    type_id = array[index + 3] + array[index + 4] + array[index + 5]
    v = int(version, 2)
    id = int(type_id, 2)
    return index + 6, v, id


def read_literal(array, index):
    idx = index
    bits = ''
    cnt = True
    while cnt:
        cnt = array[idx] == '1'
        bits += ''.join(array[idx + 1: idx + 5])
        idx = idx + 5
    return idx, int(bits, 2)


def to_decimal(array):
    bits = ''.join(array)
    return int(bits, 2)


def read_package(input, idx):
    header = read_header(input, idx)
    idx, version, h_id = header
    bit_len = 0
    pack_nr = 0
    value = -1
    my_len = 6
    subpackages = []
    computed_value = 0
    if h_id == 4:
        r = read_literal(input, idx)
        my_len += r[0] - idx
        idx = r[0]
        value = r[1]
        computed_value = value
    else:
        len_type_id = input[idx]
        idx = idx + 1
        my_len += 1
        if len_type_id == '0':
            bit_len = to_decimal(input[idx: idx + 15])
            idx = idx + 15
            my_len += 15
            read_len = 0
            start_idx = idx
            while read_len < bit_len:
                subpackage = read_package(input, start_idx)
                subpackages.append(subpackage)
                start_idx = subpackage['idx']
                read_len += subpackage["my_len"]
            idx = start_idx
            my_len += bit_len
        else:
            pack_nr = to_decimal(input[idx: idx + 11])
            idx = idx + 11
            my_len += 11
            cnt = pack_nr
            start_idx = idx
            while cnt > 0:
                subpackage = read_package(input, start_idx)
                start_idx = subpackage['idx']
                subpackages.append(subpackage)
                my_len += subpackage["my_len"]
                cnt = cnt - 1
            idx = start_idx

        if h_id == 0:
            # sum
            computed_value = 0
            for subpackage in subpackages:
                computed_value = computed_value + subpackage["computed_value"]
        if h_id == 1:
            # product
            computed_value = 1
            for subpackage in subpackages:
                computed_value = computed_value * subpackage["computed_value"]
        if h_id == 2:
            # min
            computed_value = -1
            for subpackage in subpackages:
                if subpackage["computed_value"] < computed_value or computed_value == -1:
                    computed_value = subpackage["computed_value"]
        if h_id == 3:
            # max
            computed_value = -1
            for subpackage in subpackages:
                if subpackage["computed_value"] > computed_value or computed_value == -1:
                    computed_value = subpackage["computed_value"]
        if h_id == 5:
            # gt
            if subpackages[0]["computed_value"] > subpackages[1]["computed_value"]:
                computed_value = 1
            else:
                computed_value = 0
        if h_id == 6:
            # lt
            if subpackages[0]["computed_value"] < subpackages[1]["computed_value"]:
                computed_value = 1
            else:
                computed_value = 0
        if h_id == 7:
            # lt
            if subpackages[0]["computed_value"] == subpackages[1]["computed_value"]:
                computed_value = 1
            else:
                computed_value = 0

    return {"idx": idx,
            "type_id": h_id,
            "version": version,
            "my_len": my_len,
            "next_len": bit_len,
            "pack_nr": pack_nr,
            "value": value,
            "computed_value": computed_value,
            "subpackages": subpackages}


input = parse_input('day16.txt')
res = read_package(input, 0)
print("result", res["computed_value"])
