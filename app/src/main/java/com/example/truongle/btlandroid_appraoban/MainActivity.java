package com.example.truongle.btlandroid_appraoban;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.truongle.btlandroid_appraoban.Chat.view.UserContactActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{





    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FloatingActionButton fab;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);




        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.naviga);
        navigationView.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginInten = new Intent(getApplicationContext(),LoginActivity.class);
                    loginInten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginInten);
                }
            }
        };
     if(mAuth.getCurrentUser()!= null) {
         mDataUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
         View hView = navigationView.getHeaderView(0);
         final TextView nav_user = (TextView) hView.findViewById(R.id.TextViewName);
         // nav_user.setText("Abc");
         final ImageView imageView = (ImageView) hView.findViewById(R.id.imageViewLogo);
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), UpdateAcount.class));
             }
         });

         mDataUser.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 nav_user.setText(dataSnapshot.child("name").getValue().toString());
                 if (!dataSnapshot.child("image").getValue().toString().equals("default"))
                     Picasso.with(getApplicationContext()).load(dataSnapshot.child("image").getValue().toString()).into(imageView);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

     }

    }

           @Override
           public void onStart() {
               super.onStart();


               mAuth.addAuthStateListener(mAuthListener);
           }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        else if(item.getItemId()==R.id.action_update){
            startActivity(new Intent(getApplicationContext(), UpdateAcount.class));

        }
        else if(item.getItemId()==R.id.action_home){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }else if(item.getItemId()==R.id.action_mesage){
            startActivity(new Intent(getApplicationContext(), UserContactActivity.class));

        }else if(item.getItemId()==R.id.action_Add){
            startActivity(new Intent(getApplicationContext(), AddProductActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }


    private void logout() {
        mAuth.signOut();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_list_save) {
            startActivity(new Intent(getApplicationContext(), DsSanPhamLuu.class));

        }
        else if (id == R.id.nav_list_my_product) {
            startActivity(new Intent(getApplicationContext(), DsSanPhamBan.class));
        }
        else if (id == R.id.logout) {
            logout();
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
                case 2:
                    Tab3 tab3 = new Tab3();
                    return tab3;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Đồ Điện Tử";
                case 1:
                    return "Đồ Thể Thao";
                case 2:
                    return "Đồ Cá Nhân";

            }
            return null;
        }
    }


}
