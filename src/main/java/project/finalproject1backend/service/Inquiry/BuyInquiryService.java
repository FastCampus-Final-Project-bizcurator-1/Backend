package project.finalproject1backend.service.Inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.domain.AttachmentFile;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.User;
import project.finalproject1backend.dto.ErrorDTO;
import project.finalproject1backend.dto.PrincipalDTO;
import project.finalproject1backend.dto.ResponseDTO;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryDTO;
import project.finalproject1backend.dto.inquiry.BuyInquiryResponseDTO;
import project.finalproject1backend.repository.AttachmentFileRepository;
import project.finalproject1backend.repository.Inquiry.BuyInquiryRepository;
import project.finalproject1backend.repository.UserRepository;
import project.finalproject1backend.util.UploadUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyInquiryService {

    private final BuyInquiryRepository buyInquiryRepository;

    private  final UserRepository userRepository;

    private final AttachmentFileRepository attachmentFileRepository;

    private final UploadUtil uploadUtil;

        private String path = "C:\\Users\\user\\Downloads\\my";  //로컬 테스트용

//    private String path = "/home/ubuntu/FinalProject/upload/inquiry";  // 배포용

    public ResponseEntity<?> buyInquiryCreat(BuyInquiryDTO requestDTO, List<MultipartFile> buyImageList, PrincipalDTO principal) {

        Optional<User> user = userRepository.findByUserId(principal.getUserId());
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorDTO("400","notExistId"), HttpStatus.BAD_REQUEST);
        }

        BuyInquiry buyInquiry = BuyInquiry.builder()
                .buyInquiryId(user.get())
                .category(requestDTO.getCategory())
                .product(requestDTO.getProduct())
                .amount(requestDTO.getAmount())
                .content(requestDTO.getContent())
                .estimateWishDate(requestDTO.getEstimateWishDate())
                .deliveryWishDate(requestDTO.getDeliveryWishDate())
                .build();

        buyInquiryRepository.save(buyInquiry);

        if(buyImageList == null || buyImageList.isEmpty()){
//            throw new IllegalArgumentException("checkBuyImageList");
            return new ResponseEntity<>(new ResponseDTO("200", "success"), HttpStatus.OK);
        }

        for (MultipartFile m: buyImageList) {
            UploadDTO uploadDTO = uploadUtil.upload(m,path);

            attachmentFileRepository.save(AttachmentFile.builder()
                    .fileName(uploadDTO.getFileName())
                    .filePath(path)
                    .originalFileName(uploadDTO.getOriginalName())
                    .thumbFileName(uploadDTO.getThumbFileName())
                    .buyImage(buyInquiry)
                    .build());

        }

        return new ResponseEntity<>(new ResponseDTO("200","success"), HttpStatus.OK);
    }

    public ResponseEntity<?> buyInquiryFull(PrincipalDTO principal) {

        List<BuyInquiryResponseDTO> buyInquiryResponseDTOList = new ArrayList<>();

//        if(!(principal.getBuyInquiry().iterator().next().==null||principal.getBusinessLicense().isEmpty())) {
//        }
        for (BuyInquiry buyInquiry:principal.getBuyInquiry()) {
            List<BuyInquiryResponseDTO.buyImageListInfo> imageListInfo = new ArrayList<>();
            for (AttachmentFile a:buyInquiry.getBuyImageList()) {
                imageListInfo.add(new BuyInquiryResponseDTO.buyImageListInfo(a));
            }
//                    buyInquiry.getBuyImageList().stream().map(BuyInquiryResponseDTO.buyImageListInfo::new).collect(Collectors.toList();

            buyInquiryResponseDTOList.add(BuyInquiryResponseDTO.builder()
                    .userId(principal.getUserId())
                    .email(principal.getEmail())
                    .phoneNumber(principal.getPhoneNumber())
                    .companyName(principal.getCompanyName())
                    .category(buyInquiry.getCategory())
                    .product(buyInquiry.getProduct())
                    .amount(buyInquiry.getAmount())
                    .buyImageList(imageListInfo)
                    .content(buyInquiry.getContent())
                    .estimateWishDate(buyInquiry.getEstimateWishDate())
                    .deliveryWishDate(buyInquiry.getDeliveryWishDate())
                    .state(buyInquiry.getState())
                    .build());
        }

        return new ResponseEntity<>(buyInquiryResponseDTOList, HttpStatus.OK);
//        return new ResponseEntity<>(principal.getBuyInquiry(), HttpStatus.OK);
    }
    if(!(u.getBusinessLicense()==null||u.getBusinessLicense().isEmpty())){
        this.businessLicense = u.getBusinessLicense().stream().map(businessLicenseInfo::new).collect(Collectors.toList());
    }

//    public ResponseEntity<?> getUserInfo(String userId) {
//        return new ResponseEntity<>(userRepository.findByUserId(userId).map(UserInfoResponseDTO::new), HttpStatus.OK);
//    }
}
