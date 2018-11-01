package com.ayuan.phonelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //开启服务
        Intent intent1 = new Intent(context, PhoneService.class);
        context.startService(intent1);
    }
}
