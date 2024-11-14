package com.example.chaincode;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.Contract;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyModification;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Contract(name = "Storedata)
@Default
public class Storedata extends Contract implements ContractInterface {
    private final static Logger logger = Logger.getLogger(AssetChaincode.class.getName());

    private static class Asset {
        public String id;
        public String name;
        public int value;

        public Asset(String id, String name, int value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void storeAsset(Context ctx, String id, String name, int value) {
        ChaincodeStub stub = ctx.getStub();
        Asset asset = new Asset(id, name, value);
        String assetJson = gson.toJson(asset);
        stub.putStringState(id, assetJson);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String retrieveAsset(Context ctx, String id) {
        ChaincodeStub stub = ctx.getStub();
        String assetJson = stub.getStringState(id);
        if (assetJson.isEmpty()) {
            throw new RuntimeException("Asset ID:" + id + " does not exist");
        }
        return assetJson;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void updateAsset(Context ctx, String id, String name, int value) {
        ChaincodeStub stub = ctx.getStub();
        if (stub.getStringState(id).isEmpty()) {
            throw new RuntimeException("Asset ID:" + id + " does not exist");
        }
        Asset asset = new Asset(id, name, value);
        String assetJson = gson.toJson(asset);
        stub.putStringState(id, assetJson);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public List<String> getHistory(Context ctx, String id) {
        ChaincodeStub stub = ctx.getStub();
        List<String> history = new ArrayList<>();
        QueryResultsIterator<KeyModification> resultsIterator = stub.getHistoryForKey(id);

        for (KeyModification modification : resultsIterator) {
            history.add(modification.getStringValue());
        }
        return history;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public List<String> getByNonPrimaryKey(Context ctx, String name) {
        ChaincodeStub stub = ctx.getStub();
        String queryString = String.format("{\"selector\":{\"name\":\"%s\"}}", name);
        List<String> assets = new ArrayList<>();
        QueryResultsIterator<KeyValue> resultsIterator = stub.getQueryResult(queryString);

        for (KeyValue keyValue : resultsIterator) {
            assets.add(keyValue.getStringValue());
        }
        return assets;
    }
}
