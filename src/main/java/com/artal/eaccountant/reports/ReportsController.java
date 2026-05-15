package com.artal.eaccountant.reports;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportsController {

    private final InventoryPdfReportGenerator inventoryPdfReportGenerator;

    public ReportsController(InventoryPdfReportGenerator inventoryPdfReportGenerator) {
        this.inventoryPdfReportGenerator = inventoryPdfReportGenerator;
    }

    @GetMapping("/api/reports/inventory/pdf")
    public ResponseEntity<byte[]> downloadInventoryReport() {
        byte[] pdfBytes = inventoryPdfReportGenerator.generateReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
