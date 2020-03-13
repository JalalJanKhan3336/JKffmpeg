package com.pakistan.jkffmpeg.manager;

import android.app.Activity;
import android.util.Log;

import com.netcompss.ffmpeg4android.CommandValidationException;
import com.netcompss.loader.LoadJNI;
import com.pakistan.jkffmpeg.callback.AddSubtitle2VideoCallback;
import com.pakistan.jkffmpeg.callback.ConvertToFormatCallback;
import com.pakistan.jkffmpeg.callback.Pictures2VideoCallback;
import com.pakistan.jkffmpeg.callback.Video2ClipsCallback;
import com.pakistan.jkffmpeg.callback.Video2GifCallback;
import com.pakistan.jkffmpeg.callback.Video2PicturesCallback;
import com.pakistan.jkffmpeg.callback.VideoResizeCallback;
import com.pakistan.jkffmpeg.callback.VideoTrimCallback;

import java.io.File;

public class JKFFmpegManager {
    private static final String TAG = "JKFFmpegManager";

    private static JKFFmpegManager instance;

    public synchronized static JKFFmpegManager getInstance() {
        if(instance == null)
            instance = new JKFFmpegManager();
        return instance;
    }

    // Extracts pictures from single video
    public void video2Pictures(Activity activity, String videoPath, String outputFileName, File folder, Video2PicturesCallback listener){
        if(!folder.exists())
            folder.mkdir();

        String output = folder.getAbsolutePath()+"/"+outputFileName+"_%2d.png";

        File targetFolder = new File(folder, outputFileName);

        String[] cmd = new String[]{"ffmpeg", "-y","-i",videoPath,"-r","1","-f","image2",output};

        LoadJNI loadJNI = new LoadJNI();

        try {
            loadJNI.run(cmd, folder.getAbsolutePath(), activity);
            listener.onVideo2PicturesSuccess(targetFolder, "Clips created successfully");

        } catch (CommandValidationException e) {
            Log.e(TAG, "_onVideo2Pictures_Exception: "+e.getMessage());
            e.printStackTrace();
            listener.onVideo2PicturesFailed(e.getMessage());
        }

    }

    // Converts selected video to Animated GIF
    public void video2Gif(Activity activity, String videoPath, String outputFileName, File folder, Video2GifCallback listener){
        if(!folder.exists())
            folder.mkdir();

        String output = folder.getAbsolutePath()+"/"+outputFileName+".gif";
        String[] cmd = new String[]{"ffmpeg", "-y","-i", videoPath,"-vf","scale=500:-1","-t","10","-r","10", output};

        LoadJNI loadJNI = new LoadJNI();

        try {
            loadJNI.run(cmd,output,activity);
            listener.onVideo2GifSuccess(output, "Animated Gif has been created successfully");
        } catch (CommandValidationException e) {
            e.printStackTrace();
            listener.onVideo2GifFailed(e.getMessage());
        }

    }

    // Create short clips from single video
    public void video2Clips(Activity activity, String videoPath, int startTimeInSeconds, int timeDifferenceInSeconds,
                            int duration, String outputFileName, File folder, Video2ClipsCallback listener){
        if(!folder.exists())
            folder.mkdir();

        boolean isSuccessful = false;
        String error = "Something went wrong... please try again later";

        File targetFolder = new File(folder, outputFileName);

        while (startTimeInSeconds + timeDifferenceInSeconds <= duration){

            String output = folder.getAbsolutePath() + "/" + outputFileName + "_" +startTimeInSeconds+".mp4";

            String[] cmd = new String[]{
                    "ffmpeg",
                    "-y",
                    "-i",
                    videoPath,
                    "-ss",
                    "" + startTimeInSeconds,
                    "-t",
                    "" + timeDifferenceInSeconds,
                    "-codec",
                    "copy",
                    output
            };

            LoadJNI loadJNI = new LoadJNI();

            try {
                loadJNI.run(cmd, folder.getAbsolutePath(), activity);
                isSuccessful = true;

                startTimeInSeconds = startTimeInSeconds + timeDifferenceInSeconds;

            } catch (CommandValidationException e) {
                error = e.getMessage();
                Log.e(TAG, "_onVideo2Clips_Exception: "+e.getMessage());
                e.printStackTrace();
                listener.onVideo2ClipsFailed(e.getMessage());
            }
        }

        if(isSuccessful)
            listener.onVideo2ClipsSuccess(targetFolder, "Clips created successfully");
        else
            listener.onVideo2ClipsFailed(error);
    }

