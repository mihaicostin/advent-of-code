
use std::{
    fs::File,
    io::{prelude::*, BufReader},
    path::Path,
};



fn main() {
    let result = lines_from_file("src/bin/day3.txt");

    let p1array = process_array(&result[0]);
    let p2array = process_array(&result[1]);

    let mut min = 0;

    for p1 in &p1array {

        for p2 in &p2array {
            if eq(p1, p2) {
                if distance(p1) < min || min == 0 {
                    min = distance(p1);
                }
            }
        }
    }


    println!("Result a:\n{:?}", min);
}

#[derive(Debug)]
struct Point {
    x: i32,
    y: i32,
}

fn distance(p: &Point) -> i32 {
    return p.x.abs() + p.y.abs();
}


fn eq(p1: &Point, p2: &Point) -> bool {
    return p1.x == p2.x && p1.y == p2.y;
}


fn lines_from_file(filename: impl AsRef<Path>) -> Vec<Vec<String>> {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|l| {
            let line = l.expect("Could not parse line");
            let vec: Vec<String> = line.split(",").map(|l| l.to_string()).collect();
            return vec;
        })
        .collect()
}




fn process_array(input: &Vec<String>) -> Vec<Point>{

    let mut points: Vec<Point> = vec![];

    let mut x = 0;
    let mut y = 0;

    for op in input {
        let (direction, second) = op.split_at(1);
        let amount = second.parse::<i32>().expect("not a number?");

        if direction.starts_with('L') {
            for _step in 1..(amount+1) {
                x = x - 1;
                points.push(Point{x, y})
            }
        }

        if direction.starts_with('R') {
            for _step in 1..(amount+1) {
                x = x + 1;
                points.push(Point{x, y})
            }
        }

        if direction.starts_with('U') {
            for _step in 1..(amount+1) {
                y = y + 1;
                points.push(Point{x, y})
            }
        }

        if direction.starts_with('D') {
            for _step in 1..(amount+1) {
                y = y - 1;
                points.push(Point{x, y})
            }
        }

    }

    return points;


}