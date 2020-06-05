package com.derevo.noty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EstimadorRapidoActivity extends AppCompatActivity implements dialogo_add.ListenerDialogo {

    private TextView[] materia = new TextView[10];
    private Spinner[] nota = new Spinner[10];
    private EditText indiceActual, creditosAcum;
    private TextView indicePeriodo, indiceAcum;
    private int materias = 0;
    private int[] creditosParcial = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimador_rapido);

        indiceActual = findViewById(R.id.IndiceActual_num);
        creditosAcum = findViewById(R.id.CreditosCursados_num);
        indicePeriodo = findViewById(R.id.IndicePeriodo_num);
        indiceAcum = findViewById(R.id.IndiceAcum_num);

        materia[0] = (TextView)findViewById(R.id.materia1);
        materia[1] = (TextView)findViewById(R.id.materia2);
        materia[2] = (TextView)findViewById(R.id.materia3);
        materia[3] = (TextView)findViewById(R.id.materia4);
        materia[4] = (TextView)findViewById(R.id.materia5);
        materia[5] = (TextView)findViewById(R.id.materia6);
        materia[6] = (TextView)findViewById(R.id.materia7);
        materia[7] = (TextView)findViewById(R.id.materia8);
        materia[8] = (TextView)findViewById(R.id.materia9);
        materia[9] = (TextView)findViewById(R.id.materia10);

        nota[0] = findViewById(R.id.spinner1);
        nota[1] = findViewById(R.id.spinner2);
        nota[2] = findViewById(R.id.spinner3);
        nota[3] = findViewById(R.id.spinner4);
        nota[4] = findViewById(R.id.spinner5);
        nota[5] = findViewById(R.id.spinner6);
        nota[6] = findViewById(R.id.spinner7);
        nota[7] = findViewById(R.id.spinner8);
        nota[8] = findViewById(R.id.spinner9);
        nota[9] = findViewById(R.id.spinner10);

        String[] opciones = {"A", "B", "C", "D","F"};
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        for(int i = 0; i < 10; i++){
            nota[i].setAdapter(adapter);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_est, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.add_est){
            if(materias < 10){
                    dialogoAdd();
            }else{
                Toast.makeText(this, "No puede añadir más materias", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if(id==R.id.reload_est){
            resetCampos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetCampos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea resetear datos?");
        builder.setTitle("Resetear datos");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < 10; i++){
                    materia[i].setVisibility(View.INVISIBLE);
                    nota[i].setVisibility(View.INVISIBLE);
                    nota[i].setSelection(0);
                    creditosParcial[i] = 0;
                }
                materias = 0;
                indiceActual.setText("");
                creditosAcum.setText("");
                indicePeriodo.setText("");
                indiceAcum.setText("");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void calcularIndice(View view){
        String indiceAct_s = indiceActual.getText().toString();
        String creditosAcu_s = creditosAcum.getText().toString();
        if (!indiceAct_s.isEmpty() && !creditosAcu_s.isEmpty() && materias > 0) {
            float indiceAct = Float.valueOf(indiceAct_s);
            int creditosAcu = Integer.parseInt(creditosAcu_s);
            float indicePar = 0;
            float indiceTot = 0;
            float puntosPar = 0;
            float puntosTot = 0;
            int creditosPar = 0;
            int creditosTot = 0;

            for(int i = 0; i < materias; i++){
                switch (nota[i].getSelectedItem().toString()){
                    case "A":
                        puntosPar = puntosPar + creditosParcial[i] * 4;
                        break;
                    case "B":
                        puntosPar = puntosPar + creditosParcial[i] * 3;
                        break;
                    case "C":
                        puntosPar = puntosPar + creditosParcial[i] * 2;
                        break;
                    case "D":
                        puntosPar = puntosPar + creditosParcial[i] * 1;
                        break;
                    default:
                        puntosPar = puntosPar + 0;
                        break;
                }

                creditosPar = creditosPar + creditosParcial[i];
            }

            creditosTot = creditosPar + creditosAcu;
            puntosTot = (creditosAcu * indiceAct) + puntosPar;

            indicePar = puntosPar/creditosPar;
            indiceTot = puntosTot/creditosTot;

            if (indicePar <= 4 && indiceTot <= 4) {
                indicePeriodo.setText(String.format("%.1f",indicePar));
                indiceAcum.setText(String.format("%.1f",indiceTot));
            } else {
                indicePeriodo.setText("ERROR");
                indiceAcum.setText("ERROR");
            }
        } else {
            Toast.makeText(this, "Datos incompletos", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogoAdd(){
        dialogo_add dialogo =new dialogo_add();
        dialogo.show(getSupportFragmentManager(), "Añadir materia");
    }

    @Override
    public void annadir(String mat, String cred) {
        if (!mat.isEmpty() && !cred.isEmpty()) {
            materia[materias].setText(mat);
            creditosParcial[materias] = Integer.parseInt(cred);
            materia[materias].setVisibility(View.VISIBLE);
            nota[materias].setVisibility(View.VISIBLE);
            materias++;
        }else{
            Toast.makeText(this, "Datos incompletos", Toast.LENGTH_SHORT).show();
        }
    }
}
