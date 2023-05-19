package com.jecna.task.service;

import com.jecna.task.model.*;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://guides.codepath.com/android/Creating-Custom-Listeners
public class ServerClientImpl {

    private Retrofit retrofit;

    public interface RegisterListener {
        public void onRegisterFinish(LoginDTO login);

    }

    public interface LoginListener {
        public void onLoginFinish(LoginResponseDTO user);
    }

    private RegisterListener registerListener;
    private LoginListener loginListener;


    // Assign the listener implementing events interface that will receive the events
    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void setLoginListener(LoginListener registerListener) {
        this.loginListener = loginListener;
    }

    public ServerClientImpl() {
        this.registerListener = null;
        this.loginListener = null;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(getToken()))
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://secret-river-54565.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    //@Override
    public void Login(LoginDTO loginDTO) {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<LoginResponseDTO> call = client.loginUser(loginDTO);
        System.out.println(call.toString());
        call.enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.code() == 200) {
                    //if (loginListener != null) {
                        LoginResponseDTO user = (LoginResponseDTO) response.body();
                        loginListener.onLoginFinish(user);
                   // }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    //@Override
    public void Register(RegisterDTO registerDTO) {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<RegisterResponseDTO> call = client.createUser(registerDTO);
        System.out.println(call.toString());
        call.enqueue(new Callback<RegisterResponseDTO>() {
            @Override
            public void onResponse(Call<RegisterResponseDTO> call, Response<RegisterResponseDTO> response) {
                if (response.code() == 200) {
                    if (registerListener != null) {
                        registerListener.onRegisterFinish(new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword()));
                    }else {
                        registerListener.onRegisterFinish(null);
                    }

                }
            }

            ;


            @Override
            public void onFailure(Call<RegisterResponseDTO> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    //@Override
    public void ReadTask() {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<TaskDTO[]> call = client.readTask();
        System.out.println(call.toString());
        call.enqueue(new Callback<TaskDTO[]>() {
            @Override
            public void onResponse(Call<TaskDTO[]> call, Response<TaskDTO[]> response) {
                TaskDTO[] returnTasks = response.body();
                for (TaskDTO t : returnTasks) {
                    System.out.println(t.toString());
                }

            }

            @Override
            public void onFailure(Call<TaskDTO[]> call, Throwable t) {
                System.out.println("Not Saved " + t.toString());
            }
        });
    }

    //@Override
    public void SaveTask(TaskDTO[] taskDTOS) {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<String> call = client.saveTask(taskDTOS);
        System.out.println(call.toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Saved");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Not Saved " + t.toString());
            }
        });
    }

    private String getToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXNjaGEua29taW5la0BnbWFpbC5jb20iLCJpYXQiOjE2ODQzMzMxMzAsImV4cCI6MTY4NDQxOTUzMH0.TSGlQgGfbCjtTCmyRFSqDn2suhGyUFI2y5oL0wpOFv1oyidhfs0pBSOikPjThcT5nqbiK24cPyAEUjWyWJmfLQ";
    }
}
