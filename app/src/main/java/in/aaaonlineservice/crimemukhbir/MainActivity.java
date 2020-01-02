package in.aaaonlineservice.crimemukhbir;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {
    FragmentManager myFragmentManager;
    NavigationView navigationView;
    LinearLayout breaking;
    TextView textView;
   ImageView live;
    SharedPreferences.Editor editor;
    private SwitchCompat switcher;
    FragmentTransaction myFragmentTransaction;
    DrawerLayout drawer;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final int MY_SOCKET_TIMEOUT_MS =5000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        live=(ImageView) findViewById(R.id.youdata);
        breaking=(LinearLayout)findViewById(R.id.breaki);
        textView = (TextView) this.findViewById(R.id.mar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        FcmMessaging.con=0;
        ShortcutBadger.applyCount(MainActivity.this,FcmMessaging.con);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        myFragmentManager = getSupportFragmentManager();
        myFragmentTransaction = myFragmentManager.beginTransaction();
        myFragmentTransaction.replace(R.id.containerView, new HomeFragment()).commit();
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_notification);
        View actionView = MenuItemCompat.getActionView(menuItem);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switcher);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean val=prefs.getBoolean("name",true);
        if (val) {
            switcher.setChecked(true);
        }
        else{
            switcher.setChecked(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem selectedMenuItem) {
                drawer.closeDrawers();
                if (selectedMenuItem.getItemId() == R.id.nav_home) {
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new HomeFragment()).commit();
                  }
                else if (selectedMenuItem.getItemId() == R.id.nav_saved) {
                    Intent i = new Intent(MainActivity.this, Saved_VAlue.class);
                    startActivity(i);
                }


                else if (selectedMenuItem.getItemId() == R.id.nav_video) {
                    Intent i = new Intent(MainActivity.this, VideoYou.class);
                    startActivity(i);
                }

                else if (selectedMenuItem.getItemId() == R.id.nav_care) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crimemukhbir.com/"));
                    startActivity(browserIntent);
                }
                else if (selectedMenuItem.getItemId() == R.id.nav_facebook) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Crime-Mukhbir-108387510680670"));
                    startActivity(browserIntent);
                }

                else if (selectedMenuItem.getItemId() == R.id.nav_privacy) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://crimemukhbir.com/privacy-policy/"));
                    startActivity(browserIntent);
                }
                else if (selectedMenuItem.getItemId() == R.id.nav_rate) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=in.aaaonlineservice.crimemukhbir"));
                    startActivity(browserIntent);
                }
                else if (selectedMenuItem.getItemId() == R.id.nav_you) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCEqcabBRv-aNwS8Wh-aoygw"));
                    startActivity(browserIntent);
                }
                else if (selectedMenuItem.getItemId() == R.id.nav_share) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Crime Mukhbir App from Play Store free"+"\n"+"https://play.google.com/store/apps/details?id=in.aaaonlineservice.crimemukhbir");
                    startActivity(Intent.createChooser(shareIntent, "Share Via..."));
                }

                return false;

            }
        });
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("name", true);
                    editor.apply();

                } else {
                    editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("name", false);
                    editor.apply();
                }


            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, VideoYou.class);
                startActivity(i);
            }
        });


        String abc = "https://crimemukhbir.com/wp-json/wp/v2/posts";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, abc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    StringBuilder sb=new StringBuilder();
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        JSONObject jsonObject1=jsonObject.getJSONObject("title");
                        String val=jsonObject1.getString("rendered");
                        sb.append(Html.fromHtml(val)+"   ||   ");
                    }
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    textView.setText(sb);
                    textView.setSelected(true);
                    textView.setSingleLine(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                breaking.setVisibility(View.GONE);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.app_logo)
                    .setTitle("Confirm Exit...")
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }
}