use std::{
    fs::File,
    io::{BufReader, prelude::*},
    path::Path,
};
use std::collections::HashMap;

use num::bigint::{BigInt, ToBigInt};
use num::ToPrimitive;

#[derive(Debug)]
struct Program {
    mem: HashMap<i32, BigInt>,
    op_index: i32,
    base: i32,
    halted: bool,
    result: Vec<BigInt>,
}

impl Program {
    fn write_mem(&mut self, index: i32, value: BigInt) {
        self.mem.insert(index, value);
        return;
    }

    fn read_mem(&self, index: i32) -> &BigInt {
        if index < 0 {
            println!("Invalid address !")
        }
        let option = self.mem.get(&index);
        if option.is_none() {
            return self.mem.get(&-1).unwrap();
        }
        return self.mem.get(&index).unwrap();
    }

    fn read_mem_int(&self, index: i32) -> i32 {
        let val = self.read_mem(index);
        to_int(&val)
    }

    fn get_param(&self, index: i32, mode: i32, base: i32) -> &BigInt {
        if mode == 0 {
            //position mode
            let mem_val = self.read_mem(index);
            let new_idx = to_int(mem_val);
            let value = self.read_mem(new_idx);
            return value;
        } else if mode == 1 {
            //immediate mode
            return self.read_mem(index);
        } else if mode == 2 {
            //relative mode
            let mem_val = self.read_mem(index);
            let new_idx = base + to_int(mem_val);
            return self.read_mem(new_idx);
        }
        println!("Unknown mode {:?}", mode);
        return self.read_mem(0);
    }

    fn increment_base(&mut self, amount: i32) {
        self.base = self.base + amount;
    }

    fn write_output(&mut self, value: BigInt) {
        self.result.push(value);
    }

    fn get_output(&self) -> &Vec<BigInt> {
        return &self.result;
    }

    fn reset_output(&mut self) {
        self.result = Vec::new();
    }

    //---
    fn get_output_tuple(&self) -> (i32, i32) {
        return (to_int(&self.result.get(0).unwrap()), to_int(&self.result.get(1).unwrap()));
    }
}


#[derive(Debug)]
struct Hull {
    data: Vec<(i32, i32, i32)>
}

impl Hull {
    fn paint(&mut self, x: i32, y: i32, value: i32) {
        let mut pos: i32 = -1;
        for (idx, point) in self.data.iter().enumerate() {
            if point.0 == x && point.1 == y {
                pos = idx as i32;
            }
        }
        if pos != -1 {
            self.data.remove(pos as usize);
        }

        self.data.push((x, y, value));
    }


    fn get_paint(&self) -> &Vec<(i32, i32, i32)> {
        return &self.data;
    }

    fn get_paint_at(&self, x: i32, y: i32) -> i32 {
        for (idx, point) in self.data.iter().enumerate() {
            if point.0 == x && point.1 == y {
                return point.2;
            }
        }

        //starting point
        if x == 0 && y == 0 {
            println!("return 1 for start");
            return 1;
        }

        return 0;
    }
}


fn main() {
    let result = read_comma_int_array("src/bin/day11.txt");

    println!("Input array :{:?}\n", result);
    let mut mem: HashMap<i32, BigInt> = HashMap::new();
    let mut index = 0;
    for elem in result {
        mem.insert(index, elem.to_bigint().unwrap());
        index = index + 1;
    }
    mem.insert(-1, 0.to_bigint().unwrap());

    let mut program = Program {
        mem,
        op_index: 0,
        base: 0,
        halted: false,
        result: Vec::new(),
    };


    let mut hull = Hull {
        data: Vec::new()
    };
    let mut x = 0;
    let mut y = 0;

    let mut direction = 0; // 0 - up, 1 - right, 2 - down, 3 - left

    let mut min_x = 0;
    let mut min_y = 0;
    let mut max_x = 0;
    let mut max_y = 0;


    loop {

        run_program(&mut program, hull.get_paint_at(x, y));
        if program.halted {
            break;
        }

        let (color_to_paint, rotation ) = program.get_output_tuple();
        program.reset_output();

        hull.paint(x, y, color_to_paint);
        //println!("Painted :{:?} at {:?}, {:?} \n", out.0, x, y);


        if rotation == 0 {
            //left 90
            direction = direction - 1;
            if direction < 0 {
                direction = 3;
            }
        } else {
            //right 90
            direction = direction + 1;
            if direction > 3 {
                direction = 0;
            }
        }

        //advance
        if direction == 0 {
            y = y - 1;
        }
        if direction == 2 {
            y = y + 1;
        }
        if direction == 1 {
            x = x + 1;
        }
        if direction == 3 {
            x = x - 1;
        }

        if x >= max_x {
            max_x = x;
        }
        if x <= min_x {
            min_x = x;
        }

        if y <= min_y {
            min_y = y;
        }
        if y >= max_y {
            max_y = y;
        }
    }

    let mut painted = hull.get_paint();

    println!("Painted spaces :{:?}\n", painted.len());
    println!("Painted :{:?}\n", painted);
    println!("x :{:?} | {:?}\n", min_x, max_x);
    println!("y :{:?} | {:?}\n", min_y, max_y);

    for py in min_y..(max_y + 1) {
        print!("\n");
        for px in min_x.. (max_x + 1) {
            let col = hull.get_paint_at(px, py);
            if col == 0 {
                //black
                print!("  ");
            } else {
                print!("##");
            }
        }
    }
}

