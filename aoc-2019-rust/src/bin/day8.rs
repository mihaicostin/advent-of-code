
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn main() {
    let result = read_file("src/bin/day8.txt");
    let size = result.len();
    println!("Result:\n{:?}", result);
    println!("Result:\n{:?}", result.len());
    //25 x 6

    let layer_size = 25 * 6;
    let mut min_zero_digits = -1;

    let mut zero_digits = 0;
    let mut one_digits =0 ;
    let mut two_digits = 0;
    let mut index = 0;
    let mut layer = 0;

    let mut answer = 0;

    for digit in result {

        match digit {
            0 => zero_digits = zero_digits + 1,
            1 => one_digits = one_digits + 1,
            2 => two_digits = two_digits + 1,
            _ => println!("whatever, that's not an image!")
        }

        if index != 0 && ( (index + 1) % layer_size == 0  || index == size - 1 ){
            //another layer is filled
            println!("Layer {:?} size {:?}", layer, index);
            println!("Zero  {:?}", zero_digits);
            println!("One   {:?}", one_digits);
            println!("Two   {:?}", two_digits);

            if min_zero_digits == -1 || zero_digits < min_zero_digits {
                min_zero_digits = zero_digits;
                answer = one_digits * two_digits;
            }

            zero_digits = 0;
            one_digits = 0;
            two_digits = 0;
            layer = layer + 1;

        }

        index = index + 1;

    }
    println!("Result:\n{:?}", answer);


}

fn read_file(filename: impl AsRef<Path>) -> Vec<u32> {

    let file = File::open(filename);
    let mut buf_reader = BufReader::new(file.unwrap());
    let mut contents = String::new();
    let _err = buf_reader.read_to_string(&mut contents);

    let mut result: Vec<u32> = vec![];
    let string = contents.to_string();

    for b in string.bytes() {
        result.push((b as char).to_digit(10).unwrap());
    }

    result

}

