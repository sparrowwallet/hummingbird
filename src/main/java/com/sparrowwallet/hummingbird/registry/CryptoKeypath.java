package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class CryptoKeypath {
    public static final int COMPONENTS_KEY = 1;
    public static final int PARENT_FINGERPRINT_KEY = 2;
    public static final int DEPTH_KEY = 3;

    private final List<PathComponent> components;
    private final byte[] parentFingerprint;
    private final Integer depth;

    public CryptoKeypath(List<PathComponent> components, byte[] parentFingerprint) {
        this(components, parentFingerprint, 0);
    }

    public CryptoKeypath(List<PathComponent> components, byte[] parentFingerprint, Integer depth) {
        this.components = components;
        this.parentFingerprint = parentFingerprint == null ? null : Arrays.copyOfRange(parentFingerprint, parentFingerprint.length - 4, parentFingerprint.length);
        this.depth = depth;
    }

    public List<PathComponent> getComponents() {
        return components;
    }

    public String getPath() {
        if(components.isEmpty()) {
            return null;
        }

        StringJoiner joiner = new StringJoiner("/");
        for(PathComponent component : components) {
            joiner.add((component.isWildcard() ? "*" : component.getIndex()) + (component.isHardened() ? "'" : ""));
        }
        return joiner.toString();
    }

    public byte[] getParentFingerprint() {
        return parentFingerprint;
    }

    public Integer getDepth() {
        return depth;
    }

    public static CryptoKeypath fromCbor(DataItem item) {
        List<PathComponent> components = new ArrayList<>();
        byte[] parentFingerprint = null;
        Integer depth = null;

        Map map = (Map)item;
        for(DataItem key : map.getKeys()) {
            UnsignedInteger uintKey = (UnsignedInteger)key;
            int intKey = uintKey.getValue().intValue();
            if(intKey == COMPONENTS_KEY) {
                Array componentArray = (Array)map.get(key);
                for(int i = 0; i < componentArray.getDataItems().size(); i+=2) {
                    boolean hardened = (componentArray.getDataItems().get(i+1) == SimpleValue.TRUE);
                    DataItem pathSeg = componentArray.getDataItems().get(i);
                    if(pathSeg instanceof UnsignedInteger) {
                        UnsignedInteger uintIndex = (UnsignedInteger)pathSeg;
                        components.add(new PathComponent(uintIndex.getValue().intValue(), hardened));
                    } else if(pathSeg instanceof Array) {
                        components.add(new PathComponent(hardened));
                    }
                }
            } else if(intKey == PARENT_FINGERPRINT_KEY) {
                parentFingerprint = ((UnsignedInteger)map.get(key)).getValue().toByteArray();
            } else if(intKey == DEPTH_KEY) {
                depth = ((UnsignedInteger)map.get(key)).getValue().intValue();
            }
        }

        return new CryptoKeypath(components, parentFingerprint, depth);
    }
}
