package project.finalproject1backend.domain;

import lombok.*;
import project.finalproject1backend.domain.constant.Delivery;
import project.finalproject1backend.domain.constant.ProductStatus;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderItems {
    @Id
    @Column(name="orderItem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="order_id")
    @Setter
    private Orders orders;

    @Setter
    private Long productId;

    @Setter
    @Column(nullable = false, length = 50)
    private String productName;   //상품명

    @Setter
    @Column(nullable = false, length = 50)
    private int consumerPrice;   //소비자가

    @Setter
    @Column(nullable = false, length = 50)
    private int productPrice;   //판매가

    @Setter
    @Column(nullable = false, length = 50)
    private String manufacturer;   //제조사

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Delivery delivery;    //배송방법

    @Setter
    @Column(nullable = false)
    private int deliveryCharge;    //배송비

    @Setter
    @Column(nullable = false)
    private int stockNumber;     //재고수량

    @Setter
    @Column(nullable = false)
    private int minimumQuantity;     //최소수량

    public OrderItems(Product product, Orders orders) {
        this.productId = product.getId();
        this.orders = orders;
        this.productName = product.getProductName();
        this.consumerPrice = product.getConsumerPrice();
        this.productPrice = product.getProductPrice();
        this.manufacturer = product.getManufacturer();
        this.delivery = product.getDelivery();
        this.deliveryCharge = product.getDeliveryCharge();
        this.stockNumber = product.getStockNumber();
        this.minimumQuantity = product.getMinimumQuantity();
    }
}
