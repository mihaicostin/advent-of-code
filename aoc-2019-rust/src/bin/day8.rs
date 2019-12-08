
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn main() {
§    let layer_size = 25 * 6;
    let result = read_file("src/bin/day8.txt");
    let size = result.len();
    println!("Result:\n{:?}", result);
    println!("Result:\n{:?}", result.len());

//    let mut min_zero_digits = -1;
//    let mut zero_digits = 0;
//    let mut one_digits =0 ;
//    let mut two_digits = 0;
//    let mut index = 0;
//    let mut layer = 0;
//    let mut answer = 0;
//    for digit in result {
//
//        match digit {
//            0 => zero_digits = zero_digits + 1,
//            1 => one_digits = one_digits + 1,
//            2 => two_digits = two_digits + 1,
//            _ => println!("whatever, that's not an image!")
//        }
//
//        if index != 0 && ( (index + 1) % layer_size == 0  || index == size - 1 ){
//            //another layer is filled
//            println!("Layer {:?} size {:?}", layer, index);
//            println!("Zero  {:?}", zero_digits);
//            println!("One   {:?}", one_digits);
//            println!("Two   {:?}", two_digits);
//
//            if min_zero_digits == -1 || zero_digits < min_zero_digits {
//                min_zero_digits = zero_digits;
//                answer = one_digits * two_digits;
//            }
//
//            zero_digits = 0;
//            one_digits = 0;
//            two_digits = 0;
//            layer = layer + 1;
//
//        }
//
//        index = index + 1;
//
//    }
//    println!("Result:\n{:?}", answer);


    let mut message: Vec<u32> = vec![];
//    let mut msg: String = "".to_string();
    let mut message_size = 0;

    while message_size < layer_size {

        let mut index = message_size;
        while index < size && *result.get(index).unwrap() == 2 {
            index = index + layer_size;
        }
        //println!("Index :\n{:?}", index);
        let value = result.get(index).unwrap();
        //println!("Element:\n{:?}", value);

        message.push(*value);
        //msg.push_str(&value.to_string());

//        if value == 1 {
//            print!("{:?}");
//        } else if value == 0 {
//
//        }

        print!("{:?}, ", value);

        if (message_size  + 1) % 25 == 0 {
            println!();
        }

        message_size = message_size + 1;
    }







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

