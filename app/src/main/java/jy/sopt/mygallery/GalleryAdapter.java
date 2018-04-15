package jy.sopt.mygallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jyoung on 2018. 4. 15..
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private List<GalleryItem> galleryItemList;
    private View.OnClickListener onClickListener;

    public GalleryAdapter(List<GalleryItem> galleryItemList, View.OnClickListener onClickListener) {
        this.galleryItemList = galleryItemList;
        this.onClickListener = onClickListener;
    }

    public List<GalleryItem> getGalleryItemList() {
        return galleryItemList;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_layout, parent, false);
        view.setOnClickListener(onClickListener);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        ((GalleryViewHolder)holder).bindView(galleryItemList.get(position));
    }


    @Override
    public int getItemCount() {
        return galleryItemList != null ? galleryItemList.size() : 0;
    }


    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgPhoto)
        public ImageView imgPhoto;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(GalleryItem galleryItem){

            Glide.with(imgPhoto.getContext())
                    .load(galleryItem.getImagePath())
                    .centerCrop()
                    .crossFade()
                    .into(imgPhoto);

        }

    }
}

