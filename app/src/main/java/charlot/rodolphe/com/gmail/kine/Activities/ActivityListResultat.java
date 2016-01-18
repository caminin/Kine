package charlot.rodolphe.com.gmail.kine.Activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;

import charlot.rodolphe.com.gmail.kine.Adapter.PathologieAdapter;
import charlot.rodolphe.com.gmail.kine.Bdd.PathologieBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.ResultatBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.TemoinResBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PathologieInterface;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TemoinResInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;

public class ActivityListResultat extends ListActivity {
    public PathologieInterface[] tab_pathologie;
    public Hashtable<Integer,ResultatInterface[]> tab_res_par_pathologie;
    public int nombre_pathologie;
    public PathologieAdapter pathologie_adapter;
    public PatientInterface patient;
    public String localisation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_res_par_pathologie = new Hashtable<>();
        nombre_pathologie=0;
        PathologieBdd path_bdd=new PathologieBdd(getApplicationContext());
        path_bdd.open();
            try {
                int nb_path;
                nb_path = (path_bdd.getAll().length);
                tab_pathologie=new PathologieInterface[nb_path+1];
            } catch (BddException.BddNoElementException e) {
                Toast.makeText(this,"Pas de pathologie reconnue",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        path_bdd.close();

        setContentView(R.layout.list_simple);
        Intent intent = getIntent();

        if (intent != null) {
            localisation=intent.getStringExtra("articulationName");
            PatientBdd pat_bdd=new PatientBdd(this);
            pat_bdd.open();
                try {
                    patient=pat_bdd.getPatientWithId(intent.getIntExtra("idPatient",0));
                } catch (BddException.BddNoElementException e) {
                    e.printStackTrace();
                }
            pat_bdd.close();
        }
        else{
            onBackPressed();
        }

        ResultatBdd res_bdd=new ResultatBdd(this);
        res_bdd.open();
            ResultatInterface tab_resultat[];
            try {
                tab_resultat = res_bdd.getResultatWithIdPatient(patient.id_pat);
                if(tab_resultat!=null) {
                    Log.v("debug list test", "tableau de taille :" + tab_resultat.length);
                    TemoinResBdd temoin_bdd = new TemoinResBdd(this);
                    temoin_bdd.open();
                    for (int i = 0; i < tab_resultat.length; i++) {//Parcours tous les éléments des résultats
                        TemoinResInterface tab_temoin[];
                        try {
                            tab_temoin = temoin_bdd.getTemoinWithIdTest(tab_resultat[i].id_test_res);//si le résultat a un témoin
                            for (int j = 0; j < tab_temoin.length; j++) {//parcours les témoins de ce résultat
                                if (tab_temoin[j].comparaison_res_temoin(tab_resultat[i])) {//si le résultat correspond à un témoin
                                    Log.v("sendRes", "Le résultat est conforme, on recherche " + tab_temoin[j].id_list_res);
                                    TemoinResInterface tab_temoin_une_path[];
                                    try {
                                        tab_temoin_une_path = temoin_bdd.getTemoinWithListRes(tab_temoin[j].id_list_res);//on récupère la liste des autres témoins de la liste de la pathologie
                                        Log.v("listres_tabtemoin", tab_temoin_une_path[0].toString());
                                        Log.v("listres", "je regarde si les autres sont valides aussi");
                                        hasOtherRes(tab_resultat, tab_temoin_une_path);//on regarde si tous sont valides
                                    } catch (BddException.BddNoElementException e) {
                                        Toast.makeText(this,"il n'y a pas d'élément", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Log.v("sendRes", "Le résultat n'est pas conforme");
                                }
                            }
                        } catch (BddException.BddNoElementException e) {
                            Toast.makeText(this,"il n'y a pas d'élément", Toast.LENGTH_LONG).show();
                        }
                    }
                    temoin_bdd.close();
                    PathologieInterface[] tableau = new PathologieInterface[nombre_pathologie];
                    for (int i = 0; i < nombre_pathologie; i++) {
                        tableau[i] = new PathologieInterface(tab_pathologie[i].nom_pathologie, tab_pathologie[i].id_list_res_pathologie);
                        tableau[i].id_pathologie = tab_pathologie[i].id_pathologie;
                        Log.v("listres", tableau[i].toString());
                    }
                    pathologie_adapter = new PathologieAdapter(this, tableau,tab_res_par_pathologie);
                    setListAdapter(pathologie_adapter);
                    ListView list = (ListView) findViewById(android.R.id.list);
                    registerForContextMenu(list);
                }
            } catch (BddException.BddNoElementException e) {
                Toast.makeText(this,"il n'y a pas d'élément", Toast.LENGTH_LONG).show();
            }
        res_bdd.close();
    }

    //regarde si le temoin permet
    protected boolean isInsideList(ResultatInterface res){
        return false;
    }

    //ajoute toutes les pathologie dont l'élément fait partie en tant que res
    protected void hasOtherRes(ResultatInterface tab_resultat[],TemoinResInterface tab_temoin_path[]){
        boolean res=true;
        ArrayList <ResultatInterface> tab_res_temp=new ArrayList<>();
        Log.v("listres","je compare un tableau de taille "+tab_resultat.length+" avec un tableau de temoin de taille "+tab_temoin_path.length);
        for(int i=0;(i<tab_temoin_path.length)&&res;i++){//on parcours le tableau de témoin de path
            res=false;//on considère faux le fait qu'un res est égal au témoin
            for(int j=0;(j<tab_resultat.length)&&!res;j++){//sor s'il n'a rien trouvé avec res=false, sinon s'il a trouvé avec res=true
                if(tab_temoin_path[i].comparaison_res_temoin(tab_resultat[j])){
                    tab_res_temp.add(tab_resultat[j]);
                    res=true;//si un des résultats confirme finalement le temoin on sort de la boucle
                }
            }
        }

        if(res){//si on doit ajouter la pathologie
            PathologieBdd path_bdd=new PathologieBdd(getApplicationContext());
            path_bdd.open();

            try {
                tab_pathologie[nombre_pathologie]=(path_bdd.getPathologieWithListRes(tab_temoin_path[0].id_list_res));
            } catch (BddException.BddNoElementException e) {
                Log.v("listres","je recherche une liste qui n'existe pas en pathologie");
            }
            Log.v("listres", "je rajoute la pathologie " + tab_pathologie[nombre_pathologie].nom_pathologie);
                tab_res_par_pathologie.put(tab_pathologie[nombre_pathologie].id_pathologie, tab_res_temp.toArray(new ResultatInterface[tab_res_temp.size()]));
                ResultatInterface tab[] = tab_res_par_pathologie.get(tab_pathologie[nombre_pathologie].id_pathologie);
                Log.v("listres", "j'ai mis le res " + tab[0].toString());
                nombre_pathologie++;
            path_bdd.close();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_test, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.sendRes:
                createPdf();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createPdf(){
        //http://androidsrc.net/create-and-display-pdf-within-android-application/
        PdfDocument document = new PdfDocument();
        int pageNumber = 1;
        ListView lv=getListView();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(lv.getWidth(),lv.getHeight(),pageNumber).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        lv.draw(page.getCanvas());

// do final processing of the page
        document.finishPage(page);

// saving pdf document to sdcard
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.FRANCE);
        String pdfName = "pdfdemo"
                + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

// all created files will be saved at path /sdcard/PDFDemo_AndroidSRC/
        File outputFile = new File(Environment.getExternalStorageDirectory().getPath()+"/PDFDemo_AndroidSRC/", pdfName);
        Log.v("listres",Environment.getExternalStorageDirectory().getPath()+"/PDFDemo_AndroidSRC/");
        try {
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("listres",outputFile.getAbsolutePath()+"...."+outputFile.toString());
        Intent intent=new Intent(ActivityListResultat.this,ActivityShowPdf.class);
        intent.putExtra("filepath",outputFile.getAbsolutePath());
        intent.putExtra("idPatient",patient.id_pat);
        startActivity(intent);
    }

    public void backPressed(View v){
        onBackPressed();
    }

    public void onBackPressed(){
        Intent intent=new Intent(ActivityListResultat.this,ActivityListTest.class);
        intent.putExtra("idPatient",patient.id_pat);
        intent.putExtra("articulationName", localisation);
        startActivity(intent);
    }
}