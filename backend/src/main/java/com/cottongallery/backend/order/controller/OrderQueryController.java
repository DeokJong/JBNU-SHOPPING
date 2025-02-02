package com.cottongallery.backend.order.controller;

import com.cottongallery.backend.common.argumentResolver.annotation.Login;
import com.cottongallery.backend.common.dto.AccountSessionDTO;
import com.cottongallery.backend.common.dto.Response;
import com.cottongallery.backend.order.controller.api.OrderQueryApi;
import com.cottongallery.backend.order.dto.response.OrderListResponse;
import com.cottongallery.backend.order.dto.response.OrderResponse;
import com.cottongallery.backend.order.service.OrderQueryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/orders")
public class OrderQueryController implements OrderQueryApi {

    private final OrderQueryService orderQueryService;

    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<Response<OrderResponse>> retrieveOrder(@PathVariable Long orderId, @Login AccountSessionDTO accountSessionDTO) {
        OrderResponse orderResponse = orderQueryService.getOrderResponse(orderId, accountSessionDTO);

        return new ResponseEntity<>(Response.createResponse(HttpServletResponse.SC_OK, "주문 조회에 성공했습니다.", orderResponse),
                HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<Response<List<OrderListResponse>>> retrieveOrderList(@Login AccountSessionDTO accountSessionDTO, @RequestParam(defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

        Slice<OrderListResponse> orders = orderQueryService.getOrderListResponses(accountSessionDTO, pageRequest);
        List<OrderListResponse> content = orders.getContent();

        return new ResponseEntity<>(Response.createResponse(HttpServletResponse.SC_OK, "주문 " + page + " 페이지 조회에 성공했습니다.", content), HttpStatus.OK);
    }
}
