use std::{
    fs::File,
    io::{BufReader, prelude::*},
    path::Path,
};

fn main() {
    let result = read_comma_int_array("src/bin/day5.txt");
    process_array(result);
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


fn process_array(input_vec: Vec<i32>) -> Vec<i32> {

    let input_value = 5;

    let mut i = 0;

    let mut result = input_vec.to_vec();

    while i < result.len() {
        let mut op_code = result[i];
        println!("Input :{:?}\n", result);
        println!("Processing a new op code :{:?}\n", op_code);

        let mut modes = vec![];

        let mut mode1 = 0;
        let mut mode2 = 0;
        let mut mode3 = 0;

        if op_code > 100 {
            //this op code has param mode
            println!("this op code has param mode:{:?}\n", op_code);
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
                let index3 = result[i + 3] as usize;
//                println!("Output index for add :{:?}\n", index3);
                let param1 = get_param(&result, i + 1, mode1);
                let param2 = get_param(&result, i + 2, mode2);
//                println!("Param 1 :{:?}\n", param1);
//                println!("Param 2 :{:?}\n", param2);
                result[index3] = param1 + param2;
//                println!("Output result of add:{:?}\n", result[index3]);
                i = i + 4
            }
            99 => {
                print!("Done");
                break;
            }
            2 => {
                //multiply
                let index3 = result[i + 3] as usize;
                result[index3] = get_param(&result, i + 1, mode1) * get_param(&result, i + 2, mode2);
                i = i + 4
            }
            3 => {
                //read input
                let out_index = result[i + 1] as usize;
                let value = input_value;
                result[out_index] = value;
                i = i + 2
            }
            4 => {
                //write input
                let value = get_param(&result, i + 1, mode1);
//                println!("Result out value:{:?}\n", value);
                i = i + 2
            }
            5 => {
                //jump if true
                let param1 = get_param(&result, i + 1, mode1);
                let param2 = get_param(&result, i + 2, mode2);
                if param1 != 0 {
                    i = param2 as usize;
                } else {
                    i = i + 3;
                }
            }
            6 => {
                //jump if false
                let param1 = get_param(&result, i + 1, mode1);
                let param2 = get_param(&result, i + 2, mode2);
                if param1 == 0 {
                    i = param2 as usize;
                } else {
                    i = i + 3;
                }
            }
            7 => {
                //less than
                let param1 = get_param(&result, i + 1, mode1);
                let param2 = get_param(&result, i + 2, mode2);
                let param3 = get_param(&result, i + 3, 1);
                if param1 < param2 {
                    result[param3 as usize] = 1;
                } else {
                    result[param3 as usize] = 0;
                }
                i = i + 4;
            }
            8 => {
                //equals
                let param1 = get_param(&result, i + 1, mode1);
                let param2 = get_param(&result, i + 2, mode2);
                let param3 = get_param(&result, i + 3, 1);
                let index3 = param3 as usize;
//                println!("i :{:?}\n", i);
//                println!("mode :{:?}\n", mode1);
//                println!("Param 1 :{:?}\n", param1);
//                println!("Param 2 :{:?}\n", param2);
//                println!("Param 3 :{:?}\n", param3);
                if param1 == param2 {
                    result[index3] = 1;
                } else {
                    result[index3] = 0;
                }
//                println!("Output result :{:?}\n", result[index3]);
                i = i + 4;
            }

            _ => {
                println!("What opcode is this ?! {:?}", op_code);
                break;
            }
        }
    }


    return result;
}


fn get_param(vec: &Vec<i32>, index: usize, mode: i32) -> i32 {
    if mode == 0 {
        return vec[(vec[index] as usize)];
    } else {
        return vec[index];
    }
}