package br.edu.ifspsaocarlos.sdm.sitesinteressantes;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.ifspsaocarlos.sdm.sitesinteressantes.model.Site;

public class ListaSitesActivity extends ListActivity {
    private static final int INTENT_NOVO_SITE = 0;
    private static final int INTENT_NAVEGADOR = 1;
    private List<Site> listaSites;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> sitesURL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cria a lista inicial de sites que vai aparecer no tela
        listaSites = new ArrayList<Site>();
        sharedPreferences = getSharedPreferences("SitesInteressantes", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sitesURL = sharedPreferences.getStringSet("sitesURL", new HashSet<String>());
        for (String siteUrl :
                sitesURL) {
            listaSites.add(new Site(corrigeEndereco(siteUrl), sharedPreferences.getInt(siteUrl, R.drawable.icone_favorito_off)));
        }
        // Cria o adaptador que preencherá as células da tela com o conteúdo da lista
        ListAdapter adaptador = new ListaSitesAdapter(this, listaSites);
        setListAdapter(adaptador);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista_sites, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.novo_site) {
            Intent intentNovo = new Intent(this, NovoSiteActivity.class);
            startActivityForResult(intentNovo, INTENT_NOVO_SITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onClickTextView(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(((TextView) view).getText().toString()));
        startActivityForResult(intent, INTENT_NAVEGADOR);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_NOVO_SITE) {
            switch (resultCode) {
                case RESULT_OK:
                    Site novoSite = new Site();
                    novoSite.setUrl(corrigeEndereco(data.getStringExtra("url")));
                    if (data.getBooleanExtra("favorito", false)) {
                        novoSite.setImagemFavorito(R.drawable.icone_favorito_on);
                    } else {
                        novoSite.setImagemFavorito(R.drawable.icone_favorito_off);
                    }
                    listaSites.add(novoSite);
                    ListAdapter adaptador = new ListaSitesAdapter(this, listaSites);
                    setListAdapter(adaptador);
                    Toast.makeText(this, "Novo site adicionado.", Toast.LENGTH_SHORT).show();

                    sitesURL.add(novoSite.getUrl());
                    editor.putStringSet("sitesURL", sitesURL);
                    editor.putInt(novoSite.getUrl(), novoSite.getImagemFavorito());
                    editor.commit();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "Ação cancelada.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Ação inexistente.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    protected void onClickImageView(View view) {
        ImageView imageView = (ImageView) view;
        Site site = listaSites.get(imageView.getLabelFor());
        if (site.getImagemFavorito() == R.drawable.icone_favorito_on) {
            imageView.setImageResource(R.drawable.icone_favorito_off);
            site.setImagemFavorito(R.drawable.icone_favorito_off);
        } else if (site.getImagemFavorito() == R.drawable.icone_favorito_off) {
            imageView.setImageResource(R.drawable.icone_favorito_on);
            site.setImagemFavorito(R.drawable.icone_favorito_on);
        }
        editor.putInt(site.getUrl(), site.getImagemFavorito());
        editor.commit();
        ((ArrayAdapter) getListView().getAdapter()).notifyDataSetChanged();
    }

    private String corrigeEndereco(String endereco) {
        String url = endereco.trim().replace(" ", "");
        if (!url.startsWith("http://")) {
            return "http://" + url;
        }
        return url;
    }
}