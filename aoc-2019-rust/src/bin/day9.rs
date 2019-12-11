use std::{
    fs::File,
    io::{BufReader, prelude::*},
    path::Path,
};

use std::collections::HashMap;

use num::bigint::{ToBigInt, BigInt};
use num::ToPrimitive;
use num::Zero;

//const zero : BigInt = Zero::zero();

fn main() {
    let result = read_comma_int_array("src/bin/day9.txt");

    println!("Input array :{:?}\n", result);

    let mut mem: HashMap<i32, BigInt> = HashMap::new();
    let mut index = 0;
    for elem in result {
        mem.insert(index, elem.to_bigint().unwrap());
        index = index + 1;
    }
    mem.insert(-1, 0.to_bigint().unwrap());

    println!("MEM :{:?}\n", mem);

    run_program(&mut mem, 2);
}

fn read_comma_int_array(filename: impl AsRef<Path>) -> Vec<i32> {
    let file = File::open(filename);
    let mut buf_reader = BufReader::new(file.unwrap());
    let mut contents = String::new();
    let _err = buf_reader.read_to_string(&mut contents);

    contents.split(",")
        .map(|l| l.parse::<i32>().expect("not a number?"))
        .collect()
}


fn run_program(mem: &mut HashMap<i32, BigInt>, input_value: i32) -> &mut HashMap<i32, BigInt> {

    let mut base = 0;
    let mut i = 0;

    //let mut result = input_vec.to_vec();
    let mut op_code = 0;

    while op_code != 99 {
        op_code = read_mem_int(mem, i);

        println!("Processing a new op code :{:?}\n", op_code);

        let mut modes = vec![];
        let mut mode1 = 0;
        let mut mode2 = 0;
        let mut mode3 = 0;

        if op_code > 100 {
            //this op code has param mode
            let op_digit = op_code % 10;
            op_code = op_code / 100;

            while op_code > 0 {
                let digit = op_code % 10;
                modes.push(digit);
                op_code = op_code / 10;
            }

            println!("Modes:{:?}\n", modes);
            if modes.len() > 0 {
                mode1 = modes[0];
            }
            if modes.len() > 1 {
                mode2 = modes[1];
            }
            if modes.len() > 2 {
                mode3 = modes[2];
            }

            op_code = op_digit;
        }


        match op_code {
            1 => {
                //add
                println!("Add\n");
                let mut index3 = read_mem_int(mem, i + 3);
                if mode3 == 2 {
                    index3 = index3 + base;
                }
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                let value = param1 + param2;

                println!("param1: {:?}\n", param1);
                println!("param2: {:?}\n", param2);
                println!("result: {:?}\n", value);
                println!("res index: {:?}\n", index3);

                write_mem(mem, index3, value);

                i = i + 4;
            }
            99 => {
                print!("Done");
                break;
            }
            2 => {
                //multiply
                println!("Multiply\n");
                let mut index3 = read_mem_int(mem, i + 3);
                if mode3 == 2 {
                    index3 = index3 + base;
                }
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                let value = param1 * param2;

                println!("param1: {:?}\n", param1);
                println!("param2: {:?}\n", param2);
                println!("result: {:?}\n", value);
                println!("res index: {:?}\n", index3);

                write_mem(mem, index3, value);

                i = i + 4;
            }
            3 => {
                //read input
                let mut out_index = read_mem_int(mem, i + 1);
                if mode1 == 2 {
                    out_index = out_index + base;
                }
                write_mem(mem, out_index, to_bigint(input_value));

                println!("Mem: {:?}\n", mem);
                i = i + 2;
            }
            4 => {
                //write input
                let value =  get_param(mem, i + 1, mode1, base);
                println!("Out Value: {:?}\n", value);
                i = i + 2;
            }
            5 => {
                //jump if true
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                if *param1 != to_bigint(0) {
                    i = to_int(param2);
                } else {
                    i = i + 3;
                }
            }
            6 => {
                //jump if false
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                if *param1 == to_bigint(0) {
                    i = to_int(param2);
                } else {
                    i = i + 3;
                }
            }
            7 => {
                //less than
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                let param3 = get_param(mem, i + 3, 1, base);
                let mut idx =  to_int(param3);
                if mode3 == 2 {
                    idx = idx + base;
                }

                if param1 < param2 {
                    write_mem(mem, idx, to_bigint(1));
                } else {
                    write_mem(mem, idx, to_bigint(0));
                }
                i = i + 4;
            }
            8 => {
                //equals
                let param1 = get_param(mem, i + 1, mode1, base);
                let param2 = get_param(mem, i + 2, mode2, base);
                let param3 = get_param(mem, i + 3, 1, base);
                let mut idx = to_int(param3);
                if mode3 == 2 {
                    idx = idx + base;
                }

                if param1 == param2 {
                    write_mem(mem, idx, to_bigint(1));
                } else {
                    write_mem(mem, idx, to_bigint(0));
                }
                i = i + 4;
            }
            9 => {
                //increment base
                let param1 = get_param(mem, i + 1, mode1, base);
                base = base + to_int(param1);
                println!("New base {:?}", base);
                i = i + 2;
            }

            _ => {
                println!("What opcode is this ?! {:?}", op_code);
                break;
            }
        }
    }


    return mem;
}


fn get_param(mem: &HashMap<i32, BigInt>, index: i32, mode: i32, base: i32) -> &BigInt {
    if mode == 0 {
        //position mode
        let mem_val = read_mem(mem, index);
        let new_idx = to_int(mem_val);
        let value = read_mem(mem, new_idx);
        return value;
    } else if mode == 1 {
        //immediate mode
        return read_mem(mem, index);
    } else if mode == 2 {
        //relative mode
        let mem_val = read_mem(mem, index);
        let new_idx = base + to_int(mem_val);
        return read_mem(mem, new_idx);
    }
    println!("Unknown mode {:?}", mode);
    return read_mem(mem, 0);
}


fn to_bigint(val: i32) -> BigInt {
    val.to_bigint().unwrap()
}

fn to_int(val: &BigInt) -> i32 {
    val.to_i32().unwrap()
}


fn write_mem(mem: &mut HashMap<i32, BigInt>, index: i32, value: BigInt) {
    mem.insert(index, value);
}


fn read_mem(mem: &HashMap<i32, BigInt>, index: i32) -> &BigInt {
    if index < 0 {
        println!("Invalid address !")
    }
    let option = mem.get(&index);
    if option.is_none() {
        return  mem.get(&-1).unwrap();
    }
    return mem.get(&index).unwrap();
}

fn read_mem_int(mem: &HashMap<i32, BigInt>, index: i32) -> i32 {
    let val = read_mem(mem, index);
    to_int(&val)
}