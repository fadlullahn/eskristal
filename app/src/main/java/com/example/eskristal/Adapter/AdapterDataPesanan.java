package com.example.eskristal.Adapter;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Config;
import com.example.eskristal.Model.Pesanan.Pesanan;
import com.example.eskristal.Model.Pesanan.PostPutDelPesanan;
import com.example.eskristal.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDataPesanan extends RecyclerView.Adapter<AdapterDataPesanan.MyViewHolder> {
    List<Pesanan> mPesananList;
    public AdapterDataPesanan(List<Pesanan> PesananList) {
        mPesananList = PesananList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_pesanan, parent, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // Set data pada TextView dan ImageView
        holder.mTextViewProduk.setText(mPesananList.get(position).getProduk());
        holder.mTextViewDate.setText(mPesananList.get(position).getDate());
        holder.mTextViewProses.setText(mPesananList.get(position).getProses());
        holder.mTextViewUser.setText(mPesananList.get(position).getUser());

        SpannableString spannablemTextViewPrice = new SpannableString("Rp." + mPesananList.get(position).getPrice());
        holder.mTextViewPrice.setText(spannablemTextViewPrice);

        Glide.with(holder.itemView.getContext())
                .load(Config.IMAGES_URL + mPesananList.get(position).getImage())
                .apply(new RequestOptions().override(350, 350))
                .into(holder.mImageViewFoto);
        SessionManager sessionManager = new SessionManager(holder.itemView.getContext());
        String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

        // Tentukan visibilitas tombol berdasarkan level pengguna menggunakan if-else
        if (level != null) {
            if (level.equals("karyawan")) {
                holder.btnAction.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.GONE);
            } else if (level.equals("admin") || level.equals("user")) {
                holder.btnAction.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                holder.btnAction.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
            }
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idToDelete = mPesananList.get(position).getId(); // Simpan ID

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<PostPutDelPesanan> call = apiInterface.deletePesanan(idToDelete);
                call.enqueue(new Callback<PostPutDelPesanan>() {
                    @Override
                    public void onResponse(Call<PostPutDelPesanan> call, Response<PostPutDelPesanan> response) {
                        if (response.isSuccessful()) {
                            // Data berhasil dihapus
                            mPesananList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mPesananList.size());
                            Toast.makeText(holder.itemView.getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            // Gagal menghapus data
                            Toast.makeText(holder.itemView.getContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostPutDelPesanan> call, Throwable t) {
                        Toast.makeText(holder.itemView.getContext(), "Koneksi gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cek status saat ini
                final String currentStatus = mPesananList.get(position).getProses(); // Ambil status saat ini
                final String idToUpdate = mPesananList.get(position).getId(); // Simpan ID

                // Tentukan status baru berdasarkan status saat ini
                String newStatus;
                if ("diproses".equals(currentStatus)) {
                    newStatus = "diantar";
                } else if ("diantar".equals(currentStatus)) {
                    newStatus = "selesai";
                } else {
                    newStatus = currentStatus; // Jika status tidak cocok dengan yang diharapkan, tetap gunakan status saat ini
                }

                // Update status pesanan
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<PostPutDelPesanan> call = apiInterface.updateStatusPesanan(idToUpdate, newStatus);
                call.enqueue(new Callback<PostPutDelPesanan>() {
                    @Override
                    public void onResponse(Call<PostPutDelPesanan> call, Response<PostPutDelPesanan> response) {
                        if (response.isSuccessful()) {
                            // Status berhasil diperbarui
                            mPesananList.get(position).setProses(newStatus); // Update status di UI
                            notifyItemChanged(position); // Beritahu adapter untuk memperbarui item
                            Toast.makeText(holder.itemView.getContext(), "Status pesanan diperbarui", Toast.LENGTH_SHORT).show();
                        } else {
                            // Gagal memperbarui status
                            Toast.makeText(holder.itemView.getContext(), "Gagal memperbarui status", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostPutDelPesanan> call, Throwable t) {
                        // Tangani kegagalan jaringan
                        Toast.makeText(holder.itemView.getContext(), "Koneksi gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




    }

    @Override
    public int getItemCount() {
        return mPesananList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewProduk, mTextViewPrice, mTextViewDate, mTextViewProses, mTextViewUser;
        public ImageView mImageViewFoto;
        public Button btnDelete, btnAction;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewProduk = (TextView) itemView.findViewById(R.id.tv_item_produk);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            mTextViewDate = (TextView) itemView.findViewById(R.id.tv_item_date);
            mTextViewProses = (TextView) itemView.findViewById(R.id.tv_item_proses);
            mTextViewUser = (TextView) itemView.findViewById(R.id.tv_item_user);
            mImageViewFoto = itemView.findViewById(R.id.img_item_photo);
            btnDelete = itemView.findViewById(R.id.btn_delete); // Inisialisasi tombol hapus
            btnAction = itemView.findViewById(R.id.btn_action); // Inisialisasi tombol aksi
        }
    }

}