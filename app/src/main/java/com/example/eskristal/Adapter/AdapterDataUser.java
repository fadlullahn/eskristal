package com.example.eskristal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.User.DataModel;
import com.example.eskristal.Model.User.ResponseModel;
import com.example.eskristal.R;
import com.example.eskristal.UserDataActivity;
import com.example.eskristal.UserUbahActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDataUser extends RecyclerView.Adapter<AdapterDataUser.HolderData> {
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listUser;
    private int idUser;

    public AdapterDataUser(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_user, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));

        SpannableString spannableName = new SpannableString("Name: " + dm.getName());
        SpannableString spannableUsername = new SpannableString("Username: " + dm.getUsername());
        SpannableString spannableLevel = new SpannableString("Level: " + dm.getLevel());
        SpannableString spannablePassword = new SpannableString("Password: " + dm.getPassword());

        holder.tvName.setText(spannableName);
        holder.tvUsername.setText(spannableUsername);
        holder.tvPassword.setText(spannablePassword);
        holder.tvLevel.setText(spannableLevel);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvUsername, tvLevel, tvPassword;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvLevel = itemView.findViewById(R.id.tv_level);
            tvPassword = itemView.findViewById(R.id.tv_password);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                    dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    idUser = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((UserDataActivity) ctx).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Ubah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                            dialogInterface.dismiss();
                        }
                    });

                    dialogPesan.show();

                    return false;
                }
            });
        }

        private void deleteData() {
            ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> hapusData = ardData.ardDeleteData(idUser);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData() {
            ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> ambilData = ardData.ardGetData(idUser);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listUser = response.body().getData();

                    int varIdUser = listUser.get(0).getId();
                    String varName = listUser.get(0).getName();
                    String varUsername = listUser.get(0).getUsername();
                    String varLevel = listUser.get(0).getLevel();
                    String varPassword = listUser.get(0).getPassword();

                    Intent kirim = new Intent(ctx, UserUbahActivity.class);
                    kirim.putExtra("xId", varIdUser);
                    kirim.putExtra("xName", varName);
                    kirim.putExtra("xUsername", varUsername);
                    kirim.putExtra("xLevel", varLevel);
                    kirim.putExtra("xPassword", varPassword);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
