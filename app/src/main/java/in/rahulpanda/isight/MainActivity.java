package in.rahulpanda.isight;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    RequestQueue rq;
    JsonObjectRequest jor;
    EditText user, pass;
    Button log,reg;
    ProgressDialog p;
    Dialog d;
    String lang_selected;
    SharedPreferences sh;
    SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (Button)findViewById(R.id.login);
        reg = (Button)findViewById(R.id.register);

        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);

        sh = getSharedPreferences("data", Context.MODE_PRIVATE);

        p = new ProgressDialog(MainActivity.this);
        p.setMessage("Loading ...");
        p.setCancelable(false);

        try{
            if(!sh.getString("user","").equals(""))
            {
                startActivity(new Intent(MainActivity.this,Dashboard.class));
                finishAffinity();
            }

        }catch(Exception e){}

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpDialog();
                rq = Volley.newRequestQueue(getApplicationContext());
                String url = "http://rahulpanda.in/isight/server/index.php?username="+user.getText()+"&password="+pass.getText();
                jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("check").equals("True"))
                            {
                                hidepDialog();
                                Toast.makeText(getApplicationContext(),"Successfully Loggedin",Toast.LENGTH_LONG).show();
                                ed = sh.edit();
                                ed.putString("user",user.getText()+"");
                                ed.commit();
                                startActivity(new Intent(MainActivity.this,Dashboard.class));
                                finishAffinity();
                            }
                            else
                            {
                                //d.hide();
                                hidepDialog();
                                Toast.makeText(getApplicationContext(),"Failed !",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog();
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
                rq.add(jor);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = new Dialog(MainActivity.this);
                d.setContentView(R.layout.dialog);
                final EditText name,age,phone,email,user,pass;
                final Spinner lang;
                Button regi;
                regi = (Button)d.findViewById(R.id.reg);
                name = (EditText)d.findViewById(R.id.fname);
                age = (EditText)d.findViewById(R.id.age);
                phone = (EditText)d.findViewById(R.id.phone);
                email = (EditText)d.findViewById(R.id.email);
                user = (EditText)d.findViewById(R.id.user);
                pass = (EditText)d.findViewById(R.id.pass);

                lang = (Spinner)d.findViewById(R.id.lang);

                regi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showpDialog();
                        rq = Volley.newRequestQueue(getApplicationContext());
                        String url = "http://rahulpanda.in/isight/server/index.php?name="+name.getText()+"&age="+age.getText()+"&phone="+phone.getText()+"&email="+email.getText()+"&username="+user.getText()+"&password="+pass.getText()+"&language="+lang.getSelectedItem();;
                        Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
                        jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.getString("check").equals("True"))
                                    {
                                        d.hide();
                                        hidepDialog();
                                        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                                        ed = sh.edit();
                                        ed.putString("user",user.getText()+"");
                                        ed.commit();
                                        startActivity(new Intent(MainActivity.this,Dashboard.class));
                                        finishAffinity();
                                    }
                                    else
                                    {
                                        d.hide();
                                        hidepDialog();
                                        Toast.makeText(getApplicationContext(),"Failed !",Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                d.hide();
                                hidepDialog();
                                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                        rq.add(jor);
                    }
                });
                d.show();
            }
        });
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
