package project.finalproject1backend.util;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.dto.UploadDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
@Component
public class UploadUtil {
    private AwsS3 awsS3=AwsS3.getInstance();

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    //파일 업로드
    public UploadDTO upload(MultipartFile multipartFile, String uploadPath,boolean isImg){
        if(multipartFile.isEmpty() || multipartFile==null){
            throw new IllegalArgumentException("checkMultiPartFile");
        }
        String originalName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
//        Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
        String fileName =uuid+"_"+originalName;

        try{
//            multipartFile.transferTo(savePath);
            File file = convert(multipartFile);
            awsS3.upload(file,uploadPath+fileName);
            //이미지 파일이면 썸네일 파일도 생성
            if(isImg==true){
                List<File> thumbFile =Thumbnails.of(file).size(200, 200).asFiles(Rename.NO_CHANGE);
                awsS3.upload(thumbFile.get(0),uploadPath+"s_"+fileName);
//                Thumbnailator.createThumbnail(savePath.toFile(),thumbFile,200,200);
                return new UploadDTO(uuid,originalName,fileName,"s_"+fileName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadDTO(uuid,originalName,fileName);
    }

    //파일 조회
    public ResponseEntity<?> viewFile(String fileName,String path){
        Resource resource = new FileSystemResource(path+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public Map<String,Boolean> deleteFile(String fileName,String path){
        Resource resource = new FileSystemResource(path+File.separator+fileName);
        String resourceName = resource.getFilename();
        Map<String,Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            //이미지 파일이면 썸네일 파일도 생성
            if(contentType.startsWith("image")){
                File thumbFile = new File(path+File.separator+"s_"+fileName);
                thumbFile.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resultMap.put("result",removed);
        return resultMap;
    }

}
