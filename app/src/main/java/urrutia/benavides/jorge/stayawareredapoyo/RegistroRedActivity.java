package urrutia.benavides.jorge.stayawareredapoyo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegistroRedActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOGIN_URL = "https://bancademia.net/StayAware/registerR.php";

    EditText editTextCorreo;
    EditText editTextClave;
    EditText editTextNombre;
    EditText editTextApellido;
    EditText editTextTelefono;
    EditText editTextRut;

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_red);
        editTextCorreo = (EditText) findViewById(R.id.Correoregistrored);
        editTextClave = (EditText) findViewById(R.id.Claveregistrored);
        editTextNombre = (EditText) findViewById(R.id.Nombreregistrored);
        editTextApellido = (EditText) findViewById(R.id.Apellidoregistrored);
        editTextTelefono = (EditText) findViewById(R.id.Telefonoregistrored);
        editTextRut = (EditText) findViewById(R.id.Rutregistrored);


        boton = (Button) findViewById(R.id.btnregistrored);

        boton.setOnClickListener(this);
    }
    private void registro(){
        String correo = editTextCorreo.getText().toString().trim();
        String clave = editTextClave.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String rut = editTextRut.getText().toString().trim();
        registrar(correo,clave,nombre,apellido,telefono,rut);
    }
    private void registrar(final String correo, final String clave, final String nombre, final String apellido, final String telefono, final String rut){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegistroRedActivity.this,"Espere por favor",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("registrado")){
                    Toast.makeText(RegistroRedActivity.this,"registrado exitosamente",Toast.LENGTH_LONG).show();
                    finish();

                }else{
                    Toast.makeText(RegistroRedActivity.this,"error en el registro",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);
                data.put("nombre",params[2]);
                data.put("apellido",params[3]);
                data.put("telefono",params[4]);
                data.put("rut",params[5]);
                HttpsQuerys ruc = new HttpsQuerys();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(correo,clave,nombre,apellido,telefono,rut);
    }
    @Override
    public void onClick(View v) {
        if(v == boton){
            registro();
        }
    }

}
