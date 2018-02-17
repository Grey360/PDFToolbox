package main.java.tests;
import org.apache.pdfbox.cos.COSInputStream;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PDFToolboxUnitTests {
    private static PDDocument doc_0 = new PDDocument();
    private static PDDocument doc_1 = new PDDocument();
    private static PDDocument[] pdDocuments = new PDDocument[] {};
    private static String path = System.getProperty("user.dir") + "\\res\\";

    @Test
    @DisplayName("Merge files.")
    void merge() {
        PDFMergerUtility pdfUtility = new PDFMergerUtility();
        //Don't forget to change file names with ones that actually are on your disk!
        File file_0 = new File(this.path + "helloWorld.pdf");
        File file_1 = new File(this.path + "B3 Epsi et oss.pdf");
        try {
            pdfUtility.addSource(file_0);
            pdfUtility.addSource(file_1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pdfUtility.setDestinationFileName(this.path + file_0.getName() + file_1.getName() + ".pdf");

        MemoryUsageSetting setting = MemoryUsageSetting.setupMainMemoryOnly();
        try {
            pdfUtility.mergeDocuments(setting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Merge files.")
    void mergePDFs(File file_0, File file_1) {
        PDFMergerUtility pdfUtility = new PDFMergerUtility();

        try {
            pdfUtility.addSource(file_0);
            pdfUtility.addSource(file_1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pdfUtility.setDestinationFileName(this.path + file_0.getName() + file_1.getName() + ".pdf");

        MemoryUsageSetting setting = MemoryUsageSetting.setupMainMemoryOnly();
        try {
            pdfUtility.mergeDocuments(setting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Sample Test.")
    void test() throws IOException {
        PDDocument helloPdf = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        helloPdf.addPage(page);

        //Write within a page
        PDPageContentStream contentStream = new PDPageContentStream(helloPdf, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(10, 100);
        contentStream.showText("Hello PDF World!!!");
        contentStream.endText();
        contentStream.close();

        Iterator<PDStream> contentStreams = page.getContentStreams();

        String filename = "helloWorld.pdf";
        helloPdf.save(new File(this.path + filename));
        helloPdf.close();
    }

    @Test
    @DisplayName("???")
    void test_2(){
        PDAcroForm pdAcroForm = new PDAcroForm(doc_0);

        PDTextField pdTextField = new PDTextField(pdAcroForm);
        try {
            pdAcroForm.exportFDF();
            pdTextField.setValue("k");
            List<PDField> test = pdAcroForm.getFields();
            System.out.println(test.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDPage page_0 = new PDPage();
        PDStream pdStream_0 = new PDStream(doc_0);
        COSInputStream inputStream_0 = null;
        page_0.setContents(pdStream_0);
        doc_0.addPage(page_0);
    }

    @Test
    @DisplayName("Adding text to the new page of one PDF file.")
    void twoPages() throws IOException {

        String previousPdf = "helloWorld.pdf";
        File file = new File(this.path + previousPdf);
        PDDocument hiPdf = PDDocument.load(file);
        PDPage page = new PDPage(PDRectangle.A4);
        int nb = hiPdf.getNumberOfPages();
        hiPdf.addPage(page);
        assertEquals(hiPdf.getNumberOfPages(), nb+1);

        //Write within a page
        PDPageContentStream contentStream = new PDPageContentStream(hiPdf, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.newLineAtOffset(10, 10);
        contentStream.showText("Hi!!!");
        contentStream.endText();
        contentStream.close();
        hiPdf.save(file);
        hiPdf.close();
    }

    @Test
    @DisplayName("Adding an image to a pdf page")
    void addImage() throws Exception{
        PDDocument pdDoc = new PDDocument();
        PDPage pdPage = addPage();
        pdDoc.addPage(pdPage);

        addImageToPage(pdDoc, pdPage);

        pdDoc.save(this.path + "imageAdded.pdf");
        pdDoc.close();
    }

    private PDPage addPage(){
        return new PDPage(PDRectangle.A4);
    }

    private void addImageToPage(PDDocument pdDoc, PDPage pdPage) throws Exception{
        PDImageXObject pdImage = PDImageXObject.createFromFile(this.path, pdDoc);
        PDPageContentStream contentStream = new PDPageContentStream(pdDoc, pdPage);
        contentStream.drawImage(pdImage,10,400);
        contentStream.close();
    }

    /*@Test
    @DisplayName("JPedal Example.")
    void testJPedal(){
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        JInternalFrame rootContainer = new JInternalFrame("INTERNAL FRAME 1");
        JLabel label = new JLabel("This is a very simple program.");
        label.setFont(new Font("Lucida", 1, 20));
        label.setForeground(Color.RED);
        frame.add(label, "North");
        Viewer viewer = new Viewer(rootContainer, (String)null);
        viewer.setupViewer();
        frame.add(rootContainer, "Center");
        rootContainer.setVisible(true);
        frame.setTitle("Viewer in External Viewer");
        frame.setSize(800, 600);
        frame.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }

            public void windowDeactivated(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowOpened(WindowEvent e) {
            }
        });
        frame.setVisible(true);
        Object[] input = new Object[]{System.getProperty("user.dir") + "\\res\\merged.pdf"};
        viewer.executeCommand(10, input);
    }*/
}
