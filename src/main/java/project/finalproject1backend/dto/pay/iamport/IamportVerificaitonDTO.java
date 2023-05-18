package project.finalproject1backend.dto.pay.iamport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IamportVerificaitonDTO {

    @NotBlank
    private String imp_uid; // 고유 주문 번호
    @NotBlank
    private String merchant_uid; // 가맹점 주문 번호
    @NotBlank
    private int amount; // 총 금액
    @NotBlank
    @Email
    private String buyer_email; // 주문자 이메일

}
