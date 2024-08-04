package com.example.eskristal.Api;

import com.example.eskristal.Model.Login.Login;
import com.example.eskristal.Model.Pesanan.GetPesanan;
import com.example.eskristal.Model.Pesanan.PostPutDelPesanan;
import com.example.eskristal.Model.Produk.GetProduk;
import com.example.eskristal.Model.Produk.PostPutDelProduk;
import com.example.eskristal.Model.Register.Register;
import com.example.eskristal.Model.User.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<Login> loginResponse(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Register> registerResponse(
            @Field("username") String username,
            @Field("password") String password,
            @Field("name") String name,
            @Field("level") String level
    );

    @GET("user/retrieve.php")
    Call<ResponseModel> ardRetrieveData();

    @FormUrlEncoded
    @POST("user/create.php")
    Call<ResponseModel> ardCreateData(
            @Field("name") String name,
            @Field("username") String username,
            @Field("level") String level,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/delete.php")
    Call<ResponseModel> ardDeleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("user/get.php")
    Call<ResponseModel> ardGetData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("user/update.php")
    Call<ResponseModel> ardUpdateData(
            @Field("id") int id,
            @Field("name") String name,
            @Field("username") String username,
            @Field("level") String level,
            @Field("password") String password
    );

    @GET("pesanan.php")
    Call<GetPesanan> getPesanan();

    @Multipart
    @POST("pesanan.php")
    Call<PostPutDelPesanan> postPesanan(@Part MultipartBody.Part image,
                                      @Part("produk") RequestBody produk,
                                        @Part("user") RequestBody user,
                                      @Part("price") RequestBody price,
                                      @Part("date") RequestBody date,
                                        @Part("alamat") RequestBody alamat,
                                        @Part("nohp") RequestBody nohp,
                                        @Part("jumlah") RequestBody jumlah,
                                        @Part("proses") RequestBody proses,
                                      @Part("flag") RequestBody flag);

    @FormUrlEncoded
    @POST("update_proses.php")
    Call<PostPutDelPesanan> updateStatusPesanan(
            @Field("id") String id,
            @Field("proses") String status
    );


    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "pesanan.php", hasBody = true)
    Call<PostPutDelPesanan> deletePesanan(@Field("id") String id);



    @GET("produk.php")
    Call<GetProduk> getHeros();

    @Multipart
    @POST("produk.php")
    Call<PostPutDelProduk> postHeros(@Part MultipartBody.Part image,
                                     @Part("name") RequestBody name,
                                     @Part("price") RequestBody price,
                                     @Part("date") RequestBody date,
                                     @Part("flag") RequestBody flag);

    @Multipart
    @POST("produk.php")
    Call<PostPutDelProduk> postUpdateHeros(@Part MultipartBody.Part image,
                                           @Part("id") RequestBody id,
                                           @Part("name") RequestBody name,
                                           @Part("price") RequestBody price,
                                           @Part("date") RequestBody date,
                                           @Part("flag") RequestBody flag);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "produk.php", hasBody = true)
    Call<PostPutDelProduk> deleteHeros(@Field("id") String id);


}

