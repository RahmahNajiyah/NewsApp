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
public class DetikFragment extends Fragment {


    @BindView(R.id.listviva)
    ListView listviva;
    private ArrayList<Berita> data;

    public DetikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viva, container, false);
        ButterKnife.bind(this, view);
        data = new ArrayList<>();
        Toast.makeText(getActivity(),"update dunia",Toast.LENGTH_LONG).show();
        getData();
        return view;
    }

    private void getData() {
        String url = Config.BASE_URL + "Update_Dunia";
data.clear();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..harap sabar..");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                Log.d("data detik fragment", response.toString());
                try {
                    JSONObject jarray = new JSONObject(response);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject a = jarray.getJSONObject(String.valueOf(i));
                        Berita b = new Berita();
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue req = Volley.newRequestQueue(getActivity());
        req.add(request);
    }

    private void PindahKeGridView(final ArrayList<Berita> data) {
        CustomAdapter3 adapter = new CustomAdapter3 (getActivity(), data);
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



