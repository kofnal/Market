package app.fil.market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

import app.fil.market.korzina.KorzinaActivity;

public class BokovoeMenu extends AppCompatActivity   {

    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    public static TextView tvKorzinaCount;
    ImageView ivKorzina;
    public static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bokovoe_menu_all_includ);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ivKorzina = findViewById(R.id.ivKorzina);
        tvKorzinaCount = findViewById(R.id.tvKorzinaCount);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
