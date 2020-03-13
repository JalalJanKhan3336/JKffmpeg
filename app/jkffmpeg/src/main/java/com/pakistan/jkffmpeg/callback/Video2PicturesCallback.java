package com.pakistan.jkffmpeg.callback;

import java.io.File;

public interface Video2PicturesCallback {
    void onVideo2PicturesSuccess(File picturesTargetFolder, String msg);
    void onVideo2PicturesFailed(String error);
}
