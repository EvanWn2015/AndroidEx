package app.inspection.com.powerkey;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        handler = new Handler();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Process proc = null;
//                try {
//                    proc = Runtime.getRuntime().exec("su");
//
//                    DataOutputStream opt = new DataOutputStream(proc.getOutputStream());
//                    opt.writeBytes("ifdown eth0\n");
//                    opt.writeBytes("exit\n");
//                    opt.flush();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    Process process = Runtime.getRuntime().exec("su");
//                    DataOutputStream out = new DataOutputStream(process.getOutputStream());
//                    out.writeBytes("reboot -p\n");
//                    out.writeBytes("exit\n");
//                    out.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // 做其它處理或忽略它...
//                }

//                Process process = null;
//                try {
//                    process = Runtime.getRuntime().exec("/system/bin/ping");
//
//                    OutputStream stdout = process.getOutputStream();
//                    InputStream stderr = process.getErrorStream();
//                    InputStream stdin = process.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(stdin));
//                    BufferedReader err = new BufferedReader(new InputStreamReader(stderr));
//                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdout));
//                } catch (IOException e) {
//                    e.printStackTrace();


//                String cmd = "iptables -A INPUT -p udp --sport 53 -j ACCEPT";
//                Process p = null;
//                try {
//                    p = Runtime.getRuntime().exec("su -c reboot");
//
//                OutputStream outputStream = p.getOutputStream();
//                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//                dataOutputStream.writeBytes(cmd);
//                dataOutputStream.flush();
//                dataOutputStream.close();
//                outputStream.close();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


//                }
//                try {
//                    Runtime.getRuntime().exec("su -c reboot");
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    // 做其它處理或忽略它...
//                }

//                File su = new File("/system/bin/su");
//                // 检测su文件是否存在,如果不存在则直接返回
//                if (!su.exists()) {
//                    Toast toast = Toast.makeText(context, "Unable to find /system/bin/su.", Toast.LENGTH_LONG);
//                    toast.show();
//                    return;
//                }
//
//                if (su.length() == suStream.available()) {
//                    suStream.close();
//                    return;   //
//                }
//
//                try {
//
//                    byte[] bytes = new byte[suStream.available()];
//                    DataInputStream dis = new DataInputStream(suStream);
//                    dis.readFully(bytes);
//                    FileOutputStream suOutStream = new FileOutputStream("/data/data/com.koushikdutta.superuser/su");
//                    suOutStream.write(bytes);
//                    suOutStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Process process = Runtime.getRuntime().exec("su");
//                    DataOutputStream os = new DataOutputStream(process.getOutputStream());
//                    os.writeBytes("mount -oremount,rw /dev/block/mtdblock3 /system\n");
//                    os.writeBytes("busybox cp /data/data/com.koushikdutta.superuser/su /system/bin/su\n");
//                    os.writeBytes("busybox chown 0:0 /system/bin/su\n");
//                    os.writeBytes("chmod 4755 /system/bin/su\n");
//                    os.writeBytes("exit\n");
//                    os.flush();
//                }catch (IOException e) {
//
//                }

            }
        });

    }

}
