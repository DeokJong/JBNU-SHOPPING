package com.cottongallery.backend.repository;

import com.cottongallery.backend.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.cottongallery.backend.util.AccountTestData.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Account 생성 성공")
    void saveAccount() {
        // given
        Account testAccount = createTestAccount();

        // when
        Account savedAccount = accountRepository.save(testAccount);

        // then
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getUsername()).isEqualTo(testAccount.getUsername());
        assertThat(savedAccount.getPassword()).isEqualTo(testAccount.getPassword());
        assertThat(savedAccount.getEmail()).isEqualTo(testAccount.getEmail());
        assertThat(savedAccount.getPhoneNumber()).isEqualTo(testAccount.getPhoneNumber());
        assertThat(savedAccount.getAddressList()).containsExactlyInAnyOrderElementsOf(testAccount.getAddressList());
    }

    @Test
    @DisplayName("Username 중복 확인 - 이미 사용 중인 경우")
    void existsByUsernameReturnTrue() {
        // given
        Account testAccount = createTestAccount();
        accountRepository.save(testAccount);

        // when
        boolean exists = accountRepository.existsByUsername(testAccount.getUsername());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Username 중복 확인 - 사용하지 않는 경우")
    void existsByUsernameReturnFalse() {
        // given - when
        boolean exists = accountRepository.existsByUsername(USERNAME);

        // then
        assertThat(exists).isFalse();
    }
}
