package br.edu.ifspsaocarlos.sdm.sitesinteressantes;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.sitesinteressantes.model.Site;

public class ListaSitesActivity extends ListActivity {
    private static final int INTENT_NOVO_SITE = 0;
    private static final int INTENT_NAVEGADOR = 1;
    private List<Site> listaSites;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cria a lista inicial de sites que vai aparecer no tela
        listaSites = new ArrayList<Site>();
        listaSites.add(new Site(corrigeEndereco("http://www.ifsp.edu.br"), R.drawable.icone_favorito_on));
        listaSites.add(new Site(corrigeEndereco("http://www.ifspsaocarlos.edu.br"), R.drawable.icone_favorito_on));
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

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // Pega o item na posição da lista da adaptador e converte em um objeto do tipo Site
        Site site = (Site) getListAdapter().getItem(position);
        // Abre o navegador com a URL do objeto recuperado
        Intent intencaoNavegador = new Intent(Intent.ACTION_VIEW, Uri.parse(site.getUrl()));
        startActivityForResult(intencaoNavegador, INTENT_NAVEGADOR);
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

    private String corrigeEndereco(String endereco) {
        String url = endereco.trim().replace(" ", "");
        if (!url.startsWith("http://")) {
            return "http://" + url;
        }
        return url;
    }
}