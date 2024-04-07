package com.example.food2.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food2.Domain.Users;
import com.example.food2.R;
import com.example.food2.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistroActivity extends BaseActivity {

    ActivityRegistroBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onBackPressed() {
        // Ir a IntroActivity
        startActivity(new Intent(this, IntroActivity.class));
        finish(); // Finalizar RegistroActivity para que no se pueda volver atrás a ella
    }

    private boolean passAcept(String pass) {
        if (pass.length() >= 6) {
            boolean hasUppercase = false;
            boolean hasLowercase = false;

            for (int i = 0; i < pass.length(); i++) {
                char c = pass.charAt(i);
                if (Character.isUpperCase(c)) {
                    hasUppercase = true;
                } else if (Character.isLowerCase(c)) {
                    hasLowercase = true;
                }
            }

            return hasUppercase && hasLowercase;
        }

        return false;
    }

    private void setVariable() {

        binding.logTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
            }
        });

        binding.RegistroBtn.setOnClickListener(v -> {
            String nombre = binding.nombreEdt.getText().toString();
            String apellido = binding.apellidoEdt.getText().toString();
            String email = binding.emailEdt.getText().toString();
            String password = binding.passwrdEdt.getText().toString();
            String passwordR = binding.passwrdEdtR.getText().toString();

            if(!passAcept(password)){
                Toast.makeText(RegistroActivity.this, "La contraseña debe tener al menos 6 carácteres, una mayúscula y una minúscula", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(passwordR)) {
                Toast.makeText(RegistroActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * TODO: TERMINAR LA CONFIGURACIÓN DEL EMAIL DE VERIFICACIÓN,
             * COMPROBAR SI LLEGA EL CORREO, SI NO LLEGA NO REGISTRES AL USER
             */

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

            Query query = databaseReference.orderByChild("email").equalTo(email);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(RegistroActivity.this, "Ese correo ya está en uso", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){

                                            FirebaseUser user = mAuth.getCurrentUser();

                                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        // Registro exitoso
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                                        String userId = ref.push().getKey();

                                                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                        // Crear objeto Users
                                                        Users nuevoUsuario = new Users(nombre, apellido, user.getEmail(), userId, userUid);

                                                        // Guardar usuario en la base de datos
                                                        ref = FirebaseDatabase.getInstance().getReference("Users").child(userUid);
                                                        ref.setValue(nuevoUsuario)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        // Usuario guardado exitosamente en la base de datos
                                                                        Log.i(TAG, "Usuario registrado exitosamente");

                                                                        Context context = RegistroActivity.this;

                                                                        Dialog dialogVerify = new Dialog(context);
                                                                        dialogVerify.setContentView(R.layout.pop_up_verifica_email);
                                                                        dialogVerify.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                                        dialogVerify.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_dialog_bg));
                                                                        dialogVerify.setCancelable(false);

                                                                        Button cerrarDialogVerify = dialogVerify.findViewById(R.id.confirmPop);

                                                                        dialogVerify.show();

                                                                        cerrarDialogVerify.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                dialogVerify.dismiss();
                                                                                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                                                                            }
                                                                        });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Error al guardar usuario en la base de datos
                                                                        Log.i(TAG, "Error al guardar usuario en la base de datos", e);
                                                                        Toast.makeText(RegistroActivity.this, "Error al guardar usuario en la base de datos", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    } else {
                                                        Toast.makeText(RegistroActivity.this, "Email no encontrado", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            // Error en el registro
                                            Log.i(TAG, "Error en el registro", task.getException());
                                            Toast.makeText(RegistroActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(RegistroActivity.this, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
                }
            });


        });
    }
}