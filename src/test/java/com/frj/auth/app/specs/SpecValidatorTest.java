package com.frj.auth.app.specs;

import static org.junit.jupiter.api.Assertions.*;

import com.frj.auth.app.CreateUserRequest;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class SpecValidatorTest {

    @Test
    void isUsernameValid_Simple() {
        final List<String> valid = Arrays.asList(
                "asdf",
                "ASDF",
                "asdfasdf12341234",
                "AaQq1144mmMMii99"
        );

        final List<String> invalid = Arrays.asList(
                "",
                " ",
                "a",
                "asd",
                "0asdfasdf",
                "asdfasdf123412341",
                "hi-im-frjchael",
                "no_no_no_no_no"
        );

        assertValidAndInvalid(
                (s) -> SpecValidator.isUsernameValid(CreateUserRequest.UsernameSpec.SIMPLE, s),
                valid,
                invalid
        );
    }

    @Test
    void isUsernameValid_Email() {
        final List<String> valid = Arrays.asList(
                "somedude+spam@gmail.com",
                "asdfasdf @ asdfasdf",
                "@"
        );

        final List<String> invalid = Arrays.asList(
                "",
                " ",
                "sdfasdf   asdfasdf"
        );

        assertValidAndInvalid(
                (s) -> SpecValidator.isUsernameValid(CreateUserRequest.UsernameSpec.EMAIL, s),
                valid,
                invalid
        );
    }

    @Test
    void isPasswordValid_Pin() {
        final List<String> valid = Arrays.asList(
                "00000",
                "12345",
                "123456",
                "1234567",
                "12345678",
                "12345678"
        );

        final List<String> invalid = Arrays.asList(
                "",
                " ",
                "1234",
                "12345a",
                "123456789",
                "asdfas"
        );

        assertValidAndInvalid(
                (s) -> SpecValidator.isPasswordValid(CreateUserRequest.PasswordSpec.PIN, s),
                valid,
                invalid
        );
    }

    @Test
    void isPasswordValid_Complex() {
        assertTrue(SpecValidator.isPasswordValid(CreateUserRequest.PasswordSpec.COMPLEX, "fp198n4fp1i jc4lj1 239f82hf]c012!@F$!P@F$!C$#_C!J$!J$!J"));
    }

    private void assertValidAndInvalid(final Function<String, Boolean> methodUnderTest, final List<String> valids, final List<String> invalids) {
        for (String valid : valids) {
            assertTrue(methodUnderTest.apply(valid), "Expected to be valid: " + valid);
        }

        for (String invalid : invalids) {
            assertFalse(methodUnderTest.apply(invalid), "Expected to be invalid: " + invalid);
        }
    }
}