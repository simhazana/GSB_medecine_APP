 package com.example.gsb_medicineapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

 public class MainActivity extends AppCompatActivity {
     //instance pour faire appel a d'autre fichier  java
    private static final String PREF_NAME = "userPrefs";
    private static final String KEY_USER_STATUS = "userStatus";
    private EditText denomination,forme,titulaire,substance;
    private Button btnSearch;
    private DatabaseHelper dbHelper;
    private Spinner spinnerVoieAdmin;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
//         if (!isuserAuthentificated()){
//             Intent authIntent = new Intent(this, Authentification.class);
//             startActivity(authIntent);
//             finish();
//
//         }
         denomination = findViewById(R.id.edit_text_denomination_du_medicament);
         forme = findViewById(R.id.edit_text_forme_pharmaceutique);
         titulaire = findViewById(R.id.edit_text_titulaires);
         substance = findViewById(R.id.edit_text_substance);
         btnSearch=findViewById(R.id.btn_rechercher);
         spinnerVoieAdmin=findViewById(R.id.voieAdminSpinner);
         dbHelper = new DatabaseHelper(this);//initialisation de
         setupVoieAdminSpinner();


         btnSearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 performSearch();
                 cacherClavier();
             }
         });

     }


     private boolean isuserAuthentificated() {
         SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
         String userStatus = preferences.getString(KEY_USER_STATUS,"");
         return "authentification=OK".equals(userStatus);
     }

     private void performSearch() {
         String denomination_du_medicament = denomination.getText().toString().trim();
         String forme_pharmaceutique = forme.getText().toString().trim();
         String titulaires = titulaire.getText().toString().trim();
         String denomination_substance = substance.getText().toString().trim();
         Object SpinnerVoieAdmin;
         String voie_admin=SpinnerVoieAdmin.getselctitem().toString();
         // ici faire appel à la fonction search medicament en mettant en paramètres les saisies utilisateurs récupérées.
         List<string> searchResult=dbHelper.searchMedicaments[denomination_du_medicament,forme_pharmaceutique,titulaires,denomination_substance,voie_admin];
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

     private void setupVoieAdminSpinner(){
         List<String> voieAdminList = dbHelper.getVoieAdministration();
         ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(context his,android.R.layout.simple_spinner_item, voieAdminListdmin);
         spinnerAdapter.setDraopDownViewRessource(android.R.layout.simple_spinner_dropdown_item);
         spinnerVoieAdmin.setAdapter(spinnerAdapter);
     }

 }
