package project.finalproject1backend.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPriceResponseDTO {
    private int sumOfPrice;
    private int deliveryCharge;
    private int totalPrice;
}
