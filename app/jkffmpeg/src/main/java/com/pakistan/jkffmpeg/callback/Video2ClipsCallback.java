package com.pakistan.jkffmpeg.callback;

import java.io.File;

public interface Video2ClipsCallback {
    void onVideo2ClipsSuccess(File clipsTargetFolder, String msg);
    void onVideo2ClipsFailed(String error);
}
