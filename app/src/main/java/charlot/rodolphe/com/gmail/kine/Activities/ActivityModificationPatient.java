package charlot.rodolphe.com.gmail.kine.Activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;

public class ActivityModificationPatient extends Activity {
	int id=1;
    PatientInterface patient;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifier_patient);
		Intent intent = getIntent();
	        
		if (intent != null) {
           id=intent.getIntExtra("idPatient",0);
		}
		else{
			Log.v("ModifierPatient","L'id du patient n'est pas reconnu");
			Intent newIntent=new Intent(ActivityModificationPatient.this,ActivityListPatient.class);
			startActivity(newIntent);
		}
		PatientBdd pat_bdd=new PatientBdd(this);
		pat_bdd.open();
            try {
                patient=pat_bdd.getPatientWithId(id);
                Log.v("ModifierPatient", patient.symptome_pat);
                EditText formNom=(EditText) findViewById(R.id.formNom);
                EditText formDateNaiss=(EditText) findViewById(R.id.formDateNaiss);
                EditText formPrenom=(EditText) findViewById(R.id.formPrenom);
                EditText formSymptome=(EditText) findViewById(R.id.formSymptome);

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.FRANCE);

                String getDateNaiss=formatter.format(patient.dateNaiss_pat);


                formNom.setText(patient.nom_pat);
                formPrenom.setText(patient.prenom_pat);
                formDateNaiss.setText(getDateNaiss);
                formSymptome.setText(patient.symptome_pat);
            } catch (BddException.BddNoElementException e) {
                e.printStackTrace();
            }

		
		pat_bdd.close();
	}
	
	public void modifierPatient(View v){
		PatientBdd pat_bdd=new PatientBdd(this);
		pat_bdd.open();
			EditText formNom=(EditText) findViewById(R.id.formNom);
			EditText formDateNaiss=(EditText) findViewById(R.id.formDateNaiss);
			EditText formPrenom=(EditText) findViewById(R.id.formPrenom);
			EditText formSymptome=(EditText) findViewById(R.id.formSymptome);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.FRANCE);
			
			Date date;
			try {
				date = formatter.parse(formDateNaiss.getText().toString());
				patient.nom_pat = (formNom.getText().toString());
				patient.prenom_pat = (formPrenom.getText().toString());
				patient.symptome_pat = (formSymptome.getText().toString());
				patient.dateNaiss_pat = (date);
				pat_bdd.updatePatient(patient.id_pat, patient);
				pat_bdd.close();
				
				Intent intent=new Intent(ActivityModificationPatient.this,ActivityListPatient.class);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this,"La date n'est pas valide, elle doit être de la forme \"jj/mm/aaaa\"", Toast.LENGTH_LONG).show();
				pat_bdd.close();
			}
			
	}

    public void supprimerPatient(View v){
        PatientBdd pat_bdd=new PatientBdd(this);
        pat_bdd.open();
        try {
            pat_bdd.removePatientWithID(patient.id_pat);
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }
        pat_bdd.close();
        Toast.makeText(this,"Patient supprimé", Toast.LENGTH_LONG).show();
        backPressed(v);
    }
	
	public void backPressed(View v){
		onBackPressed();
	}

    public void onBackPressed(){
        Intent new_intent=new Intent(ActivityModificationPatient.this,ActivityListPatient.class);
        startActivity(new_intent);
    }
	
}
