package com.example.nangkringbangdriver.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
    private static final String TAG = "Activity_Profile";
    private final int PICK_IMAGE_REQUEST = 1;

    private getUriData getUriData;
    private getResponse getResponse;

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
        alamatProfile       = main.almtProfile1;
        imgProf             = main.imgProfile;
        btnEdit             = main.btnEdit;
        btnLogout           = main.btnLogout;

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

                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final View alertProfLayout = getLayoutInflater().inflate(R.layout.alert_profile, null);
                                TextInputLayout p_nama = alertProfLayout.findViewById(R.id.txtNama);
                                TextInputLayout p_user = alertProfLayout.findViewById(R.id.txtUser);
                                TextInputLayout p_telp = alertProfLayout.findViewById(R.id.txtTelp);
                                TextInputLayout p_address = alertProfLayout.findViewById(R.id.txtAddress);
                                AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Profile.this);

                                alert.setTitle("Ubah profile");

                                // Set view
                                alert.setView(alertProfLayout);

                                p_nama.getEditText().setText(model.getUser_nama());
                                p_user.getEditText().setText(model.getUser_username());
                                p_telp.getEditText().setText(model.getUser_telp());
                                p_address.getEditText().setText(model.getUser_alamat());

                                alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        Map<String, Object> item = new HashMap<>();
                                        item.put("user_nama", p_nama.getEditText().getText().toString());
                                        item.put("user_username", p_user.getEditText().getText().toString());
                                        item.put("user_telp", p_telp.getEditText().getText().toString());
                                        item.put("user_alamat", p_address.getEditText().getText().toString());

                                        firebaseFirestore.collection(PROFILE).document(mUser.getUid()).update(item)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Snackbar.make(main.getRoot(), "Berhasil ubah profile", Snackbar.LENGTH_LONG).show();
                                                        } else if (!task.isSuccessful()) {
                                                            Snackbar.make(main.getRoot(), "Gagal ubah profile", Snackbar.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                });

                                alert.show();
                            }
                        });

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
                final View customLayout = getLayoutInflater().inflate(R.layout.alert_profile, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Profile.this);

                alert.setTitle("Ubah profile");
                alert.setMessage("Anda yakin ?");

                // Set view
                alert.setView(customLayout);

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

    // Select Image method
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih foto profil"), PICK_IMAGE_REQUEST);
    }

    //ConvertBitmap
    private void uploadFile(Uri uri) {
        String path = "foto profil/" + uri.getLastPathSegment();
        storageReference.child(path).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (!task.isSuccessful()){
                    Log.d(TAG, "onComplete: gagalUpload"+task.getException());
                    getResponse.onResponse(false, null);
                }else {
                    getResponse.onResponse(true, path);
                }
            }
        });
    }

    public interface getResponse{
        void onResponse(Boolean status, String path);
    }

    public void setResponse(getResponse getResponse){this.getResponse = getResponse;}

    public interface getUriData{
        void onUriData(Bitmap bitmap, Uri uri);
    }

    public void setUriData(getUriData getUriData){this.getUriData = getUriData;}
}