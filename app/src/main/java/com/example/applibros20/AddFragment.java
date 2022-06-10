package com.example.applibros20;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText add_et_title; //Titulo
    EditText add_et_author; //Autor
    EditText add_et_description; //Descripción
    RadioGroup add_rg; //RadioGroup
    RadioButton add_rb_read; //RadioButton Leído
    RadioButton add_rb_reading; //RadioButton Leyendo
    RadioButton add_rb_to_read; //RadioButton Para Leer
    RadioButton add_rb_wish; //RadioButton Deseados
    Button add_bt_save; //RadioButton Guardar
    Button add_bt_delete; //RadioButton Borrar
    View aux; //Auxiliar para declarar
    String status_book =""; //Estado del libro
    String nombre;
    TextView texto;

    //Objetos de tipo request que nos permiten establecer la conexión con el webservice
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        aux = inflater.inflate(R.layout.fragment_add, container, false);

        // EditText Título
        add_et_title = aux.findViewById(R.id.add_et_title);
        // EditText Autor
        add_et_author = aux.findViewById(R.id.add_et_author);
        // EditText Descripción
        add_et_description = aux.findViewById(R.id.add_et_description);
        // RadioGroup
        add_rg = aux.findViewById(R.id.add_rg);
        // RadioButton Leído
        add_rb_read = aux.findViewById(R.id.add_rb_read);
        // RadioButton Leyendo
        add_rb_reading = aux.findViewById(R.id.add_rb_reading);
        // RadioButton Para Leer
        add_rb_to_read = aux.findViewById(R.id.add_rb_to_read);
        // RadioButton Deseados
        add_rb_wish = aux.findViewById(R.id.add_rb_wish);
        //Nombre del usuario
        texto = (TextView) aux.findViewById(R.id.tv_name_user);

CargarPreferencias();
        request = Volley.newRequestQueue(getContext());


        //Cambiar el estado del libro para guardar
        add_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int idDeRadioButtonSeleccionado = add_rg.getCheckedRadioButtonId();
                if (idDeRadioButtonSeleccionado == add_rb_wish.getId()) {
                    status_book = "Deseado";
                } else if (idDeRadioButtonSeleccionado == add_rb_read.getId()) {
                    status_book = "Leido";
                } else if (idDeRadioButtonSeleccionado == add_rb_reading.getId()) {
                    status_book = "Leyendo";
                } else if (idDeRadioButtonSeleccionado == add_rb_to_read.getId()) {
                    status_book = "Para leer";
                }
            }
        });


        //Botón GUARDAR
        add_bt_save = aux.findViewById(R.id.add_bt_save);
        add_bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_et_title.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Falta el título", Toast.LENGTH_SHORT).show();
                }
                else if (add_et_author.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Falta el autor", Toast.LENGTH_SHORT).show();
                }else if (status_book == "") {
                    Toast.makeText(getContext(), "¿Dónde quieres guardar tu libro?", Toast.LENGTH_SHORT).show();
                } else {
                    saveBook();

                }
            }

            private void saveBook() {

                String title = add_et_title.getText().toString();
                String author = add_et_author.getText().toString();
                String description = add_et_description.getText().toString();
                String state = status_book;
                String usuario = texto.getText().toString();

                String url = "https://mipequeniabiblioteca.000webhostapp.com/wsJSONRegistro.php?titulo="+title+
                        "&autor="+ author + "&descripcion=" + description + "&estado=" + state + "&usuario=" + usuario;



                url = url.replace(" " , "%20"); //reemplazamos los espacios para no tener errores

                //mandamos la URL para que la lea
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, AddFragment.this, AddFragment.this);
                request.add(jsonObjectRequest);//nos permite establecer la comunicación con los dos métodos: onErrorResponse y onResponse
            }


        });

        //Botón BORRAR
        add_bt_delete = aux.findViewById(R.id.add_bt_delete);
        add_bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_et_title.setText("");
                add_et_author.setText("");
                add_et_description.setText("");
                add_rg.clearCheck();
            }
        });

        return aux;
    }
    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        texto.setText(user);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        //Por si reporta algún error

        Toast.makeText(getContext(), "No se pudo registrar " + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        //Si todo sale bien
        add_et_title.setText("");
        add_et_author.setText("");
        add_et_description.setText("");
        add_rg.clearCheck();
        status_book = "";
        Toast.makeText(getContext(), "Libro registrado", Toast.LENGTH_SHORT).show();

    }
}