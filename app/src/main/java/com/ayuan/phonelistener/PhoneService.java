package com.ayuan.phonelistener;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


public class PhoneService extends Service {
    private String TAG = "PhoneService";
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取TelephoneManager的实例
        TelephonyManager systemService = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        /**
         * listen(PhoneStateListener listener,int events)
         * 注册一个监听的对象，可以接收指定电话信息状态的改变
         * listener：这个对象可以监视指定电话状态的改变
         * event:自己想要监视的状态
         */
        systemService.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    //定义一个类用来监听电话的状态
    private class MyPhoneStateListener extends PhoneStateListener {
        //当电话设备状态发生改变的时候调用
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            //具体判断一下电话的状态
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态
                    if (mediaRecorder != null) {
                        //停止录制
                        mediaRecorder.stop();
                        //重置
                        mediaRecorder.reset();
                        //释放资源
                        mediaRecorder.release();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接听电话
                    Log.i(TAG, "开始录制");
                    if (mediaRecorder != null) {
                        //开始录制
                        mediaRecorder.start();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING://电话的响铃状态
                    Log.i(TAG, "准备一个录音机");
                    try {
                        //创建MediaRecorder的实例
                        mediaRecorder = new MediaRecorder();
                        //设置音频的来源(使用VOICE_CALL可以录制通话中的两个人的音频，而MIC只能录制自己的声音)
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        //设置输出格式
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        //设置音频的编码方式
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        //设置存放的路径
                        mediaRecorder.setOutputFile("/sdcard/luyin.3gp");
                        //准备录音
                        mediaRecorder.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
