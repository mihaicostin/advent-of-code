
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn main() {
    let mut result = read_comma_int_array("src/bin/day2.txt");

    let mut done = false;
    let mut a = 0;
    let mut b = 0;

    for a in 0..100 {
        for b in  0..100 {
            let mut input = result.to_vec();
            input[1] = a;
            input[2] = b;
            let r = process_array(input);
            if r[0] == 19690720 {
                println!("Result a:\n{:?}", 100* a + b);
            }
        }
    }
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


fn process_array(input: Vec<i32>) -> Vec<i32> {

    let mut i = 0 ;

    let mut result = input.to_vec();

    while i < result.len() {

        let op_code = result[i];


        if op_code == 99 {
            break;
        }
        else if op_code == 1 {
            //add
            let index3 = result[i + 3] as usize;

            result[index3] = result[result[i + 1] as usize] + result[result[i + 2] as usize]

        }
        else if op_code == 2 {
            //multiply]
            let index3 = result[i + 3] as usize;
            result[index3] = result[result[i+1] as usize] * result[result[i+2] as usize]
        }

        i = i + 4
    }


    return result;
}