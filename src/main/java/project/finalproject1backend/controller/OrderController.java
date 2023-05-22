package project.finalproject1backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.Order.*;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.service.OrderService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping

public class OrderController {

    private final OrderService orderService;

    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "단일 상품 주문하기", description = "상품 상세 페이지의 '구매하기' 버튼으로 상품을 주문하는 api입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/directPurchasing")
    public ResponseEntity<?> directPurchasing(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                              @RequestBody @Valid OrderRequestDTO requestDTO,
                                              BindingResult bindingResult){
        return orderService.purchaseOne(principal,requestDTO);
    }
    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "장바구니 상품 주문하기", description = "장바구니에서 구매하기 버튼으로 상품을 주문하는 api입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/cartPurchasing")
    public ResponseEntity<?> cartPurchasing(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                              @RequestBody @Valid OrderCartRequestDTO requestDTO,
                                              BindingResult bindingResult){
        return orderService.purchaseInCart(principal,requestDTO);
    }

    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "주문 상품 정보 확인", description = "주문하기 페이지에서 상품에 대한 정보를 받는 api입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = OrderItemResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/OrderItemResponse")
    public ResponseEntity<?> OrderItemResponse(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                               @RequestParam List<Long> cartItemIds){
        return orderService.OrderItemResponse(principal,cartItemIds);
    }

    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "주문 상품 가격 확인", description = "주문하기 페이지에서 선택한 상품에 대해 결제 금액을 보여주는 api입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = OrderItemResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/OrderPriceResponse")
    public ResponseEntity<?> OrderPriceResponse(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                               @RequestParam List<Long> cartItemIds){
        return orderService.OrderPriceResponse(principal,cartItemIds);
    }

    @Tag(name = "API 관리자 페이지", description = "관리자페이지 api 입니다.")
    @Operation(summary = "주문 월별 조회 API", description = "연,월에 따라 주문 목록을 확인하는 API입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = OrderListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/admin/getOrder")
    public ResponseEntity<?> getOrderListAdmin(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                               @RequestBody @Valid OrderListRequestDTO requestDTO,
                                               BindingResult bindingResult){
        return orderService.getOrderList(principal,requestDTO);
    }

//    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
//    @Operation(summary = "주문 내역 조회 API", description = "유저의 주문 목록을 조회하는 API입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = OrderListUserResponseDTO.class))),
//            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
//    })
//    @GetMapping("/account/admin/order")
//    public ResponseEntity<?> getOrderListUser(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal){
//        return orderService.getOrderListUser(principal);
//    }

}
