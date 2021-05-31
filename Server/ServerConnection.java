package MultiServer;

import MultiClient.ClientConnection;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection extends Thread{

    Socket socket;
    Server server;
    boolean willRun=true;
    boolean write = false;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    File file;

    public ServerConnection(Socket socket,Server server,File file){
        this.server =server;
        this.socket = socket;
        this.file=file;
    }
    public void writeToFile(){
        String message = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            System.out.println(write);
            //while(write){
            message = dataInputStream.readUTF();
            System.out.println(message);
            System.out.println("1");
            //System.out.println(message);
            printWriter.println(message);
            printWriter.flush();

            //}
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadTextFile(File file){
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Scanner scanner = new Scanner(bufferedReader);
            String message;
            while ((message=scanner.nextLine())!=null){
                //System.out.println(message);
                //System.out.println("1");
                sendMessageToAllClient(message);
                if(message.toLowerCase().compareTo("end")==0){
                    break;
                }
            }
            fileReader.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());

            loadTextFile(file);

            //System.out.println("out");
            while (willRun) {

                while (dataInputStream.available() == 0) {
                    try {
                          Thread.sleep(1);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                }

                String message = dataInputStream.readUTF();

                if(message.toLowerCase().compareTo("true")==0) {
                    write=true;
                }
                if(write){

                    writeToFile();
                    break;
                }

                else if(!write && message.toLowerCase().compareTo("false")!=0){
                    sendMessageToAllClient(message);
                }

            }
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    public void sendMessageToClient(String message){
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessageToAllClient(String message){

        for(int i=0;i<server.connections.size();i++){
            ServerConnection serverConnection = server.connections.get(i);
            serverConnection.sendMessageToClient(message);
        }

    }

    }


