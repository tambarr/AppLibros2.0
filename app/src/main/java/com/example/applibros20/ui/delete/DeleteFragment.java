package com.example.applibros20.ui.delete;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.applibros20.R;
import com.example.applibros20.SearchFragment;
import com.example.applibros20.ui.adapter.BookAdapter;
import com.example.applibros20.ui.entidades.Libro;
import com.example.applibros20.ui.read.ReadViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.applibros20.ui.wish.WishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    private DeleteFragment toolsViewModel;


    TextView delete_tw_id; //ID
    EditText delete_et_search; //Busqueda
    EditText delete_et_title; //Titulo
    EditText delete_et_author; //Autor
    EditText delete_et_description; //Descripción
    RadioGroup delete_rg; //RadioGroup
    RadioButton delete_rb_read; //RadioButton Leído
    RadioButton delete_rb_reading; //RadioButton Leyendo
    RadioButton delete_rb_to_read; //RadioButton Para Leer
    RadioButton delete_rb_wish; //RadioButton Deseados
    Button delete_bt_search; //Botón Buscar
    Button delete_bt_update; //RadioButton Guardar
    Button delete_bt_delete; //RadioButton Borrar
    View aux; //Auxiliar para declarar
    String status_book =""; //Estado del libro
    String nombre; //Nombre del usuario
    TextView texto;

    //Objetos de tipo request que nos permiten establecer la conexión con el webservice
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    ArrayList<Libro> listBooks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DeleteViewModel deleteViewModel =
                new ViewModelProvider(this).get(DeleteViewModel.class);

        View aux = inflater.inflate(R.layout.fragment_delete, container, false);


        // EditText ID
        delete_tw_id = aux.findViewById(R.id.delete_tw_id);
        // EditText Búsqueda
        delete_et_search = aux.findViewById(R.id.delete_et_search);
        // EditText Titulo
        delete_et_title = aux.findViewById(R.id.delete_et_title);
        // EditText Autor
        delete_et_author = aux.findViewById(R.id.delete_et_author);
        // EditText Descripción
        delete_et_description = aux.findViewById(R.id.delete_et_description);
        // RadioGroup
        delete_rg = aux.findViewById(R.id.delete_rg);
        // RadioButton Leído
        delete_rb_read = aux.findViewById(R.id.delete_rb_read);
        // RadioButton Leyendo
        delete_rb_reading = aux.findViewById(R.id.delete_rb_reading);
        // RadioButton Para Leer
        delete_rb_to_read = aux.findViewById(R.id.delete_rb_to_read);
        // RadioButton Deseados
        delete_rb_wish = aux.findViewById(R.id.delete_rb_wish);
        //Botón buscar
        delete_bt_search = aux.findViewById(R.id.delete_imageButton_search);
        //Botón actualizar
        delete_bt_update = aux.findViewById(R.id.delete_bt_update);
        //Botón borrar
        delete_bt_delete = aux.findViewById(R.id.delete_bt_delete);
        //Nombre del usuario
        texto = (TextView) aux.findViewById(R.id.tv_name_user);


        requestQueue = Volley.newRequestQueue(getContext());
CargarPreferencias();


        delete_bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Buscando...", Toast.LENGTH_SHORT).show();
                delete_tw_id.setText("");
                delete_et_title.setText("");
                delete_et_author.setText("");
                delete_et_description.setText("");
                delete_rg.clearCheck();
                searchBook();
            }

            private void searchBook() {
                String usuario = texto.getText().toString();

                String url="https://mipequeniabiblioteca.000webhostapp.com/wsJSONBuscar.php?id=" + delete_et_search.getText().toString() + "&usuario="+usuario;
                    url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, DeleteFragment.this, DeleteFragment.this);
                requestQueue.add(jsonObjectRequest);

            }
        });


        delete_bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (delete_et_search.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "No hay nada que actualizar.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Update();
                }
            }


        });

        delete_bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete_et_search.getText().toString().isEmpty() || delete_et_title.getText().toString().isEmpty() ||
                delete_et_author.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Por favor, selecciona un libro.", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("¿Confirmas que deseas borrar este libro?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Delete();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create().show();
                }
            }
        });


        return aux;

    }
    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        texto.setText(user);
    }
    private void Delete() {
        String url = "https://mipequeniabiblioteca.000webhostapp.com/wsJSONEliminar.php?id=" + delete_et_search.getText();
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("elimina")) {
                    delete_tw_id.setText("");
                    delete_et_search.setText("");
                    delete_et_title.setText("");
                    delete_et_author.setText("");
                    delete_et_description.setText("");
                    delete_rg.clearCheck();
                    Toast.makeText(getContext(), "Se eliminó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    if (response.trim().equalsIgnoreCase("noExiste")) {
                        Toast.makeText(getContext(), "No se encuentra el registro", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No se ha podido eliminar.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar.", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);
    }
    private void Update() {
        String url = "https://mipequeniabiblioteca.000webhostapp.com/wsJSONActualizar.php";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Se actualizó correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError{
                String id = delete_tw_id.getText().toString();
                String title = delete_et_title.getText().toString();
                String author = delete_et_author.getText().toString();
                String description = delete_et_description.getText().toString();
                int idDeRadioButtonSeleccionado = delete_rg.getCheckedRadioButtonId();
                if (idDeRadioButtonSeleccionado == delete_rb_wish.getId()) {
                    status_book = "Deseado";
                } else if (idDeRadioButtonSeleccionado == delete_rb_read.getId()) {
                    status_book = "Leido";
                } else if (idDeRadioButtonSeleccionado == delete_rb_reading.getId()) {
                    status_book = "Leyendo";
                } else if (idDeRadioButtonSeleccionado == delete_rb_to_read.getId()) {
                    status_book = "Para leer";
                }
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id", id);
                parametros.put("titulo", title);
                parametros.put("autor", author);
                parametros.put("descripcion", description);
                parametros.put("estado", status_book);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No hay libros disponibles con esos datos.", Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getContext(), "Libro encontrado.", Toast.LENGTH_SHORT).show();
        Libro book = new Libro();
        JSONArray jsonArray = response.optJSONArray("libro");
        JSONObject jsonObject = null;
        try{
            jsonObject=jsonArray.getJSONObject(0);
            book.setId(jsonObject.optInt("id"));
            book.setTitle(jsonObject.optString("titulo"));
            book.setAuthor(jsonObject.optString("autor"));
            book.setDescription(jsonObject.optString("descripcion"));
            book.setStatus(jsonObject.optString("estado"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se pudo establecer conexión con el servidor.", Toast.LENGTH_SHORT).show();
        }
        delete_tw_id.setText(String.valueOf(book.getId()));
        delete_et_title.setText(book.getTitle());
        delete_et_author.setText(book.getAuthor());
        delete_et_description.setText(book.getDescription());
        if (book.getStatus().equals("Leido")){
           delete_rb_read.setChecked(true);
        } else if (book.getStatus().equals("Leyendo")){
            delete_rb_reading.setChecked(true);
        } else if (book.getStatus().equals("Deseado")){
            delete_rb_wish.setChecked(true);
        } else if (book.getStatus().equals("Para leer")){
            delete_rb_to_read.setChecked(true);
        }
    }
}