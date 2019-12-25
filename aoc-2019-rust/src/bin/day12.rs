use num::bigint::{BigInt, ToBigInt};
use num::Integer;

#[derive(Default)]
#[derive(Debug)]
struct Moon {
    init_x: i32,
    init_y: i32,
    init_z: i32,

    x: i32,
    y: i32,
    z: i32,

    vx: i32,
    vy: i32,
    vz: i32,

    steps_x: u32,
    steps_y: u32,
    steps_z: u32,
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

    fn same_x(&mut self) -> bool {
        return self.x == self.init_x && self.vx == 0;
    }

    fn same_y(&mut self) -> bool {
        return self.y == self.init_y && self.vy == 0;
    }

    fn same_z(&mut self) -> bool {
        return self.z == self.init_z && self.vz == 0;
    }

    fn save_step_x(&mut self, step: u32) {
        if self.steps_x == 0 {
            self.steps_x = step;
        }
    }
    fn save_step_y(&mut self, step: u32) {
        if self.steps_y == 0 {
            self.steps_y = step;
        }
    }
    fn save_step_z(&mut self, step: u32) {
        if self.steps_z == 0 {
            self.steps_z = step;
        }
    }

    fn reached_initial_position(&mut self) -> bool {
        return self.steps_x != 0 && self.steps_y != 0 && self.steps_z != 0;
    }

    fn lcm(&self) -> BigInt {
        let x = Moon::to_bigint(self.steps_x);
        let y = Moon::to_bigint(self.steps_y);
        let z = Moon::to_bigint(self.steps_z);

        let lcm = x.lcm(&y).lcm(&z);

        return lcm;
    }

    fn to_bigint(val: u32) -> BigInt {
        val.to_bigint().unwrap()
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
        x: -7,
        init_x: -7,
        y: 17,
        init_y: 17,
        z: -11,
        init_z: -11,
        ..Default::default()
    };

    let mut m2 = Moon {
        x: 9,
        init_x: 9,
        y: 12,
        init_y: 12,
        z: 5,
        init_z: 5,
        ..Default::default()
    };

    let mut m3 = Moon {
        x: -9,
        init_x: -9,
        y: 0,
        init_y: 0,
        z: -4,
        init_z: -4,
        ..Default::default()
    };

    let mut m4 = Moon {
        x: 4,
        init_x: 4,
        y: 6,
        init_y: 6,
        z: 0,
        init_z: 0,
        ..Default::default()
    };


//<x=-8, y=-10, z=0>
//<x=5, y=5, z=10>
//<x=2, y=-7, z=3>
//<x=9, y=-8, z=-3>
//    let mut m1 = Moon {
//        x: -8,
//        init_x: -8,
//        y: -10,
//        init_y: -10,
//        z: 0,
//        init_z: 0,
//        ..Default::default()
//    };
//
//    let mut m2 = Moon {
//        x: 5,
//        init_x: 5,
//        y: 5,
//        init_y: 5,
//        z: 10,
//        init_z: 10,
//        ..Default::default()
//    };
//
//    let mut m3 = Moon {
//        x: 2,
//        init_x: 2,
//        y: -7,
//        init_y: -7,
//        z: 3,
//        init_z: 3,
//        ..Default::default()
//    };
//
//    let mut m4 = Moon {
//        x: 9,
//        init_x: 9,
//        y: -8,
//        init_y: -8,
//        z: -3,
//        init_z: -3,
//        ..Default::default()
//    };

//<x=-1, y=0, z=2>
//<x=2, y=-10, z=-7>
//<x=4, y=-8, z=8>
//<x=3, y=5, z=-1>
//    let mut m1 = Moon {
//        x: -1,
//        init_x: -1,
//        y: 0,
//        init_y: 0,
//        z: 2,
//        init_z: 2,
//        ..Default::default()
//    };
//
//    let mut m2 = Moon {
//        x: 2,
//        init_x: 2,
//        y: -10,
//        init_y: -10,
//        z: -7,
//        init_z: -7,
//        ..Default::default()
//    };
//
//    let mut m3 = Moon {
//        x: 4,
//        init_x: 4,
//        y: -8,
//        init_y: -8,
//        z: 8,
//        init_z: 8,
//        ..Default::default()
//    };
//
//    let mut m4 = Moon {
//        x: 3,
//        init_x: 3,
//        y: 5,
//        init_y: 5,
//        z: -1,
//        init_z: -1,
//        ..Default::default()
//    };

    let mut step = 0;
    loop {
        step = step + 1;

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

        if step == 1000 {
            let energy = m1.total() + m2.total() + m3.total() + m4.total();
            println!("Energy : {:?} ", energy);
            //break;
        }

        if m1.same_x() && m2.same_x() && m3.same_x() && m4.same_x() {
            m1.save_step_x(step);
            m2.save_step_x(step);
            m3.save_step_x(step);
            m4.save_step_x(step);
            println!("X {:?}", step);
        }
        if m1.same_y() && m2.same_y() && m3.same_y() && m4.same_y() {
            m1.save_step_y(step);
            m2.save_step_y(step);
            m3.save_step_y(step);
            m4.save_step_y(step);
            println!("Y {:?}", step);
        }
        if m1.same_z() && m2.same_z() && m3.same_z() && m4.same_z() {
            m1.save_step_z(step);
            m2.save_step_z(step);
            m3.save_step_z(step);
            m4.save_step_z(step);
            println!("Z {:?}", step);
        }


        let m1_reached = m1.reached_initial_position();
        let m2_reached = m2.reached_initial_position();
        let m3_reached = m3.reached_initial_position();
        let m4_reached = m4.reached_initial_position();


        if m1_reached && m2_reached && m3_reached && m4_reached {
            let lcm_m1 = m1.lcm();
            let lcm_m2 = m2.lcm();
            let lcm_m3 = m3.lcm();
            let lcm_m4 = m4.lcm();

            let lcm_moons = lcm_m1.lcm(&lcm_m2).lcm(&lcm_m3).lcm(&lcm_m4);

            println!("Steps - {:#?}", lcm_moons.to_str_radix(10));

            break;
        }
    }
}



