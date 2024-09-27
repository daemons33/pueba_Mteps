package mteps.rtep.report;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
    private Image headerImage;

    public HeaderFooterPageEvent(String imagePath) throws IOException, DocumentException {
        this.headerImage = Image.getInstance(imagePath);
        this.headerImage.scaleToFit(100, 100); // Ajusta el tamaño del logo
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        PdfPTable header = new PdfPTable(1);
        try {
            header.setWidths(new int[]{24});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(50);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.addCell(headerImage);
           
            

            
            header.writeSelectedRows(0, -1, 34, 803, cb);
        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }
}
