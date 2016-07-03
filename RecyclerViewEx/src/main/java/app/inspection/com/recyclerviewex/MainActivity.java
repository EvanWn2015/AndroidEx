package app.inspection.com.recyclerviewex;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Context context;
    private List<LostPetsVo> list;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    private RecyclerView.LayoutManager manager;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getService();

        findViews();

        initObject();

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < 100; i++) {
                    LostPetsVo lostPetsVo = new LostPetsVo();
                    lostPetsVo.setTv_name("" + new Random().nextInt(100));
                    lostPetsVo.setTv_gender("" + i);
                    lostPetsVo.setTv_master_phone(new Random().nextInt(100000000) + "");
                    list.add(lostPetsVo);
                }

                adapter.notifyDataSetChanged();

//                String url = "http://data.coa.gov.tw/Service/OpenData/DataFileService.aspx?UnitId=127";
//                new RunLostPets(url).start();
            }
        });

       


    }

    public void getService() {
        manager = new LinearLayoutManager(context);
    }

    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

    }

    public void initObject() {
        list = new ArrayList<LostPetsVo>();

        gson = new Gson();

        adapter = new MyAdapter(context, list);
    }


    class RunLostPets extends Thread {
        private String url;
        private String resp_json;
        private List<LostPetsVo> lostPetsVos = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        RunLostPets(String url) {
            this.url = url;
        }


        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        };

        @Override
        public void run() {
            try {
                resp_json = run(url);
            } catch (Exception e) {

            }
            runOnUiThread(r);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
