import java.io.*;

import java.net.*;

class client

{

public static void main(String argv[]) throws Exception

{


Socket clientSocket = new Socket("localhost", 6789);
BufferedReader inFromServer = new BufferedReader(

new InputStreamReader(clientSocket.getInputStream()));
System.out.println("in fromserver"+inFromServer);
clientSocket.close();
}
}