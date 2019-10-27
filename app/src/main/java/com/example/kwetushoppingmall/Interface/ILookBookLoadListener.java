package com.example.kwetushoppingmall.Interface;

import com.example.kwetushoppingmall.Model.Banner;

import java.util.List;

public interface ILookBookLoadListener {
    void onLookBookLoadSuccess(List<Banner> banners);
    void  onLookBookLoadFailed (String message);
}
