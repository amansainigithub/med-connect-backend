package com.med.connect.bucket.bucketService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.med.connect.bucket.BucketUrlMappings;
import com.med.connect.bucket.bucketModels.BucketModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class BucketService {

    @Value("${application.bucket.name}")
    private String bucketName;


    @Autowired
    private AmazonS3 s3Client;

    public BucketModel uploadFile(MultipartFile file) {
       try {
           File fileObj = convertMultiPartFileToFile(file);
           String fileName = null;
           if(fileObj != null)
           {
               fileName= System.currentTimeMillis() + "_" + file.getOriginalFilename();
               s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
               fileObj.delete();
           }

           //Creating Bucket Models
           return new BucketModel(BucketUrlMappings.BUCKET_URL + fileName,fileName);
       }
       catch (Exception e)
       {
           log.error("Exception : " , e);
           log.error("AWS Configuration Problem :::::::::::::::: {}");
           return this.getRandomFile();
       }
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
       try {
           s3Client.deleteObject(bucketName, fileName);
       }catch (Exception e)
       {
           log.error("Exception : " , e);
           log.error("Error in delete File in AWS Bucket :::::::::::  {}");
       }
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {
       if(file == null || file.isEmpty())
       {
           return null;
       }
       else {
           File convertedFile = new File(file.getOriginalFilename());
           try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
               fos.write(file.getBytes());
           } catch (IOException e) {
               log.error("Error converting multipartFile to file", e);
           }
           return convertedFile;
       }
    }


    private BucketModel getRandomFile()
    {
        return new BucketModel(
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80",
                "BUSINESS");
    }
}
