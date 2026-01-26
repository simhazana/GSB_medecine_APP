package com.example.gsb_medicineapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;

public class Authentification extends AppCompatActivity {
    //Ici je déclare mes attributs en privé
    private EditText codeV,codeS;
    private LinearLayout layoutCle;
    String myRandomKey;

    @Override
    //constructeur de la classe
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);
        codeV = findViewById(R.id.edit_text_code_visiteur);
        codeS = findViewById(R.id.edit_text_code_securite); //ici je relie les composants de ma vue avec les attributs déclarés.
        layoutCle = findViewById(R.id.layout_cle);
        layoutCle.setVisibility(View.INVISIBLE);
    } //ici se ferme le constructeur
    public void afficherLayout(View v){
        layoutCle.setVisibility(View.VISIBLE);
        String codeVisiteur =codeV.getText().toString();
        myRandomKey = genererChaineAleatoire(5);
    }
    private String genererChaineAleatoire(int longueur){
        String caracteresPermis = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder chaineAleatoire = new StringBuilder();

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(caracteresPermis.length());
            char caractereAleatoire = caracteresPermis.charAt(index);
            chaineAleatoire.append(caractereAleatoire);
        }

        return chaineAleatoire.toString();
    }
    //ici je déclare mes méthodes
}