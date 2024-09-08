package ResturantApp;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class BillGenerator {
    public static void generateBill(String customerName, String phoneNumber, String[] items, double totalAmount) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Bill.pdf"));
            document.open();
            // Add restaurant name and logo
            document.add(new Paragraph("Restaurant Name"));
            // Add customer details
            document.add(new Paragraph("Customer: " + customerName));
            document.add(new Paragraph("Phone: " + phoneNumber));
            // Add table with items and prices
            // Add total amount
            document.add(new Paragraph("Total: $" + totalAmount));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

