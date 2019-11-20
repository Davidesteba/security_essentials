package com.example.security_essentials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    static String clave = "davidSanchez2019";
    private static SecretKeySpec secret;
    private EditText editTextEmailR, editTextPasswordR, editTextRepPassword;
    private Button buttonRegistrar, buttonRegresar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmailR = (EditText) findViewById(R.id.editTextEmail);
        editTextPasswordR = (EditText) findViewById(R.id.editTextPassword);
        editTextRepPassword = (EditText) findViewById(R.id.editTextRepPassword);
        buttonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        buttonRegresar = (Button) findViewById(R.id.buttonRegresar);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmailR.getText().toString().isEmpty() && editTextPasswordR.getText().toString().isEmpty() && editTextRepPassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Digite Correo y Contraseña",
                            Toast.LENGTH_SHORT).show();
                } else if (!editTextPasswordR.getText().toString().equals(editTextRepPassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no son iguales",
                            Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        byte[] claveEncriptada = encryptMsg(editTextPasswordR.getText().toString(), generateKey());
                        String claveDesencriptada = decryptMsg(claveEncriptada, generateKey());
                        mAuth.createUserWithEmailAndPassword(editTextEmailR.getText().toString(), claveDesencriptada)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                            Intent cambio = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(cambio);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Error al Registrar", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeyException {
        return secret = new SecretKeySpec(clave.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
}
