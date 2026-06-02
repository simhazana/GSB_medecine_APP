package com.example.gsb_medicineapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "userPrefs";
    private static final String KEY_USER_STATUS = "userStatus";
    private EditText denomination,forme,titulaire,substance;
    private Button btnSearch;
    private DatabaseHelper dbHelper;
    private Spinner spinnerVoieAdmin;

    @Override //redefinie une methode héritée
    protected void onCreate(Bundle savedInstanceState) {//Bundle savedInstanceState :contient l’état précédent de l’activité
        super.onCreate(savedInstanceState);// appel la version parente
        setContentView(R.layout.activity_main);// fait appel au layout

         if (!isuserAuthentificated()){
             Intent authIntent = new Intent(this, Authentification.class);//redirige vers une page de connection
             startActivity(authIntent);
             finish();
         }
        denomination = findViewById(R.id.edit_text_denomination_du_medicament);//findby: chercheune vue grace au id
        forme = findViewById(R.id.edit_text_forme_pharmaceutique);
        titulaire = findViewById(R.id.edit_text_titulaires);
        substance = findViewById(R.id.edit_text_substance);
        btnSearch=findViewById(R.id.btn_rechercher);
        spinnerVoieAdmin=findViewById(R.id.voieAdminSpinner);
        dbHelper = new DatabaseHelper(this); //initialisation de databaseHelper
        setupVoieAdminSpinner();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //gere quand on clique sur un bouton
                performSearch();
                cacherClavier();
            }
        });

    }

//verifie si l'utilisateur et authentifié ou non
    private boolean isuserAuthentificated() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);//recupere nom du fichier de preference
        String userStatus = preferences.getString(KEY_USER_STATUS, "");
        return "authentification=OK".equals(userStatus);
    }

        //recuper les critere de l'utilisateur et fait appel a la bd
        private void performSearch() {
            String denomination_du_medicament = denomination.getText().toString().trim();
            String forme_pharmaceutique = forme.getText().toString().trim();
            String titulaires = titulaire.getText().toString().trim();
            String denomination_substance = substance.getText().toString().trim();
            String voie_admin = spinnerVoieAdmin.getSelectedItem().toString();
            List<Medicament> searchResults = dbHelper.searchMedicaments(denomination_du_medicament, forme_pharmaceutique, titulaires, denomination_substance, voie_admin);
            // ici faire appel à la fonction search medicament en mettant en paramètres les saisies utilisateurs récupérées.
        }

        private void cacherClavier() {
            // Obtenez le gestionnaire de fenêtre
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            // Obtenez la vue actuellement focalisée, qui devrait être la vue avec le clavier
            View vueCourante = getCurrentFocus();

            // Vérifiez si la vue est non nulle pour éviter les erreurs
            if (vueCourante != null) {
                // Masquez le clavier
                imm.hideSoftInputFromWindow(vueCourante.getWindowToken(), 0);
            }
        }

        public void setUserStatus (String status){
            SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_USER_STATUS, status);
            editor.apply();

        }
        public void deconnexion (View v){
            setUserStatus("authentification=KO");
            Intent authIntent = new Intent(this, MainActivity.class);//redirige vers main activity
            startActivity(authIntent);
            finish();
        }

        //initialise un spinner:list deroulante
        private void setupVoieAdminSpinner () {
            List<String> voieAdminList = dbHelper.getVoieAdministration();
            //arrayadapter pont entre les donnée et le spinner
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, voieAdminList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerVoieAdmin.setAdapter(spinnerAdapter);
        }



    }