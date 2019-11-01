package com.finports.took_pos_demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;

    ImageView product1_minus;
    ImageView product1_plus;
    TextView product1_qty;
    TextView product1_price;
    int iProduct1_qty = 1;
    int iProduct1_prict = 50454;

    ImageView product2_minus;
    ImageView product2_plus;
    TextView product2_qty;
    TextView product2_price;

    TextView total_price;
    int iProduct2_qty = 1;
    int iProduct2_price = 50454;
    String fcm_id = "";
    ImageView imageView_took;
    DecimalFormat formatter = new DecimalFormat("###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        product1_qty = findViewById(R.id.mainactivity_product1_qty);
        product2_qty = findViewById(R.id.mainactivity_product2_qty);
        product1_price = findViewById(R.id.mainactivity_textview_product1_price);
        product2_price = findViewById(R.id.mainactivity_textview_product2_price);
        total_price = findViewById(R.id.mainactivity_textview_totalprice);
        setTotalPrice();
        product1_minus = findViewById(R.id.mainactivity_product1_minus);
        product1_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iProduct1_qty > 1){
                    iProduct1_qty--;
                    setProduct1();
                }
            }
        });
        product1_plus = findViewById(R.id.mainactivity_product1_plus);
        product1_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iProduct1_qty < 9){
                    iProduct1_qty++;
                    setProduct1();
                }
            }
        });

        product2_minus = findViewById(R.id.mainactivity_product2_minus);
        product2_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iProduct2_qty > 1){
                    iProduct2_qty--;
                    setProduct2();
                }
            }
        });

        product2_plus = findViewById(R.id.mainactivity_product2_plus);
        product2_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iProduct2_qty < 9){
                    iProduct2_qty++;
                    setProduct2();
                }
            }
        });

        imageView_took = findViewById(R.id.mainactivity_image_took);
        imageView_took.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setBeepEnabled(false);//바코드 인식시 소리
                intentIntegrator.setCaptureActivity(ScannerActivity.class);
                intentIntegrator.initiateScan();

                imageView_took.setImageResource(R.drawable.to_ok);

            }
        });

        Log.d("test", "asdfasdf");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                fcm_id = newToken;
                Log.d("test", newToken);
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        iProduct2_qty = 1;
        iProduct1_qty = 1;
    }

    public void setProduct1(){
        product1_qty.setText(String.valueOf(iProduct1_qty));
        product1_price.setText(formatter.format(iProduct1_prict * iProduct1_qty)+"원");
        setTotalPrice();
    }

    public void setProduct2(){
        product2_qty.setText(String.valueOf(iProduct2_qty));
        product2_price.setText(formatter.format(iProduct2_price*iProduct2_qty)+"원");
        setTotalPrice();
    }

    public void setTotalPrice(){
        total_price.setText(formatter.format((iProduct1_prict*iProduct1_qty)+(iProduct2_price*iProduct2_qty))+"원");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        imageView_took.setImageResource(R.drawable.to_ok);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                imageView_took.setImageResource(R.drawable.icon_logo_ok);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result.getContents());
                    Log.d("test", "json = "+jsonObject);
                    Intent intent = new Intent(MainActivity.this, PaymentPopupActivity.class);
                    intent.putExtra("customer_name", jsonObject.getString("customer_name"));
                    intent.putExtra("price", ((iProduct1_prict*iProduct1_qty)+(iProduct2_price*iProduct2_qty)));
                    intent.putExtra("payment_code", jsonObject.getString("payment_code"));
                    intent.putExtra("fcm_code", jsonObject.getString("fcm_id"));
                    intent.putExtra("fcm_id", fcm_id);
                    startActivity(intent);
                    imageView_took.setImageResource(R.drawable.to_ok);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void initializeValue() {

        product1_qty.setText(String.valueOf(iProduct1_qty));
        product2_qty.setText(String.valueOf(iProduct2_qty));

        product1_price.setText(String.valueOf(formatter.format(iProduct1_prict*iProduct1_qty)) + "원");
        product2_price.setText(String.valueOf(formatter.format(iProduct2_price*iProduct2_qty)) + "원");

        total_price.setText(formatter.format((iProduct1_prict*iProduct1_qty)+(iProduct2_price*iProduct2_qty))+ "원");

    }

    public void changeTookBtn() {
        imageView_took.setImageResource(R.drawable.icon_logo_ok);
    }

}
