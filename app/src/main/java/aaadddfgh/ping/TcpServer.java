package aaadddfgh.ping;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    static StringBuilder stringBuilder =new StringBuilder();
    static Boolean running=true;

    public static Socket clientSocket;
    public static ServerSocket serverSocket;

    public static void main(Integer port, MutableLiveData string) {
        //int port = 12345; // 服务器监听的端口号

        try {

            serverSocket = new ServerSocket(port);


            stringBuilder.append("Server started, listening on port " + port+" ip:"+serverSocket.getInetAddress());
            string.postValue(stringBuilder.toString());


            while (running) {
                // 监听客户端连接
                clientSocket = serverSocket.accept();

                stringBuilder.append("\nClient connected: " + clientSocket.getInetAddress().getHostAddress());
                string.postValue(stringBuilder.toString());
                // 创建新线程处理客户端请求
                try {
                    // 获取输入流和输出流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream outputStream = clientSocket.getOutputStream();


                    //ip信息
                    stringBuilder.append("\nIP from client: " + clientSocket.getInetAddress());

                    // 接收客户端信息
                    String request = reader.readLine();
                    stringBuilder.append("\nReceived from client: " + request);


                    // 向客户端送信息
                    String response = "niu\n"; // 服务器响应消息
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    stringBuilder.append("\nSent to client: " + response);

                    // 关闭连接
                    if(!clientSocket.isClosed())
                        clientSocket.close();

                } catch (IOException e) {
                    stringBuilder.append("\n"+e.toString());
                    //string.postValue(stringBuffer.toString());
                }

                string.postValue(stringBuilder.toString());
            }


        } catch (IOException e) {
            stringBuilder.append("\n"+e.toString());
            string.postValue(stringBuilder.toString());
        }
        finally {

            string.postValue(stringBuilder.toString());
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
                stringBuilder.append("\nReceived from client: " + request);

                // 处理请求并发送响应
                String response = "niu\n"; // 服务器响应消息
                outputStream.write(response.getBytes());
                outputStream.flush();
                stringBuilder.append("\nSent to client: " + response);

                // 关闭连接
                if(!clientSocket.isClosed())
                    clientSocket.close();

            } catch (IOException e) {
                stringBuilder.append("\n"+e.toString());
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

