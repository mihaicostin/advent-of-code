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
    let mut station_x = 0;
    let mut station_y = 0;

    let mut station_tan = vec![];

    for y in 0..result.h {
        for x in 0..result.w {
            if at(&result, x, y) == 1 {
                let mut tan = vec![];
                let mut pos = vec![];

                for y1 in 0..result.h {
                    for x1 in 0..result.w {
                        if (x != x1 || y != y1) && at(&result, x1, y1) == 1 {
                            let tx: i32 = (x1 - x) as i32;
                            let ty: i32 = (y1 - y) as i32;

                            let atan = (ty as f64).atan2(tx as f64);
                            if !tan.contains(&atan) {
                                tan.push(atan);
                            }
                            pos.push((x1, y1, atan))
                        }
                    }
                }

                let nr = tan.len() as i32;
                if max == -1 || max < nr {
                    max = nr;
                    station_x = x;
                    station_y = y;
                    station_tan = pos;
                }
            }
        }
    }

    println!("MAX {:?}, at x = {:?}, y = {:?}", max, station_x, station_y);
    println!("All others {:?}", station_tan);

    station_tan.sort_by(|a, b| {
        if a.2 != b.2 {
            return a.2.partial_cmp(&b.2).unwrap();
        } else {
            let atx: i32 = (a.0 - station_x) as i32;
            let aty: i32 = (a.1 - station_y) as i32;

            let btx: i32 = (b.0 - station_x) as i32;
            let bty: i32 = (b.1 - station_y) as i32;

            if atx == 0 {
                return bty.partial_cmp(&aty).unwrap();
            } else if atx > 0 {
                return atx.partial_cmp(&btx).unwrap();
            } else {
                return btx.partial_cmp(&atx).unwrap();
            }
        }
    });

    println!("SORTED all others {:?}", station_tan);

    let mut starting_index = 0;
    for (idx, &point) in station_tan.iter().enumerate() {
        if point.0 == station_x && point.1 < station_y {
            println!("Starting point {:?}", point);
            starting_index = idx;
            break;
        }
    }

    println!("Starting index {:?}", starting_index);

    let mut destroyed: Vec<(i32,i32,f64)> = vec![];
    let mut index  = starting_index;

    while destroyed.len() < 201 {

        let mut zapped: (i32,i32,f64) = *(station_tan.get(index).unwrap());

        if (destroyed.len() == 0 || zapped.2 != destroyed.last().unwrap().2) && (!destroyed.contains(&zapped)) {
            destroyed.push(zapped);
        }
        index = index + 1;

        if index >= station_tan.len() {
            index  = 0;
        }

    }

    println!("Destroyed 200 : {:?}", destroyed.get(199).unwrap());
}


fn at(map: &Map, x: i32, y: i32) -> u32 {
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


