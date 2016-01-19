package charlot.rodolphe.com.gmail.kine.Activities;




import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;

import charlot.rodolphe.com.gmail.kine.Adapter.PatientAdapter;
import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;

public class ActivityListPatient extends ListActivity {

	public PatientInterface tab_pat[];
	public PatientAdapter pat_adapter;
	EditText etSearchContact;
	public static final int Menu_suppression=Menu.FIRST+1;
	public static final int Menu_modifier=Menu.FIRST+2;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_patient);
		etSearchContact = (EditText) findViewById(R.id.pat_search);
		
		PatientBdd pat_bdd=new PatientBdd(this);
		pat_bdd.open();
		
		    tab_pat=pat_bdd.getAllPatient();
		
		pat_bdd.close();
		
		if(tab_pat==null){
            Toast.makeText(this,"La base de donnée ne contient aucun patient", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(ActivityListPatient.this,ActivityMenuApplication.class);

            startActivity(intent);

		}
		else {
            pat_adapter = new PatientAdapter(this, tab_pat);
            setListAdapter(pat_adapter);
            ListView list = (ListView) findViewById(android.R.id.list);
            registerForContextMenu(list);


            etSearchContact.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int start, int before,
                                          int count) {
                    if (cs != "")
                        pat_adapter.getFilter().filter(cs);
                    else {
                        getListView().clearTextFilter();
                    }

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
	}
	
	public void onListItemClick( ListView parent, View v, int pos, long id){
		Intent intent=new Intent(ActivityListPatient.this,ActivityEntreTestPatient.class);
		PatientInterface patient=pat_adapter.getItem(pos);
		intent.putExtra("idPatient",patient.id_pat);
		startActivity(intent);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, Menu_modifier, Menu.NONE, "Modifier");
		menu.add(Menu.NONE, Menu_suppression, Menu.NONE, "Supprimer");
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo(); 
		PatientInterface patient=pat_adapter.getItem(info.position);
		switch(item.getItemId()){
			case Menu_suppression:
				pat_adapter.remove(patient);
				PatientBdd pat_bdd=new PatientBdd(this);
				pat_bdd.open();
                    try {
                        pat_bdd.removePatientWithID(patient.id_pat);
                    } catch (BddException.BddNoElementException e) {
                        Toast.makeText(getApplicationContext(),"Le patient n'a pas pu être supprimé",Toast.LENGTH_LONG).show();
                    }
                pat_bdd.close();
                pat_adapter.notifyDataSetChanged();
				break;

			case Menu_modifier:
				Intent intent=new Intent(ActivityListPatient.this,ActivityModificationPatient.class);
				intent.putExtra("idPatient",patient.id_pat);
				startActivity(intent);
				break;
		}
		return (super.onContextItemSelected(item));
	}

}
