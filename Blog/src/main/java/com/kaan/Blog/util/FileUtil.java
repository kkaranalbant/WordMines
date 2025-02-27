package com.kaan.Blog.util;

import com.kaan.Blog.exception.UserException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FileUtil {

    public static byte[] convertMultipartFileToByteArray(MultipartFile file) {
        byte [] imageByte = null ;
        if (file == null) return imageByte;
        CompletableFuture<byte[]> cf = CompletableFuture.supplyAsync(() -> {
            byte [] image = null ;
            try {
                 image =  file.getBytes();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return image ;
        });
        String contentType = file.getContentType() ;
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/jpg") && !contentType.equals("image/png")) {
            throw new UserException("Invalid Image Type") ;
        }
        cf.join() ;
        try {
            imageByte = cf.get() ;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return  imageByte ;
    }

}
