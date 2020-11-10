package com.sparrowwallet.hummingbird.registry;

import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.Map;
import co.nstant.in.cbor.model.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CryptoOutput {
    private final List<ScriptExpression> scriptExpressions;

    //Only one of the following will be not null
    private final CryptoECKey ecKey;
    private final CryptoHDKey hdKey;
    private final MultiKey multiKey;

    public CryptoOutput(List<ScriptExpression> scriptExpressions, CryptoECKey ecKey) {
        this.scriptExpressions = scriptExpressions;
        this.ecKey = ecKey;
        this.hdKey = null;
        this.multiKey = null;
    }

    public CryptoOutput(List<ScriptExpression> scriptExpressions, CryptoHDKey hdKey) {
        this.scriptExpressions = scriptExpressions;
        this.ecKey = null;
        this.hdKey = hdKey;
        this.multiKey = null;
    }

    public CryptoOutput(List<ScriptExpression> scriptExpressions, MultiKey multiKey) {
        this.scriptExpressions = scriptExpressions;
        this.ecKey = null;
        this.hdKey = null;
        this.multiKey = multiKey;
    }

    public List<ScriptExpression> getScriptExpressions() {
        return scriptExpressions;
    }

    public CryptoECKey getEcKey() {
        return ecKey;
    }

    public CryptoHDKey getHdKey() {
        return hdKey;
    }

    public MultiKey getMultiKey() {
        return multiKey;
    }

    public static CryptoOutput fromCbor(DataItem cbor) {
        List<ScriptExpression> expressions = new ArrayList<>();

        Tag tag = cbor.getTag();
        do {
            if(tag.getValue() != RegistryType.CRYPTO_HDKEY.getTag() && tag.getValue() != RegistryType.CRYPTO_ECKEY.getTag()) {
                expressions.add(ScriptExpression.fromTagValue(tag.getValue()));
            }
            tag = tag.getTag();
        } while(tag != null);

        boolean isMultiKey = expressions.get(0) == ScriptExpression.MULTISIG || expressions.get(0) == ScriptExpression.SORTED_MULTISIG;
        Collections.reverse(expressions);

        Map map = (Map)cbor;
        if(isMultiKey) {
            MultiKey multiKey = MultiKey.fromCbor(map);
            return new CryptoOutput(expressions, multiKey);
        } else if(cbor.getTag().getValue() == RegistryType.CRYPTO_ECKEY.getTag()) {
            CryptoECKey cryptoECKey = CryptoECKey.fromCbor(map);
            return new CryptoOutput(expressions, cryptoECKey);
        } else if(cbor.getTag().getValue() == RegistryType.CRYPTO_HDKEY.getTag()) {
            CryptoHDKey cryptoHDKey = CryptoHDKey.fromCbor(map);
            return new CryptoOutput(expressions, cryptoHDKey);
        }

        throw new IllegalStateException("Unknown tag for data item: " + cbor.getTag().getValue());
    }
}
