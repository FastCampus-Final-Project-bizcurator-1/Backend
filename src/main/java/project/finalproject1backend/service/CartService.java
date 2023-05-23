package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.finalproject1backend.domain.Cart;
import project.finalproject1backend.domain.CartItem;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.cart.CartItemDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.cart.CartItemListResponseDTO;
import project.finalproject1backend.repository.cart.CartItemRepository;
import project.finalproject1backend.repository.cart.CartRepository;
import project.finalproject1backend.repository.ProductRepository;
import project.finalproject1backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ResponseEntity<ResponseDTO> addToCart(PrincipalDTO principal, CartItemDTO cartItemDTO) {
        String userId = principal.getUserId();
        long productId = cartItemDTO.getProductId();
        int count = cartItemDTO.getCount();
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        Optional<Product> optionalProduct = productRepository.findById(productId);




        if (optionalUser.isPresent() && optionalProduct.isPresent()) {

            User user = optionalUser.get();
            Product product = optionalProduct.get();
            Optional<Cart> optionalCart = cartRepository.findByCartUser(optionalUser.get());
            Cart cart;
            if (optionalCart.isPresent()) {
                cart = optionalCart.get();
            } else {
                cart = new Cart();
                cart.setUser(user);
            }
            Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();
            CartItem cartItem;
            if (optionalCartItem.isPresent()) {
                cartItem = optionalCartItem.get();
                cartItem.setCount(cartItem.getCount() + count);
            } else {
                cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCount(count);
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
            }
            cartRepository.save(cart);
            return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("UserID or ProductID is Not Found");
        }
    }

    public ResponseEntity<?> getCart(Pageable pageable, PrincipalDTO principal) { // 장바구니 조회
        Optional<Cart> cart = cartRepository.findByCartUser_Id(principal.getId());
        List<CartItem> cartItemList = cart.get().getCartItems();
        if(!cart.isPresent()){
            throw new IllegalArgumentException("Cart Does Not Exist");
        }
        List<CartItemListResponseDTO> responseDTOS = new ArrayList<>();

        for (CartItem i:
             cartItemList) {
            responseDTOS.add(new CartItemListResponseDTO(i));
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseDTOS.size());
        Page<CartItemListResponseDTO> page = new PageImpl<>(responseDTOS.subList(start, end), pageable, responseDTOS.size());

        return new ResponseEntity<>(page,HttpStatus.OK);
    }
}