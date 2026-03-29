package dam.pmdm.fichaprogramador;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity_V2 extends AppCompatActivity {

    // Listas de recursos (NO static)
    private String[] listaDeCursos;
    private String[] listaDeLenguajes;

    private TextView txtNombre, txtCurso, txtLenguajes;

    // Variables de estado (NO static)
    private String nombre = "";
    private int cursoSeleccionado = -1;
    private boolean[] lenguajesSeleccionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initComponents();

        // Si venimos de un giro de pantalla, recuperamos los datos
        if (savedInstanceState != null) {
            nombre = savedInstanceState.getString("nombre", "");
            cursoSeleccionado = savedInstanceState.getInt("curso", -1);
            lenguajesSeleccionados = savedInstanceState.getBooleanArray("lenguajes");
        }

        mostrarDatosEnPantalla();
    }

    // Guardamos los datos justo antes de que la Activity se destruya por un giro de pantalla.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nombre", nombre);
        outState.putInt("curso", cursoSeleccionado);
        outState.putBooleanArray("lenguajes", lenguajesSeleccionados);
    }

    private void initComponents() {
        txtNombre = findViewById(R.id.txtNombre);
        txtCurso = findViewById(R.id.txtCurso);
        txtLenguajes = findViewById(R.id.txtLenguajes);

        // Cargamos los arrays de strings solo si no han sido cargados antes
        if (listaDeCursos == null) listaDeCursos = getResources().getStringArray(R.array.cursos);
        if (listaDeLenguajes == null) listaDeLenguajes = getResources().getStringArray(R.array.lenguajes);

        // Inicializamos el array de booleanos solo si es nulo (primera vez)
        if (lenguajesSeleccionados == null) {
            lenguajesSeleccionados = new boolean[listaDeLenguajes.length];
        }
    }

    // Lee las variables de estado y las vuelca en los TextView.
    private void mostrarDatosEnPantalla() {
        // Nombre
        txtNombre.setText(nombre);

        // Curso
        if (cursoSeleccionado != -1) {
            txtCurso.setText(listaDeCursos[cursoSeleccionado]);
        } else {
            txtCurso.setText("");
        }

        // Lenguajes (Usamos StringBuilder por eficiencia)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listaDeLenguajes.length; i++) {
            if (lenguajesSeleccionados[i]) {
                sb.append(listaDeLenguajes[i]).append("\n");
            }
        }
        txtLenguajes.setText(sb.toString().trim());
    }

    // Reset completo de variables y pantalla.
    public void btnNuevo(View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.ad_nuevo_tit)
                .setCancelable(false)
                .setMessage(R.string.ad_nuevo_msg)
                .setNegativeButton(R.string.ad_nuevo_no, null)
                .setPositiveButton(R.string.ad_nuevo_si, (dialog, which) -> {
                    // Limpiamos las variables internas
                    nombre = "";
                    cursoSeleccionado = -1;
                    Arrays.fill(lenguajesSeleccionados, false);

                    // Actualizamos UI
                    mostrarDatosEnPantalla();
                })
                .show();
    }

    public void btnNombre(View v) {

        // Cargamos el nombre actual en el EditText del diálogo
        final View pedir_nombre = getLayoutInflater().inflate(R.layout.pedir_nombre, null);
        EditText respuestaNombre = pedir_nombre.findViewById(R.id.respuestaNombre);
        respuestaNombre.setText(nombre);

        new AlertDialog.Builder(this)
                .setTitle(R.string.ad_nombre_tit)
                .setView(pedir_nombre)
                .setNegativeButton(R.string.ad_nombre_no, null)
                .setPositiveButton(R.string.ad_nombre_si, (dialog, which) -> {
                    nombre = respuestaNombre.getText().toString().trim();
                    mostrarDatosEnPantalla(); // Actualizamos UI
                })
                .show();
    }

    public void btnCurso(View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.ad_curso_tit)
                .setNegativeButton(R.string.ad_curso_no, null)
                .setSingleChoiceItems(listaDeCursos, cursoSeleccionado, (dialog, which) -> {
                    cursoSeleccionado = which;
                })
                .setPositiveButton(R.string.ad_curso_si, (dialog, which) -> {
                    mostrarDatosEnPantalla(); // Actualizamos UI
                })
                .show();
    }

    public void btnLenguages(View v) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.ad_lenguajes_tit)
                .setNegativeButton(R.string.ad_lenguajes_no, null)
                .setMultiChoiceItems(listaDeLenguajes, lenguajesSeleccionados, (dialog, which, isChecked) -> {
                    lenguajesSeleccionados[which] = isChecked;
                })
                .setPositiveButton(R.string.ad_lenguajes_si, (dialog, which) -> {
                    mostrarDatosEnPantalla(); // Actualizamos UI
                })
                .setNeutralButton(R.string.ad_lenguajes_neutro, (dialog, which) -> {
                    Arrays.fill(lenguajesSeleccionados, false);
                    mostrarDatosEnPantalla(); // Actualizamos UI
                })
                .show();
    }
}