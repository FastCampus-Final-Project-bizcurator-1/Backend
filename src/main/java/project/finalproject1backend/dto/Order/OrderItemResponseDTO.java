package project.finalproject1backend.dto.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.CartItem;
import project.finalproject1backend.dto.AttachmentDTO;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {

    private AttachmentDTO productImg;
    private String manufacturer;
    private int amount;
    private String productName;
    private int price;

    public OrderItemResponseDTO(CartItem cartItem) {
        if(!(cartItem.getProduct().getImgList() == null || cartItem.getProduct().getImgList().isEmpty())){
            this.productImg = new AttachmentDTO(cartItem.getProduct().getImgList().iterator().next());
        }
        this.manufacturer = cartItem.getProduct().getManufacturer();
        this.amount = cartItem.getCount();
        this.productName = cartItem.getProduct().getProductName();
        this.price = cartItem.getProduct().getConsumerPrice();
    }
}
