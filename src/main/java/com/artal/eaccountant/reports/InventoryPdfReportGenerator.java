package com.artal.eaccountant.reports;

import com.artal.eaccountant.inventory.InventoryResponse;
import com.artal.eaccountant.inventory.InventoryService;
import com.artal.eaccountant.inventory.RestockStatus;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryPdfReportGenerator extends ReportTemplate {

    private final InventoryService inventoryService;

    public InventoryPdfReportGenerator(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    protected Object loadData() {
        return inventoryService.getInventory();
    }

    @Override
    protected byte[] buildReport(Object data) {
        @SuppressWarnings("unchecked")
        List<InventoryResponse> inventory = (List<InventoryResponse>) data;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headingFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            Paragraph title = new Paragraph("Inventory Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Generated at: " + LocalDateTime.now(), normalFont));
            document.add(new Paragraph(" "));

            addSummarySection(document, inventory, headingFont, normalFont);
            addInventoryTable(document, inventory, headingFont);
            addRestockAlertsSection(document, inventory, headingFont, normalFont);
            addRecommendedTransfersSection(document, inventory, headingFont, normalFont);
            addNotesSection(document, normalFont);

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate inventory PDF report", e);
        }
    }

    private void addSummarySection(Document document,
                                   List<InventoryResponse> inventory,
                                   Font headingFont,
                                   Font normalFont) throws Exception {
        int totalItems = inventory.size();

        int totalLocalStock = inventory.stream()
                .mapToInt(InventoryResponse::localStock)
                .sum();

        int totalFulfillmentStock = inventory.stream()
                .mapToInt(InventoryResponse::fulfillmentStock)
                .sum();

        long restockAlerts = inventory.stream()
                .filter(item -> item.restockStatus() != RestockStatus.OK)
                .count();

        int recommendedTransfer = inventory.stream()
                .mapToInt(InventoryResponse::recommendedTransferToFulfillment)
                .sum();

        document.add(new Paragraph("Business Summary", headingFont));
        document.add(new Paragraph("Total active items: " + totalItems, normalFont));
        document.add(new Paragraph("Total local stock: " + totalLocalStock, normalFont));
        document.add(new Paragraph("Total fulfillment stock: " + totalFulfillmentStock, normalFont));
        document.add(new Paragraph("Items needing restock: " + restockAlerts, normalFont));
        document.add(new Paragraph("Recommended transfer to fulfillment: " + recommendedTransfer, normalFont));
        document.add(new Paragraph(" "));
    }

    private void addInventoryTable(Document document,
                                   List<InventoryResponse> inventory,
                                   Font headingFont) throws Exception {
        document.add(new Paragraph("Inventory Details", headingFont));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        table.addCell(new Phrase("Item"));
        table.addCell(new Phrase("Category"));
        table.addCell(new Phrase("Variation"));
        table.addCell(new Phrase("Local"));
        table.addCell(new Phrase("Fulfillment"));
        table.addCell(new Phrase("Status"));
        table.addCell(new Phrase("Transfer"));

        for (InventoryResponse item : inventory) {
            table.addCell(item.itemName());
            table.addCell(item.productCategoryName());
            table.addCell(item.variation() == null ? "N/A" : item.variation());
            table.addCell(String.valueOf(item.localStock()));
            table.addCell(String.valueOf(item.fulfillmentStock()));
            table.addCell(item.restockStatus().name());
            table.addCell(String.valueOf(item.recommendedTransferToFulfillment()));
        }

        document.add(table);
        document.add(new Paragraph(" "));
    }

    private void addRestockAlertsSection(Document document,
                                         List<InventoryResponse> inventory,
                                         Font headingFont,
                                         Font normalFont) throws Exception {
        document.add(new Paragraph("Restock Alerts", headingFont));

        List<InventoryResponse> alerts = inventory.stream()
                .filter(item -> item.restockStatus() != RestockStatus.OK)
                .toList();

        if (alerts.isEmpty()) {
            document.add(new Paragraph("No restock alerts. All items are above minimum stock thresholds.", normalFont));
            document.add(new Paragraph(" "));
            return;
        }

        for (InventoryResponse item : alerts) {
            document.add(new Paragraph(
                    "- " + item.itemName()
                            + " | Status: " + item.restockStatus()
                            + " | Local: " + item.localStock()
                            + " | Fulfillment: " + item.fulfillmentStock(),
                    normalFont
            ));
        }

        document.add(new Paragraph(" "));
    }

    private void addRecommendedTransfersSection(Document document,
                                                List<InventoryResponse> inventory,
                                                Font headingFont,
                                                Font normalFont) throws Exception {
        document.add(new Paragraph("Recommended Transfers", headingFont));

        List<InventoryResponse> transfers = inventory.stream()
                .filter(item -> item.recommendedTransferToFulfillment() > 0)
                .toList();

        if (transfers.isEmpty()) {
            document.add(new Paragraph("No transfer recommendations at this time.", normalFont));
            document.add(new Paragraph(" "));
            return;
        }

        for (InventoryResponse item : transfers) {
            document.add(new Paragraph(
                    "- Transfer " + item.recommendedTransferToFulfillment()
                            + " units of " + item.itemName()
                            + " to fulfillment.",
                    normalFont
            ));
        }

        document.add(new Paragraph(" "));
    }

    private void addNotesSection(Document document, Font normalFont) throws Exception {
        document.add(new Paragraph("Notes", normalFont));
        document.add(new Paragraph(
                "Inventory numbers are calculated from stock movements. "
                        + "Manual editing of inventory is avoided to keep stock calculations traceable.",
                normalFont
        ));
    }
}
