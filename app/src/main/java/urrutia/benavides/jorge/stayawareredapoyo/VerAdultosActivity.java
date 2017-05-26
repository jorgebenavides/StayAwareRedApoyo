package urrutia.benavides.jorge.stayawareredapoyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VerAdultosActivity extends AppCompatActivity {
    String url="https://bancademia.net/StayAware/VerAdultos.php";
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_adultos);
        final String idred = getIntent().getExtras().getString("idred");

        userLogin(idred);


    }
    public void userLogin(final String idred) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            final ListView lv= (ListView) findViewById(R.id.lv);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(VerAdultosActivity.this, "Espere por favor", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                final ArrayList <JSONArray> arreglo = new ArrayList<>();
                final ArrayAdapter<JSONObject> AdultosMayores = new ArrayAdapter(VerAdultosActivity.this, android.R.layout.simple_expandable_list_item_1,arreglo);
                try {
                    JSONArray obj = new JSONArray(s);
                    for(int i=0;i < obj.length();i++){
                        JSONObject Adulto = obj.getJSONObject(i);
                        AdultosMayores.add(Adulto);
                        lv.setAdapter(AdultosMayores);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("idred", params[0]);

                HttpsQuerys ruc = new HttpsQuerys();

                String result = ruc.sendPostRequest(url, data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(idred);
    }
}
