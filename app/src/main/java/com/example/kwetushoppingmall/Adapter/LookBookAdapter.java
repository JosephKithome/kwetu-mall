package com.example.kwetushoppingmall.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kwetushoppingmall.Model.Banner;
import com.example.kwetushoppingmall.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LookBookAdapter extends RecyclerView.Adapter<LookBookAdapter.MyViewHolder> {
    Context context;
    List<Banner> lookbook;

    public LookBookAdapter(Context context, List<Banner> lookbook) {
        this.context = context;
        this.lookbook = lookbook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context)
                .inflate(R.layout.layout_lookbook,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get().load(lookbook.get(i).getImage()).into(myViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return lookbook.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image_lookBook);
        }
    }
}
