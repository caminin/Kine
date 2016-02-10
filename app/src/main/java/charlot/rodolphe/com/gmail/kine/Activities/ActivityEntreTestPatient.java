package charlot.rodolphe.com.gmail.kine.Activities;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Adapter.ArticulationAdapter;
import charlot.rodolphe.com.gmail.kine.Bdd.PathologieBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.TemoinResBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.UniteBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PathologieInterface;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TemoinResInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.Interface.UniteInterface;
import charlot.rodolphe.com.gmail.kine.Enum.LocalisationTest;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;
import charlot.rodolphe.com.gmail.kine.Enum.TypeTest;

public class ActivityEntreTestPatient extends ListActivity {
	public static final int Menu_addTest=Menu.FIRST+1;
	public int idPatient;
	public ArticulationAdapter art_adapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_patient);
		
		Intent intent = getIntent();
		
		if (intent != null) {
			idPatient=intent.getIntExtra("idPatient",0);
		}
		
		TextView symptome=(TextView) findViewById(R.id.list_symptomes);
		PatientBdd pat_bdd=new PatientBdd(this);
		pat_bdd.open();
            PatientInterface patient;
            try {
                patient = pat_bdd.getPatientWithId(idPatient);
                symptome.setText(patient.symptome_pat);
            } catch (BddException.BddNoElementException e) {
                e.printStackTrace();
            }

		pat_bdd.close();
		
		
		LocalisationTest[] tab_loc=new LocalisationTest[]{LocalisationTest.atm,LocalisationTest.cervicales,LocalisationTest.cheville,LocalisationTest.coude,LocalisationTest.epaule,LocalisationTest.genou,LocalisationTest.hanche,LocalisationTest.lombaire,LocalisationTest.main,LocalisationTest.pied,LocalisationTest.poignet,LocalisationTest.sacro_illiaque,LocalisationTest.thoracique};
		art_adapter= new ArticulationAdapter(this, tab_loc);
		setListAdapter(art_adapter);
		Log.v("ActivityMenuPatient", "" + art_adapter.getCount());
		ListView mList=(ListView)findViewById(android.R.id.list);
		mList.setAdapter(art_adapter);
	}
	
	public void onListItemClick( ListView parent, View v, int pos, long id){
		TestBdd test_bdd=new TestBdd(this);
		LocalisationTest art = art_adapter.getItem(pos);
		test_bdd.open();
            try {
                test_bdd.getAllTestWithLocalisation(art.toString());
                Intent intent=new Intent(ActivityEntreTestPatient.this,ActivityListTest.class);
                intent.putExtra("idPatient",idPatient);
                intent.putExtra("articulationName",art.toString());
                startActivity(intent);
            } catch (BddException.BddNoElementException e) {
                Toast.makeText(this,"Il n'existe pas de Test pour cette articulation", Toast.LENGTH_LONG).show();
            }
        test_bdd.close();
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(Menu.NONE, Menu_addTest, Menu.NONE, "add some test");
		return (super.onCreateOptionsMenu(menu));
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		ContextBoxList();
		return (super.onOptionsItemSelected(item));
	}


	@SuppressLint("InflateParams")
	public void ContextBoxList(){
		TestBdd test_bdd=new TestBdd(getApplicationContext());
		UniteBdd unite_bdd=new UniteBdd(getApplicationContext());
        TemoinResBdd temoin_bdd=new TemoinResBdd((getApplicationContext()));
        PathologieBdd path_bdd = new PathologieBdd(getApplicationContext());
		test_bdd.open();
		unite_bdd.open();
		
		TestInterface test=new TestInterface("ChatonTest", TypeTest.valueOf("test1"),"installation des chatons","manoeuvre des chatons","contre_indication des chatons","interpretation des chatons",LocalisationTest.valueOf("genou"));
		TestInterface test2=new TestInterface("Petits chatons",TypeTest.valueOf("test2"),"installation des chatons2","manoeuvre des chatons2","contre_indication des chatons2","interpretation des chatons2",LocalisationTest.valueOf("cervicales"));
		TestInterface test3=new TestInterface("UniteChaton",TypeTest.valueOf("test3"),"installation des chatons2","manoeuvre des chatons2","contre_indication des chatons2","interpretation des chatons2",LocalisationTest.valueOf("hanche"));
        try {
            test_bdd.insertTest(test3);
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes", Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"Le test existe déjà", Toast.LENGTH_LONG).show();
        }

        TestInterface new_test3= null;
        try {
            new_test3 = test_bdd.getTestWithName("UniteChaton");
        } catch (BddException.BddNoElementException e) {
            Toast.makeText(this,"Le test n'existe pas", Toast.LENGTH_LONG).show();
        }

        try {
            unite_bdd.insertUnite(new UniteInterface(new_test3.id_test, "cm"));
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes", Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"L'unite existe déjà", Toast.LENGTH_LONG).show();
        }

        try {
            test_bdd.insertTest(test);
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes", Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"Le test existe déjà", Toast.LENGTH_LONG).show();
        }


        try {
            test_bdd.insertTest(test2);
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes", Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"Le test existe déjà", Toast.LENGTH_LONG).show();
        }


        temoin_bdd.open();
        path_bdd.open();

        TestInterface new_test2;
        try {
            new_test2 = test_bdd.getTestWithName("Petits chatons");
            TemoinResInterface temoin1=new TemoinResInterface(1,new_test2.id_test,"=","Neutre");
            temoin_bdd.insertTemoin(temoin1);
        } catch (BddException.BddNoElementException e) {
            Toast.makeText(this,"pas d'élément", Toast.LENGTH_LONG).show();
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes", Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"Le test existe déjà", Toast.LENGTH_LONG).show();
        }

        PathologieInterface path=new PathologieInterface("chatonpat");
        try {
            path_bdd.insertPathologie(path);
        } catch (BddException.BddInsertException e) {
            Toast.makeText(this,"Insertion a posé des problèmes",Toast.LENGTH_LONG).show();
        } catch (BddException.BddDejaExistantException e) {
            Toast.makeText(this,"La pathologie existe déjà", Toast.LENGTH_LONG).show();
        }


        path_bdd.close();
        unite_bdd.close();
        test_bdd.close();
        temoin_bdd.close();

	}

}
