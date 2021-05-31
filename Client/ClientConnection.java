package MultiClient;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends Thread {
    Client client;
    Socket socket;
    boolean willRun=true;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    TextAreaDemo textAreaDemo;
    StartingPage startingPage;

    public ClientConnection(Socket socket,Client client){
        this.client = client;
        this.socket=socket;
    }


    public void sendMessageToServer(String message){
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadStartingPage(){
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(StartingPage.class);
            }
        }.start();
        startingPage = StartingPage.waitForStartingPage(new TextField(),new TextField());
    }


    public void loadNotePad(){
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(TextAreaDemo.class);
            }
        }.start();
        textAreaDemo = TextAreaDemo.waitForTextAreaDemo(new TextArea());
    }

    public String chosenOption(){
        while (true){
            String chosen =  startingPage.optionChosen();
            if(chosen.compareTo("exit")==0)
                return "exit";
            else if(chosen.compareTo("Connect")==0){
                return "Connect";
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("1");
        }
    }


    public void run(){
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            /*loadStartingPage();
            Thread.sleep(10);
            String chosen = chosenOption();

            if(chosen.compareTo("exit")==0){
                close();
            }

            System.out.println("1");
            */
            loadNotePad();
            Thread.sleep(10);

            loadFileToNotePad();
            while (willRun){

                try {

                    while (dataInputStream.available()==0){

                        if(textAreaDemo.exitButton){
                            dataOutputStream.writeUTF(String.valueOf(textAreaDemo.exitButton));
                            dataOutputStream.writeUTF(textAreaDemo.newText);
                            break;
                        }
                        Thread.sleep(1);

                    }
                    if(textAreaDemo.exitButton)
                        break;
                    String messageReply = dataInputStream.readUTF();
                    System.out.println(messageReply);

                } catch (InterruptedException e) {
                    close();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    close();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void loadFileToNotePad() {

        while (true){
            try {
                while (dataInputStream.available()==0){
                    Thread.sleep(1);
                }
                String message = dataInputStream.readUTF();
                if(message.toLowerCase().compareTo("end")==0)
                    break;
                textAreaDemo.writeText(message);
                //System.out.println(message);


            } catch (InterruptedException e) {
                close();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                close();
            }
        }

    }
    public void close(){
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
