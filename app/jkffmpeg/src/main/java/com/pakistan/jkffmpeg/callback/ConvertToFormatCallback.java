package com.pakistan.jkffmpeg.callback;

public interface ConvertToFormatCallback {
    void onConvertToFormatSuccess(String videoNewPath, String msg);
    void onConvertToFormatFailed(String error);
}
