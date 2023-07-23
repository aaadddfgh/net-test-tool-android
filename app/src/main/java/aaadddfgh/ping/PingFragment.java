package aaadddfgh.ping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mm.pp.ping.R;


/**
 * A simple {@link Fragment} subclass.
 *
 * create an instance of this fragment.
 */

public class PingFragment extends Fragment {


    private boolean v6=false;

    public PingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ping.
     */
    //
    public static PingFragment newInstance() {
        PingFragment fragment = new PingFragment();
        return fragment;
  }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText ipEditText = getView().findViewById(R.id.editTextIPAddr);
        final Button button = getView().findViewById(R.id.testButton);
        final Switch sw = getView().findViewById(R.id.pingv6);

        sw.setOnCheckedChangeListener((v,useV6)->{
            v6=useV6;
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);

                StringBuffer stringBuffer=new StringBuffer(200);

                if(ping(ipEditText.getText().toString(),3, stringBuffer))
                    ((MainActivity)getActivity()).showDialog(stringBuffer.toString());
                else {
                    ((MainActivity)getActivity()).showDialog(stringBuffer.toString());
                }
                button.setEnabled(true);
            }
        });
    }


    //
    public boolean ping(String ipAddr, int times, StringBuffer output) {
        BufferedReader br = null;
        String cmd;
        if(v6){
            cmd = "ping6 -c " + times + " " + ipAddr;
        }
        else {
            cmd = "ping -c " + times + " " + ipAddr;
        }

        Process process = null;
        boolean result = false;

        try {
            process = Runtime.getRuntime().exec(cmd);

            if (process == null) {
                appendLine(output, "ping fail:process is null.");
                return false;
            }

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Thread.sleep(1000);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                appendLine(output, line);
            }

            if (process.waitFor() == 0) {
                appendLine(output, "exec cmd success:\n" + cmd);
                result = true;
            } else {
                appendLine(output, "exec cmd fail.");
            }
            appendLine(output, "exec finished.");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }

            try {
                if (br != null)
                    br.close();
            } catch (IOException unused8) {
                return result;
            }
        }
    }

    private static void appendLine(StringBuffer sb, String line) {
        if (sb != null) {
            sb.append(line + "\n");
        }
    }

}