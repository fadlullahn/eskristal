package com.example.eskristal.Adapter;

import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eskristal.Config;
import com.example.eskristal.Model.Produk.Produk;
import com.example.eskristal.PesananTambahActivity;
import com.example.eskristal.ProdukUbahActivity;
import com.example.eskristal.R;

import java.util.List;

public class AdapterDataProduk extends RecyclerView.Adapter<AdapterDataProduk.MyViewHolder> {
    List<Produk> mHerosList;
    public AdapterDataProduk(List<Produk> HerosList) {
        mHerosList = HerosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_produk, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.mTextViewName.setText(mHerosList.get(position).getName());
        holder.mTextViewDate.setText(mHerosList.get(position).getDate());

        SpannableString spannablemTextViewPrice = new SpannableString("Rp." + mHerosList.get(position).getPrice());

        holder.mTextViewPrice.setText(spannablemTextViewPrice);

        Glide.with(holder.itemView.getContext())
                .load(Config.IMAGES_URL + mHerosList.get(position).getImage())
                .apply(new RequestOptions().override(350, 350))
                .into(holder.mImageViewFoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent;
                SessionManager sessionManager = new SessionManager(view.getContext());
                String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

                if (level != null && level.equals("admin")) {
                    mIntent = new Intent(view.getContext(), ProdukUbahActivity.class);
                } else {
                    mIntent = new Intent(view.getContext(), PesananTambahActivity.class);
                }

                mIntent.putExtra("Id", mHerosList.get(position).getId());
                mIntent.putExtra("Name", mHerosList.get(position).getName());
                mIntent.putExtra("Price", mHerosList.get(position).getPrice());
                mIntent.putExtra("Date", mHerosList.get(position).getDate());
                mIntent.putExtra("Image", mHerosList.get(position).getImage());
                view.getContext().startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHerosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName, mTextViewPrice, mTextViewDate;
        public ImageView mImageViewFoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            mTextViewDate = (TextView) itemView.findViewById(R.id.tv_item_date);
            mImageViewFoto = itemView.findViewById(R.id.img_item_photo);
        }
    }
}