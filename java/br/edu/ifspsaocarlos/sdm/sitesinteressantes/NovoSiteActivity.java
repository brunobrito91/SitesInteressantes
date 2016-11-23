package br.edu.ifspsaocarlos.sdm.sitesinteressantes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import static android.app.Activity.RESULT_CANCELED;

public class NovoSiteActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_site);
    }

    public void onClick(View v) {
        EditText etEndereco = (EditText) findViewById(R.id.et_novo_site);
        CheckBox cbFavorito = (CheckBox) findViewById(R.id.cb_favorito);
        int resultado = RESULT_CANCELED;
        Intent intentRetorno = new Intent();
        if (v == findViewById(R.id.bt_ok)) {
            if (!etEndereco.getText().toString().isEmpty()) {
                resultado = RESULT_OK;
                intentRetorno.putExtra("url", etEndereco.getText().toString());
                intentRetorno.putExtra("favorito", cbFavorito.isChecked());
            }
        }
        setResult(resultado, intentRetorno);
        finish();
    }
}

