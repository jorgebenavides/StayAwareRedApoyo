package urrutia.benavides.jorge.stayawareredapoyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AgregarAdultosActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOGIN_URL = "https://bancademia.net/StayAware/registerAM.php";

    EditText editTextNombre;
    EditText editTextApellido;
    EditText editTextTelefono;
    EditText editTextDireccion;
    EditText editTextRut;

    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_adultos);
        editTextNombre = (EditText) findViewById(R.id.nombreagregar);
        editTextApellido = (EditText) findViewById(R.id.apellidoagregar);
        editTextTelefono = (EditText) findViewById(R.id.telefonoagregar);
        editTextDireccion = (EditText) findViewById(R.id.direccionagregar);
        editTextRut = (EditText) findViewById(R.id.rutagregar);

        boton = (Button) findViewById(R.id.btnagregar);

        boton.setOnClickListener(this);
    }

    private void registro(){
        String rut = editTextRut.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String apellido = editTextApellido.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String direccion = editTextDireccion.getText().toString().trim();
        registrar(rut,nombre,apellido,telefono,direccion);
    }

    private void registrar(final String rut, final String nombre, final String apellido, final String telefono, final String direccion){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AgregarAdultosActivity.this,"Espere por favor",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject obj = new JSONObject(s);
                    if(obj.isNull("error")){
                        Toast.makeText(AgregarAdultosActivity.this,"registrado exitosamente",Toast.LENGTH_LONG).show();
                        String idAdulto = obj.getString("id_AdultoMayor");
                        Intent intent4 = new Intent(AgregarAdultosActivity.this, SyncBeanActivity.class);
                        intent4.putExtra("idAdulto", idAdulto);
                        startActivity(intent4);

                    }else{
                        Toast.makeText(AgregarAdultosActivity.this,obj.getString("error"),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("rut",params[0]);
                data.put("nombre",params[1]);
                data.put("apellido",params[2]);
                data.put("telefono",params[3]);
                data.put("direccion",params[4]);
                HttpsQuerys ruc = new HttpsQuerys();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(rut,nombre,apellido,telefono,direccion);
    }

    @Override
    public void onClick(View v) {
        if(v == boton){
            registro();
        }
    }


}
