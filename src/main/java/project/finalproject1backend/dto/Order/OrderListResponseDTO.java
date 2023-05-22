package project.finalproject1backend.dto.Order;

import lombok.*;
import project.finalproject1backend.domain.OrderStatus;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.domain.User;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseDTO {

    private Long orderId; // 주문 ID

    private String userName; // 구매자 이름

    private String userEmail; // 구매자 이메일

    private String deliveryName; // 수령인

    private String deliveryZipCode; // 우편 번호

    private String deliveryAddress; // 배송지

    private String deliveryDetailedAddress; // 상세 주소

    private String deliveryPhoneNumber; // 수령인 전화번호

    private String deliveryCompany; // 배송사

    private long deliveryNumber; //송장 번호

    private LocalDate deadline; // 구매 확정 기한

    public OrderListResponseDTO(Orders orders) {
        this.orderId = orders.getId();
        this.userName = orders.getUser().getManagerName();
        this.userEmail = orders.getUser().getEmail();
        this.deliveryName = orders.getDeliveryName();
        this.deliveryZipCode = orders.getDeliveryZipCode();
        this.deliveryAddress = orders.getDeliveryAddress();
        this.deliveryDetailedAddress = orders.getDeliveryDetailedAddress();
        this.deliveryPhoneNumber = orders.getDeliveryPhoneNumber();
        this.deliveryCompany = orders.getDeliveryCompany();
        this.deliveryNumber = orders.getDeliveryNumber();
        this.deadline = orders.getDeadline();
    }
}
