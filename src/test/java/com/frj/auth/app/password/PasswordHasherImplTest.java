package com.frj.auth.app.password;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.frj.auth.app.password.algorithms.AlgorithmType;
import com.frj.auth.app.password.algorithms.HashParamsEncoder;
import com.frj.auth.app.password.algorithms.PasswordHashingAlgorithm;
import com.frj.auth.app.password.models.PasswordHashParams;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tests the {@link PasswordHasherImpl} class.
 *
 * @author fridge
 */
class PasswordHasherImplTest {

    @InjectMocks
    private PasswordHasherImpl passwordHasher;

    @Mock
    private PasswordHashingAlgorithm injectedPasswordHashingAlgorithm;

    @Mock
    private HashParamsEncoder injectedHashParamsEncoder;

    private String password;
    private byte[] hashedPasswordBytes;
    private String encodedHash;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        resetTestData();
        stubPasswordHashingAlgorithm();
    }

    private void resetTestData() {
        password = RandomStringUtils.random(10);
        hashedPasswordBytes = randomBytes(10);
        encodedHash = RandomStringUtils.random(20);
    }

    private void stubPasswordHashingAlgorithm() throws Exception {
        final PasswordHashParams mockedPasswordHashParamsCloned = mock(PasswordHashParams.class);
        when(mockedPasswordHashParamsCloned.getHash()).thenReturn(hashedPasswordBytes);

        final PasswordHashParams mockedPasswordHashParams = mock(PasswordHashParams.class);
        when(mockedPasswordHashParams.cloneAndSetHash(eq(hashedPasswordBytes))).thenReturn(mockedPasswordHashParamsCloned);

        when(injectedPasswordHashingAlgorithm.newHashParams()).thenReturn(mockedPasswordHashParams);
        when(injectedPasswordHashingAlgorithm.hash(eq(password), any())).thenReturn(hashedPasswordBytes);
    }

    private void stubHashDecoder(final byte[] hash) throws Exception {
        PasswordHashParams params = new PasswordHashParams(
                AlgorithmType.PBKDF2SHA1,
                1,
                2,
                randomBytes(2),
                hash
        );
        when(injectedHashParamsEncoder.decodeHash(encodedHash)).thenReturn(params);
    }

    @Test
    void createStorableHash() throws Exception {
        ArgumentCaptor<PasswordHashParams> captor = ArgumentCaptor.forClass(PasswordHashParams.class);
        when(injectedHashParamsEncoder.encodeHash(captor.capture())).thenReturn(encodedHash);

        String storableHash = passwordHasher.createStorableHash(password);

        assertEquals(encodedHash, storableHash);
        assertEquals(hashedPasswordBytes, captor.getValue().getHash());
    }

    @Test
    void matches_CorrectPassword() throws Exception {
        stubHashDecoder(hashedPasswordBytes);

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertTrue(matches);
    }

    @Test
    void matches_IncorrectPassword_BytesSameLength() throws Exception {
        stubHashDecoder(randomBytes(hashedPasswordBytes.length));

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertFalse(matches);
    }

    @Test
    void matches_IncorrectPassword_BytesDiffLength() throws Exception {
        stubHashDecoder(randomBytes(hashedPasswordBytes.length * 2));

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertFalse(matches);
    }

    private byte[] randomBytes(final int size) {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
