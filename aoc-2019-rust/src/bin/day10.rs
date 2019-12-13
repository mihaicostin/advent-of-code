use std::{
    fs::File,
    io::{BufReader, prelude::*},
    path::Path,
};

#[derive(Debug)]
struct Map {
    data: Vec<u32>,
    w: i32,
    h: i32,
}

fn main() {
    let result = parse_input("src/bin/day10.txt");

    println!("Input array :{:?}\n", result);

    let mut max: i32 = -1;

    for y in 0..result.h {
        for x in 0..result.w {

            if at(&result, x, y) == 1 {
                println!("Asteroid at {:?},{:?}", x,y);
                let mut tan = vec![];

                for y1 in 0..result.h {
                    for x1 in 0..result.w {
                        if (x != x1 || y != y1) && at(&result, x1, y1) == 1 {

                            let tx: i32 = (x1 - x) as i32;
                            let ty: i32 = (y1 - y) as i32;

                            let atan = (ty as f64).atan2(tx as f64);
                            println!("ATan2 {:?}", atan);
                            if !tan.contains(&atan) {
                                tan.push(atan);
                            }
                        }

                    }
                }

                let nr = tan.len()  as i32;
                println!("Can see {:?} asteroids", nr);
                if max ==-1 || max < nr {
                    max = nr;
                }

            }
        }
    }

    println!("MAX {:?}", max);

}


fn at(map: &Map, x:i32, y:i32) -> u32{
    let index = ((y * map.w) + x) as usize;
    return map.data[index];
}

fn parse_input(filename: impl AsRef<Path>) -> Map {
    let mut out = vec![];
    let mut w: i32 = 0;
    let mut h: i32 = 0;
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .for_each(|l| {
            let a_line = l.expect("Could not parse line");
            w = 0;
            for ch in a_line.chars() {
                if ch == '.' {
                    out.push(0);
                } else if ch == '#' {
                    out.push(1);
                } else {
                    println!("Unknown char in map {:?}", ch);
                }
                w = w + 1;
            }
            h = h + 1;
        });

    Map { data: out, w, h }
}


