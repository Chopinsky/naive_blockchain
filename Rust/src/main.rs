extern crate crypto;

use std::env;
use std::path::Path;
use std::fs::File;

use std::io::BufReader;
use std::io::BufRead;

use crypto::sha2::Sha256;
use crypto::digest::Digest;
// use openssl::crypto::pkey::PKey;

static DEFAULT_WALLET: &'static str = "./keys/public.pem";

fn main() {
    let args: Vec<String> = env::args().collect();
    let path;

    if args.len() > 1 {
        path = Path::new(&args[1]);
    } else {
        path = Path::new(&DEFAULT_WALLET);
    }

    let f = File::open(path).expect("file not found");
    let file = BufReader::new(&f);

    for (_, line) in file.lines().enumerate() {
        let l = line.unwrap();
        println!("{}", l);
    }

    // let mut contents = String::new();
    // file.read_to_string(&mut contents)
    //     .expect("can't read contents");
    //
    // println!("Content:\n{}", contents);
}

fn hash_worker(message: &str) -> String {
    let mut digest = Sha256::new();
    digest.input_str(&message);
    return digest.result_str();
}
