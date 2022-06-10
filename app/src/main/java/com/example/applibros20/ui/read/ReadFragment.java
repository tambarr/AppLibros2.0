package com.example.applibros20.ui.read;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.applibros20.R;
import com.example.applibros20.databinding.FragmentGalleryBinding;
import com.example.applibros20.ui.adapter.BookAdapter;
import com.example.applibros20.ui.entidades.Libro;
import com.example.applibros20.ui.reading.ReadingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReadFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener  {
    RecyclerView recyclerViewBooks;
    ArrayList<Libro> listBooks;
    String nombre;
    TextView texto;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReadViewModel readViewModel =
                new ViewModelProvider(this).get(ReadViewModel.class);

        View view = inflater.inflate(R.layout.fragment_reading, container, false);

//Nombre del usuario
        texto = (TextView) view.findViewById(R.id.tv_name_user);
        listBooks = new ArrayList<>();
        recyclerViewBooks = (RecyclerView) view.findViewById(R.id.rv_reading);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerViewBooks.setHasFixedSize(true);
        requestQueue= Volley.newRequestQueue(getContext());

        CargarPreferencias();
        ReadBooks();

        return view;
    }
    private void ReadBooks() {
        String usuario = texto.getText().toString();

        String url="https://mipequeniabiblioteca.000webhostapp.com/wsJSONConsultarLeidos.php?usuario="+ usuario;
        url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, ReadFragment.this, ReadFragment.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        texto.setText(user);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No hay libros disponibles para leer.", Toast.LENGTH_SHORT).show();
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