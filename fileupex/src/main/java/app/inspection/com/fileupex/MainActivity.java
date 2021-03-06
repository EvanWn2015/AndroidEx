package app.inspection.com.fileupex;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 實作拍照取得File & 錄音取得File，
 * okHttp 實作 post FromData 上傳 file,
 * okHttp 實作 忽略 SSL 協議
 * Android 6.0 權限
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Context context;
    private ImageView ivPic;
    private Display display;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // MENU
    private final static int UPLOAD_IMAGE_MENU_ID = 1;
    private final static int UPLOAD_MP3_MENU_ID = 2;

    // onActivityResult  ResultId
    //    private final static int REQUEST_TAKE_RECORD = 999999;
    private final static int REQUEST_TAKE_PIC = 0;

    private OkHttpGet okHttpGet = new OkHttpGet();
    private OkHttpPost okHttpPost = new OkHttpPost("");
    private File file, dir;

    // 錄音
    private final static int RECORD_AUDIO_REQUEST_CODE = 669;
    private MediaRecorder mediaRecorder;
    private Dialog dialog;
    private TextView tv_path;
    private ImageView iv_start, iv_stop, iv_upload;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        display = getWindowManager().getDefaultDisplay();

        findViews();
        setSupportActionBar(toolbar);

        // 6.0 權限
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 申請 RECORD_AUDIO 權限
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
//        }

        // 上傳圖片
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null) {
                    String url = "https://172.16.0.106:443/IS-AS/sit/1/backend/check/upload_image?apsystem=SIT&user_id=1&check_result_id=308";
                    String file_name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime()) + ".jpg";
                    new RunUpLoad(url, file, "image_file", file_name, "image/png").start();
                    setTitle(" " + file_name);
                } else {
                    Log.i(TAG, "file == null");
                }

            }
        });

        // 開始錄音
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // android 6.0 錄音權限
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // 申請 RECORD_AUDIO 權限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
                }

                dir = getRecordDir();
                if (dir == null) {
                    tv_path.setText("audio file does not exist");
                    return;
                }
                String name = String.format("%tY%<tm%<td_%<tH%<tM%<tS", new Date()) + ".mp3";

                Log.i(TAG, "name : " + name);
                path = new File(dir, name).getPath();
                if (recordAudio(path)) {
                    tv_path.setText("Recording...");
                    iv_start.setEnabled(false);
                }

                // 觀察 dir size
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, new File(path).length() + "");
                        handler.postDelayed(this, 500);
                    }
                });
            }
        });

        // 停止錄音
        iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    tv_path.setText("File saved: " + path);
                    iv_start.setEnabled(true);
                    dir = new File(path);
                    Toast.makeText(context, dir.length() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 上傳 錄音 file
        iv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://172.16.0.106:443/IS-AS/sit/1/backend/check/upload_audio?apsystem=SIT&user_id=1&check_result_id=308";
                String file_name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime()) + ".mp3";
                if (dir != null) {
                    Log.i(TAG, "dir != null");
                    new RunUpLoad(url, dir, "audio_file", file_name, "audio/mp3").start();
                } else {
                    Log.i(TAG, "dir == null");
                }
            }
        });
    }

    public void findViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        ivPic = (ImageView) findViewById(R.id.ivPic);

        // 錄音 dialog
        dialog = new Dialog(context);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.dialog_record, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = (int) (display.getWidth() * 0.8);
        params.height = display.getHeight() / 3;
        dialog.addContentView(inflater.inflate(R.layout.dialog_record, null), params);
        tv_path = (TextView) dialog.findViewById(R.id.tv_path);
        iv_start = (ImageView) dialog.findViewById(R.id.iv_start);
        iv_stop = (ImageView) dialog.findViewById(R.id.iv_stop);
        iv_upload = (ImageView) dialog.findViewById(R.id.iv_upload);
    }


    private boolean mediaMounted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private File getRecordDir() {
        if (!mediaMounted()) {
            return null;
        }
        return getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    // 錄音相關設定
    private boolean recordAudio(String path) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        } else {
            mediaRecorder.reset();
        }
        try {
            // 指定錄音來源 (MIC為麥克風)
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 指定錄音檔輸出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // 指定錄音編碼格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 指定錄音檔存放位置
            mediaRecorder.setOutputFile(path);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
        return true;
    }

    // android 6.0 權限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);

    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PIC:
                    Log.i(TAG, file.getPath() + "");
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

//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmapPic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//                    pic = baos.toByteArray();

                    break;
                case 333:
                    Log.i(TAG, file.getPath() + "");
                    break;
            }

        } else {
            Log.i(TAG, "resultCode != OK");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(Menu.NONE, UPLOAD_IMAGE_MENU_ID, Menu.NONE, "Upload Image");
        menu.add(Menu.NONE, UPLOAD_MP3_MENU_ID, Menu.NONE, "Upload mp3");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case UPLOAD_IMAGE_MENU_ID:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file = new File(file, "upload_image.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                if (isIntentAvailable(this, intent)) {
                    startActivityForResult(intent, REQUEST_TAKE_PIC);
                } else {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
                break;
            case UPLOAD_MP3_MENU_ID:
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    // UpLoad Thread
    class RunUpLoad extends Thread {
        String url;
        String json;
        File file;
        String file_key, file_name, file_type;

        RunUpLoad(String url, File file, String file_key, String file_name, String file_type) {
            this.url = url;
            this.file = file;
            this.file_key = file_key;
            this.file_name = file_name;
            this.file_type = file_type;
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {

            }
        };

        @Override
        public void run() {
            Log.i(TAG, "Thread  In");
            try {
                json = okHttpPost.postFile(url, file, file_key, file_name, file_type);
                Log.i(TAG, json);
            } catch (Exception e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }


}
