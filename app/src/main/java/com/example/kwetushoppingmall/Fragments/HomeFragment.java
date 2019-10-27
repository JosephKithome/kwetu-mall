package com.example.kwetushoppingmall.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kwetushoppingmall.Adapter.HomeSliderAdapter;
import com.example.kwetushoppingmall.Adapter.LookBookAdapter;
import com.example.kwetushoppingmall.BookingActivity;
import com.example.kwetushoppingmall.Common.Common;
import com.example.kwetushoppingmall.Interface.IBannerLoadListener;
import com.example.kwetushoppingmall.Interface.ILookBookLoadListener;
import com.example.kwetushoppingmall.Model.Banner;
import com.example.kwetushoppingmall.R;
import com.example.kwetushoppingmall.Service.PicassoLoadingService;
import com.facebook.accountkit.AccountKit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

public class HomeFragment extends Fragment implements ILookBookLoadListener, IBannerLoadListener {
    private Unbinder unbinder;
    @BindView(R.id.user_information)
    LinearLayout linearLayout_user_information;
    @BindView(R.id.txt_username)
    TextView txt_username;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recycler_lookBook)
    RecyclerView recycler_lookBook;
    //firestore
    CollectionReference bannerRef,lookBookRef;
    //Interface
    IBannerLoadListener iBannerLoadListener;
    ILookBookLoadListener iLookBookLoadListener;
    @OnClick(R.id.cardview_booking)
    void booking()
    {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }



    public HomeFragment() {
        bannerRef= FirebaseFirestore.getInstance().collection("Banner");
        lookBookRef=FirebaseFirestore.getInstance().collection("Lookbook");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        unbinder= ButterKnife.bind(this,view);
        Slider.init(new PicassoLoadingService());
        iBannerLoadListener=this;
        iLookBookLoadListener=this;
        loadBanner();
        loadLookBook();
        //check is logged
        if (AccountKit.getCurrentAccessToken() !=null);
        {
            setUserInformation();
        }
        return view;

    }

    private void loadLookBook() {
        lookBookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookbooks=new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannerSnapshot:task.getResult())
                            {
                                Banner banner=bannerSnapshot.toObject(Banner.class);
                                lookbooks.add(banner);
                            }
                            iLookBookLoadListener.onLookBookLoadSuccess(lookbooks);
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookBookLoadListener.onLookBookLoadFailed(e.getMessage());


            }
        });

    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners=new ArrayList<>();
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot bannersnapshot:task.getResult())
                            {
                                Banner banner= bannersnapshot.toObject(Banner.class);
                                banners.add(banner);

                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                iBannerLoadListener.onBannerLoadFailed(e.getMessage());


            }
        });
    }

    private void setUserInformation() {
        linearLayout_user_information.setVisibility(View.VISIBLE);
        txt_username.setText(Common.currentUser.getName());
    }

    @Override
    public void onLookBookLoadSuccess(List<Banner> banners) {
        recycler_lookBook.setHasFixedSize(true);
        recycler_lookBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_lookBook.setAdapter(new LookBookAdapter(getActivity(),banners));

    }

    @Override
    public void onLookBookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));

    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }
}
