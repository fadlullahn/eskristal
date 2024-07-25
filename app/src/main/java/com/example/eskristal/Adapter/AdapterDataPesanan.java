package com.example.eskristal.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eskristal.Api.ApiClient;
import com.example.eskristal.Api.ApiInterface;
import com.example.eskristal.Model.Pesanan.DataModelPesanan;
import com.example.eskristal.Model.Pesanan.ResponseModelPesanan;
import com.example.eskristal.PesananDataActivity;
import com.example.eskristal.PesananUbahActivity;
import com.example.eskristal.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDataPesanan extends RecyclerView.Adapter<AdapterDataPesanan.HolderData> {
    private Context ctx;
    private List<DataModelPesanan> listDataPesanan;
    private int idPesanan;
    private SessionManager sessionManager;

    public AdapterDataPesanan(Context ctx, List<DataModelPesanan> listDataPesanan) {
        this.ctx = ctx;
        this.listDataPesanan = listDataPesanan;
        this.sessionManager = new SessionManager(ctx);
        filterDataByLevel();
    }

    private void filterDataByLevel() {
        String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);
        String name = sessionManager.getUserDetail().get(SessionManager.NAME);

        if (level.equals("admin") || level.equals("karyawan")) {

        } else {
            List<DataModelPesanan> filteredList = new ArrayList<>();
            for (DataModelPesanan dm : listDataPesanan) {
                if (dm.getIdUser().equals(name)) {
                    filteredList.add(dm);
                }
            }
            listDataPesanan = filteredList;
        }
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_pesanan, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModelPesanan dm = listDataPesanan.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));

        SpannableString spannabletvIdProduk = new SpannableString("Nama Produk: " + dm.getIdProduk());
        SpannableString spannabletvIdUser = new SpannableString("Nama Pemesan: " + dm.getIdUser());
        SpannableString spannabletvAlamat = new SpannableString("Alamat Lengkap: " + dm.getAlamat());
        SpannableString spannabletvNohp = new SpannableString("No.Hp: " + dm.getNohp());
        SpannableString spannabletvLevel = new SpannableString("Jumlah Pesanan: " + dm.getLevel());
        SpannableString spannabletvPassword = new SpannableString("Total Pembayaran: Rp." + dm.getPassword());

        holder.tvIdProduk.setText(spannabletvIdProduk);
        holder.tvIdUser.setText(spannabletvIdUser);
        holder.tvAlamat.setText(spannabletvAlamat);
        holder.tvNohp.setText(spannabletvNohp);
        holder.tvLevel.setText(spannabletvLevel);
        holder.tvPassword.setText(spannabletvPassword);
        holder.tvProses.setText(dm.getProses());
    }

    @Override
    public int getItemCount() {
        return listDataPesanan.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvAlamat, tvIdProduk, tvIdUser, tvNohp, tvLevel, tvPassword, tvProses;

        Button btnAction;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvIdProduk = itemView.findViewById(R.id.tv_id_produk);
            tvIdUser = itemView.findViewById(R.id.tv_id_user);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvNohp = itemView.findViewById(R.id.tv_nohp);
            tvLevel = itemView.findViewById(R.id.tv_level);
            tvPassword = itemView.findViewById(R.id.tv_password);
            tvProses = itemView.findViewById(R.id.tv_proses);
            btnAction = itemView.findViewById(R.id.btn_action); // Inisialisasi tombol

            String level = sessionManager.getUserDetail().get(SessionManager.LEVEL);

            if (level.equals("karyawan")) {
                btnAction.setVisibility(View.VISIBLE); // Tampilkan tombol untuk karyawan
                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String proses = tvProses.getText().toString();
                        idPesanan = Integer.parseInt(tvId.getText().toString());

                        if ("diproses".equals(proses)) {
                            antarPesanan();
                        } else if ("diantar".equals(proses)) {
                            selesaiPesanan();
                        }
                    }
                });
            } else {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        String proses = tvProses.getText().toString();
                        AlertDialog.Builder dialogPesan = new AlertDialog.Builder(ctx);
                        dialogPesan.setMessage("Pilih Operasi yang Akan Dilakukan");
                        dialogPesan.setTitle("Perhatian");
                        dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                        dialogPesan.setCancelable(true);

                        idPesanan = Integer.parseInt(tvId.getText().toString());

                        if (level.equals("admin") || level.equals("user")) {
                            dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteDataPesanan();
                                    dialogInterface.dismiss();
                                    Handler hand = new Handler();
                                    hand.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((PesananDataActivity) ctx).retrieveDataPesanan();
                                        }
                                    }, 1000);
                                }
                            });
                        }
                        dialogPesan.show();
                        return false;
                    }
                });
            }
        }

        private void antarPesanan() {
            // Logika untuk mengantar pesanan
            getDataPesanan();
        }

        private void selesaiPesanan() {
            // Logika untuk menyelesaikan pesanan
            getDataPesanan();
        }

        private void getDataPesanan() {
            ApiInterface ardDataPesanan = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModelPesanan> ambilData = ardDataPesanan.ardGetDataPesanan(idPesanan);

            ambilData.enqueue(new Callback<ResponseModelPesanan>() {
                @Override
                public void onResponse(Call<ResponseModelPesanan> call, Response<ResponseModelPesanan> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listDataPesanan = response.body().getDataPesanan();

                    int varIdPesanan = listDataPesanan.get(0).getId();
                    String varIdProduk = listDataPesanan.get(0).getIdProduk();
                    String varIdUser = listDataPesanan.get(0).getIdUser();
                    String varAlamat = listDataPesanan.get(0).getAlamat();
                    String varNohp = listDataPesanan.get(0).getNohp();
                    String varLevel = listDataPesanan.get(0).getLevel();
                    String varPassword = listDataPesanan.get(0).getPassword();
                    String varProses = listDataPesanan.get(0).getProses();

                    Intent kirim = new Intent(ctx, PesananUbahActivity.class);
                    kirim.putExtra("xId", varIdPesanan);
                    kirim.putExtra("xAlamat", varAlamat);
                    kirim.putExtra("xIdProduk", varIdProduk);
                    kirim.putExtra("xIdUser", varIdUser);
                    kirim.putExtra("xNohp", varNohp);
                    kirim.putExtra("xLevel", varLevel);
                    kirim.putExtra("xPassword", varPassword);
                    kirim.putExtra("xProses", varProses);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModelPesanan> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void deleteDataPesanan() {
            ApiInterface ardDataPesanan = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModelPesanan> hapusDataPesanan = ardDataPesanan.ardDeleteDataPesanan(idPesanan);

            hapusDataPesanan.enqueue(new Callback<ResponseModelPesanan>() {
                @Override
                public void onResponse(Call<ResponseModelPesanan> call, Response<ResponseModelPesanan> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModelPesanan> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
