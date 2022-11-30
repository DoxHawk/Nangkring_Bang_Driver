package com.example.nangkringbangdriver.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.example.nangkringbangdriver.Model.Model_Profile;
import com.example.nangkringbangdriver.R;
import com.example.nangkringbangdriver.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Activity_Profile extends AppCompatActivity {

    private ActivityProfileBinding main;
    private Button btnLogout, btnEdit;
    private SharedPreferences.Editor editor;
    private TextView emailProfile, namaProfile, alamatProfile, telpProfile, userProfile;
    private ImageView imgProf;
    private NestedScrollView nestedScrollView;
    private SharedPreferences sharedPreferences;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference storageReference;
    private static final String PROFILE = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                =   ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());

        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        nestedScrollView    = main.nestedScrollView;
        namaProfile         = main.namaProfile1;
        emailProfile        = main.emailProfile1;
        userProfile         = main.usernameProfile1;
        telpProfile         = main.telpProfile1;
        alamatProfile       = main.almtProfile;
        imgProf             = main.imgProfile;
        btnLogout           = main.btnLogout;
        btnEdit             = main.btnEdit;

        getUserDetails();
    }

    private void getUserDetails(){
        if(mUser != null){
            firebaseFirestore.collection(PROFILE).document(mUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(Activity_Profile.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                        Log.d("TAG_PROFILE", error.toString());
                    } else if (value != null) {
                        Model_Profile model = value.toObject(Model_Profile.class);
                        namaProfile.setText(model.getUser_nama());
                        emailProfile.setText(model.getUser_email());
                        userProfile.setText(model.getUser_username());
                        telpProfile.setText(String.valueOf(model.getUser_telp()));
                        alamatProfile.setText(model.getUser_alamat());

                        if (!model.getUser_img().equals("default")) {
                            storageReference.child("foto profile").child(model.getUser_img()).getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                                                    .fit().centerInside().into(imgProf, new Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Picasso.get().load(uri).fit().centerInside().into(imgProf);
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    imgProf.setImageResource(R.drawable.placeholder_img);
                                }
                            });
                        }

                        btnLogout.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Snackbar.make(nestedScrollView, "You have logged out, " + model.getUser_nama(), Snackbar.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if(mUser != null){
                                            mAuth.signOut();
                                            startActivity(new Intent(Activity_Profile.this, Activity_Index.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            finish();
                                        }
                                    }
                                },(1000));
                            }
                        });
                    }
                }
            });
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Profile.this);

                alert.setTitle("Ubah profile");
                alert.setMessage("Anda yakin ?");

                // Set view
                alert.setView(R.layout.alert_profile);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
        });

    }
}