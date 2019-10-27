package com.example.kwetushoppingmall;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kwetushoppingmall.Common.Common;
import com.example.kwetushoppingmall.Fragments.HomeFragment;
import com.example.kwetushoppingmall.Fragments.ShoppingFragment;
import com.example.kwetushoppingmall.Interface.IBannerLoadListener;
import com.example.kwetushoppingmall.Model.User;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class HomeActivity extends AppCompatActivity {
 @BindView(R.id.bottom_navigation)
 BottomNavigationView bottomNavigationView;
 BottomSheetDialog bottomSheetDialog;
 CollectionReference userRef;
 AlertDialog dialog;
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
                //initialize
        userRef= FirebaseFirestore.getInstance().collection("User");
        dialog= new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        //now dont forget to enable the rules of firestore to test mode
        //check intent if is login=true, enable full access
        //if  is login= false just allow  view of shopping
        if (getIntent() !=null)
        {
            boolean isLogin=getIntent().getBooleanExtra(Common.IS_LOGIN,false);
            if (isLogin)
            dialog.show();
            {
                //check if user exists
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        if (account !=null)
                        {
                            DocumentReference currentUser=userRef.document(account.getPhoneNumber().toString());
                            currentUser.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful())
                                            {
                                                DocumentSnapshot userSnapshot=task.getResult();
                                                if (!userSnapshot.exists())
                                                {

                                                    showUpdateDialog(account.getPhoneNumber().toString());
                                                }
                                                else {
                                                    //if user already in the system
                                                    Common.currentUser=userSnapshot.toObject(User.class);
                                                    bottomNavigationView.setSelectedItemId(R.id.home);

                                                }

                                                if (dialog.isShowing())
                                                    dialog.dismiss();
                                            }
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Toast.makeText(HomeActivity.this, ""+accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
        //initialize view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment =null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.home)
                    fragment= new HomeFragment();
                else if (menuItem.getItemId()==R.id.cart)
                    fragment=new ShoppingFragment();
                

                return loadFragment(fragment);
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment !=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                    .commit();
            return true;

        }
        return false;
    }

    private void showUpdateDialog(final String phoneNumber) {
        //initialize dialog


        bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("One more Step!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView=getLayoutInflater().inflate(R.layout.layout_update_information,null);
        Button button_update=(Button)sheetView.findViewById(R.id.btn_update);
         final TextInputEditText edit_name=(TextInputEditText)sheetView.findViewById(R.id.edit_name);
         final TextInputEditText edit_address=(TextInputEditText)sheetView.findViewById(R.id.edit_address);

         button_update.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (!dialog.isShowing())
                     dialog.show();
                 final User user=new User(edit_name.getText().toString()
                 ,edit_address.getText().toString()
                 ,phoneNumber);
            userRef.document(phoneNumber)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            bottomSheetDialog.dismiss();
                            if (dialog.isShowing())
                                dialog.dismiss();
                            Common.currentUser= user;
                            bottomNavigationView.setSelectedItemId(R.id.home);



                            Toast.makeText(HomeActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    bottomSheetDialog.dismiss();
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
             }
         });
         bottomSheetDialog.setContentView(sheetView);
         bottomSheetDialog.show();

    }
}