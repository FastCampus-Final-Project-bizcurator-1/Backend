package project.finalproject1backend.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInquiryDTO {

    private String category;

    private String product;

    private int amount;

    private String content;

    private LocalDate estimateWishDate;

    private LocalDate deliveryWishDate;

}
