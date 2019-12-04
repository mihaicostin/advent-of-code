

fn main() {

    let mut count = 0;

    for index in 236491..713787 {

        let mut number = index;

        let mut next_digit = -1;
        let mut increasing = true;
        let mut same= false;
        let mut digs = vec![];
        let mut same_count = 0;

        while number > 0 {

            let digit = number % 10;

            if next_digit != -1 {

                if next_digit == digit {
                    same_count = same_count + 1;
                } else {
                    if same_count == 1 {
                        same = true;
                    }
                    same_count = 0;
                }

                if digit > next_digit {
                    increasing = false;
                }
            }
            digs.push(digit);
            number = number / 10;
            next_digit = digit;
        }

        if same_count == 1 {
            same = true;
        }

        if same && increasing {
            count = count + 1;
            //println!("Result index WITHOUT :\n{:?}", index);
        }

    }

    println!("Result :\n{:?}", count);
}

