package com.sparrowwallet.hummingbird.registry.pathcomponent;

public class WildcardPathComponent extends PathComponent {
    private final boolean hardened;

    public WildcardPathComponent(boolean hardened) {
        this.hardened = hardened;
    }

    public boolean isHardened() {
        return hardened;
    }

    public String toString() {
        return "*";
    }
}
