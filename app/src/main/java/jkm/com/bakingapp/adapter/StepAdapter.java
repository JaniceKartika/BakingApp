package jkm.com.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.R;
import jkm.com.bakingapp.model.StepModel;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context context;
    private ArrayList<StepModel> stepModels;
    private OnItemClickListener itemClickListener;

    public StepAdapter(Context context, ArrayList<StepModel> stepModels, OnItemClickListener itemClickListener) {
        this.context = context;
        this.stepModels = stepModels;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        StepModel stepModel = stepModels.get(position);

        holder.stepTextView.setText(stepModel.getShortDescription());
        holder.stepDescriptionTextView.setText(stepModel.getDescription());

        if (stepModel.getVideoURL() != null && !stepModel.getVideoURL().isEmpty()) {
            holder.videoImageView.setVisibility(View.VISIBLE);
        } else {
            holder.videoImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (stepModels == null) return 0;
        else return stepModels.size();
    }

    public interface OnItemClickListener {
        void setOnItemClickListener(View view, int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_video_step)
        ImageView videoImageView;
        @BindView(R.id.tv_step)
        TextView stepTextView;
        @BindView(R.id.tv_step_description)
        TextView stepDescriptionTextView;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.setOnItemClickListener(view, getAdapterPosition());
            }
        }
    }
}
