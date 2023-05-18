package project.finalproject1backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.MainCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsById(Long id); //아이디 중복확인
    Optional<Product> findById(Long id); //아이디로 찾기

    int countByRecommendedTrue();


    //상품명으로 검색
//    @Query(value = "SELECT * FROM product WHERE main_category = :mainCategoryName ORDER BY RAND() LIMIT 4", nativeQuery = true)
//    List<Product> findRandomByCategory(@Param("mainCategoryName") String mainCategoryName);




}
