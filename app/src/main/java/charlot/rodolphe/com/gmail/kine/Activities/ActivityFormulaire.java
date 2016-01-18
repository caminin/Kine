package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;

public class ActivityFormulaire extends Activity {

    private String action;
    private String bdd;
    private String myactivity;
    private ElementInterface element;
    private BddInterface mybdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            action = intent.getStringExtra("action");
            bdd = intent.getStringExtra("bdd");
            myactivity = intent.getStringExtra("activity");
            element = (ElementInterface) intent.getSerializableExtra("element");
            BddClass bdd_class = new BddClass();
            mybdd = bdd_class.getBdd(bdd);
            setContentView(mybdd.getXml());

            if (element != null) {//si on avait pas d'élément on prend le premier de la base, mais on ne remplit pas
                mybdd.setFieldByElement(this, element);
            }
        }
    }

    public void liste1(View v){
        Button btn=(Button)v;
        int numliste=Integer.parseInt((String)btn.getTag());
        try {
            mybdd.getForeignList(element, numliste, action);
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }
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

    public void onBackPressed(){
        Intent intent;
        if(myactivity!=null){
            if(myactivity.equals("menu")){
                intent=new Intent(ActivityFormulaire.this,ActivityMenuApplication.class);
            }
            else{
                intent=new Intent(ActivityFormulaire.this,ActivityListShowBdd.class);
                intent.putExtra("action",action);
                intent.putExtra("bdd",bdd);
            }
        }
        else{
            intent=new Intent(ActivityFormulaire.this,ActivityListShowBdd.class);
            intent.putExtra("action",action);
            intent.putExtra("bdd",bdd);
        }
        startActivity(intent);
    }
}
