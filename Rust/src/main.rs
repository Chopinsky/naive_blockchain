extern crate crypto;

use std::env;
use std::path::Path;
use std::fs::File;
use std::io::Read;

use crypto::sha2::Sha256;
use crypto::digest::Digest;

static DEFAULT_WALLET_PEM: &'static str = "./keys/public.pem";
static DEFAULT_WALLET_DER: &'static str = "./keys/public.der";

fn main() {
    let args: Vec<String> = env::args().collect();

    let der = load_wallet_der(args);
    let mut count = 0;
    for x in &der {
        print!("{} ", format!("{:03}", x));
        count += 1;
        if (count % 16) == 0 {
            println!();
        }
    }

    // let pem = load_wallet_pem(args);
    // print!("{}", pem);
}

fn load_wallet_der(args: Vec<String>) -> Vec<u8> {
    let path;
    if args.len() > 1 {
        path = Path::new(&args[1]);
    } else {
        path = Path::new(&DEFAULT_WALLET_DER);
    }

    let mut file = File::open(path).expect("file not found");
    let mut contents: Vec<u8> = Vec::new();

    file.read_to_end(&mut contents)
        .expect("can't read contents");

    return contents;
}

fn load_wallet_pem(args: Vec<String>) -> String {
    let path;
    if args.len() > 1 {
        path = Path::new(&args[1]);
    } else {
        path = Path::new(&DEFAULT_WALLET_PEM);
    }

    let mut file = File::open(path).expect("file not found");
    let mut contents = String::new();

    file.read_to_string(&mut contents)
        .expect("something went wrong reading the file");

    return contents;
}

fn hash_worker(message: &str) -> String {
    let mut digest = Sha256::new();
    digest.input_str(&message);
    return digest.result_str();
}
