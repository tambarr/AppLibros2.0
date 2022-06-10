package com.example.applibros20;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.applibros20.ui.adapter.BookAdapter;
import com.example.applibros20.ui.entidades.Libro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText search_et_search; //Campo de búsqueda

    TextView search_button_title; //Botón de búsqueda por título
    TextView search_button_author; //Botón de búsqueda por autor

    RecyclerView recyclerViewBooks;
    ArrayList<Libro> listBooks;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    TextView texto;
    String nombre;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search_et_search = (EditText) view.findViewById(R.id.search_et_search); //Campo a buscar
        search_button_title = (Button) view.findViewById(R.id.search_button_title); //Botón buscar por título
        search_button_author = (Button) view.findViewById(R.id.search_button_author); //Botón buscar por autor
        //Nombre del usuario
        texto = (TextView) view.findViewById(R.id.tv_name_user);
        getParentFragmentManager().setFragmentResultListener("bundle", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                nombre = result.getString("nombre");
                texto.setText(nombre);

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        listBooks = new ArrayList<>();
        recyclerViewBooks = (RecyclerView) view.findViewById(R.id.rv_search);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewBooks.setHasFixedSize(true);
CargarPreferencias();
        search_button_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBooks.clear();
                
                loadWebServiceTitle();
            }


        });

        search_button_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listBooks.clear();
                loadWebServiceAuthor();
            }
        });

        return view;
    }

    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        texto.setText(user);
    }

    private void loadWebServiceTitle() {
        String usuario = texto.getText().toString();

        String url="https://mipequeniabiblioteca.000webhostapp.com/wsJSONBuscarTitulo.php?titulo=" + search_et_search.getText().toString()+ "&usuario="+usuario;
        url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, SearchFragment.this, SearchFragment.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void loadWebServiceAuthor() {
        String usuario = texto.getText().toString();

        String url="https://mipequeniabiblioteca.000webhostapp.com/wsJSONBuscarAutor.php?autor=" + search_et_search.getText().toString()+ "&usuario="+usuario;
        url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, SearchFragment.this, SearchFragment.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No hay libros disponibles con esos datos.", Toast.LENGTH_SHORT).show();
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