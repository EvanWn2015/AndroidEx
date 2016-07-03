package app.inspection.com.recyclerviewex;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by EVAN on 2016/6/29.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final static String TAG = "MyAdapter";
    private static Context context;
    private List<LostPetsVo> list;

    public MyAdapter(Context context, List<LostPetsVo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LostPetsVo lostPetsVo = list.get(position);
        holder.tv_name.setText(lostPetsVo.getTv_name());
        holder.tv_gender.setText(lostPetsVo.getTv_gender());
        holder.tv_master_phone.setText(lostPetsVo.getTv_master_phone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_row;
        ImageView iv_icon;
        TextView tv_name, tv_gender, tv_species, tv_location, tv_master_name, tv_master_phone;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG,"RecyclerView.ViewHolder  findView");
            ll_row = (LinearLayout)itemView.findViewById(R.id.ll_row);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);
            tv_species = (TextView) itemView.findViewById(R.id.tv_species);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_master_name = (TextView) itemView.findViewById(R.id.tv_master_name);
            tv_master_phone = (TextView) itemView.findViewById(R.id.tv_master_phone);


            ll_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + tv_master_phone.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
