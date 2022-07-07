package com.nomgindia.nomg.Activtiy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.nomgindia.nomg.MainActivity;
import com.nomgindia.nomg.R;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    EditText etEmailSignIn,etPasswordSignIn;
    Button btnLogin,btnGoToSignUp;
    TextView tvForgotPassword;
//    ProgressBar spin_kit_login;
    private String APIKey;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_APIKEY = "APIKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailSignIn=findViewById(R.id.etEmailSignIn);
        etPasswordSignIn=findViewById(R.id.etPasswordSignIn);
        btnGoToSignUp=findViewById(R.id.btnGoToSignUp);
        btnLogin=findViewById(R.id.btnLogin);
        tvForgotPassword=findViewById(R.id.tvForgotPassword);
//        spin_kit_login=findViewById(R.id.spin_kit_login);
//
//
//        ChasingDots chasingDots = new ChasingDots();
//        spin_kit_login.setIndeterminateDrawable(chasingDots);


        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String APIkey = sharedPreferences.getString(KEY_APIKEY,null);

        if(APIkey!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }


//        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
//                startActivity(i);
//            }
//        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                spin_kit_login.setVisibility(View.VISIBLE);
                String email= etEmailSignIn.getText().toString().trim();
                String password = etPasswordSignIn.getText().toString().trim();


                loginUser(email,password);

            }
        });

    }

    private void loginUser(String email, String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String url = "https://nomgindia.herokuapp.com/nomgindia/api/v1/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("responseForLogin",response);


                APIKey =null;
                try
                {
                    APIKey= response;
                    if(APIKey.equals(null))
                    {
//                        spin_kit_login.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,"Enter the details properly!",Toast.LENGTH_SHORT).show();
                        etEmailSignIn.setError("Your phone number or password is wrong, please enter the correct credentials!");
                        etEmailSignIn.requestFocus();
                        etPasswordSignIn.setError("Your phone number or password is wrong, please enter the correct credentials!",null);
                        etPasswordSignIn.requestFocus();

                    }

                    else
                    {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_APIKEY,APIKey);
                        editor.apply();
//                        spin_kit_login.setVisibility(View.GONE);

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorFound", error.toString());
            }
        }) {


            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password",password);
                params.put("emailAddress",email);
                return params;

            }


        };

        requestQueue.add(stringRequest);
    }
}