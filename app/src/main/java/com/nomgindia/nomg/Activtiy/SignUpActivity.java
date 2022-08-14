package com.nomgindia.nomg.Activtiy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.nomgindia.nomg.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    EditText etNameSignUp,etEmailSignUp,etPasswordSignUp;
    Button btnSignUp;
    TextView tvGoToLogin;
    ProgressBar spin_kit_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etNameSignUp=findViewById(R.id.etNameSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        btnSignUp=findViewById(R.id.btnSignUp);
        tvGoToLogin=findViewById(R.id.tvGotoLoginPage);
        spin_kit_signup=findViewById(R.id.spin_kit_signup);

        ChasingDots chasingDots = new ChasingDots();
        spin_kit_signup.setIndeterminateDrawable(chasingDots);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin_kit_signup.setVisibility(View.VISIBLE);
                String name = etNameSignUp.getText().toString().trim();
                String password = etPasswordSignUp.getText().toString().trim();
                String email = etEmailSignUp.getText().toString().trim();
                createAccount(name,password,email);
            }
        });

        tvGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void createAccount(String name, String password, String email){
        RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
        String url = "https://nomgindia.herokuapp.com/nomgindia/api/v1/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Registered"))
                {
                    spin_kit_signup.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else if(response.equals("Email Address already in use"))
                {
                    spin_kit_signup.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Email Address already in use", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    spin_kit_signup.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, "Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpActivity.this, "Something unusual occurred", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("password", password);
                params.put("emailAddress", email);
                Log.d("paramsValue", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}