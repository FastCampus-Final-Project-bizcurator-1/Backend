package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.finalproject1backend.domain.Cart;
import project.finalproject1backend.domain.Orders;
import project.finalproject1backend.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o WHERE YEAR(o.createdAt) = :year AND MONTH(o.createdAt) = :month")
    List<Orders> findByCreatedAt(@Param("year") int year, @Param("month") int month);
    Optional<Orders> findByNumber(String number);
    Optional<Orders> findByPgUid(String pgUid);

}