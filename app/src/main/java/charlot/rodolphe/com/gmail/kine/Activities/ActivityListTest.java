package charlot.rodolphe.com.gmail.kine.Activities;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Adapter.TestAdapter;
import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.ResultatBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;

public class ActivityListTest extends ListActivity  {
	
	public TestInterface tab_test[];
	public PatientInterface patient;
	public TestAdapter test_adapter;
    public String localisation;

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
                sendRes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_simple);

		Intent intent = getIntent();
		
		if (intent != null) {
			localisation=intent.getStringExtra("articulationName");
		
			TestBdd test_bdd=new TestBdd(this);
                test_bdd.open();
                try {
                    tab_test=test_bdd.getAllTestWithLocalisation(localisation);
                } catch (BddException.BddNoElementException e) {
                    e.printStackTrace();
                }
            test_bdd.close();

            PatientBdd pat_bdd=new PatientBdd(this);
            pat_bdd.open();
                try {
                    patient=pat_bdd.getPatientWithId(intent.getIntExtra("idPatient",0));
                    if(tab_test!=null){
                        test_adapter = new TestAdapter (this, tab_test,patient);
                        setListAdapter(test_adapter);
                        ListView list=(ListView)findViewById(android.R.id.list);
                        registerForContextMenu(list);
                    }
                } catch (BddException.BddNoElementException e) {
                    e.printStackTrace();
                }
            pat_bdd.close();


		}
		else{
			Toast.makeText(this,"La base de donnée ne contient aucun test", Toast.LENGTH_LONG).show();
			Intent new_intent=new Intent(ActivityListTest.this,ActivityListTest.class);
			
			startActivity(new_intent);
		}
	}
	
	public void onListItemClick( ListView parent, View v, int pos, long id){
	}
	

	void setRes(View v,int visibility,String text){
		ListView lv=((ListView)findViewById(android.R.id.list));
		boolean end=false;
		int i=0;
		v.getId();
		while (i<lv.getCount() & end==false){
			if(lv.getChildAt(i).findViewById(v.getId())==v){
				end=true;
				Toast.makeText(this,"j'ai trouve le boutton", Toast.LENGTH_LONG).show();
			}
			else
				i++;
		}

		View myView=lv.getChildAt(i);
		Button btn=(Button)myView.findViewById(R.id.button_cancel);
		TextView txv=(TextView)myView.findViewById(R.id.contenu_résultat);

		btn.setVisibility(visibility);
		txv.setVisibility(visibility);
		txv.setText(text);

        ResultatBdd res_bdd=new ResultatBdd(this);
        ResultatInterface new_res;
        new_res = new ResultatInterface(tab_test[Integer.parseInt(v.getTag().toString())].id_test,patient.id_pat,text);
        res_bdd.open();
            try {
                res_bdd.insertResultat(new_res);
            } catch (BddException.BddInsertException e) {
                e.printStackTrace();
            }
        res_bdd.close();
	}
	
	public void setResFonctionnel(View v){
        setRes(v, 1, "= Fonctionnel");

    }
	
	public void setResNonFonctionnel(View v){

		setRes(v, 1, "= Non Fonctionnel");
	}
	
	public void cancelTest(View v){
        //bug avec oui,non,neutre
        setRes(v, -1, "");
        ResultatBdd res_bdd=new ResultatBdd(this);
        int todel_id_test=tab_test[Integer.parseInt(v.getTag().toString())].id_test;
        int todel_id_pat=patient.id_pat;
        res_bdd.open();
            res_bdd.deleteResultatWithIdPatientAndIdTest(todel_id_pat, todel_id_test);
        res_bdd.close();
	}
	
	public void setResOui(View v){

        setRes(v, 1, "= Oui");
	}
	public void setResNon(View v){

        setRes(v, 1, "= Non");
	}
	public void setResNeutre(View v){
		setRes(v, 1, "= Neutre");
	}

    public void setResTest3(View v){
        ListView lv=((ListView)findViewById(android.R.id.list));
        boolean end=false;
        int i=0;
        v.getId();
        while (i<lv.getCount() & end==false){
            if(lv.getChildAt(i).findViewById(v.getId())==v){
                end=true;
                Toast.makeText(this,"j'ai trouve le boutton", Toast.LENGTH_LONG).show();
            }
            else
                i++;
        }

        View myView=lv.getChildAt(i);
        Button btn=(Button)myView.findViewById(R.id.button_cancel);
        TextView txv=(TextView)myView.findViewById(R.id.contenu_résultat);
        EditText edit=(EditText)myView.findViewById(R.id.nombre_res_test);

        btn.setVisibility(View.VISIBLE);
        txv.setVisibility(View.VISIBLE);
        txv.setText(edit.getText().toString());

        ResultatBdd res_bdd=new ResultatBdd(this);
        ResultatInterface new_res;
        new_res = new ResultatInterface(tab_test[Integer.parseInt(v.getTag().toString())].id_test,patient.id_pat,edit.getText().toString());
        res_bdd.open();
            try {
                res_bdd.insertResultat(new_res);
            } catch (BddException.BddInsertException e) {
                e.printStackTrace();
            }
        res_bdd.close();
    }

    public void sendRes(){
        Intent intent=new Intent(ActivityListTest.this,ActivityListResultat.class);
        intent.putExtra("idPatient",patient.id_pat);
        intent.putExtra("articulationName", localisation);
        startActivity(intent);
    }

    public void onBackPressed(){
        Intent new_intent=new Intent(ActivityListTest.this,ActivityEntreTestPatient.class);
        new_intent.putExtra("idPatient",patient.id_pat);
        startActivity(new_intent);
    }
	
}
