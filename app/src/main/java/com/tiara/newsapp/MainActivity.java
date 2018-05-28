package com.tiara.newsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;
import com.tiara.newsapp.Fragment.BolaFragment;
import com.tiara.newsapp.Fragment.DuniaFragment;
import com.tiara.newsapp.Fragment.FinanceFragment;
import com.tiara.newsapp.Fragment.FoodFragment;
import com.tiara.newsapp.Fragment.NewsFragment;
import com.tiara.newsapp.Fragment.OlahragaFragment;
import com.tiara.newsapp.Fragment.TeknologiFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.slider)
    SliderLayout slider;
    @BindView(R.id.gridView)
    GridView gridView;
    ArrayList<Berita> data;
    @BindView(R.id.textSearch)
    AutoCompleteTextView textSearch;
    @BindView(R.id.progressBarSearch)
    ProgressBar progressBarSearch;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = new ArrayList<>();

        getData();
        getData2();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getData2() {
        String url = Config.BASE_URL + "Update_Dunia";

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading..harap sabar..");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.d("data", response.toString());
                try {
                    JSONObject jarray = new JSONObject(response);
                    for (int i = 0; i < jarray.length(); i++) {
                        Berita b = new Berita();
                        JSONObject a = jarray.getJSONObject(String.valueOf(i));
                        b.setId(a.getString("id"));
                        b.setTittle(a.getString("judul"));
                        b.setGambar(a.getString("gambar"));
                        b.setLink(a.getString("link"));

                        data.add(b);
                        PindahKeGridView(data);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue req = Volley.newRequestQueue(MainActivity.this);
        req.add(request);
    }

    private void PindahKeGridView(final ArrayList<Berita> data) {
        progressBarSearch.setVisibility(View.GONE);
        //setAdapter untuk gridtview
        CustomAdapter adapter = new CustomAdapter(this, data);
        gridView.setAdapter(adapter);
        //search adapter untuk search berita
        SearchAdapter adapter2 = new SearchAdapter(this, R.id.itemlist, R.id.imglist, progressBarSearch, data);
        //autocomplete berjalan waktu user mengetikan satu karakter di search(TextView AutoComplete)
        textSearch.setThreshold(1);
        textSearch.setAdapter(adapter2);
        textSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Detail_Berita.class);
                intent.putExtra("link",data.get(i).getLink());

                startActivity(intent);
                finish();
            }
        });
        //script untuk jika dari gridView di klik
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Detail_Berita.class);
                intent.putExtra("link", data.get(i).getLink());//data yangkita ngkut kita masukan ke dalam putExtra

                startActivity(intent);
                finish();
            }
        });
    }


    private void getData() {
        String url = Config.BASE_URL + "Update_Teknologi";

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading please to be patient..");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.d("data", response.toString());
                try {
                    JSONObject jarray = new JSONObject(response);
                    for (int i = 0; i < jarray.length(); i++) { 
                        Berita b = new Berita();
                        JSONObject a = jarray.getJSONObject(String.valueOf(i));
                        b.setId(a.getString("id"));
                        b.setTittle(a.getString("judul"));
                        b.setGambar(a.getString("gambar"));
                        b.setLink(a.getString("link"));

                        String id = a.getString("id");
                        String judul = a.getString("judul");
                        String link = a.getString("link");
                        String gambar = a.getString("gambar");

                        data.add(b);
                        ActionSlider2(id, judul, link, gambar);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue req = Volley.newRequestQueue(MainActivity.this);
        req.add(request);
    }


    private void ActionSlider2(String id, String judul, String link, String gambar) {
        TextSliderView text = new TextSliderView(MainActivity.this);
        text.description(judul)
                .image(gambar)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        slider.addSlider(text);
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(5000);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_utama) {
            BolaFragment bone = new BolaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, bone);
            ft.commit();//menyimpan perubahan setela di refrash
            // Handle the camera action

        } else if (id == R.id.nav_teknologi) {
            TeknologiFragment tine = new TeknologiFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, tine);
            ft.commit();//menyimpan perubahan setela di refrash

        } else if (id == R.id.nav_finance) {
            FinanceFragment fine = new FinanceFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fine);
            ft.commit();//menyimpan perubahan setelah di refrash

        } else if (id == R.id.nav_olahraga) {
            OlahragaFragment olne = new OlahragaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, olne);
            ft.commit();//menyimpan perubahan setela di refrash

        } else if (id == R.id.nav_dunia) {
            DuniaFragment dune = new DuniaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, dune);
            ft.commit();//menyimpan perubahan setela di refrash

        } else if (id == R.id.nav_news) {
            NewsFragment nene = new NewsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, nene);
            ft.commit();//menyimpan perubahan setela di refrash

        }else if (id == R.id.nav_food) {
            FoodFragment fone = new FoodFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fone);
            ft.commit();//menyimpan perubahan setela di refrash

        }else if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class SearchAdapter extends ArrayAdapter {
        Activity c;
        int text, img;
        List<Berita> data, data2, item;
        ProgressBar progressBar;

        public SearchAdapter(MainActivity mainActivity, int itemlist, int imglist, ProgressBar progressBarSearch, List<Berita> data) {
            super(mainActivity, itemlist, imglist, data);
            c = mainActivity;
            text = itemlist;
            img = imglist;
            item = data;
            this.progressBar = progressBarSearch;
            this.data = new ArrayList<Berita>();
            data2 = new ArrayList<Berita>(item);

        }

        @NonNull
            @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item, parent, false);

                    ViewHolder v = new ViewHolder(convertView);
                    v.itemlist.setText(data.get(position).getTittle());
                    Picasso.with(c).load(data.get(position).getGambar()).into(v.imglist);
                    return convertView;
                }

                static class ViewHolder {
                    @BindView(R.id.itemlist)
                    TextView itemlist;
                    @BindView(R.id.imglist)
                    ImageView imglist;

                    ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return namaFilter;
        }

        Filter namaFilter = new Filter() {
            //ini method untuk yang di insert kan sama si user di autocompat di convert ke string semua

            //- langkah search with filter:
            //1. convert ke string dulu menggunakan method convertresulttostring
            //2.hasil convert dari method convertresulttostring pindah ke method perfomfiltering
            //3.kalau udah sampai method performfilter di search menggunakan script contain/mengecek
            //4.apakah karakter huruf yang di masukkan di user ada apa enggak
            //5.kalau ada baru masuk ke method public result
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((Berita) resultValue).getTittle();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    data.clear();
                    for (Berita b : data2) {
                        if (b.getTittle().contains(charSequence.toString().toLowerCase())) {
                            data.add(b);
                        }
                    }
                    FilterResults filter = new FilterResults();
                    filter.values = data;
                    filter.count = data.size();
                    return filter;
                } else {
                    return new FilterResults();
                }

            }//content buat ngambil nilai tapi hanya 1 sedangkan values ngambil nilai yang banyak seperti Array

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Berita> filterlist = ((ArrayList<Berita>) filterResults.values);
                if (filterlist != null && filterResults.count > 0) {
                    clear();
                    notifyDataSetChanged();//untuk menyimpan perubahan
                    progressBar.setVisibility(View.GONE);
                    for (Berita mm : filterlist) {
                        add(mm);

                        notifyDataSetChanged();//merefresh ulang isi dari array sesiausecara otomatis dan supaya tidak menumpuk
                    }
                }
            }
        };
    }
}
