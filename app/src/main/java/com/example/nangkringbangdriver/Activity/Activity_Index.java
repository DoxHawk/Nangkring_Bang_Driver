package com.example.nangkringbangdriver.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nangkringbangdriver.Fragments.Fragment_Login;
import com.example.nangkringbangdriver.R;
import com.example.nangkringbangdriver.databinding.ActivityIndexBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Activity_Index extends AppCompatActivity {

    private ActivityIndexBinding main;
    private int version;
    private FrameLayout viewlayout;
    private Fragment_Login fragment_login;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        if(mUser != null) {
            startActivity(new Intent(Activity_Index.this, Activity_Main.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main                =   ActivityIndexBinding.inflate(getLayoutInflater());
        setContentView(main.getRoot());
        viewlayout          = main.viewLayout;
        version             = Build.VERSION.SDK_INT;
        fragment_login      = new Fragment_Login();
        mAuth               = FirebaseAuth.getInstance();
        mUser               = mAuth.getCurrentUser();
        statusBar(version);
        loadFragment(fragment_login);
    }

    private void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) viewlayout.getContext();
        FragmentManager fm = activity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout, fragment).commit();
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.viewLayout, fragment).commit();
    }

    private void statusBar(int build) {
        if (build >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(8192);
            return;
        }
        Window window = getWindow();
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }
}
