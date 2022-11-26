package ro.taxi_ineu.taxiineusofer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Task> taskList;
    RecyclerviewAdapter(Context context){
        mContext = context;
        taskList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.task_item,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTaskName.setText(task.getName());
        holder.tvTaskDesc.setText(task.getDesc());
        holder.tvTaskId.setText(task.getId().toString());
        holder.tvTaskDate.setText(task.getCURRENT_TIME());
        holder.tvTaskMin.setText(task.getMinutes());
        holder.tvTaskPhone.setText(task.getPhone());
        Drawable bg = AppCompatResources.getDrawable(mContext, R.drawable.layout_bg);
        int galben = Color.parseColor("#ccffff");
        int verde = Color.parseColor("#c4ffa8");
        bg.setColorFilter(verde, PorterDuff.Mode.MULTIPLY);
        if(position % 2 == 0 ) {
            bg.setColorFilter(verde, PorterDuff.Mode.MULTIPLY);
            holder.RowFG.setBackground(bg);
            //holder.RowFG.setBackgroundResource(R.drawable.layout_bg);
            //holder.RowFG.setBackgroundColor(galben);
        }else{
            bg.setColorFilter(galben, PorterDuff.Mode.MULTIPLY);
            holder.RowFG.setBackground(bg);
            //holder.RowFG.setBackgroundColor(verde);
        }

    }
    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName;
        private TextView tvTaskDesc;
        private TextView tvTaskId;
        private TextView tvTaskDate;
        private TextView tvTaskMin;
        private TextView tvTaskPhone;
        private LinearLayout RowFG;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.task_name);
            tvTaskDesc = itemView.findViewById(R.id.task_desc);
            tvTaskId = itemView.findViewById(R.id.task_id);
            tvTaskDate = itemView.findViewById(R.id.task_date);
            tvTaskMin = itemView.findViewById(R.id.task_min);
            tvTaskPhone = itemView.findViewById(R.id.Tv_tel);
            RowFG = itemView.findViewById(R.id.rowFG);

        }
    }
}