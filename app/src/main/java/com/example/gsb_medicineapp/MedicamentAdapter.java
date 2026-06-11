package com.example.gsb_medicineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MedicamentAdapter extends ArrayAdapter<Medicament> {
    public MedicamentAdapter(Context context, List<Medicament> medicaments) {
        super(context,0,medicaments);//super=appel methode a une classe externe
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Medicament medicament = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_medicament, parent, false);
        }
        TextView tvCodeCIS=convertView.findViewById(R.id.code_cis); // tv c'est texte
        TextView tvDenomination=convertView.findViewById(R.id.denomination);
        TextView tvFormePharmacetique=convertView.findViewById(R.id.forme);
        TextView tvVoieAdministration=convertView.findViewById(R.id.voie_admin);
        TextView tvTitulaire=convertView.findViewById(R.id.titulaire);
        TextView tvStaturAdmin=convertView.findViewById(R.id.stat_admin);

        tvCodeCIS.setText(String.valueOf(medicament.getCodeCIS()));
        tvDenomination.setText(String.valueOf(medicament.getDenomination()));
        tvFormePharmacetique.setText(String.valueOf(medicament.getFormePharmaceutique()));
        tvVoieAdministration.setText(String.valueOf(medicament.getVoiesAdmin()));
        tvTitulaire.setText(String.valueOf(medicament.getTitulaires()));
        tvStaturAdmin.setText(String.valueOf(medicament.getStatutAdministratif()));


        return convertView;
    }
    static String pluriels(int nbr, String mot){
        String un_s="";
        if (nbr>1){
            un_s="s";
        }
        return (mot+un_s);
    }
}
