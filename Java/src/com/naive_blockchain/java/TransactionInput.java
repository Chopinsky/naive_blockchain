package com.naive_blockchain.java;

public class TransactionInput {
    private Transaction _source;
    private Integer _outputIndex;

    public TransactionInput(Transaction source, Integer index) {
        if (index >= source.getOutputsCount()) {
            throw new IllegalStateException("Error: transaction index is unable to match the parent block's record");
        } else if (index < 0) {
            throw new IllegalStateException("Error: transaction index must be greater or equal than 0");
        }

        this._source = source;
        this._outputIndex = index;
    }

    @Override
    public String toString() {
        return String.format("{'parent':%s,'index':%d}", this._source.hash(), this._outputIndex);
    }

    public double getAmount() {
        TransactionOutput output = this._source.getOutput(this._outputIndex);
        return output.getAmount();
    }

    public double GetIncomeFromSource(CryptoWallet wallet) {
        return this._source.PaymentFromSource(wallet);
    }
}
