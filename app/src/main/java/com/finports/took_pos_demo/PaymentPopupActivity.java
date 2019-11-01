package com.finports.took_pos_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.finports.took_pos_demo.Model.NotificationModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentPopupActivity extends Activity {

    TextView textView_price;
    TextView textView_name;

    ImageView imageView_ok;
    DecimalFormat formatter = new DecimalFormat("###,###");
    JSONObject jsonObject = new JSONObject();
    OkHttpClient client = new OkHttpClient();

    ImageView imageView_took;

    String payment_code = "";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("complete"));
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.activity_payment_popup);

        textView_name = findViewById(R.id.paymentpopup_textview_name);
        textView_price = findViewById(R.id.paymentpopup_textview_price);

        imageView_took = findViewById(R.id.mainactivity_image_took);

        intent = getIntent();
        textView_name.setText(intent.getExtras().getString("customer_name"));
        textView_price.setText(formatter.format(intent.getExtras().getInt("price"))+"원");

        try {
            jsonObject.put("payment_code", intent.getExtras().getString("payment_code"));
            jsonObject.put("user_id", "03f885a6f58c49dd98bf6d631dc62d00");
            jsonObject.put("amount", intent.getExtras().getInt("price"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageView_ok = findViewById(R.id.paymentpopup_image_ok);
        imageView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MainActivity.mContext).initializeValue();
                Log.d("test", "json = "+jsonObject.toString());
                httpRun("http://15.164.247.222:5000/payment/");
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((MainActivity)MainActivity.mContext).changeTookBtn();
            String message = String.valueOf(intent.getStringExtra("complete"));
            Log.d("test", "mess = "+message);
            if (message.equals("complete")){
                finish();
            }
        }
    };

    public void httpRun(String url) {
        final Request request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"), String.valueOf(jsonObject))).build();
        new Thread(){
            public void run(){
                try (Response response = client.newCall(request).execute()) {
                    Log.d("test", "TIMING");
//                    setUi();
                    sendFcm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    void sendFcm(){
        Gson gson = new Gson();
        NotificationModel notificationModel = new NotificationModel();
        Log.d("test", "fcm = "+intent.getExtras().getString("fcm_code"));
        notificationModel.to = intent.getExtras().getString("fcm_code");
        notificationModel.notification.title =" 테스트 ";
        notificationModel.notification.text = "text 테스트";
        notificationModel.data.price = intent.getExtras().getInt("price");
        notificationModel.data.payment_code = intent.getExtras().getString("payment_code");
        notificationModel.data.fcm_id = intent.getExtras().getString("fcm_id");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/jsonl charset=utf8"), gson.toJson(notificationModel));
        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyBBGdUtJWKwSolZ2bHPJfDrWSmoIRIibMk")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient= new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                finish();
            }
        });

    }
}
