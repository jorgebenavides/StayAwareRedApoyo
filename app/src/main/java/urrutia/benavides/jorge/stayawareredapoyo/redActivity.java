package urrutia.benavides.jorge.stayawareredapoyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class redActivity extends AppCompatActivity {
    Button btnver, btnagregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red);
        btnver = (Button) (findViewById(R.id.btnveradultosred));
        btnagregar = (Button) (findViewById(R.id.btnagregaradultored));
        final Button btnmodoalerta = (Button) findViewById(R.id.btnmodoalerta);
        final Button btnoffmodoalerta = (Button) findViewById(R.id.btnoffmodoalerta);
        final String idred = getIntent().getExtras().getString("idred");

        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentalo = new Intent(redActivity.this, VerAdultosActivity.class);
                intentalo.putExtra("idred", idred);
                startActivity(intentalo);
            }
        });
        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(redActivity.this, AgregarAdultosActivity.class);
                intent2.putExtra("idred", idred);
                startActivity(intent2);
            }
        });

        btnmodoalerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(redActivity.this, alertReciever.class);
                intent.putExtra("idred",idred);
                startService(intent);
                btnmodoalerta.setVisibility(View.INVISIBLE);
                btnoffmodoalerta.setVisibility(View.VISIBLE);
            }
        });

        btnoffmodoalerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(redActivity.this, alertReciever.class));
                btnmodoalerta.setVisibility(View.VISIBLE);
                btnoffmodoalerta.setVisibility(View.INVISIBLE);
            }
        });

    }
}
