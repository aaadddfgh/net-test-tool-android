package aaadddfgh.ping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import mm.pp.ping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestTCPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TestTCPFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private boolean isReciver=false;
    MutableLiveData<Boolean> rec;
    StringBuffer stringBuffer;

    MutableLiveData<String> data=new MutableLiveData<>("");

    Socket socket;

    public TestTCPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment TestNetwork.
     */

    public static TestTCPFragment newInstance() {
        TestTCPFragment fragment = new TestTCPFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText ipEditText = getView().findViewById(R.id.editTextIPAddr);
        final EditText portEditText = getView().findViewById(R.id.editTextPort);
        /**
         * 开始按钮
         */
        final Button startButton = getView().findViewById(R.id.testButton);
        final Button stopButton = getView().findViewById(R.id.stopButton);
        final Switch 作为接收者 = getView().findViewById(R.id.isReciver);
        final EditText editText=getView().findViewById(R.id.editTextTextMultiLine);

        if(savedInstanceState!=null) {
            String ipAddr = savedInstanceState.getString("IP_ADDR");
            if (ipAddr != null) {
                ipEditText.setText(ipAddr);

            }
        }

        data.observe(getViewLifecycleOwner(),(str)->{
            editText.setText(str);
        });

        rec=new MutableLiveData<>(false);
        rec.observe(getViewLifecycleOwner(),(b)->{
            if(b){
                ((MainActivity)getActivity()).showDialog(stringBuffer.toString());
                startButton.setEnabled(true);
                rec.setValue(false);
            }

        });

        stopButton.setEnabled(false);

        作为接收者.setOnCheckedChangeListener((v,b)->{
            isReciver=b;
            if(b){
                ipEditText.setEnabled(false);
            }else {
                ipEditText.setEnabled(true);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setValue("");
                StringBuffer stringBuffer=new StringBuffer(200);

                //tcp(ipEditText.getText().toString(),portEditText.getText().toString());

                if (isReciver) {

                    startButton.setEnabled(false);

                    startTcpServer(Integer.valueOf(portEditText.getText().toString()));

                    stopButton.setEnabled(true);
                }
                else {
                    startButton.setEnabled(false);
                    tcpClient(ipEditText.getText().toString(),portEditText.getText().toString());
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //serv.stop();
                TcpServer.running=false;
                try {
                    if(!TcpServer.serverSocket.isClosed())
                        TcpServer.serverSocket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(!TcpServer.clientSocket.isClosed())
                        TcpServer.clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //StringBuffer stringBuffer=new StringBuffer(200);

                //tcp(ipEditText.getText().toString(),portEditText.getText().toString());
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
    }



    private void tcpClient(String ip,String port){
        String serverIP = ip; // 服务器IP地址
        Integer serverPort = Integer.valueOf(port); // 服务器端口号
        stringBuffer=new StringBuffer();

        Thread thread = new Thread(() -> {
            try {
                // 创建Socket对象并连接服务器
                socket = new Socket(serverIP, serverPort);
                socket.setSoTimeout(8000);

                // 获取输入流和输出流
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream outputStream = socket.getOutputStream();

                // 发送消息到服务器
                String message = "niu\n";
                outputStream.write(message.getBytes());
                outputStream.flush();

                // 接收服务器的响应
                String response = reader.readLine();
                stringBuffer.append("Received: " + response);

                // 关闭连接
                socket.close();
            } catch (Exception e) {
                //e.printStackTrace();
                stringBuffer.append("Error\n");
                stringBuffer.append(e.toString());
            }
            rec.postValue(true);
        });
        thread.start();

        //return stringBuffer.toString();
    }

    Thread serv;

    private void startTcpServer(Integer port){
        serv = new Thread(() -> {
            TcpServer.main(port,data);
        });
        serv.start();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if(socket!=null&&!socket.isClosed())
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_tcp, container, false);
    }
}