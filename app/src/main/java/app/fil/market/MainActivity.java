package app.fil.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.fil.market.Model.Utils;
import app.fil.market.user.Pokupatel;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public  static RequestQueue requestQueue2;
    public static Pokupatel pokupatelStatic;
    Intent intent;
    TextView tvMainLog, tvTemp;
    Button btMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        tvMainLog=findViewById(R.id.tvMainLog);
        tvTemp=findViewById(R.id.tvTemp);
        btMain=findViewById(R.id.btMain);
        // Check if user is signed in (non-null) and update UI accordingly.
        pokupatelStatic =new Pokupatel(getApplicationContext());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            anonimnVxod();
            System.out.println("net Fir User");
            tvMainLog.append(" net Fir user");
        }else{
            updateUI(mAuth.getCurrentUser());
            System.out.println("est Fir User ");
            tvMainLog.append(" est Fir user 2");
        }
        btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onPause() {
        System.out.println("MainActiv1 onPause");
        super.onPause();
    }
    @Override
    protected void onStop() {
        System.out.println("MainActiv1 onStop");

        super.onStop();
    }

    private void anonimnVxod() {
        tvMainLog.append(" anonimn vxod ");
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            System.out.println("signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            System.out.println( "signInAnonymously:failure");
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser currentUser) {
        System.out.println("Fir user "+ currentUser.getUid());
        tvMainLog.append("\n updateUI \n");
        intent = new Intent(this, BokovoeMenu.class);
//        intent=new Intent(this, PodKategoriiActivity.class);
        insertGoogleUIDIntoMySQL(currentUser.getUid());
    }
    void insertGoogleUIDIntoMySQL (final String google_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.insertAnonimUserWithGoogleUID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvMainLog.setText("\ninsertGoogleUIDIntoMySQL "+ response);
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            System.out.println("jsObj Main Insert Anonim user UID "+jsonObject);

                            String id_sql = jsonObject.getString("sql_id");
                            String korzina_sum = jsonObject.getString("korzina_sum");

                            MainActivity.pokupatelStatic.setKorzinaCountStr(korzina_sum);
                            System.out.println("jsID Main Get ID "+id_sql);
                            pokupatelStatic.setId_sql(id_sql);
                            pokupatelStatic.setId_goog(mAuth.getUid());
                            startActivity(intent);
                            //afinish();
                        }
                        catch (JSONException e) {

                            System.out.println("\n ERR"+response.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvMainLog.append("\n eror Volley "+error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<String,String>();
                System.out.println("Otpravka na server UID  "+google_id);
                parameters.put("google_id", google_id);
//                tvMainLog.append("\n send UID toSQL "+parameters.toString());
                return parameters;
            }
        };
        requestQueue2.add(stringRequest);
    }
}
