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
import androidx.navigation.fragment.NavHostFragment;
import com.jecna.task.FirstFragment;
import com.jecna.task.LoginActivity;
import com.jecna.task.MainActivity;
import com.jecna.task.R;
import com.jecna.task.databinding.RegisterTabFragmentBinding;
import com.jecna.task.model.LoginDTO;
import com.jecna.task.model.LoginResponseDTO;
import com.jecna.task.model.RegisterDTO;
import com.jecna.task.service.*;

public class RegisterTabFragment extends Fragment{

    float v = 0;

    private EditText email;
    private EditText password;
    private Button register;
    private RegisterTabFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RegisterTabFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        email = binding.email;
        password = binding.password;
        register = binding.registerButton;

        email.setTranslationX(800);
        password.setTranslationX(800);
        register.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        register.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        register.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();



        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String registerEmail = email.getText().toString();
                String registerPassword = password.getText().toString();
                ServerClientImpl serverClient = new ServerClientImpl();
                serverClient.setRegisterListener(new ServerClientImpl.RegisterListener(){
                    @Override
                    public void onRegisterFinish(LoginResponseDTO login){
                        if(login!=null){
                            CredentialsService credentialsService = new CredentailsServiceImpl(getContext());
                            credentialsService.WriteFile(new LoginDTO(registerEmail,registerPassword));
                            SingetonToken singletonToken = com.jecna.task.service.SingetonToken.getInstance();
                            singletonToken.setToken(login.getAccessToken());
                            Bundle bundle = new Bundle();
                            bundle.getSerializable("user");
                            NavHostFragment.findNavController(RegisterTabFragment.this).navigate(R.id.action_RegisterToLogin, bundle);
                            Toast.makeText(getContext(),R.string.sucess_register, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(),R.string.username_used, Toast.LENGTH_LONG).show();
                        }
                    }

                });

                serverClient.Register(new RegisterDTO(registerEmail,registerEmail,new String[]{"ROLE_USER"},registerPassword));
            }
        });
    }
}
