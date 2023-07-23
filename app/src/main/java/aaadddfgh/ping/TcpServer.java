package aaadddfgh.ping;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    static StringBuffer stringBuffer=new StringBuffer();
    static Boolean running=true;

    public static Socket clientSocket;
    public static ServerSocket serverSocket;

    public static void main(Integer port, MutableLiveData string) {
        //int port = 12345; // 服务器监听的端口号

        try {
            //SocketAddress saEndPoint = new InetSocketAddress("0.0.0.0", port);
            serverSocket = new ServerSocket(port);
            //serverSocket.bind(saEndPoint);
            //SocketAddress saEndPoint = new InetSocketAddress("10.0.2.15", port);
            //serverSocket.bind(saEndPoint);

            stringBuffer.append("Server started, listening on port " + port+" ip:"+serverSocket.getInetAddress());
            string.postValue(stringBuffer.toString());


            while (running) {
                // 监听客户端连接
                clientSocket = serverSocket.accept();

                stringBuffer.append("\nClient connected: " + clientSocket.getInetAddress().getHostAddress());
                string.postValue(stringBuffer.toString());
                // 创建新线程处理客户端请求
                try {
                    // 获取输入流和输出流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream outputStream = clientSocket.getOutputStream();

                    // 接收客户端的请求
                    String request = reader.readLine();
                    stringBuffer.append("\nReceived from client: " + request);

                    // 处理请求并发送响应
                    String response = "niu\n"; // 服务器响应消息
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    stringBuffer.append("\nSent to client: " + response);

                    // 关闭连接
                    if(!clientSocket.isClosed())
                        clientSocket.close();

                } catch (IOException e) {
                    stringBuffer.append("\n"+e.toString());
                    //string.postValue(stringBuffer.toString());
                }

                string.postValue(stringBuffer.toString());
            }


        } catch (IOException e) {
            stringBuffer.append("\n"+e.toString());
            string.postValue(stringBuffer.toString());
        }
        finally {

            string.postValue(stringBuffer.toString());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // 获取输入流和输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputStream = clientSocket.getOutputStream();

                // 接收客户端的请求
                String request = reader.readLine();
                stringBuffer.append("\nReceived from client: " + request);

                // 处理请求并发送响应
                String response = "niu\n"; // 服务器响应消息
                outputStream.write(response.getBytes());
                outputStream.flush();
                stringBuffer.append("\nSent to client: " + response);

                // 关闭连接
                if(!clientSocket.isClosed())
                    clientSocket.close();

            } catch (IOException e) {
                stringBuffer.append("\n"+e.toString());
                //string.postValue(stringBuffer.toString());
            }
        }

        // private String receiveMessage(BufferedReader reader) throws IOException {
        //     StringBuilder messageBuilder = new StringBuilder();

        //     // 读取字符直到遇到报文头
        //     int character;
        //     while ((character = reader.read()) != '{') {
        //         // 可选：处理报文头之前的数据
        //     }

        //     // 读取字符直到遇到报文尾
        //     while ((character = reader.read()) != '}') {
        //         messageBuilder.append((char) character);
        //     }

        //     return messageBuilder.toString();
        // }

//        private void sendMessage(OutputStream outputStream, String message) throws IOException {
//            String formattedMessage = "{" + message + "}";
//            outputStream.write(formattedMessage.getBytes());
//            outputStream.flush();
//        }
    }
}

