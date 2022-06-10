package com.example.applibros20;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.applibros20.ui.adapter.BookAdapter;
import com.example.applibros20.ui.entidades.Libro;
import com.example.applibros20.ui.entidades.Usuario;
import com.example.applibros20.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText tv_user; //Campo de usuario
    EditText tv_pass; //Campo de password
    Button bt_login; //Botón de login
    Button bt_checkin; //Botón de registro
    TextView tv_name_user; //Nombre de usuario

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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


        View view = inflater.inflate(R.layout.fragment_account, container, false);

        tv_user = (EditText) view.findViewById(R.id.tv_user);
        tv_pass = (EditText) view.findViewById(R.id.tv_pass);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_checkin = (Button) view.findViewById(R.id.bt_checkin);
        tv_name_user = (TextView) view.findViewById(R.id.tv_name_user);



        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        bt_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkin();
            }
        });


        return view;

    }
//registrar usuario
    private void Checkin() {

        String user = tv_user.getText().toString();
        String pass = tv_pass.getText().toString();
        String url =
                "https://mipequeniabiblioteca.000webhostapp.com/insertarUsuario.php?usuario=" + user + "&password="+pass;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, AccountFragment.this, AccountFragment.this);
        requestQueue.add(jsonObjectRequest);
    }
//comprobar usuario
    private void Login() {
        String user = tv_user.getText().toString();
        String pass = tv_pass.getText().toString();
        String url =
                "https://mipequeniabiblioteca.000webhostapp.com/validar_usuario.php?usuario=" + user + "&password="+pass;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()){
                    Intent intent = new Intent(getContext(), HomeFragment.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Usuario o contraseña incorrectas", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario", user);
                parametros.put("password", pass);
                return parametros;
            }
        };

        requestQueue = Volley.newRequestQueue(getContext());




        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, AccountFragment.this, AccountFragment.this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Los datos no son correctos.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {

        Toast.makeText(getContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();

    }
}