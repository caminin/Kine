package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.HashMap;

import charlot.rodolphe.com.gmail.kine.Interface.IntentInterface;
import charlot.rodolphe.com.gmail.kine.MyException.IntentException;
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
        IntentInterface intent=new IntentInterface(getIntent());
        try {
            action = intent.getString("action") ;
            bdd = intent.getString("bdd");
            myactivity = intent.getString("activity");

            BddClass bdd_class = new BddClass();
            mybdd = bdd_class.getBdd(bdd);
            setContentView(mybdd.getXml());


        } catch (IntentException e) {
            e.printStackTrace();
        }

        try {
            element = (ElementInterface) intent.getSerializable("element");
            mybdd.setFieldByElement(this, element);
        } catch (IntentException e) {
            //plop
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
