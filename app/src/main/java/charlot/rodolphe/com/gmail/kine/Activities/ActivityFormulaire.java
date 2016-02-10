package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.NewBddInterface;

public class ActivityFormulaire extends Activity {

    private String action;
    private ElementInterface element;
    private NewBddInterface mybdd;
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
                mybdd.setFieldByElement(this, element,id_list);
            }
            else{
                element=mybdd.getNewElement();
            }
        }
    }

    public void AjouterListe(View v){
        Button btn = (Button) v;
        int numliste = Integer.parseInt((String) btn.getTag());
        Intent intent=new Intent(ActivityFormulaire.this,ActivityListSimple.class);
        startActivity(intent);
    }

    public void ContenuListe(View v){
        Button btn = (Button) v;
        int numliste = Integer.parseInt((String) btn.getTag());
        try {
            id_list.set(numliste, mybdd.getForeignList(element, numliste));
            Log.v("Formulaire", ""+id_list.get(numliste));
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }
    }

    public void SupprimerListe(View v){
        Button btn = (Button) v;
        final int numliste = Integer.parseInt((String) btn.getTag());
        final Activity act=this;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityFormulaire.this);
        builder1.setMessage("Voulez-vous supprimez la liste ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mybdd.open();
                            try {
                                mybdd.deleteListWithNumAndId(numliste, element.getId());
                            } catch (BddException.BddNoElementException e) {
                                e.printStackTrace();
                            }
                        mybdd.close();
                        id_list.set(numliste, -1);
                        mybdd.switchButtonVisibility(act, 0, View.INVISIBLE, View.VISIBLE);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=0){
            id_list.set(resultCode,data.getIntExtra("idList",-1));
        }
        super.onActivityResult(requestCode, resultCode, data);
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
