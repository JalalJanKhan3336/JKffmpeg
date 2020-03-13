package com.pakistan.jkffmpeg.callback;

public interface Video2GifCallback {
    void onVideo2GifSuccess(String gifNewPath, String msg);
    void onVideo2GifFailed(String error);
}
