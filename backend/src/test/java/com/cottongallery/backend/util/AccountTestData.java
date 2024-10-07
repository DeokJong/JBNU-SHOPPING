package com.cottongallery.backend.util;

import com.cottongallery.backend.constants.Role;
import com.cottongallery.backend.domain.Account;
import com.cottongallery.backend.domain.Address;
import com.cottongallery.backend.dto.account.request.AccountCreateRequest;

public final class AccountTestData {
    public static final Long ID = 1L;
    public static final String NAME = "test";
    public static final String USERNAME = "testtest";
    public static final String PASSWORD = "testtest";
    public static final String EMAIL = "test@test";
    public static final String PHONE_NUMBER = "12345678901";
    public static final int ZIPCODE = 11111;
    public static final String STREET = "testStreet";
    public static final String DETAIL = "testDetail";

    private AccountTestData() {
    }

    public static AccountCreateRequest createTestAccountCreateRequest() {
        return new AccountCreateRequest(
                NAME,
                USERNAME,
                PASSWORD,
                PASSWORD, // confirmPassword
                EMAIL,
                PHONE_NUMBER,
                ZIPCODE,
                STREET,
                DETAIL);
    }

    public static Account createTestAccount() {
        Account account = Account.createAccount(NAME, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER, Role.USER);
        account.addAddress(new Address(ZIPCODE, STREET, DETAIL));

        return account;
    }

    public static Account createTestAccountWithId() {
        Account account = Account.createAccountWithId(ID, NAME, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER, Role.USER);
        account.addAddress(new Address(ZIPCODE, STREET, DETAIL));

        return account;
    }
}
