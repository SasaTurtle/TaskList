package com.jecna.task.service;

import android.content.Context;
import com.jecna.task.model.LoginDTO;

import java.io.*;

public class CredentailsServiceImpl implements CredentialsService {
    private Context context;
    private File file;

    public CredentailsServiceImpl(Context context) {
        this.context = context;
        file = new File(context.getFilesDir(), "credentials.ser");
    }

    @Override
    public LoginDTO ReadFile(){

        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                LoginDTO loginDTO = (LoginDTO) ois.readObject();
                ois.close();
                return loginDTO;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void WriteFile (LoginDTO loginDTO) {
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(loginDTO);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
