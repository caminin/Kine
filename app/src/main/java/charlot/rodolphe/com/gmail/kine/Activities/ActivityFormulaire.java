package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;

public class ActivityFormulaire extends Activity {

    private String action;
    private ElementInterface element;
    private BddInterface mybdd;
    private ArrayList<Integer> id_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_list=new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            action = intent.getStringExtra("action");
            String bdd = intent.getStringExtra("bdd");
            element = (ElementInterface) intent.getSerializableExtra("element");
            BddClass bdd_class = new BddClass();
            mybdd = bdd_class.getBdd(bdd);
            setContentView(mybdd.getXml());

            if (element != null) {//si on avait pas d'élément on prend le premier de la base, mais on ne remplit pas
                mybdd.setFieldByElement(this, element);
            }
            else{
                element=mybdd.getNewElement();
            }
        }
    }

    public void AjouterListe(View v){
        Button btn = (Button) v;
        int numliste = Integer.parseInt((String) btn.getTag());
        try {
            id_list.set(numliste,mybdd.addForeignList(element, numliste));
            Log.v("Formulaire", ""+id_list.get(numliste));
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }
    }

    public void ContenuListe(View v){
        Button btn = (Button) v;
        int numliste = Integer.parseInt((String) btn.getTag());
        try {
            id_list.set(numliste,mybdd.getForeignList(element, numliste));
            Log.v("Formulaire", ""+id_list.get(numliste));
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }
    }

    public void SupprimerListe(View v){
        Button btn = (Button) v;
        int numliste = Integer.parseInt((String) btn.getTag());
        id_list.set(numliste,-1);
    }

    public void envoi(View v){
        mybdd.open();
        try {
            mybdd.getByContext(this);
        } catch (BddException.BddInsertException e) {
            e.printStackTrace();
        }
        mybdd.close();
    }

    public void retour(View v){
        onBackPressed();
    }

}
