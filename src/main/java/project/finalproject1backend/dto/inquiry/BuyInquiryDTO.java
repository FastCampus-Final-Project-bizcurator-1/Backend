package project.finalproject1backend.dto.inquiry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiryState;
import project.finalproject1backend.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInquiryDTO {
    @Schema(defaultValue = "대량구매")
    private String category;
    @Schema(defaultValue = "상품이름")
    private String product;

    private int amount;
    @Schema(defaultValue = "요청사항")
    private String content;

    private LocalDate estimateWishDate;

    private LocalDate deliveryWishDate;

}
/*
”category” :”대량구매, 주문제작”
”product” :”구매 희망 제품”
”amount” :”구매 수량”
”buyImageList” :”이미지 첨부파일”
”answerAttachment” :”답변 첨부파일”
”content” :”요청사항 작성”
”estimateWishDate” :”견적 수령 희망일”
”deliveryWishDate” :”제품 배송 희망일”
 */