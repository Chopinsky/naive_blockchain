extern crate crypto;
extern crate rand;

use std::io;
use crypto::sha2::Sha256;
use crypto::digest::Digest;

fn main() {
    println!("input your message to encrypto:");

    let mut guess = String::new();

    io::stdin().read_line(&mut guess)
        .expect("Failed to read line");

    let hash = hash_worker(&guess);
    println!("Hash: {}", hash);
}

fn hash_worker(message: &str) -> String {
    let mut digest = Sha256::new();
    digest.input_str(&message);
    return digest.result_str();
}
