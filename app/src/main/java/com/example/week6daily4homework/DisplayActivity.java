package com.example.week6daily4homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DisplayActivity extends AppCompatActivity {
    TextView tvDisplay;
    EditText etNewPass;
    UserDatabaseHelper db;
    private static final String TAG = "MainActivityTag";
    private CipherWrapper cipherWrapper;
    private KeystoreWrapper keystoreWrapper;
    private String alias = "Master Key";
    private KeyPair masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        db = new UserDatabaseHelper(this);

        tvDisplay = findViewById(R.id.tvDisplay);
        etNewPass = findViewById(R.id.etNewPassword);
        Intent intent = getIntent();
        String password = intent.getStringExtra("password");
        tvDisplay.setText(password);

        try{
            initWrappers();

            keystoreWrapper.createKeyPair(alias);
            masterKey = keystoreWrapper.getAsymKey(alias);

            String plainText = etNewPass.getText().toString();
            Log.d(TAG, "onCreate: PlainText: " + plainText);
            String encryptedData = cipherWrapper.encrypt(plainText, masterKey.getPublic());
            Log.d(TAG, "onCreate: EncryptedData: " + encryptedData);

        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidAlgorithmParameterException
                | UnrecoverableKeyException
                | NoSuchProviderException
                | BadPaddingException
                | KeyStoreException
                | InvalidKeyException
                | IllegalBlockSizeException
                | CertificateException
                | IOException e){
            e.printStackTrace();
        }
    }

    private void initWrappers() throws NoSuchAlgorithmException, NoSuchPaddingException, CertificateException, KeyStoreException, IOException{
        cipherWrapper = new CipherWrapper("RSA/ECB/PKCS1Padding");
        keystoreWrapper = new KeystoreWrapper(getApplicationContext());
    }

    public void onNewClick(View view) {
        try{
            masterKey = keystoreWrapper.getAsymKey(alias);

            String plainText = "Hello world, encrypt me!";
            Log.d(TAG, "onCreate: PlainText: " + plainText);
            String encryptedData = cipherWrapper.encrypt(etNewPass.getText().toString(), masterKey.getPublic());
            Log.d(TAG, "onCreate: EncryptedData: " + encryptedData);

        } catch (NoSuchAlgorithmException
                | UnrecoverableKeyException
                | BadPaddingException
                | KeyStoreException
                | InvalidKeyException
                | IllegalBlockSizeException e){
            e.printStackTrace();
        }
    }
}
