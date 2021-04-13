package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import com.sparrowwallet.hummingbird.UR;

import java.io.ByteArrayOutputStream;

public abstract class RegistryItem implements CborSerializable {
    public abstract RegistryType getRegistryType();

    public UR toUR() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CborEncoder encoder = new CborEncoder(baos);
            encoder.encode(toCbor());
            return new UR(getRegistryType(), baos.toByteArray());
        } catch(CborException | UR.InvalidTypeException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
