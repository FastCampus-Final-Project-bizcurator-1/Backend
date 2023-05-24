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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.cart.CartItemDTO;
import project.finalproject1backend.dto.cart.CartItemListResponseDTO;
import project.finalproject1backend.dto.subCategory.SubCategoryRequestDTO;
import project.finalproject1backend.service.CartService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class CartController {

    private final CartService cartService;

    @Tag(name = "API 상품구매 및 문의하기", description = "API 상품구매 및 문의하기 api입니다.")
    @Operation(summary = "장바구니 담기 메서드", description = "장바구니에 productID와 count 받아서 담는 메서드입니다",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping("/account/cart")
    public ResponseEntity<?> addToCart(@Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal,
                                       @RequestBody @Valid CartItemDTO requestDTO,
                                       BindingResult bindingResult){
        return cartService.addToCart(principal,requestDTO);
    }

    @Tag(name = "API 마이페이지", description = "마이페이지 api 입니다.")
    @Operation(summary = "장바구니 조회 메서드", description = "유저가 등록한 장바구니 목록을 반환하는 메서드입니다.",security ={ @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = CartItemListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/account/getCart")
    public ResponseEntity<?> getCart(@Parameter(example = "{\n" +
            "  \"page\": 0,\n" +
            "  \"size\": 15,\n" +
            "  \"sort\" : \"id\"\n" +
            "}")@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @Parameter(hidden = true)@AuthenticationPrincipal PrincipalDTO principal){
        return cartService.getCart(pageable,principal);
    }
}
