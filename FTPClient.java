//import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.swing.*;
class FTPClient {

    public static void main(String argv[]) throws Exception
    {
        String sentence;
        String modifiedSentence;
        boolean isOpen = true;
        int number=1;
        boolean notEnd = true;
        int port1=3483;
        int port = 3485;
        String statusCode;
        boolean clientgo = true;

        System.out.println("Welcome to the simple FTP App   \n     Commands  \nconnect servername port# connects to a specified server \nLIST: lists files on server \nRETR: fileName.txt downloads that text file to your current directory \nSTOR: fileName.txt Stores the file on the server \nQUIT terminates the connection to the server");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        sentence = inFromUser.readLine();
        StringTokenizer tokens = new StringTokenizer(sentence);


        if(sentence.startsWith("connect")){
            String serverName = tokens.nextToken(); // pass the connect command
            serverName = tokens.nextToken();
            port1 = Integer.parseInt(tokens.nextToken());
            System.out.println("You are connected to " + serverName);
            Socket ControlSocket= new Socket(serverName, port1);
            while(isOpen && clientgo)
            {

                sentence = inFromUser.readLine();
                DataOutputStream outToServer = new DataOutputStream(ControlSocket.getOutputStream());
                DataInputStream inFromServer = new DataInputStream(new BufferedInputStream(ControlSocket.getInputStream()));

                if(sentence.equals("QUIT"))
                {
                    clientgo = false;
                }
                else {
                    int column = Integer.parseInt(tokens.nextToken());
                    ServerSocket welcomeData = new ServerSocket(port);

                    outToServer.writeBytes (port + " " + sentence + " " + '\n');

                    Socket dataSocket = welcomeData.accept();

                    DataInputStream inData = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
                    while(notEnd)
                    {
                        modifiedSentence = inData.readUTF();
                        if(modifiedSentence.equals("accepted"))
                        {
                            //place piece in column from sentence
                            break;
                        }
                        else if(modifiedSentence.equals("winnerY"))
                        {
                            //Client is always yellow - so client wins
                            break;
                        }
                        else if(modifiedSentence.equals("winnerR"))
                        {
                            //Server Wins
                            break;
                        }
                        else if(modifiedSentence.equals("tie"))
                        {
                            //no one wins
                            break;
                        }
                        else
                        {
                            System.out.println("There has been an error in the move you are trying to make");
                            break;
                        }
                    }

                }
            }
        }
    }
}