fn read_comma_int_array(filename: impl AsRef<Path>) -> Vec<i64> {
    let file = File::open(filename);
    let mut buf_reader = BufReader::new(file.unwrap());
    let mut contents = String::new();
    let _err = buf_reader.read_to_string(&mut contents);

    contents.split(",")
        .map(|l| {
            l.parse::<i64>().expect("not a number?")
        })
        .collect()
}


fn run_program(p: &mut Program, input_value: i32) -> &Program {
    let mut base = p.base;
    let mut i = p.op_index;
    let mut op_code = 0;

    while op_code != 99 {

        if p.get_output().len() == 2 {
            break;
        }

        op_code = p.read_mem_int(i);

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
            99 => {
                println!("HALT");
                p.halted = true;
                break;
            }

            1 => {
                //add
                let mut index3 = p.read_mem_int(i + 3);
                if mode3 == 2 {
                    index3 = index3 + base;
                }
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                let value = param1 + param2;

                p.write_mem(index3, value);

                i = i + 4;
            }

            2 => {

                //multiply
                let mut index3 = p.read_mem_int(i + 3);
                if mode3 == 2 {
                    index3 = index3 + base;
                }
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                let value = param1 * param2;

                p.write_mem(index3, value);

                i = i + 4;
            }

            3 => {
                //read input
                let mut out_index = p.read_mem_int(i + 1);
                if mode1 == 2 {
                    out_index = out_index + base;
                }
                p.write_mem(out_index, to_bigint(input_value));
                i = i + 2;
            }

            4 => {
                //write
                {
                    let value = p.get_param(i + 1, mode1, base);
                    p.write_output(value.clone());
                }
                i = i + 2;
            }

            5 => {
                //jump if true
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                if *param1 != to_bigint(0) {
                    i = to_int(param2);
                } else {
                    i = i + 3;
                }
            }

            6 => {
                //jump if false
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                if *param1 == to_bigint(0) {
                    i = to_int(param2);
                } else {
                    i = i + 3;
                }
            }

            7 => {
                //less than
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                let param3 = p.get_param(i + 3, 1, base);
                let mut idx = to_int(param3);
                if mode3 == 2 {
                    idx = idx + base;
                }

                if param1 < param2 {
                    p.write_mem(idx, to_bigint(1));
                } else {
                    p.write_mem(idx, to_bigint(0));
                }
                i = i + 4;
            }

            8 => {
                //equals
                let param1 = p.get_param(i + 1, mode1, base);
                let param2 = p.get_param(i + 2, mode2, base);
                let param3 = p.get_param(i + 3, 1, base);
                let mut idx = to_int(param3);
                if mode3 == 2 {
                    idx = idx + base;
                }

                if param1 == param2 {
                    p.write_mem(idx, to_bigint(1));
                } else {
                    p.write_mem(idx, to_bigint(0));
                }
                i = i + 4;
            }

            9 => {
                //increment base
                let param1 = p.get_param(i + 1, mode1, base);
                base = base + to_int(param1);
                i = i + 2;
            }

            _ => {
                println!("What opcode is this ?! {:?}", op_code);
                break;
            }
        }
    }

    p.base = base;
    p.op_index = i;

    return p;
}


fn to_bigint(val: i32) -> BigInt {
    val.to_bigint().unwrap()
}

fn to_int(val: &BigInt) -> i32 {
    val.to_i32().unwrap()
}
