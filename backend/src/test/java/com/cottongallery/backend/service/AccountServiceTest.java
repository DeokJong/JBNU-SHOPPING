package com.cottongallery.backend.service;

import com.cottongallery.backend.domain.Account;
import com.cottongallery.backend.exception.account.UsernameAlreadyExistsException;
import com.cottongallery.backend.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.cottongallery.backend.util.AccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("회원 가입 성공")
    void signUpSuccess() {
        // given
        given(accountRepository
                .save(any(Account.class)))
                .willReturn(createTestAccountWithId());

        // when
        Long accountId = accountService.signUp(createTestAccountCreateRequest());

        // then
        assertThat(accountId).isEqualTo(ID);

        verify(accountRepository, times(1)).existsByUsername(any(String.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 존재하는 사용자명")
    void singUpFailUsernameAlreadyExists() {
        // given
        given(accountRepository
                .existsByUsername(any(String.class)))
                .willReturn(true);

        // when - then
        assertThatThrownBy(() -> accountService.signUp(createTestAccountCreateRequest()))
                .isInstanceOf(UsernameAlreadyExistsException.class);

        verify(accountRepository, times(1)).existsByUsername(any(String.class));
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    @DisplayName("username 중복 확인 - 이미 사용 중인 경우")
    void isUsernameDuplicateReturnTrue() {
        // given
        given(accountRepository
                .existsByUsername(any(String.class)))
                .willReturn(true);
        
        // when
        Boolean isDuplicated = accountService.isUsernameDuplicate(USERNAME);

        // then
        assertThat(isDuplicated).isTrue();

        verify(accountRepository, times(1)).existsByUsername(any(String.class));
    }

    @Test
    @DisplayName("username 중복 확인 - 사용하지 않는 경우")
    void isUsernameDuplicateReturnFalse() {
        // given
        given(accountRepository
                .existsByUsername(any(String.class)))
                .willReturn(false);

        // when
        Boolean isDuplicated = accountService.isUsernameDuplicate(USERNAME);

        // then
        assertThat(isDuplicated).isFalse();

        verify(accountRepository, times(1)).existsByUsername(any(String.class));
    }
}
