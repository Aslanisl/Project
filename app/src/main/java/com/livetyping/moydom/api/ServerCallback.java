package com.livetyping.moydom.api;

/**
 * Created by Ivan on 05.12.2017.
 */

public interface ServerCallback {
    void onUnknownError(String error);
    void onTimeout();
    void onNetworkError();
}
