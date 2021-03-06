package com.zplay.playable.playableadmobdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.playableads.PlayLoadingListener;
import com.playableads.PlayPreloadingListener;
import com.playableads.PlayableAds;

/**
 * Description: ZPLAYAdsAdMobAdapter
 * Created by lgd on 2017/12/4.
 */

@SuppressWarnings("unused")
public class ZPLAYAdsAdMobAdapter implements MediationRewardedVideoAdAdapter {
    private static final String TAG = "ZPLAYAdsAdMobAdapter";
    private String paAppId;
    private String paAdUnitId;
    private PlayableAds pAd;
    private MediationRewardedVideoAdListener mRewardedVideoEventForwarder;

    @Override
    public void initialize(Context context, MediationAdRequest mediationAdRequest, String s, MediationRewardedVideoAdListener mediationRewardedVideoAdListener, Bundle serverParameters, Bundle bundle1) {
        resetIds(serverParameters);
        pAd = PlayableAds.init(context, paAppId);
        pAd.setAutoLoadAd(false);
        pAd.enableAutoRequestPermissions(true);
        mRewardedVideoEventForwarder = mediationRewardedVideoAdListener;
        mRewardedVideoEventForwarder.onInitializationSucceeded(ZPLAYAdsAdMobAdapter.this);
    }

    @Override
    public void loadAd(MediationAdRequest mediationAdRequest, Bundle serverParameters, Bundle bundle1) {
        resetIds(serverParameters);
        loadAd();
    }

    private void loadAd() {
        pAd.requestPlayableAds(paAdUnitId, new PlayPreloadingListener() {
            @Override
            public void onLoadFinished() {
                mRewardedVideoEventForwarder.onAdLoaded(ZPLAYAdsAdMobAdapter.this);
            }

            @Override
            public void onLoadFailed(int i, String s) {
                mRewardedVideoEventForwarder.onAdFailedToLoad(ZPLAYAdsAdMobAdapter.this, 0);
            }
        });
    }

    @Override
    public void showVideo() {
        if (pAd.canPresentAd(paAdUnitId)) {
            mRewardedVideoEventForwarder.onAdOpened(ZPLAYAdsAdMobAdapter.this);
            pAd.presentPlayableAD(paAdUnitId, new PlayLoadingListener() {

                @Override
                public void onVideoStart() {
                    mRewardedVideoEventForwarder.onVideoStarted(ZPLAYAdsAdMobAdapter.this);
                }

                @Override
                public void onVideoFinished() {
                    mRewardedVideoEventForwarder.onVideoCompleted(ZPLAYAdsAdMobAdapter.this);
                }

                @Override
                public void onLandingPageInstallBtnClicked() {
                    mRewardedVideoEventForwarder.onAdClicked(ZPLAYAdsAdMobAdapter.this);
                }

                public void playableAdsIncentive() {
                    mRewardedVideoEventForwarder.onRewarded(ZPLAYAdsAdMobAdapter.this, null);
                }

                @Override
                public void onAdClosed() {
                    mRewardedVideoEventForwarder.onAdClosed(ZPLAYAdsAdMobAdapter.this);
                }

                public void onAdsError(int var1, String var2) {
                    mRewardedVideoEventForwarder.onAdFailedToLoad(ZPLAYAdsAdMobAdapter.this, 0);
                }

            });
        }
    }

    private void resetIds(@NonNull Bundle param) {
        String parameters = param.getString("parameter");
        if (parameters == null) {
            Log.e(TAG, "check parameter from AdMob web");
            return;
        }
        String[] ids = parameters.trim().split("\\s");
        if (ids.length != 2) {
            Log.e(TAG, "check parameter from AdMob web");
            return;
        }
        paAppId = ids[0];
        paAdUnitId = ids[1];
    }

    @Override
    public boolean isInitialized() {
        return pAd != null;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }
}
