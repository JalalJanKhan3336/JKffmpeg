package com.pakistan.jkffmpeg.callback;

import java.io.File;

public interface Pictures2VideoCallback {
    void onPictures2VideoSuccess(File videoTargetFolder, String msg);
    void onPictures2VideoFailed(String error);
}
