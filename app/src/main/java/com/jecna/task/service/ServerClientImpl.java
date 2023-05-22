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

    public interface SaveTaskListener {
        public void onSaveTaskFinish();
    }

    public interface ReadTaskListener {
        public void onReadTaskFinish(TaskDTO[] taskDTOS);
    }

    private RegisterListener registerListener;
    private LoginListener loginListener;
    private SaveTaskListener saveTaskListener;
    private ReadTaskListener readTaskListener;


    // Assign the listener implementing events interface that will receive the events
    public void setRegisterListener(RegisterListener registerListener) {
        this.registerListener = registerListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setSaveTaskListener(SaveTaskListener saveTaskListener) {
        this.saveTaskListener = saveTaskListener;
    }
    public void setReadTaskListener(ReadTaskListener readTaskListener) {
        this.readTaskListener = readTaskListener;
    }

    public ServerClientImpl() {
        this.registerListener = null;
        this.loginListener = null;
        this.saveTaskListener = null;
        this.readTaskListener = null;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://secret-river-54565.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ServerClientImpl(String token) {
        this.registerListener = null;
        this.loginListener = null;
        this.saveTaskListener = null;
        this.readTaskListener = null;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor(token))
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
                    if (loginListener != null) {
                        LoginResponseDTO user = (LoginResponseDTO) response.body();
                        loginListener.onLoginFinish(user);
                    }
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

            @Override
            public void onFailure(Call<RegisterResponseDTO> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }

    //@Override
    public TaskDTO[] ReadTask() {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<TaskDTO[]> call = client.readTask();
        try {
            Response<TaskDTO[]> response = call.execute();
            return response.body();
        }
        catch (Exception ex)
        {
            return new TaskDTO[0];
        }
    }

    //@Override
    public void SaveTask(TaskDTO[] taskDTOS) {
        IUserCall client = retrofit.create(IUserCall.class);
        Call<String> call = client.saveTask(taskDTOS);
        System.out.println(call.toString());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                saveTaskListener.onSaveTaskFinish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Not Saved " + t.toString());
            }
        });
    }


}
