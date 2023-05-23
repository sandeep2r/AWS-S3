package com.sandeep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sandeep.entities.Invoice;
import com.sandeep.entities.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfGeneratorService {

    @Value("${pdf.file.path}")
    private String filePath;
    
    

    public String getFilePath() {
		return filePath;
	}

	

	public void generatePdf(Invoice invoice) {
        // Check if the PDF already exists for the given data
        if (isPdfAlreadyGenerated(invoice)) {
            return;
        }

        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Add content to the PDF
            addContentToPdf(document, invoice);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

	private void addContentToPdf(Document document, Invoice invoice) throws DocumentException {
        // Add seller and buyer details
        PdfPTable detailsTable = new PdfPTable(2);
        detailsTable.setWidthPercentage(100);
      
      //  detailsTable.setSpacingBefore(20f);
       // detailsTable.setSpacingAfter(20f);

        // Seller details
        PdfPCell sellerCell = new PdfPCell();
       
        sellerCell.addElement(new Paragraph("Seller:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        sellerCell.addElement(new Paragraph(invoice.getSeller()));
        sellerCell.addElement(new Paragraph(invoice.getSellerAddress()));
        sellerCell.addElement(new Paragraph("GSTIN: " + invoice.getSellerGstin()));
 //       sellerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        sellerCell.setPaddingBottom(25f);
        sellerCell.setPadding(25f);
        sellerCell.setPaddingLeft(45f);
        detailsTable.addCell(sellerCell);

        // Buyer details
        PdfPCell buyerCell = new PdfPCell();
        buyerCell.addElement(new Paragraph("Buyer:", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        buyerCell.addElement(new Paragraph(invoice.getBuyer()));
        buyerCell.addElement(new Paragraph(invoice.getBuyerAddress()));
        buyerCell.addElement(new Paragraph("GSTIN: " + invoice.getBuyerGstin()));
       // buyerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        buyerCell.setPadding(25f);
        buyerCell.setPaddingLeft(45f);
        detailsTable.addCell(buyerCell);

        document.add(detailsTable);
        //document.add(new Paragraph(""));

        // Create table for items
        PdfPTable itemTable = new PdfPTable(4);
        itemTable.setWidthPercentage(100);
        //itemTable.setSpacingBefore(10f);
       // itemTable.setSpacingAfter(10f);

        // Set table headers
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell tableHeaderCell = new PdfPCell(new Paragraph("Item", tableHeaderFont));
        tableHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeaderCell.setPadding(5f);
       
        itemTable.addCell(tableHeaderCell);
        tableHeaderCell = new PdfPCell(new Paragraph("Quantity", tableHeaderFont));
        tableHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeaderCell.setPadding(5f);
        itemTable.addCell(tableHeaderCell);
        tableHeaderCell = new PdfPCell(new Paragraph("Rate", tableHeaderFont));
        tableHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeaderCell.setPadding(5f);
        itemTable.addCell(tableHeaderCell);
        tableHeaderCell = new PdfPCell(new Paragraph("Amount", tableHeaderFont));
        tableHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeaderCell.setPadding(5f);
        itemTable.addCell(tableHeaderCell);

        // Add items to the table
        PdfPCell cell = new PdfPCell();
        for (Item item : invoice.getItems()) {
        cell = new PdfPCell(new Paragraph(item.getName()));
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          cell.setPadding(5f);
          itemTable.addCell(cell);

          cell = new PdfPCell(new Paragraph(item.getQuantity()));
          cell.setCalculatedHeight(10f);
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          cell.setPadding(5f);
          itemTable.addCell(cell);

          cell = new PdfPCell(new Paragraph(String.valueOf(item.getRate())));
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          cell.setPadding(5f);
          itemTable.addCell(cell);

          cell = new PdfPCell(new Paragraph(String.valueOf(item.getAmount())));
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          cell.setPadding(5f);
          itemTable.addCell(cell);
        }

        // Add table to the document
        document.add(itemTable);
    }
		

    private boolean isPdfAlreadyGenerated(Invoice invoice) {
        File file = new File(filePath);
        return file.exists();
    }
}
