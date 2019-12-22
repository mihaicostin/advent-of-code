#[derive(Debug)]
struct Moon {
    index: i32,

    x: i32,
    y: i32,
    z: i32,

    vx: i32,
    vy: i32,
    vz: i32,
}


impl Moon {
    fn apply_velocity(&mut self) {
        self.x = self.x + self.vx;
        self.y = self.y + self.vy;
        self.z = self.z + self.vz;
    }

    fn change_velocity(&mut self, other: &mut Moon) {
        if self.x < other.x {
            self.vx = self.vx + 1;
            other.vx = other.vx - 1;
        } else if self.x > other.x {
            self.vx = self.vx - 1;
            other.vx = other.vx + 1;
        }

        if self.y < other.y {
            self.vy = self.vy + 1;
            other.vy = other.vy - 1;
        } else if self.y > other.y {
            self.vy = self.vy - 1;
            other.vy = other.vy + 1;
        }

        if self.z < other.z {
            self.vz = self.vz + 1;
            other.vz = other.vz - 1;
        } else if self.z > other.z {
            self.vz = self.vz - 1;
            other.vz = other.vz + 1;
        }
    }

    fn pot(&self) -> i32 {
        ((self.x as f64).abs() + (self.y as f64).abs() + (self.z as f64).abs()) as i32
    }

    fn kin(&self) -> i32 {
        ((self.vx as f64).abs() + (self.vy as f64).abs() + (self.vz as f64).abs()) as i32
    }

    fn total(&self) -> i32 {
        self.kin() * self.pot()
    }
}


fn main() {
    /*
     <x=-7, y=17, z=-11>
     <x=9, y=12, z=5>
     <x=-9, y=0, z=-4>
     <x=4, y=6, z=0>
    */

    let mut m1 = Moon {
        index: 0,
        x: -7,
        y: 17,
        z: -11,
        vx: 0,
        vy: 0,
        vz: 0,
    };

    let mut m2 = Moon {
        index: 1,
        x: 9,
        y: 12,
        z: 5,
        vx: 0,
        vy: 0,
        vz: 0,
    };

    let mut m3 = Moon {
        index: 2,
        x: -9,
        y: 0,
        z: -4,
        vx: 0,
        vy: 0,
        vz: 0,
    };

    let mut m4 = Moon {
        index: 3,
        x: 4,
        y: 6,
        z: 0,
        vx: 0,
        vy: 0,
        vz: 0,
    };


    let mut step = 0;


    while step <= 1000 {

        m1.change_velocity(&mut m2);
        m1.change_velocity(&mut m3);
        m1.change_velocity(&mut m4);

        m2.change_velocity(&mut m3);
        m2.change_velocity(&mut m4);

        m3.change_velocity(&mut m4);


        m1.apply_velocity();
        m2.apply_velocity();
        m3.apply_velocity();
        m4.apply_velocity();

        let energy = m1.total() + m2.total() + m3.total() + m4.total();

        println!("STEP {:?}", step);
        println!("Moon 1 : {:?} ", m1);
        println!("Moon 2 : {:?} ", m2);
        println!("Moon 3 : {:?} ", m3);
        println!("Moon 4 : {:?} ", m4);
        println!("Energy : {:?} ", energy);
        println!();

        step = step + 1;
    }
}


