package com.example.nangkringbangdriver.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nangkringbangdriver.Activity.Activity_Order_Detail;
import com.example.nangkringbangdriver.Activity.Activity_Profile;
import com.example.nangkringbangdriver.Adapter.Adapter_Order;
import com.example.nangkringbangdriver.Model.Model_Orders_Driver;
import com.example.nangkringbangdriver.Model.Model_Profile;
import com.example.nangkringbangdriver.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

//import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Home extends Fragment {

    private View view;
    private Context context;
    private TextView txtGreetingUser, txtTanggal, txtGreetDay;
    private DisplayMetrics metrics;
    private CircleImageView imgProfile;
    private RecyclerView recyclerView;
    private Adapter_Order adpt_o;

    private static final String PROFILE = "users";
    private static final String ORDERS = "pesanan";
    private static final String TAG = "Fragment_Home";

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference storageReference;
    private Query query;

    @Override
    public void onStart() {
        super.onStart();
        adpt_o.startListening();
        adpt_o.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        adpt_o.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adpt_o.startListening();
        adpt_o.notifyDataSetChanged();
        recyclerView.getRecycledViewPool().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        adpt_o.stopListening();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);
        context = view.getContext();
        txtGreetingUser = view.findViewById(R.id.txtGreetingUser);
        txtTanggal = view.findViewById(R.id.txtTanggal);
        txtGreetDay = view.findViewById(R.id.txtGreetDay);
        imgProfile = view.findViewById(R.id.imgUserHome);
        recyclerView = view.findViewById(R.id.recyclerView2);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference  = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        SimpleDateFormat sfd = new SimpleDateFormat("EEEE, dd LLLL yyyy");
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        txtTanggal.setText(sfd.format(now));
        if(timeOfDay >= 0 && timeOfDay < 12){
            txtGreetDay.setText("Selamat pagi");
        }else if(timeOfDay >= 12 && timeOfDay < 15){
            txtGreetDay.setText("Selamat siang");
        }else if(timeOfDay >= 15 && timeOfDay < 18){
            txtGreetDay.setText("Selamat sore");
        }else if(timeOfDay >= 18 && timeOfDay < 24){
            txtGreetDay.setText("Selamat malam");
        }

        profile();
        getOrders();

        return view;
    }

    private void profile() {
        if (mAuth.getCurrentUser() != null) {
            firebaseFirestore.collection(PROFILE)
                    .document(mUser.getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Toast.makeText(context, "Error loading profile", Toast.LENGTH_SHORT).show();
                                Log.d("TAG_PROFILE", error.toString());
                            } else if (value != null) {
                                Model_Profile model = value.toObject(Model_Profile.class);
                                txtGreetingUser.setText(model.getUser_nama());

                                if (!model.getUser_img().equals("default")) {
                                    storageReference.child("foto profile").child(model.getUser_img()).getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Picasso.get().load(uri).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.placeholder_img)
                                                            .fit().centerInside().into(imgProfile, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Picasso.get().load(uri).fit().centerInside().into(imgProfile);
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            imgProfile.setImageResource(R.drawable.placeholder_img);
                                        }
                                    });
                                }
                            }
                        }
                    });
        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fragment_Home.this.getActivity() , Activity_Profile.class);
                Fragment_Home.this.getActivity().startActivity(intent);
            }
        });
    }

    private void getOrders() {
        query = firebaseFirestore.collection(PROFILE).document(mUser.getUid()).collection(ORDERS)
                .whereEqualTo("pesanan_status", "Proses");
        FirestoreRecyclerOptions<Model_Orders_Driver> options = new FirestoreRecyclerOptions.Builder<Model_Orders_Driver>()
                .setQuery(query, Model_Orders_Driver.class)
                .build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        adpt_o = new Adapter_Order(options, mUser);
        recyclerView.setAdapter(adpt_o);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

//        adpt_o.setOnItemClickListener(new Adapter_Order.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                if (documentSnapshot != null) {
//                    Intent menuIntent = new Intent(Fragment_Home.this.getActivity(), Activity_Order_Detail.class);
//                    menuIntent.putExtra("KEY_ORDER_ID", documentSnapshot.getId());
//                    if (menuIntent.resolveActivity(Fragment_Home.this.getActivity().getPackageManager()) != null) {
//                        Fragment_Home.this.getActivity().startActivity(menuIntent);
//                    }
//                }
//            }
//        });
    }
}
