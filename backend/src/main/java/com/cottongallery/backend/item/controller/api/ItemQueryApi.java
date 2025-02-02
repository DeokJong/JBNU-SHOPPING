package com.cottongallery.backend.item.controller.api;

import com.cottongallery.backend.common.argumentResolver.annotation.Login;
import com.cottongallery.backend.common.dto.AccountSessionDTO;
import com.cottongallery.backend.common.dto.Response;
import com.cottongallery.backend.item.constants.ItemSort;
import com.cottongallery.backend.item.dto.response.ItemDetailResponse;
import com.cottongallery.backend.item.dto.response.ItemListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "상품 관리", description = "상품 관련 API")
public interface ItemQueryApi {

    @Operation(summary = "상품 목록 조회", description = "특정 페이지의 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 페이지 상품 목록 조회 성공")
    })
    ResponseEntity<Response<ItemListResponse>> retrieveItems(@Login AccountSessionDTO accountSessionDTO ,
                                                             @Parameter(description = "조회할 페이지 번호")@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(required = false) String keyword,
                                                             @RequestParam(defaultValue = "CREATED_DATE")ItemSort itemSort);

    @Operation(summary = "상품 상세 조회", description = "특정 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 상품 조회에 성공")
    })
    ResponseEntity<Response<ItemDetailResponse>> retrieveItem(@Login AccountSessionDTO accountSessionDTO, @PathVariable Long itemId);
}
