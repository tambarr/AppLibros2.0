package com.example.applibros20.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.applibros20.R;
import com.example.applibros20.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment  {

    private FragmentHomeBinding binding;

    EditText ed_name; //Nombre del usuario
    Button bt_accept; //Botón aceptar
    Button bt_change; //Botón cambiar




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        ed_name = (EditText) root.findViewById(R.id.ed_name);
        bt_accept = (Button) root.findViewById(R.id.bt_accept);
        bt_change = (Button) root.findViewById(R.id.bt_change);
        ed_name.setEnabled(false);

        //cada vez que ingresemos a la app
        CargarPreferencias();

bt_accept.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (ed_name.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Por favor, introduce tu nombre", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Bienvenido " + ed_name.getText().toString(), Toast.LENGTH_SHORT).show();
            GuardarPreferencias();
            String nombre_usuario = ed_name.getText().toString();
            Toast.makeText(getContext(), nombre_usuario, Toast.LENGTH_SHORT).show();

            ed_name.setEnabled(false);

        }
    }
});
bt_change.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ed_name.setEnabled(true);

    }
});
        return root;
    }

    private void CargarPreferencias() {
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String user = preferences.getString("user", "Introduce tu nombre");
        ed_name.setText(user);
    }

    private void GuardarPreferencias() {
        //Creamos el objeto preferences, que es un archivo de preferencias llamado "users_names"
        SharedPreferences preferences = this.requireContext().getSharedPreferences("users_names", Context.MODE_PRIVATE);
        String usuario = ed_name.getText().toString();
        SharedPreferences.Editor editor = preferences.edit(); //Nos permite almacenar los datos
        editor.putString("user", usuario);
        ed_name.setText(usuario);
        editor.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}