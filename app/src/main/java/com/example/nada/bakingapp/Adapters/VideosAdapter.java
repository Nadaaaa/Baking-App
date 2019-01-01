package com.example.nada.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nada.bakingapp.Models.Step;
import com.example.nada.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    public static final String TAG = VideosAdapter.class.getName();

    private Context mContext;
    private List<Step> mStepsList;
    private final ListItemClickListener mClickListener;

    public interface ListItemClickListener {
        void onListItemClicked(int clickedItemIndex);
    }

    public VideosAdapter(Context context, List<Step> stepsList, ListItemClickListener listener) {
        mContext = context;
        mStepsList = stepsList;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutVideoItem = R.layout.item_video;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutVideoItem, viewGroup, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.bind(mStepsList.get(i));
    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView_itemVideo)
        ImageView imageView_itemVideo;
        @BindView(R.id.itemVideo_stepNumber)
        TextView textView_stepNumber;
        @BindView(R.id.textView_shortDescription)
        TextView textView_shortDescription;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(Step step) {
            if (step.getVideoURL().isEmpty() && step.getThumbnailURL().isEmpty()) {
                imageView_itemVideo.setImageResource(R.drawable.ic_text);
            }
            if (step.getId() == 0) {
                textView_stepNumber.setVisibility(View.GONE);
            } else {
                textView_stepNumber.setText(String.valueOf(step.getId()));
            }
            textView_shortDescription.setText(step.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            mClickListener.onListItemClicked(getAdapterPosition());
        }
    }
}
