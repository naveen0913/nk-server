package com.sample.sample.Service;

import com.sample.sample.Model.Images;
import com.sample.sample.Repository.ImageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImgService {

    @Autowired
    private  ImageRepo imageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    public Images saveImage(String name, String description, MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/" + uploadDir + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Images data = new Images();
        data.setName(name);
        data.setDescription(description);
        data.setImageUrl("/" + uploadDir + filename);

        return imageRepository.save(data);
    }

    public List<Images> getAllImages() {
        return imageRepository.findAll();
    }



}

