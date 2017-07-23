package in.rahulpanda.isight;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Multi extends AppCompatActivity {
    SharedPreferences sh;
    String target="";
    ProgressDialog p;
    RequestQueue rq,irq;
    JsonArrayRequest jar;
    JsonObjectRequest ijar;
    String toSpeak;
    ArrayList <String> arr;
    TextToSpeech t1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        sh = getSharedPreferences("data", Context.MODE_PRIVATE);
        b1 = (Button)findViewById(R.id.readButton);
        p = new ProgressDialog(Multi.this);
        p.setMessage("Loading ...");
        p.setCancelable(false);

        arr = new ArrayList<>();

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
                tts();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<arr.size();i++)
                {
                   // Toast.makeText(getApplicationContext(),arr.get(i)+" Test",Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        t1.speak(arr.get(i), TextToSpeech.QUEUE_FLUSH, null, null);
                    }else{
                        t1.speak(arr.get(i), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while(t1.isSpeaking()){}

                }
            }
        });
    }

    public void tts()
    {

        rq = Volley.newRequestQueue(getApplicationContext());
        String url = "http://rahulpanda.in/isight/server/?showServices";
        jar = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++) {
                    try {
                        showpDialog();
                        JSONObject ja = response.getJSONObject(i);
                        String data = ja.getString("comment");
                        String iurl = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyBDTgBjO7cCCmsxQwSAoid9Rx_UO5KKiTA&target=" + target + "&q=" + data;
                        iurl = iurl.replace(" ", "%20");
                        //Toast.makeText(getApplicationContext(),iurl,Toast.LENGTH_LONG).show();
                        irq = Volley.newRequestQueue(Multi.this);
                        ijar = new JsonObjectRequest(Request.Method.GET, iurl, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray ja = data.getJSONArray("translations");
                                    JSONObject jo = ja.getJSONObject(0);
                                    //Toast.makeText(getApplicationContext(),jo.getString("translatedText"),Toast.LENGTH_LONG).show();
                                    arr.add(jo.getString("translatedText"));
                                } catch (Exception e) {
                                }
                                hidepDialog();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        irq.add(ijar);
                    } catch (Exception e) {
                    }
                }
                //Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_LONG).show();
            }
            //hidepDialog();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jar);
        //hidepDialog();
    }

    private void read()
    {
        for(int i=0;i<arr.size();i++)
        {
            Toast.makeText(getApplicationContext(),arr.get(i),Toast.LENGTH_LONG).show();
        }
    }

    private void speak(String text){

    }

    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }


    private void showpDialog() {
        if (!p.isShowing())
            p.show();
    }

    private void hidepDialog() {
        if (p.isShowing())
            p.dismiss();
    }
}
