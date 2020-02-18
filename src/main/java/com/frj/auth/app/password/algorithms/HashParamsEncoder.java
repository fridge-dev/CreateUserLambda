package com.frj.auth.app.password.algorithms;

import com.frj.auth.app.password.models.PasswordHashException;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.util.Base64;

/**
 * Utility class for encoding/decoding a {@link PasswordHashParams} to/from a string that can be persisted.
 *
 * Supports versioning, but currently there is only 1 version.
 *
 * @author fridge
 */
public class HashParamsEncoder {

    private static final String VERSION = "1";

    private static final String DELIMITER = ":";

    private static final int INDEX_VERSION = 0;
    private static final int INDEX_ALGORITHM = 1;
    private static final int INDEX_ITERATIONS = 2;
    private static final int INDEX_SALT = 3;
    private static final int INDEX_HASH_LEN = 4;
    private static final int INDEX_HASH = 5;
    private static final int NUM_SPLITS = 6;

    /**
     * I previously used javax.xml.bind.DatatypeConverter which has been removed from JDK11. It still exists,
     * but extra dependency is required, see https://stackoverflow.com/a/43574427. Trying this for now to
     * see if it works.
     *
     * Good luck!
     */
    private static final Base64.Encoder B64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder B64_DECODER = Base64.getDecoder();

    /**
     * Encode the hash digest and configuration params into a single String that can be used for persistence.
     */
    public String encodeHash(final PasswordHashParams params) throws PasswordHashException {
        String[] encodedParams = new String[NUM_SPLITS];
        encodedParams[INDEX_VERSION] = VERSION;
        encodedParams[INDEX_ALGORITHM] = encodeAlgorithm(params.getAlgorithm());
        encodedParams[INDEX_ITERATIONS] = encodeInteger(params.getIterations());
        encodedParams[INDEX_SALT] = encodeBytes(params.getSalt());
        encodedParams[INDEX_HASH_LEN] = encodeInteger(params.getHashLength());
        encodedParams[INDEX_HASH] = encodeBytes(params.getHash());

        checkArg(params.getHash().length == params.getHashLength(), "Hash digest length mismatch.");

        return String.join(DELIMITER, encodedParams);
    }

    /**
     * Decode the encoded hash digest and configuration params from the encoding performed in {@link #encodeHash(PasswordHashParams)}.
     */
    public PasswordHashParams decodeHash(final String encodedHash) throws PasswordHashException {
        checkArg(encodedHash != null, "Can't decode a blank hash digest.");

        String[] split = encodedHash.split(DELIMITER);
        checkArg(split.length == NUM_SPLITS, "Encoded hash has incorrect number of parts.");

        // Validate version
        String version = split[INDEX_VERSION];
        checkArg(VERSION.equals(split[INDEX_VERSION]), "Unsupported encoded hash version %s", version);

        // Params
        AlgorithmType algorithm = decodeAlgorithm(split[INDEX_ALGORITHM]);
        int iterations = decodeInteger(split[INDEX_ITERATIONS]);
        byte[] salt = decodeBytes(split[INDEX_SALT]);
        int hashLen = decodeInteger(split[INDEX_HASH_LEN]);
        byte[] hash = decodeBytes(split[INDEX_HASH]);

        checkArg(hash.length == hashLen, "Hash digest length mismatch.");

        return new PasswordHashParams(
                algorithm,
                iterations,
                hashLen,
                salt,
                hash
        );
    }

    private String encodeAlgorithm(final AlgorithmType algorithm) throws PasswordHashException {
        checkArg(null != algorithm, "Missing algorithm type.");
        return algorithm.name();
    }

    private AlgorithmType decodeAlgorithm(final String algorithmStr) throws PasswordHashException {
        try {
            return AlgorithmType.valueOf(algorithmStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new PasswordHashException(String.format("Invalid hash algorithm '%s' in the encoded string", algorithmStr));
        }
    }

    private String encodeInteger(final int integer) throws PasswordHashException {
        checkArg(integer > 0, "Integer must be greater than 0.");
        return Integer.toString(integer);
    }

    private int decodeInteger(final String intString) throws PasswordHashException {
        int i;
        try {
            i = Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            throw new PasswordHashException(String.format("'%s' is not a valid int.", intString), e);
        }

        checkArg(i > 0, "Integer decoded from string must be greater than 0.");

        return i;
    }

    private String encodeBytes(final byte[] bytes) throws PasswordHashException {
        checkArg(bytes != null && bytes.length != 0, "Bytes array must be non-empty.");

        String encodedBytes = B64_ENCODER.encodeToString(bytes);
        if (encodedBytes.contains(DELIMITER)) {
            throw new IllegalStateException("Internal failure: The encoded bytes illegally contain the hashing delimiter character.");
        }

        return encodedBytes;
    }

    private byte[] decodeBytes(final String string) throws PasswordHashException {
        checkArg(string != null && !string.isBlank(), "Can't decode blank string into bytes.");
        return B64_DECODER.decode(string);
    }

    private void checkArg(final boolean condition, final String message, final Object... args) throws PasswordHashException {
        if (!condition) {
            throw new PasswordHashException(String.format(message, args));
        }
    }
}