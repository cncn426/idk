import java.io.; import java.net.;
import java .time .LocalTime;

class server

{

public static void main(String argv[]) throws Exception

{

String clientSentence; String capitalizedSentence;

ServerSocket skt= new ServerSocket(6789);

System.out.println("Server up and running, listening for connections ");

while(true)

{

Socket connectionSocket = skt.accept();
System.out.println("Client connected "+connectionSocket.getPort());
new ClientHandling (connectionSocket).start();
}
}
}
class ClientHandling extends Thread{
    private Socket socket;
    public ClientHandling(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try{
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            String response="server time"+LocalTime.now();
            outToClient.writeBytes(response);
            socket.close();
        }catch(IOException e)
        {
            System.out.println("client handle error"+e.getMessage());
        }

    }
}