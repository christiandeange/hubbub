package com.deange.githubstatus.net;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIdService
    extends FirebaseInstanceIdService {

  private static final String TAG = "InstanceIdService";

  @Override
  public void onTokenRefresh() {
    final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);
  }
}
