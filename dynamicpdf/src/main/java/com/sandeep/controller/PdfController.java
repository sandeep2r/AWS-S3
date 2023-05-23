package com.sandeep.controller;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sandeep.entities.Invoice;
import com.sandeep.service.PdfGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pdf")
//@Api(tags = "PDF API")
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    @Autowired
    public PdfController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/generate")
//    @ApiOperation("Generate a PDF")
    public ResponseEntity<String> generatePdf(@RequestBody Invoice invoice) {
        // Generate the PDF
        pdfGeneratorService.generatePdf(invoice);
        return ResponseEntity.ok("PDF generated successfully");
    }

    @GetMapping("/download")
//    @ApiOperation("Download the generated PDF")
    public ResponseEntity<byte[]> downloadPdf() throws IOException {
        File file = new File(pdfGeneratorService.getFilePath());
        byte[] pdfBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(pdfBytes);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
