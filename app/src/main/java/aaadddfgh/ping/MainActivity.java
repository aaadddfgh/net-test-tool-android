package aaadddfgh.ping;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;

import mm.pp.ping.R;

//@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private boolean v6=false;

    private Fragment pingFragment;
    private Fragment tcpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        pingFragment= PingFragment.newInstance();
        tcpFragment=TestTCP.newInstance();
        getSupportFragmentManager().beginTransaction()
            .add(
                R.id.main_content,
                (tcpFragment),
                "tcp")
            .add(
                R.id.main_content,
                (pingFragment),
                "ping")
            .commit();

        getSupportFragmentManager().beginTransaction().hide(tcpFragment).commit();

//        getSupportFragmentManager().popBackStack();

    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        //super.onSaveInstanceState(outState, outPersistentState);
//    }



    public void showDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(text);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 在此处处理确定按钮的点击事件
                dialog.dismiss(); // 关闭对话框
            }
        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 在此处处理取消按钮的点击事件
//                dialog.dismiss(); // 关闭对话框
//            }
//        });
        //builder.setP
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        menu.getItem(R.id.action_go_ping).setOnMenuItemClickListener(
//                (s)->{
//                    getSupportFragmentManager().beginTransaction().show(pingFragment).commit();
//                    getSupportFragmentManager().beginTransaction().hide(tcpFragment).commit();
//                    return true;}
//                );
//        menu.getItem(R.id.action_go_tcp).setOnMenuItemClickListener((s)->{
//            getSupportFragmentManager().beginTransaction().show(tcpFragment).commit();
//            getSupportFragmentManager().beginTransaction().hide(pingFragment).commit();
//            return true;
//        });

        return true;
    }
//


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_go_ping: {
                getSupportFragmentManager().beginTransaction().show(pingFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(tcpFragment).commit();
                break;
            }
            case R.id.action_go_tcp: {
                getSupportFragmentManager().beginTransaction().show(tcpFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(pingFragment).commit();

                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }
}