package br.edu.ifspsaocarlos.sdm.sitesinteressantes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.sitesinteressantes.model.Site;

public class ListaSitesAdapter extends ArrayAdapter<Site> {
    private LayoutInflater inflador;

    public ListaSitesAdapter(Activity tela, List<Site> listaSites) {
        super(tela, R.layout.celula_site_interessante, listaSites);
        inflador = (LayoutInflater) tela.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // infla uma nova célula
            convertView = inflador.inflate(R.layout.celula_site_interessante, null);
            holder = new ViewHolder();
            holder.url_site = (TextView) convertView.findViewById(R.id.tv_url_site);
            holder.botao_site = (ImageView) convertView.findViewById(R.id.iv_url_site);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Site site = getItem(position);
        holder.url_site.setText(site.getUrl());
        holder.botao_site.setImageResource(site.getImagemFavorito());
        holder.botao_site.setLabelFor(position);
        return convertView;
    }

    static class ViewHolder {
        public TextView url_site;
        public ImageView botao_site;
    }
}