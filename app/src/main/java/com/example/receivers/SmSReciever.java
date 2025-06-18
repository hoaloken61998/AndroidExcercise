package com.example.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmSReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //KHi có tin nhắn SMS tới nó tự động nháy vào đây
        Bundle bundle = intent.getExtras();
        Object[] arrMessages = (Object[]) bundle.get("pdus");
        String phone, time, content;
        Date date; byte[] bytes;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for(int i = 0; i<arrMessages.length; i++){
            bytes = (byte[]) arrMessages[i];
            SmsMessage message = SmsMessage.createFromPdu(bytes);
            phone = message.getDisplayOriginatingAddress();
            date = new Date(message.getTimestampMillis());
            content = message.getDisplayMessageBody();
            time = sdf.format(date);
            String infor = phone +"\n" + content + "\n" + time;
            //Ta sẽ gửi leen server sau
            Toast.makeText(context, infor, Toast.LENGTH_LONG).show();
        }
    }
}
