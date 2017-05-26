package urrutia.benavides.jorge.stayawareredapoyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "https://bancademia.net/StayAware/loginRed.php";

    Button btnlogin, btnregistrar;
    TextView correo, clave;
    protected String enteredUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnlogin = (Button) (findViewById(R.id.btningresarMain));
        btnregistrar = (Button) (findViewById(R.id.btnregistrarMain));
        correo = (TextView) (findViewById(R.id.Correo));
        clave = (TextView) (findViewById(R.id.Clave));
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroRedActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredUsername = correo.getText().toString();
                String enteredPassword = clave.getText().toString();

                if (enteredUsername.equals("") || enteredPassword.equals("")) {
                    Toast.makeText(MainActivity.this, "El nombre de usuario o la contraseña deben rellenarse", Toast.LENGTH_LONG).show();
                    return;
                }
                if (enteredUsername.length() <= 1 || enteredPassword.length() <= 1) {
                    Toast.makeText(MainActivity.this, "Correo y clave deben tener mas de un dígito", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    userLogin(enteredUsername,enteredPassword);
                }
            }
        });
    }

    private void userLogin(final String username, final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Espere por favor", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    String idred = obj.getString("id_RedApoyo");
                    Intent intent3 = new Intent(MainActivity.this, redActivity.class);
                    intent3.putExtra("idred", idred);
                    startActivity(intent3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("Usuario", params[0]);
                data.put("Clave", params[1]);

                HttpsQuerys ruc = new HttpsQuerys();

                String result = ruc.sendPostRequest(LOGIN_URL, data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }
}
