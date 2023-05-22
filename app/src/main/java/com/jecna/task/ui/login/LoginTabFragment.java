package com.jecna.task.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.jecna.task.MainActivity;
import com.jecna.task.R;
import com.jecna.task.databinding.LoginTabFragmentBinding;
import com.jecna.task.model.LoginDTO;
import com.jecna.task.model.LoginResponseDTO;
import com.jecna.task.service.*;

public class LoginTabFragment extends Fragment {
    float v = 0;

    private EditText email;
    private EditText password;
    private Button login;
    private LoginTabFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LoginTabFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        email = binding.email;
        password = binding.password;
        login = binding.loginButton;

        email.setTranslationX(800);
        password.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = email.getText().toString();
                String loginPassword = password.getText().toString();
                ServerClientImpl serverClient = new ServerClientImpl();
                serverClient.setLoginListener(new ServerClientImpl.LoginListener() {
                    @Override
                    public void onLoginFinish(LoginResponseDTO user) {
                        CredentialsService credentialsService = new CredentailsServiceImpl(getContext());
                        credentialsService.WriteFile(new LoginDTO(loginEmail,loginPassword));
                        SingetonToken singletonToken = com.jecna.task.service.SingetonToken.getInstance();
                        singletonToken.setToken(user.getAccessToken());
                        Intent switchActivityIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(switchActivityIntent);
                        Toast.makeText(getContext(), "Welcome back User!", Toast.LENGTH_LONG).show();
                    }
                });
                serverClient.Login(new LoginDTO(loginEmail,loginPassword));
               // Toast.makeText(getContext(), loginEmail, Toast.LENGTH_LONG).show();
            }
        });
    }
}