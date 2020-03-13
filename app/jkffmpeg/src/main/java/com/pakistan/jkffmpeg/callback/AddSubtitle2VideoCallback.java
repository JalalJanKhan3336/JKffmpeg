package com.pakistan.jkffmpeg.callback;

public interface AddSubtitle2VideoCallback {
    void onAddSubtitle2VideoSuccess(String videoNewPath, String msg);
    void onAddSubtitle2VideoFailed(String error);
}
