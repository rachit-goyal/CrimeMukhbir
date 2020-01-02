package in.aaaonlineservice.crimemukhbir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VidAdapter extends RecyclerView.Adapter<VidAdapter.VideoInfoHolder> {

    //these ids are the unique id for each video
    ArrayList<res> data;
    Context ctx;
    ArrayList<String> id;
    String name,youid;
    static TextToSpeech t1;
    DatabaseHelper databaseHelper;

    public VidAdapter(Context context, ArrayList<res> resArrayList) {
        this.ctx = context;
        this.data=resArrayList;
        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("hin"));
                }
            }
        });
        databaseHelper = new DatabaseHelper(ctx);
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sinngle_video, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };
        if(data.get(position).getTitle().length()>50){
            holder.tit.setText(Html.fromHtml(data.get(position).getTitle().substring(0,50)+"..."));

        }
        else{
            holder.tit.setText(Html.fromHtml(data.get(position).getTitle()));
        }


        holder.content.setText(Html.fromHtml(data.get(position).getContent()));
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.content.setText(Html.fromHtml(data.get(position).getContent(), new URLImageParser(holder.tit, ctx), null));
        holder.youTubeThumbnailView.initialize(PlayerConfig.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(data.get(position).getYoutube_id());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

        if(holder.getAdapterPosition()==0){
            holder.more.setVisibility(View.VISIBLE);
        }
        else{
            holder.more.setVisibility(View.GONE);
        }
        if(data.size()==1){
            holder.more.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;
        protected TextView more;
        ImageView share,save,tts,textsize;
        TextView time,tsize;

        protected TextView content,speak;
        protected TextView tit;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);
            more=(TextView)itemView.findViewById(R.id.morevid);
            playButton.setOnClickListener(this);
            textsize = (ImageView) itemView.findViewById(R.id.textsize);
            speak = (TextView) itemView.findViewById(R.id.speak);
            tsize=(TextView)itemView.findViewById(R.id.tsize);
            save = (ImageView) itemView.findViewById(R.id.save);
            tts = (ImageView) itemView.findViewById(R.id.tts);
            share=(ImageView)itemView.findViewById(R.id.imgshare);
            content=(TextView)itemView.findViewById(R.id.vidcontent);
            tit=(TextView)itemView.findViewById(R.id.tit);


            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, data.get(getAdapterPosition()).getLink()+"\nShare by Crime Mukhbir App \nDownload Crime Mukhbir App from Play Store free"+"\nhttps://play.google.com/store/apps/details?id=in.aaaonlineservice.crimemukhbir");
                    ctx.startActivity(Intent.createChooser(shareIntent, "Share Via..."));
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
                        if (data.get(getLayoutPosition()).getId().equals(id.get(i))) {
                            Toast.makeText(ctx, "Already saved", Toast.LENGTH_SHORT).show();
                            isExist = true;
                            break;
                        }
                    }

                    if(!isExist) {
                        Picasso.with(ctx)
                                .load(data.get(getAdapterPosition()).getImag())
                                .into(new Target() {
                                          @Override
                                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                              try {
                                                  String root = ctx.getFilesDir().getPath();
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
                        if(data.get(getAdapterPosition()).getYoutube_id()==null){
                            youid="abc";
                        }
                        else{
                            youid=data.get(getAdapterPosition()).getYoutube_id();
                        }
                        boolean isinserted = databaseHelper.inserdata(data.get(getAdapterPosition()).getTitle(), data.get(getAdapterPosition()).getLink(), data.get(getAdapterPosition()).getTime(), data.get(getAdapterPosition()).getContent().replaceAll("<img.+?>", ""), name,data.get(getAdapterPosition()).getId(),youid);
                        if (isinserted) {
                            Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ctx, "Unable To Save", Toast.LENGTH_SHORT).show();
                        }
                    }

                }




            });
            textsize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tsize.getText().toString().equals("Text++")){
                        DisplayMetrics metrics;
                        metrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
                        float Textsize =content.getTextSize()/metrics.density;
                        content.setTextSize(Textsize+3);
                        tsize.setText("Text--");
                        textsize.setImageResource(R.drawable.dec);
                    }
                    else{
                        DisplayMetrics metrics;
                        metrics = ctx.getApplicationContext().getResources().getDisplayMetrics();
                        float Textsize =content.getTextSize()/metrics.density;
                        content.setTextSize(Textsize-3);
                        tsize.setText("Text++");
                        textsize.setImageResource(R.drawable.fontsize);
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

        @Override
        public void onClick(View v) {

            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, PlayerConfig.API_KEY, data.get(getAdapterPosition()).getYoutube_id(),100,     //after this time, video will start automatically
                    true,
                    false);
            ctx.startActivity(intent);
            }
    }
}