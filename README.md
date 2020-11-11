# Hummingbird

### Java implementation of Uniform Resources (UR)

Hummingbird is a Java implementation of the [Uniform Resources (UR)](https://github.com/BlockchainCommons/Research/blob/master/papers/bcr-2020-005-ur.md) specification.
It is a direct port of the [URKit](https://github.com/BlockchainCommons/URKit) implementation by Wolf McNally. 
It contains both the classes to represent a UR, and a UR encoder and decoder to encode and decode to/from the QR representations.

## Setup

Hummingbird is hosted in Maven Central and can be added as a dependency with the following:

```
implementation('com.sparrowwallet:hummingbird:1.4')
```

## Usage

Decoding a UR can be done as follows (here decoding a ``crypto-psbt`` UR type):

```java
URDecoder decoder = new URDecoder();
while(decoder.getResult() == null) {
    //Loop adding QR fragments to the decoder until it has a result
    String qrText = getFromNextQR();
    decoder.receivePart(qrText);
}

URDecoder.Result urResult = decoder.getResult();
if(urResult.type == ResultType.SUCCESS) {
    if(urResult.ur.getType().equals(UR.CRYPTO_PSBT_TYPE)) {
        byte[] psbt = urResult.ur.toBytes();       
    }
}
```

Encoding a UR:

```java
final int MIN_FRAGMENT_LENGTH = 10;
final int MAX_FRAGMENT_LENGTH = 100;
String type = UR.BYTES_TYPE;

UR ur = UR.fromBytes(type, data);
UREncoder encoder = new UREncoder(ur, MAX_FRAGMENT_LENGTH, MIN_FRAGMENT_LENGTH, 0);
while(true) {
    String fragment = encoder.nextPart();
    //Show UR fragment as QR code...
}
```

Hummingbird also includes a type registry to assist with decoding from CBOR to POJOs:

```java
RegistryType urRegistryType = ur.getRegistryType();
if(urRegistryType.equals(RegistryType.CRYPTO_PSBT)) {
    CryptoPSBT cryptoPSBT = (CryptoPSBT)ur.decodeFromRegistry();
} else if(urRegistryType.equals(RegistryType.CRYPTO_ADDRESS)) {
    CryptoAddress cryptoAddress = (CryptoAddress)ur.decodeFromRegistry();
} else if(urRegistryType.equals(RegistryType.CRYPTO_HDKEY)) {
    CryptoHDKey cryptoHDKey = (CryptoHDKey)ur.decodeFromRegistry();
} else if(urRegistryType.equals(RegistryType.CRYPTO_OUTPUT)) {
    CryptoOutput cryptoOutput = (CryptoOutput)ur.decodeFromRegistry();
} else if(urRegistryType.equals(RegistryType.CRYPTO_ACCOUNT)) {
    CryptoAccount cryptoAccount = (CryptoAccount)ur.decodeFromRegistry();
}
```

See `RegistryType.java` for the full list of supported types and their POJO implementation classes where available. 

## Testing

Hummingbird has a thorough testsuite ported from URKit. The tests can be run with:

```
./gradlew test
```

## License

Hummingbird is licensed under the Apache 2 software license.

## Dependencies

- [cbor-java](https://github.com/c-rack/cbor-java/tree/master/src/main/java/co/nstant/in/cbor)
