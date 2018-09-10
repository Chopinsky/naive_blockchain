package com.naive_blockchain.java;

import java.util.ArrayList;
import java.util.Collections;

public class Transaction {
    private ArrayList<TransactionInput> _inputs;
    private ArrayList<TransactionOutput> _outputs;
    private Double _fee;
    private byte[] _signature;

    public static Transaction CreateGenesisTransaction(
            CryptoWallet owner,
            String recipient,
            Double amount
    ) {
        Double amountToPay = amount != null ? amount : 25.0;
        TransactionOutput output = new TransactionOutput(recipient, amountToPay);

        return new Transaction(
                owner,
                new ArrayList<>(),
                new ArrayList<>(Collections.singletonList(output))
        );
    }

    public static Double ComputeFee(ArrayList<TransactionInput> inputs, ArrayList<TransactionOutput> outputs) {
        Double totalIn = 0.0, totalOut = 0.0;
        for (TransactionInput input: inputs) {
            totalIn += input.getAmount();
        }

        for (TransactionOutput output: outputs) {
            totalOut += output.getAmount();
        }

        if (totalOut > totalIn) {
            throw new IllegalStateException("This transaction is invalid because the output amount is larger than the input amount");
        }

        return totalIn - totalOut;
    }

    public Transaction(
            CryptoWallet owner,
            ArrayList<TransactionInput> inputs,
            ArrayList<TransactionOutput> outputs
    ) {
        this._inputs = inputs;
        this._outputs = outputs;
        this._fee = ComputeFee(inputs, outputs);
        this._signature = owner.Sign(this.toString(false));
    }

    public String toString(boolean includeSignature) {
        StringBuilder inputsBuilder = new StringBuilder();
        for (TransactionInput input : this._inputs) {
            inputsBuilder.append(input.toString()).append(",");
        }

        StringBuilder outputsBuilder = new StringBuilder();
        for (TransactionOutput output : this._outputs) {
            outputsBuilder.append(output.toString()).append(",");
        }

        String result = String.format(
                "{'inputs':%s,'outputs':%s,'fee':%s",
                inputsBuilder.toString(),
                outputsBuilder.toString(),
                this._fee.toString()
        );

        if (includeSignature) {
            String signature;

            try {
                signature = new String(this._signature, "UTF-8");
            } catch (Exception e) {
                System.out.println("Unable to convert the signature to string");
                return result + "}";
            }

            result += String.format(",'signature':%s", signature);
        }

        return result + "}";
    }

    public String hash() {
        Integer hashCode = this.toString(true).hashCode();
        return hashCode.toString();
    }

    public int getOutputsCount() {
        return this._outputs.size();
    }

    public TransactionOutput getOutput(int index) {
        if (index < 0 || index >= this._outputs.size()) {
            return null;
        }

        return this._outputs.get(index);
    }
}
