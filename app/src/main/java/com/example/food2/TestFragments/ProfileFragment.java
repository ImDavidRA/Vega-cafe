package com.example.food2.TestFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.food2.Activity.LoginActivity;
import com.example.food2.Activity.RegistroActivity;
import com.example.food2.R;
import androidx.core.content.ContextCompat;

import com.example.food2.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    String userId;
    FragmentProfileBinding binding;
    Context context;
    ImageView pic; //TODO: DEBES PONER ALGO PARA PODER CAMBIAR LA FOTO
    FirebaseUser user;
    DatabaseReference userRef;
    Dialog dialogIdioma, dialogPass, dialogPerfil;
    Button cerrarDialogIdioma, cerrarDialogPass, aceptarDialogPass, aceptarDialogPerfil, cerrarDialogPerfil, o3, o2, o1;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        context = getContext();

        user = FirebaseAuth.getInstance().getCurrentUser();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        setVariable();

    }

    private void setVariable() {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtener el nombre del usuario
                    String userName = dataSnapshot.child("name").getValue(String.class);
                    String lastNameC = dataSnapshot.child("lastName").getValue(String.class);

                    String nombre = userName + " " + lastNameC;

                    // Establece el nombre del usuario en el TextView userName
                    binding.nameFirstnameTxt.setText(nombre);

                    // Obtener el email del usuario
                    String email = user.getEmail();

                    binding.emailTxt.setText(email);

                } else {
                    binding.nameFirstnameTxt.setText("Usuario");
                    binding.emailTxt.setText("Email");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                binding.nameFirstnameTxt.setText("Error");
                binding.emailTxt.setText("Error");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        pic = binding.userPic;
        pic.bringToFront();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ////////////// PRUEBAS DIALOG //////////////

        ////// Dialog Idioma //////
        dialogIdioma = new Dialog(context);
        dialogIdioma.setContentView(R.layout.pop_up_idioma);
        dialogIdioma.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogIdioma.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.custom_dialog_bg));
        dialogIdioma.setCancelable(false);
        ////// Dialog Idioma //////

        ////// Dialog Contraseña //////
        dialogPass = new Dialog(context);
        dialogPass.setContentView(R.layout.pop_up_password);
        dialogPass.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPass.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.custom_dialog_bg));
        dialogPass.setCancelable(false);
        ////// Dialog Contraseña //////

        ////// Dialog Perfil //////
        dialogPerfil = new Dialog(context);
        dialogPerfil.setContentView(R.layout.pop_up_perfil_edit);
        dialogPerfil.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogPerfil.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.custom_dialog_bg));
        dialogPerfil.setCancelable(false);
        ////// Dialog Perfil //////

        // Botones del perfil
        o1 = binding.o1;
        o2 = binding.o2;
        o3 = binding.o3;
        // Botones del perfil

        // Botones de cerrar de los pop-ups
        cerrarDialogPerfil = dialogPerfil.findViewById(R.id.cancelPop);
        cerrarDialogPass = dialogPass.findViewById(R.id.cancelPop);
        cerrarDialogIdioma = dialogIdioma.findViewById(R.id.cancelPop);
        // Botones de cerrar de los pop-ups

        // Botones de aceptar de los pop-ups
        aceptarDialogPass = dialogPass.findViewById(R.id.confirmPop);
        aceptarDialogPerfil = dialogPerfil.findViewById(R.id.confirmPop);
        // Botones de aceptar de los pop-ups

        aceptarDialogPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newNameEdt = dialogPerfil.findViewById(R.id.newName);
                EditText newLastNameEdt = dialogPerfil.findViewById(R.id.newLastName);
                EditText newEmailEdt = dialogPerfil.findViewById(R.id.newEmail);
                EditText passEdt = dialogPerfil.findViewById(R.id.passTxt);

                // Mayuscula primera letra del nombre
                String newNameAA = newNameEdt.getText().toString();

                // Mayuscula primera letra del apellido
                String newLastNameAA = newLastNameEdt.getText().toString();

                String newEmail = newEmailEdt.getText().toString();
                String pass = passEdt.getText().toString();

                if (!TextUtils.isEmpty(pass)) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                // Si todos los campos están vacios
                                if (TextUtils.isEmpty(newNameAA) && TextUtils.isEmpty(newLastNameAA) && TextUtils.isEmpty(newEmail)) {
                                    Toast.makeText(context, "Por favor, rellena alguno de los campos", Toast.LENGTH_SHORT).show();
                                }

                                // Si el campo de nuevo Nombre tiene texto
                                if (!TextUtils.isEmpty(newNameAA)) {

                                    String newName = newNameAA.substring(0,1).toUpperCase()+newNameAA.substring(1);

                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    userRef.child("name").setValue(newName)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Nombre actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Error al actualizar el nombre", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                // Si el campo de nuevo Apellido tiene texto
                                if (!TextUtils.isEmpty(newLastNameAA)) {

                                    String newLastName = newLastNameAA.substring(0,1).toUpperCase()+newLastNameAA.substring(1);

                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    userRef.child("lastName").setValue(newLastName)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Apellido actualizado exitosamente", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Error al actualizar el apellido", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                // Si el campo de nuevo Email tiene texto
                                if (!TextUtils.isEmpty(newEmail)) {

                                    //TODO: DEBES MANDAR UN CORREO CONFIRMANDO EL EMAIL

                                    user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Context context = getContext();

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

                                                        FirebaseAuth.getInstance().signOut();

                                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                        ProfileFragment.this.requireActivity().finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(context, "Error al actualizar el email", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                // HASTA AQUI LO DE CAMBIAR EMAIL

                            } else {
                                Toast.makeText(context, "Error con su contraseña", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Por favor, introduzca su contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cerrarDialogPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPerfil.dismiss();
            }
        });

        o1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPerfil.show();
            }
        });

        aceptarDialogPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passActualEditText = dialogPass.findViewById(R.id.passActual);
                EditText newPassEditText = dialogPass.findViewById(R.id.newPass);
                EditText confirmPassEditText = dialogPass.findViewById(R.id.confirmPass);

                String passActual = passActualEditText.getText().toString();
                String newPass = newPassEditText.getText().toString();
                String newPassR = confirmPassEditText.getText().toString();

                if (TextUtils.isEmpty(passActual) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(newPassR)) {
                    Toast.makeText(context, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passAcept(newPass)) {
                    Toast.makeText(context, "La contraseña debe tener al menos 6 carácteres, una mayúscula y una minúscula", Toast.LENGTH_SHORT).show();
                    return;
                }



                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passActual);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (newPass.equals(newPassR)) {
                                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Contraseña modificada con exito", Toast.LENGTH_SHORT).show();
                                            dialogPass.dismiss();
                                        } else {
                                            Toast.makeText(context, "Ha habido un error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Contraseña Actual Incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cerrarDialogPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPass.dismiss();
            }
        });

        o2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPass.show();
            }
        });

        cerrarDialogIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIdioma.dismiss();
            }
        });

        o3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIdioma.show();
            }
        });

        ////////////// PRUEBAS DIALOG //////////////

        userRef.child("nivel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int lvl = (snapshot.getValue(Integer.class));
                if (lvl > 0) {
                    binding.stockControl.setVisibility(View.VISIBLE);
                } else {
                    binding.stockControl.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.child("userPicPath").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageURL = dataSnapshot.getValue(String.class);
                if (imageURL != null) {
                    Glide.with(context)
                            .load(imageURL)
                            .transform(new CircleCrop(), new FitCenter())
                            .override(500, 500)
                            .into(pic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return binding.getRoot();
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

}
