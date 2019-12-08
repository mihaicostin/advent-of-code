use std::{
    fs::File,
    io::{BufReader, prelude::*},
    path::Path,
};

fn main() {
    let result = lines_from_file("src/bin/day6.txt");

    println!("Input :{:?}\n", result);

    let cc = count("COM".to_string(), 0,&result);
    println!("Input :{:?}\n", cc);

    let path1 = path("YOU".to_string(), &result);
    let max_path1: &(String, i32) = path1.get(path1.len() - 1).unwrap();
    println!("Path 1 :{:?}\n", path1);
    println!("Max 1 :{:?}\n", max_path1);

    let path2 = path("SAN".to_string(), &result);
    let max_path2: &(String, i32) = path2.get(path2.len() - 1).unwrap();

    println!("Path 2 :{:?}\n", path2);
    println!("Max 2:{:?}\n", max_path2);

    let mut min_steps = -1;
    for elem1 in &path1 {
        for elem2 in &path2 {

            if elem1.0 == elem2.0 {
                let steps = (max_path1.1 - elem1.1) + (max_path2.1 - elem2.1) - 2;
                if min_steps == -1 || min_steps > steps {
                    min_steps = steps;
                    println!("Intersection Points :{:?} {:?} {:?}\n", elem1, elem2, min_steps);
                }
            }

        }
    }

    println!("Steps :{:?}\n", min_steps);

}


fn lines_from_file(filename: impl AsRef<Path>) -> Vec<(String, String)> {
    let file = File::open(filename).expect("no such file");
    let buf = BufReader::new(file);
    buf.lines()
        .map(|line| {
            let element = line.expect("Could not parse line");
            let so = element.split(")");
            let vec: Vec<&str>  = so.collect::<Vec<&str>>();
            return (vec[0].to_string(), vec[1].to_string());
        })
        .collect()
}


fn count(start: String, current: i32, list : &Vec<(String, String)>) -> i32 {

    let mut c = 0;
    for pair in list {
        if pair.0 == start {
            c = c + count(pair.1.to_string(), current + 1, list)
        }
    }

    return current + c ;
}


fn path(start: String, list : &Vec<(String, String)>) -> Vec<(String, i32)> {

    if start == "COM" {
        return vec![("COM".to_string(), 1)];
    }

    for pair in list {
        if pair.1 == start {
            let mut vec = path(pair.0.to_string(), list);
            let last: &(String, i32) = vec.get(vec.len() - 1).expect("should have an element");
            vec.push((start.to_string(), last.1 + 1));
            return vec;
        }
    }

    return vec![];
}
