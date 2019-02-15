package com.example.week6daily4homework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    UserDatabaseHelper db;
    EditText etUsername, etPassword;
    TextView tvResult;
    private static final String TAG = "MainActivityTag";
    private CipherWrapper cipherWrapper;
    private KeystoreWrapper keystoreWrapper;
    private String alias = "Master Key";
    private KeyPair masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        try{
            initWrappers();

            keystoreWrapper.createKeyPair(alias);
            masterKey = keystoreWrapper.getAsymKey(alias);
            db = new UserDatabaseHelper(this);
            String plainText = "Hello world, encrypt me!";
            Log.d(TAG, "onCreate: PlainText: " + plainText);

            String encryptedData = cipherWrapper.encrypt(plainText, masterKey.getPublic());
            Log.d(TAG, "onCreate: EncryptedData: " + encryptedData);

            String decryptedData = cipherWrapper.decrypt(encryptedData, masterKey.getPrivate());
            Log.d(TAG, "onCreate: DecryptedData: " + decryptedData);
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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                if(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    try {
                        String plainTextPassword = etPassword.getText().toString();
                        Log.d(TAG, "onCreate: PlainText: " + plainTextPassword);
                        String encryptedData = cipherWrapper.encrypt(plainTextPassword, masterKey.getPublic());
                        Log.d(TAG, "onCreate: EncryptedData: " + encryptedData);
                        User user = new User(etUsername.getText().toString(), encryptedData);
                        db.insertNewUser(user);
                        Toast.makeText(this, "User has been created", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            case R.id.btnSignIn:
                if(!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    try {
                        String decryptedData = cipherWrapper.decrypt(db.getSingleUserByName(etUsername.getText().toString()).getPassword(), masterKey.getPrivate());
                        Log.d(TAG, "onCreate: DecryptedData: " + decryptedData);
                        User user = db.getSingleUserByName(etUsername.getText().toString());
                        String s = cipherWrapper.transformation;
                        if(etPassword.getText().toString().equals(decryptedData)){
                            Intent intent = new Intent(this, DisplayActivity.class);
                            intent.putExtra("password", etPassword.getText().toString());
                            intent.putExtra("cipher", s);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
        }
    }

    private void initWrappers() throws NoSuchAlgorithmException, NoSuchPaddingException, CertificateException, KeyStoreException, IOException {
        cipherWrapper = new CipherWrapper("RSA/ECB/PKCS1Padding");
        keystoreWrapper = new KeystoreWrapper(getApplicationContext());
    }
}
