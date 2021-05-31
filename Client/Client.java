package MultiClient;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    ClientConnection clientConnection;
    boolean willRun=true;
    String host="127.0.0.1";
    int port=1234;

    public static void main(String[] args){
        new Client();
    }
    public Client(){
        try {
            Socket socket = new Socket("127.0.0.1",1234);
            clientConnection = new ClientConnection(socket,this);
        }catch (UnknownHostException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientConnection.start();
        takeInputFromClient();
    }


    public void takeInputFromClient(){
        Scanner scanner = new Scanner(System.in);
        while (willRun){

            while (!scanner.hasNext()){
                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            String message = scanner.nextLine();
            if(message.toLowerCase().compareTo("bye")==0){
                break;
            }
            //System.out.println("1");
            clientConnection.sendMessageToServer(message);
        }
        clientConnection.close();
    }
}
