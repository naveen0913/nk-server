package com.sample.sample.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sample.sample.Model.CustomerPreviewImage;
import com.sample.sample.Model.Hotspot;
import com.sample.sample.Repository.CustomerPreviewImageRepo;
import com.sample.sample.Repository.CustomizationImageRepo;
import com.sample.sample.Repository.HotspotRepo;
import com.sample.sample.Repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CustomizationImageService {

    @Autowired
    private HotspotRepo hotspotRepo;

    @Autowired
    private CustomerPreviewImageRepo customerPreviewImageRepo;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private MailService mailService;

    public void processAndSendCustomImagePdf(Long productId, Long hotspotId, MultipartFile file, String email) throws Exception {
        Hotspot hotspot = hotspotRepo.findById(hotspotId)
                .orElseThrow(() -> new RuntimeException("Hotspot not found"));

        Path uploadDir = Paths.get("uploads/customer-preview-files");
        Files.createDirectories(uploadDir);
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        CustomerPreviewImage upload = new CustomerPreviewImage();
        upload.setHotspot(hotspot);
        upload.setProductId(productId);
        upload.setFinalImageUrl("/uploads/customer-preview-files/" + filename);
        customerPreviewImageRepo.save(upload);

        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, pdfOut);
        document.open();

        document.add(new Paragraph("Final Customized Product Preview"));
        document.add(new Paragraph("Product ID: " + productId));
//        document.add(new Paragraph("Hotspot ID: " + hotspotId));

        Image img = Image.getInstance(filePath.toAbsolutePath().toString());
        img.scaleToFit(400, 400);
        document.add(img);
        document.close();

        mailService.sendEmailWithAttachment(email, pdfOut.toByteArray(), "Customization-Image-preview.pdf");
    }


}
