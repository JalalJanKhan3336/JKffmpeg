package com.pakistan.jkffmpeg.callback;

public interface VideoTrimCallback {
    void onVideoTrimSuccess(String videoNewPath, String msg);
    void onVideoTrimFailed(String error);
}
