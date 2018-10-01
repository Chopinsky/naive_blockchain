package com.naive_blockchain.java;

public class TransactionOutput {
    private String _address;
    private Double _amount;

    public TransactionOutput(String recipient, Double amount) {
        this._address = recipient;
        this._amount = amount;
    }

    @Override
    public String toString() {
        return String.format("{'recipient':%s,'amount':%f}", this._address, this._amount);
    }

    public double getAmount() {
        return this._amount;
    }

    public String GetAddress() {
        return this._address;
    }
}
