package project.finalproject1backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.*;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.dto.subCategory.SubCategoryRequestDTO;
import project.finalproject1backend.dto.subCategory.SubCategoryResponseDTO;
import project.finalproject1backend.repository.*;
import project.finalproject1backend.util.UploadUtil;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final AttachmentFileRepository attachmentFileRepository;
    private final ProductRepository productRepository;
//    private final ProductImgService productImgService;
//    private final ProductImgRepository productImgRepository;
    private final UploadUtil uploadUtil;

//    private String path = "C:\\upload";  //로컬 테스트용
    private String path = "/home/ubuntu/FinalProject/upload/product";  // 배포용



    public ResponseEntity<?> createSubCategory(SubCategoryRequestDTO requestDTO){
        String[] subcategoryList = requestDTO.getSubCategoryName().split(",");
        for (String s: subcategoryList) {
            if(!subCategoryRepository.findByName(s).isPresent()) {
                SubCategory subCategory = SubCategory.builder()
                        .mainCategory(requestDTO.getMainCategory())
                        .name(s)
                        .build();
                subCategoryRepository.save(subCategory);
            }
        }
        return new ResponseEntity<>(new ResponseDTO("200","success"),HttpStatus.OK);
    }

    public ResponseEntity<?> getSubCategory(MainCategory mainCategory) {
        List<SubCategory> subCategories = subCategoryRepository.findAllByMainCategory(mainCategory);
        List<String> result = new ArrayList<>();
        for (SubCategory s:subCategories) {
            result.add(s.getName());
        }
        return new ResponseEntity<>(new SubCategoryResponseDTO(result.toString().replace("["," ").replace("]"," ").strip()),HttpStatus.OK);
    }
    public ResponseEntity<?> createProduct(PrincipalDTO principal, ProductFormDto productDto, List<MultipartFile> productImgFileList) throws Exception {
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }
        Optional<SubCategory> subCategory = subCategoryRepository.findByName(productDto.getSubcategory());
        if(!subCategory.isPresent()){
            throw new IllegalArgumentException("checkSubcategory");
        }
        //상품등록
        Product product = productDto.createProduct();
        product.setProductSubcategory(subCategory.get());
        productRepository.save(product);

        //이미지등록

        if(productImgFileList == null || productImgFileList.isEmpty()) {
            return new ResponseEntity<>(new ErrorDTO("400","productImgFileList is null or empty"), HttpStatus.BAD_REQUEST);
        }
        for (MultipartFile m: productImgFileList) {
            UploadDTO uploadDTO = uploadUtil.upload(m,path);

            attachmentFileRepository.save(AttachmentFile.builder()
                    .fileName(uploadDTO.getFileName())
                    .filePath(path)
                    .originalFileName(uploadDTO.getOriginalName())
                    .thumbFileName(uploadDTO.getThumbFileName())
                    .productAttachment(product)
                    .build());

        }

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }


    public ResponseEntity<?> modify(PrincipalDTO principal, ProductFormDto productFormDto, List<MultipartFile> productImgFileList) throws Exception{
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }

        try {

            // 상품 수정
            Product product = productRepository.findById(productFormDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));
            product.updateProduct(productFormDto);

            // 이전에 등록된 이미지를 삭제하고, 새로운 이미지를 추가하는 로직
            List<AttachmentFile> existingImages = attachmentFileRepository.findByProductAttachment(product);

            for (AttachmentFile existingImage : existingImages) {
                // 기존 이미지 삭제 로직 (예: 파일 시스템에서 이미지 파일 삭제)
                attachmentFileRepository.delete(existingImage);
            }


            // 이미지 수정
            for (MultipartFile m: productImgFileList){
                UploadDTO uploadDTO = uploadUtil.upload(m,path);

                attachmentFileRepository.save(AttachmentFile.builder()
                        .fileName(uploadDTO.getFileName())
                        .filePath(path)
                        .originalFileName(uploadDTO.getOriginalName())
                        .thumbFileName(uploadDTO.getThumbFileName())
                        .productAttachment(product)
                        .build());
            }
                return ResponseEntity.ok(new ResponseDTO("200", "상품이 수정되었습니다."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorDTO("400", "해당 상품이 존재하지 않습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorDTO("500", "서버 에러가 발생했습니다."));
        }
    }


    public ResponseEntity<?> delete(PrincipalDTO principal, Long productId) {
        Optional<User> user=userRepository.findById(principal.getId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }
        Product id = productRepository.getReferenceById(productId);
        if (id == null) {
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }
        productRepository.deleteById(productId);
        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<Page<ProductFormDto>> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return ResponseEntity.ok(products.map(ProductFormDto::of));
    }




    public ResponseEntity<?> getProduct(Long productId) {
        ProductFormDto productFormDto = productRepository.findById(productId)
                .map(ProductFormDto::of)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        return ResponseEntity.ok(productFormDto);
    }

//    public ResponseEntity<?> setProductRecommended(PrincipalDTO principal, Long productId) {
//        if (!principal.getRole().equals("ADMIN")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Optional<Product> optionalProduct = productRepository.findById(productId);
//
//        // 해당 상품이 존재하면
//        if (optionalProduct.isPresent()) {
//            Product product = optionalProduct.get();
//            if (product.isRecommended()) {
//                return ResponseEntity.badRequest().body("이미 해당 상품은 추천상품으로 설정되어 있습니다.");
//            } else {
//                int recommendedCount = productRepository.countByRecommendedTrue();
//                if (recommendedCount >= 5) {
//                    return ResponseEntity.badRequest().body("이미 5개의 추천상품이 등록되었습니다.");
//                } else {
//                    product.setRecommended(true);
//                    productRepository.save(product);
//                    return ResponseEntity.ok().build();
//                }
//            }
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    public ResponseEntity<?> getProductDetails(Pageable pageable) {
        // 전체 상품 조회
        Page<Product> products = productRepository.findAll(pageable);

        // 상품 정보를 포함하는 리스트 생성
        List<ProductFormDto.ProductResponseDto> responseDtos = new ArrayList<>();

        for (Product product : products) {
            // 필요한 정보를 포함하는 ProductResponseDto 객체 생성 및 설정
            ProductFormDto.ProductResponseDto responseDto = new ProductFormDto.ProductResponseDto();
            responseDto.setProductName(product.getProductName());
            responseDto.setConsumerPrice(product.getConsumerPrice());
            responseDto.setProductPrice(product.getProductPrice());
            responseDto.setRecommended(product.isRecommended());
            if (!product.getImgList().isEmpty()) {
                AttachmentFile attachmentFile = product.getImgList().get(0); // 첫 번째 이미지 사용
                responseDto.setImageUrl(attachmentFile.getFilePath() + attachmentFile.getFileName());
            }

            responseDtos.add(responseDto);
        }

        return ResponseEntity.ok(responseDtos);
    }

    // 메인 카테고리별 랜덤 상품 조회
    public ResponseEntity<?>getProductByRandom(MainCategory mainCategory) {
        // 전체 상품 조회
        List<Product> products = productRepository.findAll();

        // 요청된 메인 카테고리와 일치하는 상품 필터링
        List<Product> filteredProducts = products.stream()
                .filter(product -> product.getMainCategory().equals(mainCategory))
                .collect(Collectors.toList());
        // 랜덤으로 4개 상품 선택
        Collections.shuffle(filteredProducts);
        List<Product> randomProducts = filteredProducts.stream()
                .limit(4)
                .collect(Collectors.toList());
        // ProductRandomResponseDto로 변환하여 반환
        List<ProductFormDto.ProductRandomResponseDto> responseDtos = randomProducts.stream()
                .map(product -> {
                    ProductFormDto.ProductRandomResponseDto responseDto = new ProductFormDto.ProductRandomResponseDto();
                    responseDto.setId(product.getId());
                    responseDto.setProductName(product.getProductName());
                    responseDto.setProductPrice(product.getProductPrice());
                    responseDto.setMinimumQuantity(product.getMinimumQuantity());

                    if (!product.getImgList().isEmpty()) {
                        AttachmentFile attachmentFile = product.getImgList().get(0); // 첫 번째 이미지 사용
                        responseDto.setImageUrl(attachmentFile.getFilePath() + attachmentFile.getFileName());
                    }

                    return responseDto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // 상품 이름으로 상품의 일부항목 조회
    public ResponseEntity<?> getProductNameSummary(Pageable pageable,String productName) {
        Page<Product> productList = productRepository.findByProductNameContaining(pageable,productName);

        List<ProductFormDto.ProductNameResponseDto> responseDtoList = productList.stream()
                .map(ProductFormDto.ProductNameResponseDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }

    // 상품 이름으로 상품의 전체항목 조회
    public ResponseEntity<?> getProductName(Pageable pageable,String productName) {
        Page<Product> productList = productRepository.findByProductNameContaining(pageable,productName);

        List<ProductFormDto> responseDtoList = productList.stream()
                .map(ProductFormDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }
}

