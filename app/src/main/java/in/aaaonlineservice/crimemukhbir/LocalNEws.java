package in.aaaonlineservice.crimemukhbir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.Locale;


public class LocalNEws extends AppCompatActivity {
String localid,you;
    ImageView imageView,shareimg,tts,textsize;
    TextView title,content,tsize,speak;
    Toolbar toolbar;
    YouTubeThumbnailView youTubeThumbnailView;
    ImageView play;
    RelativeLayout yourelate;
    static TextToSpeech t1;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_local_news);
        localid=getIntent().getExtras().getString("id");
        you=getIntent().getExtras().getString("you");
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        play=(ImageView)findViewById(R.id.btnplayer);
        yourelate=(RelativeLayout)findViewById(R.id.yuo);
        youTubeThumbnailView = (YouTubeThumbnailView)findViewById(R.id.you_thumbnail);
        imageView=(ImageView)findViewById(R.id.imgpost);
        shareimg=(ImageView)findViewById(R.id.imgshare);
        title=(TextView)findViewById(R.id.toolbar_title);
        tts = (ImageView) findViewById(R.id.tts);
        tsize = (TextView) findViewById(R.id.tsize);
        speak = (TextView)findViewById(R.id.speak);
        textsize = (ImageView)findViewById(R.id.textsize);
        content=(TextView)findViewById(R.id.textcont);
        t1=new TextToSpeech(LocalNEws.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin"));
                }
            }
        });        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseHelper=new DatabaseHelper(this);
        final Cursor sde=databaseHelper.getIndividualData(localid);
        if (sde.moveToFirst())
        {
            do
            {
                title.setText(sde.getString(1));
                if(you.equals("abc")){
                    yourelate.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }
                else{
                   if(isNetworkAvailable()){
                       yourelate.setVisibility(View.VISIBLE);
                       imageView.setVisibility(View.GONE);
                       youTubeThumbnailView.initialize(PlayerConfig.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                           @Override
                           public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                               youTubeThumbnailLoader.setVideo(you);
                           }

                           @Override
                           public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                           }
                       });
                       play.setVisibility(View.VISIBLE);
                       play.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               if(isNetworkAvailable()){
                                   Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) LocalNEws.this, PlayerConfig.API_KEY, you,100,     //after this time, video will start automatically
                                           true,
                                           false);
                                   startActivity(intent);
                               }
                           }
                       });
                   }
                   else{
                       imageView.setVisibility(View.VISIBLE);
                       play.setVisibility(View.VISIBLE);
                       play.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               if(isNetworkAvailable()){
                                   Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) LocalNEws.this, PlayerConfig.API_KEY, you,100,     //after this time, video will start automatically
                                           true,
                                           false);
                                   startActivity(intent);
                               }
                               else{
                                   Toast.makeText(LocalNEws.this,"You need active internet connection to play",Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }

                }
                content.setText(Html.fromHtml(sde.getString(4)));
                String photoPath = getApplicationContext().getFilesDir().getPath()+"/crimemukhbir/"+sde.getString(5);
                final Bitmap b = BitmapFactory.decodeFile(photoPath);
                if(b==null){
                    imageView.setImageResource(R.drawable.img);
                }
                else{
                    imageView.setImageBitmap(b);
                }
                final String abc=sde.getString(2);


               shareimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, abc+"\nShare by Crime Mukhbir App \nDownload Crime Mukhbir App from Play Store free"+"\nhttps://play.google.com/store/apps/details?id=in.aaaonlineservice.crimemukhbir");
                        startActivity(Intent.createChooser(shareIntent, "Share Via..."));
                    }
                });
            }while (sde.moveToNext());
        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();

                }
            }
        });
        textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tsize.getText().equals("Text++")){
                    DisplayMetrics metrics;
                    metrics = LocalNEws.this.getApplicationContext().getResources().getDisplayMetrics();
                    float Textsize =content.getTextSize()/metrics.density;
                    content.setTextSize(Textsize+3);
                    textsize.setImageResource(R.drawable.fontsize);
                    tsize.setText("Text--");
                }
                else{
                    DisplayMetrics metrics;
                    metrics = LocalNEws.this.getApplicationContext().getResources().getDisplayMetrics();
                    float Textsize =content.getTextSize()/metrics.density;
                    content.setTextSize(Textsize-3);
                    textsize.setImageResource(R.drawable.dec);
                    tsize.setText("Text++");
                }

            }
        });
        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speak.getText().toString().equals("Off")) {
                    tts.setImageResource(R.drawable.tts);
                    speak.setText("Listen");
                    if (t1 != null) {
                        t1.stop();

                    }
                }
                else if(speak.getText().toString().equals("Listen")){
                    tts.setImageResource(R.drawable.ttsoff);
                    speak.setText("Off");
                    t1.speak(content.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) LocalNEws.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
