package com.pakistan.jkffmpeg.callback;

import java.io.File;

public interface VideoResizeCallback {
    void onVideoResizeSuccess(File resizedVideoFolder, String msg);
    void onVideoResizeFailed(String error);
}
