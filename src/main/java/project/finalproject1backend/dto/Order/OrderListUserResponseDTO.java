package project.finalproject1backend.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.dto.AttachmentDTO;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListUserResponseDTO {

    private Long OrderId;
    private AttachmentDTO productImg;
    private int amount;
    private String productName;
    private int price;
    private String status;
    private Long number;
}
