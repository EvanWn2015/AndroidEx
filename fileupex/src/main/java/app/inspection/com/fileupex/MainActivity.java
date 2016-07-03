package app.inspection.com.fileupex;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Context context;
    private ImageView ivPic;

    private final static int REQUEST_PICK_PIC = 999999;
    private final static int REQUEST_TAKE_PIC = 0;

    OkHttpGet okHttpGet = new OkHttpGet();
    OkHttpPost okHttpPost = new OkHttpPost("");
    File file;
    private byte[] pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        ivPic = (ImageView) findViewById(R.id.ivPic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    String url = "https://172.16.0.106:443/IS-AS/sit/1/backend/check/upload_image?apsystem=SIT&user_id=1&check_result_id=183";
                    new RunUserInfo(url, file).start();
                } else {
                    Log.i(TAG, "file = null");
                }

            }
        });
    }

    class RunUserInfo extends Thread {
        String url;
        String json;
        File file;
        String file_name;

        RunUserInfo(String url, File file) {
            this.url = url;
            this.file = file;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };

        @Override
        public void run() {
            Log.i(TAG, "file != null");
            try {
                json = okHttpPost.postFile(url, file, file_name);
                Log.i(TAG, json);
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_PIC:
                    break;
                case REQUEST_TAKE_PIC:
                    Bitmap bitmapPic = BitmapFactory.decodeFile(file.getPath());
                    final int maxSize = 1024;  //dp
                    int outWidth;
                    int outHeight;
                    int inWidth = bitmapPic.getWidth();
                    int inHeight = bitmapPic.getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    bitmapPic = Bitmap.createScaledBitmap(bitmapPic, outWidth, outHeight, false);
                    ivPic.setImageBitmap(bitmapPic);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapPic.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    pic = baos.toByteArray();
                    break;
            }

        } else {
            Log.i(TAG, "resultCode != OK");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "DIRECTORY_DCIM");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            file = new File(file, "picture.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (isIntentAvailable(this, intent)) {
                startActivityForResult(intent, REQUEST_TAKE_PIC);
            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
