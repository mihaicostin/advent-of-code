
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn main() {
    let mut result = read_comma_int_array("src/bin/day2.txt");

    result[1] = 12;
    result[2] = 2;

    let r = process_array(result);

    println!("Result:\n{:?}", r[0]);
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



fn process_array(mut input: Vec<i32>) -> Vec<i32> {

    let mut i = 0 ;

    while i < input.len() {

        let opCode = input[i];


        if opCode == 99 {
            break;
        }
        else if opCode == 1 {
            //add
            let index3 = input[i + 3] as usize;

            input[index3] = input[input[i + 1] as usize] + input[input[i + 2] as usize]

        }
        else if opCode == 2 {
            //multiply]
            let index3 = input[i + 3] as usize;
            input[index3] = input[input[i+1] as usize] * input[input[i+2] as usize]
        }

        i = i + 4
    }


    return input;
}