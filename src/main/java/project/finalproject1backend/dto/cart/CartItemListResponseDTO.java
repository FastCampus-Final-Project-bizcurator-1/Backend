package project.finalproject1backend.dto.cart;

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
public class CartItemListResponseDTO {

    private long cartItemId;
    private AttachmentDTO productImg;
    private int amount;
    private String productName;
    private int price;

    public CartItemListResponseDTO(CartItem cartItem) {
        if(!(cartItem.getProduct().getImgList() == null || cartItem.getProduct().getImgList().isEmpty())){
            this.productImg = new AttachmentDTO(cartItem.getProduct().getImgList().iterator().next());
        }
        this.cartItemId = cartItem.getId();
        this.amount = cartItem.getCount();
        this.productName = cartItem.getProduct().getProductName();
        this.price = cartItem.getProduct().getConsumerPrice();
    }
}
