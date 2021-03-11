package com.appsinventiv.realcaller.Utils;


import com.appsinventiv.realcaller.Models.SaveContactModel;
import com.appsinventiv.realcaller.NetworkResponses.ApiResponse;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserClient {

    @POST("api/uploadFile")
    @Multipart
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file, @Part("photo") RequestBody name

    );


    @Headers("Content-Type: application/json")
    @POST("api/login")
    Call<ApiResponse> login(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/saveContactList")
    Call<ApiResponse> saveContactList(
            @Header("Authorization") String auth,
            @Body SaveContactModel jsonObject

    );

    @Headers("Content-Type: application/json")
    @GET("api/getblockedContacts")
    Call<ApiResponse> getblockedContacts(
            @Header("Authorization") String auth
    );

    @Headers("Content-Type: application/json")
    @GET("api/myProfileViewers")
    Call<ApiResponse> myProfileViewers(
            @Header("Authorization") String auth
    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/deleteAd")
    Call<ApiResponse> deleteAd(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/signup")
    Call<ApiResponse> signup(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/user/social")
    Call<ApiResponse> social(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/user/completeProfile")
    Call<ApiResponse> completeProfile(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/createAd")
    Call<ApiResponse> createAd(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/editAd")
    Call<ApiResponse> editAd(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/feedback/submitFeedback")
    Call<ApiResponse> submitFeedback(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/allAds")
    Call<ApiResponse> allAds(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/payment/payForAd")
    Call<ApiResponse> payForAd(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/search")
    Call<ApiResponse> search(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @GET("api/searchByPhone/{id}")
    Call<ApiResponse> searchByPhone(
            @Path("id") String id,
            @Header("Authorization") String auth

    );

    @Headers("Content-Type: application/json")
    @GET("api/blockNumber/{id}")
    Call<ApiResponse> blockNumber(
            @Path("id") String id,
            @Header("Authorization") String auth

    );

    @Headers("Content-Type: application/json")
    @POST("api/ad/adDetails")
    Call<ApiResponse> adDetails(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/post/createPost")
    Call<ApiResponse> createPost(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/post/allPosts")
    Call<ApiResponse> allPosts(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/room/createRoom")
    Call<ApiResponse> createRoom(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/message/allRoomMessages")
    Call<ApiResponse> allRoomMessages(
            @Body JsonObject jsonObject

    );


    @Headers("Content-Type: application/json")
    @POST("api/message/createMessage")
    Call<ApiResponse> createMessage(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/user/updateFcmKey")
    Call<ApiResponse> updateFcmKey(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/message/userMessages")
    Call<ApiResponse> userMessages(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/user/userProfile")
    Call<ApiResponse> userProfile(
            @Body JsonObject jsonObject

    );

    @POST("api/uploadFile")
    @Multipart
    Call<ResponseBody> uploadAudioFile(
            @Part MultipartBody.Part file, @Part("audio") RequestBody name

    );

    @POST("api/uploadFile")
    @Multipart
    Call<ResponseBody> uploadVideoFile(
            @Part MultipartBody.Part file, @Part("video") RequestBody name

    );


    @Headers("Content-Type: application/json")
    @POST("api/comments/getAllComments")
    Call<ApiResponse> getAllComments(
            @Body JsonObject jsonObject

    );

    @Headers("Content-Type: application/json")
    @POST("api/comments/addComment")
    Call<ApiResponse> addComment(
            @Body JsonObject jsonObject

    );

}
