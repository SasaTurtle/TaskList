package com.jecna.task;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.jecna.task.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import com.jecna.task.model.LoginDTO;
import com.jecna.task.model.LoginResponseDTO;
import com.jecna.task.model.TaskDTO;
import com.jecna.task.service.*;
import com.jecna.task.ui.login.LoginTabFragment;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        CredentialsService credentialsService =new CredentailsServiceImpl(getApplicationContext());
        LoginDTO loginDTO =  credentialsService.ReadFile();
        if(loginDTO==null){
            Intent switchActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(switchActivityIntent);
        }else{
            ServerClientImpl serverClient = new ServerClientImpl();
            serverClient.setLoginListener(new ServerClientImpl.LoginListener() {
                @Override
                public void onLoginFinish(LoginResponseDTO user) {
                    SingetonToken singletonToken = com.jecna.task.service.SingetonToken.getInstance();
                    singletonToken.setToken(user.getAccessToken());
                    //Toast.makeText(this, "User is logged", Toast.LENGTH_LONG).show();
                }
            });
            serverClient.Login(loginDTO);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.login) {
            Intent switchActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(switchActivityIntent);
        }
        if (id == R.id.logout) {

        }
        if (id == R.id.synch) {
            SingetonToken singletonToken = com.jecna.task.service.SingetonToken.getInstance();
            String token = singletonToken.getToken();
            ServerClientImpl serverClient = new ServerClientImpl(token);
            Activity a = this;
            serverClient.setReadTaskListener(new ServerClientImpl.ReadTaskListener() {
                @Override
                public void onReadTaskFinish(TaskDTO[] taskDTOS) throws IOException, ParseException, ClassNotFoundException {
                    DataService dataService = new DataServiceImpl(a);
                    dataService.synchTasks(taskDTOS);

                 //   Fragment currentFragment = a.getFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

                    //if (currentFragment instanceof FirstFragment) {
                     //   FragmentTransaction fragTransaction =   (a).getFragmentManager().beginTransaction();
                     //   fragTransaction.detach(currentFragment);
                      //  fragTransaction.attach(currentFragment);
                      //  fragTransaction.commit();
                    //}


                }
            });
            serverClient.ReadTask();
            Toast.makeText(this,"Tasks synchronized from server", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}