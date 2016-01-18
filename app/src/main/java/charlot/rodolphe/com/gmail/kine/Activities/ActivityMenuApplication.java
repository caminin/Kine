package charlot.rodolphe.com.gmail.kine.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Interface.IntentInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TemoinResInterface;
import charlot.rodolphe.com.gmail.kine.Interface.UniteInterface;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.SuperClass.ContextNeeded;

public class ActivityMenuApplication extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextNeeded.setContext(getApplicationContext());
		setContentView(R.layout.activity_kine);
	}

	public void menuNouveauPatient(View v){
        Intent intent=new Intent(ActivityMenuApplication.this,ActivityFormulaire.class);
        intent.putExtra("action","ajout");
        intent.putExtra("bdd","patient.db");
        intent.putExtra("activity","menu");
        startActivity(intent);
	}

	public void menuListPatient(View v){
		Intent intent=new Intent(ActivityMenuApplication.this,ActivityListPatient.class);
		startActivity(intent);
	}

	public void SupprimerBDD(View v){
        String tab_db[]=databaseList();
        for(String db:tab_db){
            if(!db.contains("journal")){
                deleteDatabase(db);
            }
        }
        Toast.makeText(this, "Bdd supprimée", Toast.LENGTH_LONG).show();
	}

	public void chargerBdd(View v){
        Intent intent=new Intent(ActivityMenuApplication.this,ActivityListMenuBdd.class);
        startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"Nop, tu pars pars",Toast.LENGTH_LONG).show();
    }
}
