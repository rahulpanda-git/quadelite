package in.rahulpanda.isight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCode extends AppCompatActivity {

    ZXingScannerView sv;
    TextToSpeech t1;
    String target="";
    SharedPreferences sh;
    RequestQueue rq;
    JsonObjectRequest jor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        sh = getSharedPreferences("data", Context.MODE_PRIVATE);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (sh.getString("language","").equals("JAPANESE")) {
                        t1.setLanguage(Locale.JAPANESE);
                        target = "jp";
                    } else if (sh.getString("language","").equals("FRENCH")) {
                        t1.setLanguage(Locale.FRENCH);
                        target = "fr";
                    } else if (sh.getString("language","").equals("GERMAN")) {
                        t1.setLanguage(Locale.GERMAN);
                        target = "ge";
                    } else if (sh.getString("language","").equals("CHINESE")) {
                        t1.setLanguage(Locale.CHINESE);
                        target = "chi";
                    } else if (sh.getString("language","").equals("KOREAN")) {
                        t1.setLanguage(Locale.KOREAN);
                        target = "ko";
                    } else if (sh.getString("language","").equals("ENGLISH")) {
                        t1.setLanguage(Locale.ENGLISH);
                        target = "en";
                    }
                }
                //tts();
            }
        });

        sv = new ZXingScannerView(QRCode.this);
        setContentView(sv);
        sv.startCamera();
        sv.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                Toast.makeText(getApplicationContext(),""+result.getText(),Toast.LENGTH_SHORT).show();
                sv.stopCamera();
               // Intent i = new Intent(QRCode.this,MainActivity.class);
                String temp = result.getText().toString();
                Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                rq = Volley.newRequestQueue(getApplicationContext());
                String url="http://rahulpanda.in/isight/server/?showQRID="+temp;
                jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                t1.speak(response.getString("data"), TextToSpeech.QUEUE_FLUSH, null, null);
                            }else{
                                t1.speak(response.getString("data"), TextToSpeech.QUEUE_FLUSH, null);
                            }
                            while(t1.isSpeaking()){}
                        }catch(Exception e){}
                        startActivity(new Intent(QRCode.this,Dashboard.class));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
//                i.putExtra("data",temp);
//                startActivity(i);
//                finishAffinity();
            }
        });
    }
}
