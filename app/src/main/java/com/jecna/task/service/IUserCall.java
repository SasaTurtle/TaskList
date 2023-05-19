package com.jecna.task.service;

import com.jecna.task.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUserCall {
    @POST("api/auth/signin")
    Call<LoginResponseDTO> loginUser(@Body LoginDTO user);
    @POST("api/auth/signup")
    Call<RegisterResponseDTO> createUser(@Body RegisterDTO user);

    @POST("api/task/")
    Call<String> saveTask(@Body TaskDTO[] tasks);
    @GET("api/task/")
    Call<TaskDTO[]> readTask();
}
