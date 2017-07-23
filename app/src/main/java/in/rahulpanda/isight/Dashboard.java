package in.rahulpanda.isight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {

    Button eye,dir,ser,sol,qr,tel;
    RequestQueue rq;
    JsonArrayRequest jar;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        eye = (Button)findViewById(R.id.eye);
        dir = (Button)findViewById(R.id.dir);
        ser = (Button)findViewById(R.id.services);
        sol = (Button)findViewById(R.id.solutions);
        qr = (Button)findViewById(R.id.recog);
        tel = (Button)findViewById(R.id.web);
        sh = getSharedPreferences("data", Context.MODE_PRIVATE);
        rq = Volley.newRequestQueue(getApplicationContext());
        String url ="http://rahulpanda.in/isight/server/?username="+sh.getString("user","");
        jar = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jo = response.getJSONObject(0);
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("language",jo.getString("language"));
                    Toast.makeText(getApplicationContext(),jo.getString("language"),Toast.LENGTH_LONG).show();
                    ed.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jar);

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,OCR_main.class));
            }
        });

        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Multi.class));
            }
        });
        dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Directions.class));
            }
        });
        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Solutions.class));
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,QRCode.class));
            }
        });
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_MAIN);
                PackageManager managerclock = getPackageManager();
                i = managerclock.getLaunchIntentForPackage("com.webrtc.audioconference");
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(i);
            }
        });
    }
}
