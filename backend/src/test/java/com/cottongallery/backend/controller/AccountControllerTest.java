package com.cottongallery.backend.controller;

import com.cottongallery.backend.auth.config.SecurityConfig;
import com.cottongallery.backend.controller.validator.AccountCreateRequestValidator;
import com.cottongallery.backend.dto.account.request.AccountCreateRequest;
import com.cottongallery.backend.exception.account.UsernameAlreadyExistsException;
import com.cottongallery.backend.service.AccountService;
import com.cottongallery.backend.util.AccountTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static com.cottongallery.backend.util.AccountTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @SpyBean
    private AccountCreateRequestValidator accountCreateRequestValidator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Account 계정 생성 요청 성공")
    void addAccountSuccess() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = createTestAccountCreateRequest();

        given(accountService
                .signUp(any(AccountCreateRequest.class)))
                .willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreateRequest)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.status").value(201))
                .andDo(print());

        verify(accountService, times(1)).signUp(any(AccountCreateRequest.class));
    }

    @Test
    @DisplayName("Account 계정 생성 요청 실패 - 비밀번호 불일치")
    void addAccountFailPasswordMismatch() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = createTestAccountCreateRequest();
        accountCreateRequest.setPassword(UUID.randomUUID().toString());

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreateRequest)));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(print());

        verify(accountService, times(0)).signUp(any(AccountCreateRequest.class));
    }

    @Test
    @DisplayName("Account 계정 생성 요청 실패 - username 중복 예외 발생")
    void addAccountFailUsernameAlreadyExists() throws Exception {
        // given
        AccountCreateRequest accountCreateRequest = createTestAccountCreateRequest();

        given(accountService
                .signUp(any(AccountCreateRequest.class)))
                .willThrow(new UsernameAlreadyExistsException());

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountCreateRequest)));

        // then
        resultActions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(409))
                .andDo(print());

        verify(accountService, times(1)).signUp(any(AccountCreateRequest.class));
    }

    @Test
    @DisplayName("Username 중복 체크 - 중복 시 true 반환")
    void checkUsernameReturnTure() throws Exception {
        // given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("username", NAME);

        given(accountService
                .isUsernameDuplicate(any(String.class)))
                .willReturn(true);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/accounts/check-username")
                .params(info));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.isDuplicated").value(true))
                .andDo(print());

        verify(accountService, times(1)).isUsernameDuplicate(any(String.class));
    }

    @Test
    @DisplayName("Username 중복 체크 - 중복 시 false 반환")
    void checkUsernameReturnFalse() throws Exception {
        // given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("username", NAME);

        given(accountService
                .isUsernameDuplicate(any(String.class)))
                .willReturn(false);

        // when
        ResultActions resultActions = mockMvc
                .perform(get("/api/accounts/check-username")
                .params(info));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.isDuplicated").value(false))
                .andDo(print());

        verify(accountService, times(1)).isUsernameDuplicate(any(String.class));
    }
}
