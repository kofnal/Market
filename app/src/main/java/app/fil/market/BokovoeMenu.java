package app.fil.market;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import app.fil.market.korzina.KorzinaActivity;
import app.fil.market.ui.poisk.PoiskFragment;
import app.fil.market.ui.poisk.PoiskObjectSQL;

public class BokovoeMenu extends AppCompatActivity   {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    public static TextView tvKorzinaCount;
    static TextView tvBokovI_Find;
    static EditText etTolb;
    ImageView ivKorzina;
    public static RequestQueue requestQueue;
    Activity contextThisActivity = this;
    DrawerLayout drawer;
    public  static SharedPreferences sharedPreferences;
    final public static String prefTagNameFile = "poiskHistory";
    final public static String prefTagNameRowData = "rowHistory";
    final public static int kolvoOtobrajaemixTVvPoiske = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bokovoe_menu_all_includ);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ivKorzina = findViewById(R.id.ivKorzina);
        tvKorzinaCount = findViewById(R.id.tvKorzinaCount);
        tvBokovI_Find = findViewById(R.id.tvBokovI_Find);
        etTolb = findViewById(R.id.etTolb);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences= getApplicationContext().getSharedPreferences(prefTagNameFile, MODE_PRIVATE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,  R.id.nav_tovari, R.id.nav_opisanie_tovara)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        MenuItem menuItemTelefon = navigationView.getMenu().findItem(R.id.nav_telefon);
        menuItemTelefon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                System.out.println("ItemClickListener "+menuItem.toString());
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getResources().getString(R.string.menu_share)));
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        MenuItem menuItemMail = navigationView.getMenu().findItem(R.id.nav_send);
        menuItemMail.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",getResources().getString(R.string.chetverg_mail), null));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        etTolb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode()==KeyEvent.KEYCODE_ENTER){
                    System.out.println("etTolb enter "+etTolb.getText().toString() +", "+keyEvent.toString());
                    PoiskFragment.poiskObjectSQLSList.add(0, new PoiskObjectSQL(etTolb.getText().toString()));
                    if(PoiskFragment.poiskObjectSQLSList.size()>kolvoOtobrajaemixTVvPoiske){
                        PoiskFragment.poiskObjectSQLSList.remove(PoiskFragment.poiskObjectSQLSList.size()-1);
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(PoiskFragment.poiskObjectSQLSList);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(prefTagNameRowData,json );
                    editor.commit();
                    PoiskFragment.adapter.notifyDataSetChanged();
                    etTolb.setText("");
                    return false;
                }else{
                    return false;
                }


            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onPause() {
        System.out.println("BokovoeMenu onPause");
        MainActivity.pokupatelStatic.wreateDataToPrefs();
        super.onPause();
    }
public void ibKorzinaClick(View v ){
//        System.out.println("ibKorzinaClick");
        if(ivKorzina!=null){
            Intent intentKorzina = new Intent(getApplicationContext(), KorzinaActivity.class);
            startActivity(intentKorzina);
        }
}
public void tvBokovI_FindOnClick(View v ){
        System.out.println("tvBokovI_FindOnClick");

    Navigation.findNavController(contextThisActivity, R.id.nav_host_fragment).navigate(R.id.nav_poiskFragment);

}
public static  void etPoiskActivaciya(boolean b){
        if(b){
            tvBokovI_Find.setVisibility(View.GONE);
            etTolb.setVisibility(View.VISIBLE);
        } else{
            tvBokovI_Find.setVisibility(View.VISIBLE);
            etTolb.setVisibility(View.GONE);
        }
}
    @Override
    protected void onResume() {
        if(tvKorzinaCount!=null){
//            tvKorzinaCount.setText(Integer.toString(MainActivity.pokupatelStatic.getKorzina_kountInt()));
            System.out.println("BokovoeMenu onResume");
        }
        super.onResume();
    }
}