    // Trimming selected video
    public void trimVideo(Activity activity, String videoPath, int startTimeInSeconds, int endTimeInSeconds, String outputFileName, File folder, VideoTrimCallback listener){
        if(!folder.exists())
            folder.mkdir();

        String output = folder.getAbsolutePath()+"/"+outputFileName+".mp4";
        String[] cmd = new String[]{"ffmpeg","-y","-i",videoPath,"-ss",""+startTimeInSeconds,"-codec","copy","-t",""+endTimeInSeconds,output};

        LoadJNI loadJNI = new LoadJNI();

        try {
            loadJNI.run(cmd, output, activity);
            listener.onVideoTrimSuccess(output, "Video trimmed successfully");
        } catch (CommandValidationException e) {
            Log.e(TAG, "_onTrimVideo_Exception: "+e.getMessage());
            e.printStackTrace();
            listener.onVideoTrimFailed(e.getMessage());
        }

    }

    // Add sub-title to selected video
    public void addSubTitleVideo(Activity activity, String videoPath, String subTitleFilePath, String outputFileName, File folder, AddSubtitle2VideoCallback listener){
        if(!folder.exists())
            folder.mkdir();

        String output = folder.getAbsolutePath() + "/" + outputFileName+".mp4";
        String[] cmd = new String[]{"ffmpeg","-y","-i",videoPath,"-i",subTitleFilePath,"-map","0","-map","1","-c","copy","-c:v","libx264","-crf","23","-preset","veryfast",output};

        LoadJNI loadJNI = new LoadJNI();

        try {
            loadJNI.run(cmd, output, activity);
            listener.onAddSubtitle2VideoSuccess(output, "Subtitle has been added successfully");
        } catch (CommandValidationException e) {
            e.printStackTrace();
            listener.onAddSubtitle2VideoFailed(e.getMessage());
        }

    }

    // Resize Video
    public void resizeVideo(Activity activity, String videoPath, int width, int height, String outputFileName, File folder, VideoResizeCallback listener){
        if(!folder.exists())
            folder.mkdir();

        String ext = videoPath.substring(videoPath.indexOf("."));
        String output = folder.getAbsolutePath()+"/"+outputFileName+ext;
        String[] cmd = new String[]{"ffmpeg","-i", videoPath, "-vf", "scale="+width+":"+height, output};

        LoadJNI loadJNI = new LoadJNI();

        try {
            loadJNI.run(cmd, output, activity);
            listener.onVideoResizeSuccess(new File(folder, outputFileName),"Video Resized Successfully");
        } catch (CommandValidationException e) {
            e.printStackTrace();
            listener.onVideoResizeFailed(e.getMessage());
        }

    }

    // Convert video format to other format
    public void convertToFormat(Activity activity, String videoPath, String outputFileName, String outputFileExtension, File folder, ConvertToFormatCallback listener){

        if (!folder.exists())
            folder.mkdir();

        String output = folder.getAbsolutePath()+"/"+outputFileName+outputFileExtension;
        String[] cmd = new String[]{"ffmpeg", "-y", "-i",videoPath,output};

        LoadJNI loadJNI = new LoadJNI();
        try {
            loadJNI.run(cmd, output, activity);
            listener.onConvertToFormatSuccess(output, "Video format changed successfully");

        } catch (CommandValidationException e) {
            Log.e(TAG, "_onConvertToFormat_Exception: "+e.getMessage());
            e.printStackTrace();
            listener.onConvertToFormatFailed(e.getMessage());
        }

    }

    // Preview Video
    public void previewVideo(Activity activity, String videoPath){
        String[] cmd = new String[]{"ffplay","-i", videoPath};

        LoadJNI loadJNI = new LoadJNI();
        try {
            loadJNI.run(cmd, null, activity);
        } catch (CommandValidationException e) {
            Log.e(TAG, "_onPreviewVideo_Exception: "+e.getMessage());
            e.printStackTrace();
        }
    }

    // Preview Audio
    public void previewAudio(Activity activity, String audioPath){
        String[] cmd = new String[]{"ffplay","-i", audioPath};

        LoadJNI loadJNI = new LoadJNI();
        try {
            loadJNI.run(cmd, null, activity);
        } catch (CommandValidationException e) {
            Log.e(TAG, "_onPreviewAudio_Exception: "+e.getMessage());
            e.printStackTrace();
        }
    }

}
