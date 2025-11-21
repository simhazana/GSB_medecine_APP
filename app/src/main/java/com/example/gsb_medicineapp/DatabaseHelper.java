package com.example.gsb_medicineapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context mycontext;
    private static final String DATABASE_NAME = "medicaments.db";
    private static final int DATABASE_VERSION = 2;
    private String DATABASE_PATH;
    private static DatabaseHelper sInstance;
    private static final String PREMIERE_VOIE = "Selectionnez une voie d'administration";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
        String filesDir = context.getFilesDir().getPath();
        DATABASE_PATH = filesDir.substring(0, filesDir.lastIndexOf("/")) + "/databases/";

        if (!checkdatabase()) {
            Log.d("APP", "BDD à copier");
            copydatabase();
        }
    }

    private boolean checkdatabase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copydatabase() {

        final String outFileName = DATABASE_PATH + DATABASE_NAME;

        //AssetManager assetManager = mycontext.getAssets();
        InputStream myInput;

        try {
            // Ouvre le fichier de la  bdd de 'assets' en lecture
            myInput = mycontext.getAssets().open(DATABASE_NAME);

            // dossier de destination
            File pathFile = new File(DATABASE_PATH);
            if (!pathFile.exists()) {
                if (!pathFile.mkdirs()) {
                    Toast.makeText(mycontext, "Erreur : copydatabase(), pathFile.mkdirs()", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Ouverture en écriture du fichier bdd de destination
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfert de inputfile vers outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Fermeture
            Log.d("APP", "BDD copiée");
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", "erreur copie de la base");
            Toast.makeText(mycontext, "Erreur : copydatabase()", Toast.LENGTH_SHORT).show();
        }

        // on greffe le numéro de version
        try {
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion(DATABASE_VERSION);
        } catch (SQLiteException e) {
            // bdd n'existe pas
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: Define the tables and necessary structures
        // Note: You should execute the appropriate CREATE TABLE queries here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            //Log.d("debug", "onUpgrade() : oldVersion=" + oldVersion + ",newVersion=" + newVersion);
            mycontext.deleteDatabase(DATABASE_NAME);
            copydatabase();
        }

    } // onUpgrade


    // TODO: Implement methods to interact with the database, such as fetching distinct Voies_dadministration
    // and searching for medicaments based on criteria

    public List<String> getVoieAdministration(){
        List<String> voiesAdminList = new ArrayList<>();
        //cette ligne ci dessous permet la connexion avec la base de donnée
        SQLiteDatabase db = this.getReadableDatabase(); //connexion a la bd
        Cursor cursor = db.rawQuery("SELECT DISTINCT UPPER (Voies_dadministration) FROM CIS_bdpm WHERE Voies_dadministration NOT LIKE '%;%' ORDER BY Voies_dadministration",null);
        voiesAdminList.add(PREMIERE_VOIE);
        if (cursor.moveToFirst()){
            do {
                String voieAdmin = cursor.getString(0).toString();
                voiesAdminList.add((voieAdmin));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return voiesAdminList;
    }
    //fonction permettant de lancer la recherche de medicament selon la recherche
    public List<Medicament> searchMedicaments(String denomination_du_medicament,String forme_pharmaceutique,String titulaires,String denomination_substance, String SpinnerVoieAdminvoie_admin, String SpinnerVoieAdmin){
        List<Medicament> medicamentList=new ArrayList<>();
        List<String> selectionArgs= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); //connexion a la bd
        selectionArgs.add("%" + denomination_du_medicament +"%");
        selectionArgs.add("%"+ forme_pharmaceutique +"%");
        selectionArgs.add("%"+ titulaires +"%");
        selectionArgs.add("%"+ removeAccents(denomination_substance)  +"%");
        String finSQL="";

        if (!voie_admin.equal(PREMIERE_VOIE)){
            finSQL=" AND voie_admin LIKE ?";
            selectionArgs.add(voie_admin);
        }
        String SQLSubstance = "SELECT CODE_CIS FROM CIS_COMPO_bdpm WHERE replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(upper(Denomination_substance), 'Â','A'),'Ä','A'),'À','A'),'É','E'),'Á','A'),'Ï','I'), 'Ê','E'),'È','E'),'Ô','O'),'Ü','U'), 'Ç','C' ) LIKE ?" ;

        String query = "SELECT * ,(select count(*) from CIS_COMPO_bdbm c where c.Code_CIS = m.Code_CIS) AS nb_molecule FROM CIS_bdpm a WHERE " +
                "denomination_du_medicament LIKE ? AND " +
                " forme_pharmaceutique LIKE ? AND " +
                "titulaires LIKE ? AND "  +
                "Code_CIS IN (" + SQLSubstance + ") " +
                finSQL;
        //connexion a la base de donné
        Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));
        return medicamentList;

    }

    // fonction qui enlever les accents
    private String removeAccents(String input) {
        if (input == null) {
            return null;
        }

        // Normalisation en forme de décomposition (NFD)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Remplacement des caractères diacritiques
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
