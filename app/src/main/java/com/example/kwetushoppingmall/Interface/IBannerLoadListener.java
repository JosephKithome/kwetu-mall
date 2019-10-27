package com.example.kwetushoppingmall.Interface;

import com.example.kwetushoppingmall.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void  onBannerLoadFailed (String message);
}
