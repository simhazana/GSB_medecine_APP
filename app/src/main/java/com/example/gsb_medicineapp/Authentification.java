package com.example.gsb_medicineapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class Authentification extends AppCompatActivity {
    //Ici je déclare mes attributs en privé
    private EditText codeV,codeS;
    private LinearLayout layoutCle;
    String myRandomKey;
    private static final String PREF_NAME = "userPrefs"; //fichier stocker en local qui donne le statut si qlq1 connecte
    private static final String KEY_USER_STATUS = "userStatus"; // statut du visiteur
    private static final String SECURETOKEN = "Euroforma"; // token qui permt la conncetion securise

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
        myRandomKey = genererChaineAleatoire(5);
        //Log.d("APPLI","myKey="+myRandomKey);
        String codeVisiteur =codeV.getText().toString();
        SendKeyTask sendKeyTask=new SendKeyTask(getApplicationContext());
        sendKeyTask.execute(codeVisiteur,myRandomKey,SECURETOKEN);
    }
    private void afficherMessage(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    public void comparerChaine(View v){
        String codeSecurite =codeS.getText().toString().trim();
        if (myRandomKey.equals(codeSecurite)){
            afficherMessage("Connexion validé");
            setUserStatus("authentification=OK");
            Intent authIntent = new Intent(this, MainActivity.class);//redirige vers main activity
            startActivity(authIntent);
            finish();

        }else{
            afficherMessage("La clé de sécurité saisie incorrect");
            setUserStatus("authentification=KO");
        }

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

    public void setUserStatus(String status){
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(KEY_USER_STATUS,status);
        editor.apply();
    }
    //ici je déclare mes méthodes
}