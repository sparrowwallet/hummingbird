package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CryptoAccount {
    public static final long MASTER_FINGERPRINT_KEY = 1;
    public static final long OUTPUT_DESCRIPTORS_KEY = 2;

    private final byte[] masterFingerprint;
    private final List<CryptoOutput> outputDescriptors;

    public CryptoAccount(byte[] masterFingerprint, List<CryptoOutput> outputDescriptors) {
        this.masterFingerprint = Arrays.copyOfRange(masterFingerprint, masterFingerprint.length - 4, masterFingerprint.length);
        this.outputDescriptors = outputDescriptors;
    }

    public byte[] getMasterFingerprint() {
        return masterFingerprint;
    }

    public List<CryptoOutput> getOutputDescriptors() {
        return outputDescriptors;
    }

    public static CryptoAccount fromCbor(DataItem cbor) {
        Map cryptoAccountMap = (Map)cbor;

        UnsignedInteger uintMasterFingerprint = (UnsignedInteger)cryptoAccountMap.get(new UnsignedInteger(MASTER_FINGERPRINT_KEY));
        Array outputDescriptors = (Array)cryptoAccountMap.get(new UnsignedInteger(OUTPUT_DESCRIPTORS_KEY));
        List<CryptoOutput> cryptoOutputs = new ArrayList<>(outputDescriptors.getDataItems().size());
        for(DataItem item : outputDescriptors.getDataItems()) {
            cryptoOutputs.add(CryptoOutput.fromCbor(item));
        }

        return new CryptoAccount(uintMasterFingerprint.getValue().toByteArray(), cryptoOutputs);
    }
}
