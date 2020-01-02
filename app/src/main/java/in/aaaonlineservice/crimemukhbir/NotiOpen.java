package in.aaaonlineservice.crimemukhbir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NotiOpen extends AppCompatActivity {
    Toolbar toolbar;
    List list;
    Gson gson;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    Map<String,Object> mapdesc;
    Map<String,Object> img;
    Map<String,Object> guild;
    ArrayList data;
    String name;
    DatabaseHelper databaseHelper;
    static TextToSpeech t1;
    int mapid;
    String youid;
    ArrayList<String> id;
    YouTubeThumbnailView youTubeThumbnailView;
    Map<String, Object> post_meta;
    ImageView imageView,save,textsize,imgshare,tts;
    LinearLayout ll;
    String you_id,link;
    private ProgressDialog dialog;

    String ttl,image,time,description;
    TextView tags_name,speak,textView,tsize, textViewcon;
    RelativeLayout you;
    ArrayList<res> contactArrayList;
    String[]postTitle;
    ImageView play;
    URLImageParser urlImageParser;
    String srch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_noti_open);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(NotiOpen.this);

        srch=getIntent().getStringExtra("title");
        srch=srch.replaceAll("\\s+", "%20");
        databaseHelper = new DatabaseHelper(NotiOpen.this);
        contactArrayList = new ArrayList<res>();
        id = new ArrayList<String>();
        imageView = (ImageView) findViewById(R.id.imgpost);
        ll = (LinearLayout) findViewById(R.id.tagsbgs);
        save = (ImageView) findViewById(R.id.save);
        tts = (ImageView)findViewById(R.id.tts);
        play=(ImageView)findViewById(R.id.btnplayer);
        textsize = (ImageView)findViewById(R.id.textsize);
        youTubeThumbnailView=(YouTubeThumbnailView) findViewById(R.id.you_thumbnail);
        tags_name = (TextView) findViewById(R.id.tags_name);
        speak = (TextView) findViewById(R.id.speak);
        textView = (TextView) findViewById(R.id.textqw);
        tsize = (TextView) findViewById(R.id.tsize);
        you=(RelativeLayout)findViewById(R.id.yuo);
        textViewcon = (TextView) findViewById(R.id.textcont);
        imgshare = (ImageView) findViewById(R.id.imgshare);
        final ImageView textsize = (ImageView) findViewById(R.id.textsize);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        t1=new TextToSpeech(NotiOpen.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin"));
                }
            }
        });
        getdata();
    }

    private void getdata() {
        if(isNetworkAvailable()) {
            this.dialog.setMessage("Please Wait");
            this.dialog.show();
            handleIntent();
        }
        else{
            Toast.makeText(NotiOpen.this, "No Network", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleIntent() {
        String url="https://crimemukhbir.com/wp-json/wp/v2/posts?search="+srch;
        Log.d("url",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                gson = new Gson();
                list = (List) gson.fromJson(response, List.class);
                postTitle = new String[list.size()];
                for(int i=0;i<list.size();++i){
                    res res=new res();
                    mapPost = (Map<String,Object>)list.get(i);
                    guild=(Map<String, Object>) mapPost.get("guid");
                    link= mapPost.get("slug").toString();
                    mapTitle = (Map<String, Object>) mapPost.get("title");
                    ttl = (String) mapTitle.get("rendered");
                    mapdesc = (Map<String, Object>) mapPost.get("content");
                    description = (String) mapdesc.get("rendered").toString();
                    Document doc = Jsoup.parse(description);
                    Elements p= doc.select("p");
                    img=(Map<String, Object>) mapPost.get("better_featured_image");
                    post_meta = (Map<String, Object>) mapPost.get("post-meta-fields");
                    data = (ArrayList) post_meta.get("_fvp_video");
                    if (data != null) {
                        for (int j = 0; j < data.size(); j++) {
                            SerializedPhpParser serializedPhpParser = new SerializedPhpParser(data.get(0).toString());
                            Object result = serializedPhpParser.parse();
                            HashMap newMap = new HashMap((Map) result);
                            String sas = newMap.get("full").toString();

                            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

                            Pattern compiledPattern = Pattern.compile(pattern);
                            Matcher matcher = compiledPattern.matcher(sas);

                            if (matcher.find()) {
                                Log.d("abc", matcher.group());
                                you_id = matcher.group();
                            }
                        }

                    }

                    else{
                        you_id = null;
                    }
                    mapid=((Double)mapPost.get("id")).intValue();
                    if(img!=null) {
                        image = (String) img.get("source_url");
                    }
                    else{
                        image=null;
                    }

                    res.setId(String.valueOf(mapid));
                    res.setImag(image);
                    res.setTitle(ttl);
                    res.setLink(link);
                    res.setContent(p.toString());
                    if(you_id!=null) {
                        res.setYoutube_id(you_id);
                    }
                    contactArrayList.add(res);
                }
                for(int i=0;i<contactArrayList.size();i++){
                    if(you_id==null){
                        imageView.setVisibility(View.VISIBLE);
                        you.setVisibility(View.GONE);
                        Picasso.with(NotiOpen.this).load(contactArrayList.get(0).getImag()).placeholder(R.drawable.app_logo).into(imageView);
                    }
                    else{
                        imageView.setVisibility(View.GONE);
                        you.setVisibility(View.VISIBLE);
                        youTubeThumbnailView.initialize(PlayerConfig.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                                youTubeThumbnailLoader.setVideo(contactArrayList.get(0).getYoutube_id());
                            }

                            @Override
                            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                            }
                        });
                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = YouTubeStandalonePlayer.createVideoIntent(NotiOpen.this, PlayerConfig.API_KEY, contactArrayList.get(0).getYoutube_id(),100,     //after this time, video will start automatically
                                        true,
                                        false);
                                startActivity(intent);
                            }
                        });
                    }
                    urlImageParser = new URLImageParser(textViewcon, NotiOpen.this);

                    textView.setText(Html.fromHtml(contactArrayList.get(0).getTitle()));
                    textViewcon.setText(Html.fromHtml(contactArrayList.get(0).getContent(), urlImageParser, null));

                }
                textsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tsize.getText().equals("Text++")){
                            DisplayMetrics metrics;
                            metrics = NotiOpen.this.getApplicationContext().getResources().getDisplayMetrics();
                            float Textsize =textViewcon.getTextSize()/metrics.density;
                            textViewcon.setTextSize(Textsize+3);
                            textsize.setImageResource(R.drawable.fontsize);
                            tsize.setText("Text--");
                        }
                        else{
                            DisplayMetrics metrics;
                            metrics = NotiOpen.this.getApplicationContext().getResources().getDisplayMetrics();
                            float Textsize =textViewcon.getTextSize()/metrics.density;
                            textViewcon.setTextSize(Textsize-3);
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
                            t1.speak(textViewcon.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                });

                imgshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, contactArrayList.get(0).getLink()+"\nShare by KV News App \nDownload KV News App from Play Store free"+"\nhttps://play.google.com/store/apps/details?id=in.aaaonlineservice.kvnews");
                        startActivity(Intent.createChooser(shareIntent, "Share Via..."));
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor sde = databaseHelper.getAllData();
                        id = new ArrayList<String>();
                        while (sde.moveToNext()) {
                            id.add(sde.getString(6));
                        }
                        boolean isExist = false;
                        for (int i = 0; i < id.size(); i++) {
                            if (contactArrayList.get(0).getId().equals(id.get(i))) {
                                Toast.makeText(NotiOpen.this, "Already saved", Toast.LENGTH_SHORT).show();
                                isExist = true;
                                break;
                            }
                        }

                        if(!isExist) {
                            Picasso.with(NotiOpen.this)
                                    .load(contactArrayList.get(0).getImag())
                                    .into(new Target() {
                                              @Override
                                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                  try {
                                                      String root = NotiOpen.this.getFilesDir().getPath();
                                                      File myDir = new File(root + "/crimemukhbir");

                                                      if (!myDir.exists()) {
                                                          myDir.mkdirs();
                                                      }

                                                      name = new Date().toString() + ".jpg";
                                                      myDir = new File(myDir, name);
                                                      FileOutputStream out = new FileOutputStream(myDir);
                                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                      out.flush();
                                                      out.close();
                                                  } catch (Exception e) {
                                                      // some action
                                                  }
                                              }

                                              @Override
                                              public void onBitmapFailed(Drawable errorDrawable) {
                                              }

                                              @Override
                                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                                              }
                                          }
                                    );
                            if(contactArrayList.get(0).getYoutube_id()==null){
                                youid="abc";
                            }
                            else{
                                youid=contactArrayList.get(0).getYoutube_id();
                            }
                            boolean isinserted = databaseHelper.inserdata(contactArrayList.get(0).getTitle(), contactArrayList.get(0).getLink(), contactArrayList.get(0).getTime(), contactArrayList.get(0).getContent().replaceAll("<img.+?>", ""), name, contactArrayList.get(0).getId(),youid);
                            if (isinserted) {
                                Toast.makeText(NotiOpen.this, "Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NotiOpen.this, "Unable To Save", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }




                });




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("resp",error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) NotiOpen.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(NotiOpen.this,MainActivity.class);
        startActivity(i);
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        finish();
        super.onBackPressed();
    }
}

