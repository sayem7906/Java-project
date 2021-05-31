package MultiClient;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

public class StartingPage extends Application {

    Stage window;
    public static final CountDownLatch latch = new CountDownLatch(1);
    public static StartingPage startingPage = null;
    public static TextField textField,textField2;
    public static boolean exit,connect;
    public static StartingPage waitForStartingPage(TextField textField,TextField textField2) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startingPage.textField = textField;
        startingPage.textField2=textField2;
        return startingPage;
    }
    public StartingPage() {
        setStartingPage(this);
    }

    public static void setStartingPage(StartingPage StartingPage1) {
        startingPage = StartingPage1;
        latch.countDown();
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("The Concurrent NotePad");
        window.setMinWidth(200);
        window.setMinHeight(200);


        BorderPane layout = new BorderPane();


        VBox vBox = new VBox();

        Label IP = new Label("IP Address : ");
        IP.setFont(new Font("Ariel",20));

        textField = new TextField();
        textField.setPromptText("IP Address");
        textField.setFont(new Font("Cambria",15));


        Label port = new Label("Port Number : ");
        port.setFont(new Font("Ariel",20));

        textField2 = new TextField();
        textField2.setPrefWidth(20);
        textField2.setPromptText("Port Number");
        textField2.setFont(new Font("Cambria",15));

        vBox.setSpacing(10);
        vBox.getChildren().addAll(IP,textField,port,textField2);

        HBox hBox = new HBox();

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e->{
            connect(textField,textField2);
        });


        Button exitButton = new Button("Close");
        exitButton.setOnAction(e->{
            System.exit(0);
        });

        connectButton.setMaxWidth(80);
        exitButton.setMinWidth(80);
        hBox.setSpacing(50);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.getChildren().addAll(connectButton,exitButton);
        layout.setPadding(new Insets(20,50,20,50));
        layout.setCenter(vBox);
        layout.setBottom(hBox);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
    public void connect(TextField textField,TextField textField2){
        String ipAddress = textField.getText();
        int portNumber = Integer.parseInt(textField2.getText());
        textField.clear();
        textField2.clear();
        System.out.println("IP : "+ipAddress);
        System.out.println("Port Number : "+portNumber);
        window.close();
    }
    public String optionChosen(){
        if(exit){
            return "exit";
        }
        else if(connect){
            return "connect";
        }
        else
            return "wait";
    }
}
