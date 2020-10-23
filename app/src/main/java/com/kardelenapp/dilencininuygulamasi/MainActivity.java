package com.kardelenapp.dilencininuygulamasi;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    AdsController adsController ;
    List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();
    SimpleAdapter adapter;
    @BindView(R.id.listview_gonluzenginler) ListView listview_gonluzenginler;

    @BindView(R.id.textView_bugun) TextView textView_bugun;
    @BindView(R.id.textView_toplam) TextView textView_toplam;
    @BindView(R.id.button_sadaka) TextView button_sadaka;

    @OnClick(R.id.button_sadaka) void submit_sadaka() {
        adsController.showRevardedVideo();
    }

    String googleplay= "https://play.google.com/store/apps/details?id=com.kardelenapp.dilencininuygulamasi";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();
        Paper.init(context);
        ButterKnife.bind(this);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        /* layout ayarları */
        int toplam_odul = Paper.book().read("toplam_odul",0);
        textView_toplam.setText(String.valueOf(toplam_odul) );
        int gunluk_odul = Paper.book().read("gunluk_odul",0);
        textView_bugun.setText(String.valueOf(gunluk_odul) );
        button_sadaka.setEnabled(false);

        processIntent(getIntent());

        adapter = new SimpleAdapter(this, listItems,
                android.R.layout.simple_list_item_2,
                new String[] {"First Line", "Second Line" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        Map<String, String> datum = new HashMap<String, String>(2);
        datum.put("First Line","yoldan geçen biri");
        datum.put("Second Line","Bu kısım yapım aşamasındadır!");
        listItems.add(datum);
        adapter.notifyDataSetChanged();

        listview_gonluzenginler.setAdapter(adapter);

        /* reklam ayarları */
        LinearLayout layout = (LinearLayout) findViewById(R.id.adsContainer);
        adsController = new AdsController(this);
        adsController.loadBanner(layout);
        adsController.RevardedVideoAdsPrepare();


        adsController.odulVerilsin.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
               if (adsController.odulVerilsin.isBoo()) {
                   Paper.book().write("toplam_odul",Paper.book().read("toplam_odul",0)+1);
                   Paper.book().write("gunluk_odul",Paper.book().read("gunluk_odul",0)+1);
                   int toplam_odul = Paper.book().read("toplam_odul",0);
                   textView_toplam.setText(String.valueOf(toplam_odul) );
                   int gunluk_odul = Paper.book().read("gunluk_odul",0);
                   textView_bugun.setText(String.valueOf(gunluk_odul) );

                   adsController.odulVerilsin.setBoo(false);
                   adsController.videoHazir.setBoo(false);

                   new DialogController(MainActivity.this).Simple("Sadaka Verildi","Allah razı olsun!","Sağol!");
               }
            }
        });

        adsController.videoHazir.setListener(new BooVariable.ChangeListener() {
            @Override
            public void onChange() {
                if (adsController.videoHazir.isBoo()) {
                    button_sadaka.setEnabled(true);
                }
                else{
                    button_sadaka.setEnabled(false);
                }
            }
        });

        DialogController dg = new DialogController(this);

        Boolean ilkGiris = Paper.book().read("ilkGiris",true);
        if(ilkGiris){
            dg.Simple("Abi kusura bakma","Allah ne muradın varsa versin. Uygulama yapım aşamasında, yinede sadaka verme düğmesi çalışıyor. Yakında pek çok özellik eklenecek. Uygulamanın google play sayfasında yorum kısmında programla ilgili istekte bulunabilir, dua talep edebilirsin. Haydi Allaha emanet ol!","Peki");
            Paper.book().write("ilkGiris",false);
        }


        alarmPeriodSet();

    }

        public void alarmPeriodSet(){
            Calendar calendar=Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 10);

            AlarmManager manager= (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent=new Intent(getApplicationContext(),AlarmReceiver.class);

            PendingIntent alarmintent=PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HALF_DAY,alarmintent);

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exit:
                this.finish();
                System.exit(0);
                return true;

            case R.id.action_about:
                Intent myIntent = new Intent(this, Hakkinda.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(myIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    };

    private void processIntent(Intent intent){
        cancelNotification(getApplicationContext(),001);
    }
}
