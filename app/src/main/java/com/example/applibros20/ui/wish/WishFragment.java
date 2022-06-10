package com.example.applibros20.ui.wish;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.applibros20.R;
import com.example.applibros20.ui.adapter.BookAdapter;
import com.example.applibros20.ui.entidades.Libro;
import com.example.applibros20.ui.reading.ReadingFragment;
import com.example.applibros20.ui.reading.ReadingViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    RecyclerView recyclerViewBooks;
    ArrayList<Libro> listBooks;
    String nombre;
    TextView texto;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishFragment newInstance(String param1, String param2) {
        WishFragment fragment = new WishFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WishViewModel readingViewModel =
                new ViewModelProvider(this).get(WishViewModel.class);

        View view = inflater.inflate(R.layout.fragment_reading, container, false);


        listBooks = new ArrayList<>();
        recyclerViewBooks = (RecyclerView) view.findViewById(R.id.rv_reading);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewBooks.setHasFixedSize(true);
        //Nombre del usuario
        texto = (TextView) view.findViewById(R.id.tv_name_user);
        requestQueue= Volley.newRequestQueue(getContext());
        CargarPreferencias();
        WishBooks();

        return view;
    }
    private void WishBooks() {
        String usuario = texto.getText().toString();

        String url="https://mipequeniabiblioteca.000webhostapp.com/wsJSONConsultarDeseados.php?usuario="+ usuario;
        url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, WishFragment.this, WishFragment.this);
        requestQueue.add(jsonObjectRequest);
    }
    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        texto.setText(user);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No hay libros deseados.", Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Libro book = null;
        JSONArray jsonArray = response.optJSONArray("libro");
        try {
            for (int i =0; i<jsonArray.length(); i++){
                book= new Libro();
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(i);
                book.setId(jsonObject.optInt("id"));
                book.setTitle(jsonObject.optString("titulo"));
                book.setAuthor(jsonObject.optString("autor"));
                book.setDescription(jsonObject.optString("descripcion"));
                book.setStatus(jsonObject.optString("estado"));
                listBooks.add(book);

            }
            BookAdapter adapter = new BookAdapter(listBooks);
            recyclerViewBooks.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se pudo establecer conexión con el servidor.", Toast.LENGTH_SHORT).show();
        }
    }
}