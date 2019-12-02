
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};

fn main() {
    let result = lines_from_file("src/bin/day1.txt");
    println!("Result:\n{:?}", result);
}


fn lines_from_file(filename: impl AsRef<Path>) -> i32 {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|l| l.expect("Could not parse line").parse::<i32>().expect("not a number?")/3 - 2)
        .sum()
}