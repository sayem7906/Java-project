package MultiServer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ServerSocket serverSocket;
    Socket socket;
    boolean willRun = true;
    File file;
    ArrayList<ServerConnection>connections = new ArrayList<>();
    public static void main(String[] args){
        new Server();
    }
    public Server(){
        try {
            serverSocket = new ServerSocket(1234);


            while(willRun){

                File file = new File("C:\\Users\\ASUS\\Desktop\\abc.txt");
                socket=serverSocket.accept();
                ServerConnection serverConnection = new ServerConnection(socket,this,file);
                connections.add(serverConnection);
                serverConnection.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
