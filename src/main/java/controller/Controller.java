package main.java.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.model.TextAreaFloating;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private MenuItem menu_bt_new;
    @FXML
    private MenuItem menu_bt_open;
    @FXML
    private MenuItem menu_bt_close;
    @FXML
    private MenuItem menu_bt_save;
    @FXML
    private MenuItem menu_bt_saveas;
    @FXML
    private MenuItem menu_bt_setting;
    @FXML
    private MenuItem menu_bt_quit;
    @FXML
    private MenuItem menu_bt_extract;
    @FXML
    private MenuItem menu_bt_fusion;
    @FXML
    private MenuItem menu_bt_split;
    @FXML
    private VBox pdf_list_pages;
    @FXML
    private VBox pdf_view_pages;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label zoom_coefficient;
    @FXML
    private Slider tool_zoom_slider;




    private PDDocument currentPdf;
    private File currentPdfFile;
    private PDFRenderer currentRenderer;
    private int currentPageIndex;
    private ImageView currentPageView = new ImageView();
    private ArrayList<Dimension2D> pagesDimension;
    private boolean onZoom = false;
    private String path = System.getProperty("user.dir");
    private String currentPdfPath;
    private String destinationFolder = "\\src\\main\\resources\\";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private void fillPagesQuickView(ArrayList al, int indexPage){
        double w = pdf_list_pages.getWidth(), h = pdf_list_pages.getHeight();

        Image img = getImage(indexPage);
        //System.out.println(img.getWidth() + " " + img.getHeight());

        ImageView imgv = new ImageView(img);
        imgv.setFitWidth(w*0.8);
        imgv.setFitHeight(w*1.2);
        Region sp = new Region();
        sp.setPrefWidth(w);
        sp.setPrefHeight(1);
        sp.setPadding(new Insets(20,0,0,0));
        al.add(imgv);
        al.add(sp);
    }

    public void NewFileMenu(ActionEvent event){
        currentPdf = new PDDocument();
        currentRenderer = new PDFRenderer(currentPdf);
        currentPdf.addPage(new PDPage(PDRectangle.A4));
        System.out.println(currentPdf.getNumberOfPages());
        ArrayList ls = new ArrayList();
        fillPagesQuickView(ls,0);
        ObservableList<? extends Node> listimage = FXCollections.observableArrayList(ls);
        pdf_list_pages.getChildren().addAll(listimage);

        currentPageView.setImage( ((ImageView)ls.get(0)).getImage()  );
        currentPageIndex = 1;
        currentPageView.setFitWidth(pdf_view_pages.getWidth() * 1.);
        currentPageView.setFitHeight(pdf_view_pages.getHeight() * 1.);
        pdf_view_pages.getChildren().add(currentPageView);



    }
    public void OpenFileMenu(ActionEvent event){
        try {
            if (currentPdf != null)
                currentPdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open PDF");
        File file = fc.showOpenDialog(stage);
        this.currentPdfPath = file.getAbsolutePath();
        try{
            currentPdf = PDDocument.load(file);
            currentRenderer = new PDFRenderer(currentPdf);
        }
        catch (Exception e){

        }
        ArrayList ls = new ArrayList();
        System.out.println(pdf_list_pages.getWidth() + " : " + pdf_list_pages.getHeight());


        pagesDimension = new ArrayList<>();

        for( int i = 0 ; i < currentPdf.getNumberOfPages();i++ ){
            fillPagesQuickView(ls,i);

        }
        ObservableList<? extends Node> listimage = FXCollections.observableArrayList(ls);
        System.out.println(pdf_list_pages.toString());

        pdf_list_pages.getChildren().addAll(listimage);

        currentPageIndex=1;
        currentPageView.setImage( ((ImageView)ls.get(0)).getImage()  );
        currentPageView.setFitWidth(pdf_view_pages.getWidth() * 1.);
        currentPageView.setFitHeight(pdf_view_pages.getHeight() * 1.);
        pdf_view_pages.getChildren().add(currentPageView);
    }


    
    public void CloseFileMenu(ActionEvent event) throws IOException {
        if(currentPdf != null)
            currentPdf.close();
        currentPdf = null;
        currentPdfFile = null;
        currentPageView.setImage(null);
        currentRenderer = null;
        pdf_view_pages.getChildren().clear();
        pdf_list_pages.getChildren().clear();

    }
    public void SaveFileMenu(ActionEvent event){
        
    }
    public void SaveAsFileMenu(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

        fc.setTitle("Select a destination file");
        File f = fc.showSaveDialog(stage);
        currentPdf.save(f);
        currentPdfFile = f;
    }
    public void ExitApplication(ActionEvent event)throws IOException{
        if( currentPdf != null){
            currentPdf.close();
            currentRenderer = null;
            currentPdfFile = null;
        }

        System.exit(0);
    }
    public void ExtractPDFPagesMenu(ActionEvent event){

    }
    public void FusionPDFMenu(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF to merge");
        File
                file0 = new File(this.currentPdfPath),
                file1 = new File(fileChooser.showOpenDialog(stage).getAbsolutePath());

        currentPdf.close();
        pdf_list_pages.getChildren().clear();

        currentPdf = PDDocument.load(mergePDFs(file0, file1));
        currentRenderer = new PDFRenderer(currentPdf);

        ArrayList ls = new ArrayList();


        for( int i = 0 ; i < currentPdf.getNumberOfPages();i++ ){
            fillPagesQuickView(ls,i);
        }
        ObservableList<? extends Node> listImage = FXCollections.observableArrayList(ls);

        pdf_list_pages.getChildren().addAll(listImage);

        currentPageIndex=0;
        currentPageView.setImage( ((ImageView)ls.get(0)).getImage()  );
        currentPageView.setFitWidth(pdf_view_pages.getWidth() * 1.);
        currentPageView.setFitHeight(pdf_view_pages.getHeight() * 1.);
        pdf_view_pages.getChildren().add(currentPageView);
    }
    public void SplitPDFMenu(ActionEvent event){
        if( currentPdf == null) return;

    }

    public void changeZoom(Event event){

        if( !onZoom && currentPdf!=null) {
            onZoom = true;
            double coeff = tool_zoom_slider.getValue();
            coeff *= 100;
            zoom_coefficient.setText(Math.floor(coeff) + "%");

            Image img = ((ImageView) pdf_view_pages.getChildren().get(0)).getImage();
            ImageView imgv = new ImageView(img);

            double w = img.getWidth() / 100, h = img.getHeight() / 100;
            imgv.setFitWidth(w * coeff );
            imgv.setFitHeight(h * coeff );
            pdf_view_pages.getChildren().set(0, imgv);
            onZoom = false;
        }

    }

    private void switchCurrentPage(int indexpage){

        currentPageIndex = indexpage;
        Image img = getImage( currentPageIndex-1);
        ImageView imgv = new ImageView(img);

        double w = img.getWidth(), h = img.getHeight(), coeff = tool_zoom_slider.getValue();
        System.out.println("zi:"+img.getWidth() + " " + img.getHeight());
        coeff = Math.floor(coeff*100)/100;
        imgv.setFitWidth(w * coeff );
        imgv.setFitHeight(h * coeff );
        if( !pdf_view_pages.getChildren().isEmpty() )
            pdf_view_pages.getChildren().set(0, imgv);
        else
            pdf_view_pages.getChildren().add(imgv);

    }

    public void PreviousPage(MouseEvent event){
        if( currentPdf == null) return;
        if(currentPageIndex >1  ){
            switchCurrentPage(currentPageIndex-1);
        }
    }
    public void NextPage(MouseEvent event){
        if( currentPdf == null) return;
        if(currentPageIndex < currentPdf.getNumberOfPages()  ){
            switchCurrentPage(currentPageIndex+1);
        }
    }

    public void PutText(MouseEvent event){

        if( currentPdf == null) return;
/*
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.UNDECORATED);
        VBox comp = new VBox();
        TextArea nameField = new TextArea("Text");
        comp.setPadding(new Insets(5));

        comp.getChildren().add(nameField);

        Scene stageScene = new Scene(comp, 100, 50);

        newStage.setScene(stageScene);
        newStage.show();
*/
        TextAreaFloating ta = new TextAreaFloating();
        pdf_view_pages.getChildren().add(ta);
        ta.setSettings();
    }

    public void newPage(MouseEvent event){
        if( currentPdf == null) return;

        currentPdf.addPage(new PDPage(PDRectangle.A4));
        ArrayList al = new ArrayList();
        fillPagesQuickView(al,currentPdf.getNumberOfPages()-1);
        pdf_list_pages.getChildren().addAll(al);
        switchCurrentPage(currentPdf.getNumberOfPages());


    }

    public void removePage(MouseEvent event){

    }

    public void MainViewScroll(ScrollEvent event){
        //System.out.println(event);

    }

    public void ListSwitchPages(MouseEvent event){
        if( event.getTarget() instanceof ImageView ){
            //System.out.println(event);
            ImageView target = (ImageView)event.getTarget();
            currentPageIndex = (pdf_list_pages.getChildren().indexOf(event.getTarget())-1)/2;
            switchCurrentPage(currentPageIndex+1);

        }

    }

    Image getImage(int pageNumber) {
        BufferedImage pageImage;
        try {
            pageImage = (currentRenderer).renderImage(pageNumber);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDFRenderer throws IOException", ex);
        }
        return SwingFXUtils.toFXImage(pageImage, null);
    }

    public File mergePDFs(File file_0, File file_1) {
        PDFMergerUtility pdfUtility = new PDFMergerUtility();

        try {
            pdfUtility.addSource(file_0);
            pdfUtility.addSource(file_1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String
                filePath = this.path + this.destinationFolder + file_0.getName().substring(0,file_0.getName().length()-4) + file_1.getName();
        pdfUtility.setDestinationFileName(filePath);

        MemoryUsageSetting setting = MemoryUsageSetting.setupMainMemoryOnly();
        try {
            pdfUtility.mergeDocuments(setting);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File mergedFile = new File(filePath);
        return mergedFile;
    }

    public void FusionPDFTool(MouseEvent mouseEvent) {
    }
}
