package com.tiara.newsapp.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tiara.newsapp.Berita;
import com.tiara.newsapp.Config;
import com.tiara.newsapp.Detail_Berita;
import com.tiara.newsapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    @BindView(R.id.listviva)
    ListView listviva;
    ArrayList<Berita> data5;
    {
        // Required empty public constructor
    }
    public NewsFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viva, container, false);
        ButterKnife.bind(this, view);
        data5 = new ArrayList<>();
        getData();
        return view;
    }
    private void getData() {
        String url = Config.BASE_URL + "Update_Bola";

        final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                        JSONObject a = jarray.getJSONObject(String.valueOf(i));
                        Berita b = new Berita();
                        b.setId(a.getString("id"));
                        b.setTittle(a.getString("judul"));
                        b.setGambar(a.getString("gambar"));
                        b.setLink(a.getString("link"));

                        data5.add(b);
                        PindahKeGridView(data5);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue req = Volley.newRequestQueue(getActivity());
        req.add(request);
    }
    private void PindahKeGridView(final ArrayList<Berita> data) {
        CustomAdapter2 adapter = new CustomAdapter2(getActivity(), data);
        listviva.setAdapter(adapter);
        listviva.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Detail_Berita.class);
                intent.putExtra("link", data.get(i).getLink());

                startActivity(intent);

            }
        });
    }

}
