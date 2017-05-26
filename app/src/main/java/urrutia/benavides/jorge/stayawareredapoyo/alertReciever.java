package urrutia.benavides.jorge.stayawareredapoyo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

public class alertReciever extends Service {

    final String listenURL = "https://www.bancademia.net/StayAware/listenAlert.php";
    public static final int notification_id = 1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void listenAlert(final String idRedApoyo){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject obj = new JSONObject(s);
                    if(obj.isNull("error")){
                        String tipoAlerta = obj.getString("tipoAlerta");
                        String nombreAM = obj.getString("nombreAdultoMayor");
                        String apellidoAM = obj.getString("apellidoAdultoMayor");
                        String nombreRA = obj.getString("nombreRedApoyo");
                        String apellidoRA = obj.getString("apellidoRedApoyo");
                        String idAlerta = obj.getString("idAlerta");

                        String setTicket = "";
                        String setContentTitle = "";
                        String setSubText = "";
                        // CONFIG NOTIFICACION

                        if(tipoAlerta.equals("TemperaturaBaja")){
                            // NOTIFICACION
                            setTicket = "Alerta de Temperatura Baja! - StayAware";
                            setContentTitle = nombreAM+" "+apellidoAM+ ", siente frio.";
                            setSubText = "Pulse para ayudarlo";
                        }else if(tipoAlerta.equals("TemperaturaAlta")){
                            // NOTIFICACION
                            setTicket = "Alerta de Temperatura Alta! - StayAware";
                            setContentTitle = nombreAM+" "+apellidoAM+ ", siente calor";
                            setSubText = "Pulse para ayudarlo";
                        }else if(tipoAlerta.equals("Caida")){
                            // NOTIFICACION
                            setTicket = "Alerta de Caida! - StayAware";
                            setContentTitle = nombreAM+" "+apellidoAM+ ", sufrió una caída";
                            setSubText = "Pulse para ayudarlo";
                        }else if(tipoAlerta.equals("Panico")){
                        // NOTIFICACION
                        setTicket = "Alerta de Pánico! - StayAware";
                        setContentTitle = nombreAM+" "+apellidoAM+ ", activo el boton de pánico";
                        setSubText = "Pulse para ayudarlo";
                    }


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bancademia.net/StayAware/procesarAlerta.php?idAlerta=" +idAlerta));
                        PendingIntent pendingIntent = PendingIntent.getActivity(alertReciever.this,0,intent,0);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(alertReciever.this);
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentIntent(pendingIntent);
                        builder.setAutoCancel(true);
                        builder.setVibrate(new long[] {1000,1000,1000,1000,1000,1000});
                        builder.setTicker(setTicket);
                        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                        builder.setContentTitle(setContentTitle);
                        builder.setSubText(setSubText);

                        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(notification_id,builder.build());
                    }else{
                        Toast.makeText(getApplicationContext(),obj.getString("error"),Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("redApoyo",params[0]);
                HttpsQuerys ruc = new HttpsQuerys();

                String result = ruc.sendPostRequest(listenURL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(idRedApoyo);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                listenAlert(intent.getExtras().getString("idred"));
            }

        };


        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(3000);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        return START_STICKY;
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Modo Alerta desactivado",Toast.LENGTH_LONG).show();
        this.stopSelf();
    }
}
