package MultiClient;


import javafx.application.Application;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

import java.io.*;
public class TextAreaDemo extends Application {

    public static final CountDownLatch latch = new CountDownLatch(1);
    public static TextAreaDemo textAreaDemo = null;
    public static boolean exitButton;
    public static String newText;

    public static TextAreaDemo waitForTextAreaDemo(TextArea textArea) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextAreaDemo.textArea = textArea;
        return textAreaDemo;
    }
    public TextAreaDemo() {
        setTextAreaDemo(this);
    }

    public static void setTextAreaDemo(TextAreaDemo textAreaDemo1) {
        textAreaDemo = textAreaDemo1;
        latch.countDown();
    }

    public VBox layout;
    public static TextArea textArea;
    public static void main(String[] args) {
        launch(args);
    }


    public TextArea getTextArea() {
        return textArea;
    }

    @Override
    public void start(Stage window) throws Exception {


        Rectangle2D rect = Screen.getPrimary().getVisualBounds();
        textArea = new TextArea();
        textArea.setMinSize(rect.getWidth(), rect.getHeight());

        //int numberOfPage =1;
        window.setTitle("New window");
        Menu fileMenu = new Menu("_File");





        //NEW WINDOW
        MenuItem newPad = new MenuItem("New");
        newPad.setOnAction(e->{
            Stage stage = new Stage();
            TextAreaDemo textAreaDemo = new TextAreaDemo();
            try {
                textAreaDemo.start(stage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            //stage.show();
        });
        fileMenu.getItems().add(newPad);







        //OPEN WINDOW
        MenuItem open = new MenuItem("Open");
        open.setOnAction(e->{

            FileChooser fileChooser =new FileChooser();
            fileChooser.setTitle("Open resource file");
            File file = fileChooser.showOpenDialog(window);
            if(file!=null) {
                Stage stage = new Stage();
                TextAreaDemo textAreaDemo = new TextAreaDemo();
                try {
                    textAreaDemo.start(stage);
                    textAreaDemo.openFile(file, textAreaDemo.getTextArea());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                //openFile(file,textArea);
                String Filename = file.getName();
                stage.setTitle(Filename);
                //textAreaDemo.buttonPressed("ABC");
            }
        });
        fileMenu.getItems().add(open);







        //SAVE FILE
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(window);
            String sampleText = textArea.getText();

            if (file != null) {
                saveTextToFile(sampleText, file);
            }
            String string = file.getName();
            window.setTitle(string);
        });
        fileMenu.getItems().add(save);





        //SETTINGS
        fileMenu.getItems().add(new MenuItem("Settings"));






        //EXIT
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e->{

            exitButton = true;
            newText = textArea.getText();
            //System.out.println(newText);
            newText +="\nEND";
            try {
                Thread.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        });
        fileMenu.getItems().add(exit);






        //CREATING MENUBAR
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);




        layout = new VBox();
        layout.getChildren().addAll(menuBar, textArea);

        Scene scene = new Scene(layout, 1000, 800);
        window.setScene(scene);
        window.show();
    }



    public void saveTextToFile(String content, File file){
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        }   catch (IOException e){
            System.out.println(e);
        }
    }



    public void openFile(File file,TextArea textArea) throws IOException {
        textArea.setText("");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader =new BufferedReader(fileReader);
        String line="";
        while((line=bufferedReader.readLine())!=null){
            textArea.appendText(line+"\n");
        }
        fileReader.close();
        bufferedReader.close();
    }


    public void writeText(String message){
        textArea.appendText(message+"\n");
    }


}
