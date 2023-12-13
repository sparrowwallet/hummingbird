package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.UnsignedInteger;

import java.util.ArrayList;
import java.util.List;

public class URAccount extends CryptoAccount {
    public URAccount(byte[] masterFingerprint, List<CryptoOutput> outputDescriptors) {
        super(masterFingerprint, outputDescriptors);
    }

    @Override
    public RegistryType getRegistryType() {
        return RegistryType.ACCOUNT;
    }

    public static URAccount fromCbor(DataItem cbor) {
        Map accountMap = (Map)cbor;

        UnsignedInteger uintMasterFingerprint = (UnsignedInteger)accountMap.get(new UnsignedInteger(MASTER_FINGERPRINT_KEY));
        byte[] masterFingerprint = bigIntegerToBytes(uintMasterFingerprint.getValue(), 4);
        Array outputDescriptors = (Array)accountMap.get(new UnsignedInteger(OUTPUT_DESCRIPTORS_KEY));
        List<CryptoOutput> cryptoOutputs = new ArrayList<>(outputDescriptors.getDataItems().size());
        for(DataItem item : outputDescriptors.getDataItems()) {
            cryptoOutputs.add(CryptoOutput.fromCbor(item));
        }

        return new URAccount(masterFingerprint, cryptoOutputs);
    }
}
