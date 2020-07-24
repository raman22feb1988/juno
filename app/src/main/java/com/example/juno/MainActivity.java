package com.example.juno;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String website = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

        Viewdata v1 = new Viewdata();
        v1.delegate = this;
        v1.setId(1);
        v1.execute("GET", website);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String output, int viewdata) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            String date = jsonObject.getString("date");
            String heading = jsonObject.getString("title");
            String description = jsonObject.getString("explanation");
            final String url = jsonObject.getString("url");
            String media_type = jsonObject.getString("media_type");

            TextView t1 = findViewById(R.id.date);
            TextView t2 = findViewById(R.id.heading);
            TextView t3 = findViewById(R.id.description);
            final ImageView i1 = findViewById(R.id.picture);
            ImageView i2 = findViewById(R.id.calendar);
            ImageView i3 = findViewById(R.id.schedule);

            t1.setText(date);
            t2.setText(heading);
            t3.setText(description);

            if(media_type.equals("image"))
            {
                new ImageLoadTask(url, i1).execute();
                i3.setImageResource(R.drawable.zoom);

                i3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        final View yourCustomView = inflater.inflate(R.layout.photo, null);

                        ImageView i4 = yourCustomView.findViewById(R.id.photo);
                        new ImageLoadTask(url, i4).execute();

                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Zoomed Photo")
                                .setView(yourCustomView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).create();
                        dialog.show();
                    }
                });
            }
            else
            {
                String u[] = url.split("/");
                String v[] = u[u.length - 1].split("\\?");
                final String w = v[0];
                String t = "https://img.youtube.com/vi/" + w + "/default.jpg";
                new ImageLoadTask(t, i1).execute();
                i3.setImageResource(R.drawable.play);

                i3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        final View yourCustomView = inflater.inflate(R.layout.video, null);

                        VideoView v1 = yourCustomView.findViewById(R.id.video);
                        String r = "https://www.youtube.com/watch?v=" + w;

                        v1.setMediaController(new MediaController(MainActivity.this));
                        v1.requestFocus();

                        RTSPUrltask truitonTask = new RTSPUrltask(v1);
                        truitonTask.execute(r);

                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Playing Video")
                                .setView(yourCustomView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).create();
                        dialog.show();
                    }
                });
            }

            i2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    final View yourCustomView = inflater.inflate(R.layout.calendar, null);

                    final DatePicker d1 = yourCustomView.findViewById(R.id.datepicker);

                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Choose Date")
                            .setView(yourCustomView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String s = d1.getYear() + "-" + (d1.getMonth() <= 8 ? ("0" + (d1.getMonth() + 1)) : Integer.toString(d1.getMonth() + 1)) + "-" + (d1.getDayOfMonth() <= 9 ? ("0" + d1.getDayOfMonth()) : Integer.toString(d1.getDayOfMonth()));

                                    String url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&&date=" + s;

                                    Viewdata v2 = new Viewdata();
                                    v2.delegate = MainActivity.this;
                                    v2.setId(1);
                                    v2.execute("GET", url);
                                }
                            }).create();
                    dialog.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
