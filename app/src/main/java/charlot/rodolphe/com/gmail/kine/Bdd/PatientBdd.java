package charlot.rodolphe.com.gmail.kine.Bdd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;

public class PatientBdd extends BddClass implements BddInterface {
	private static final int VERSION_BDD = 2;
	private static final String NOM_BDD = "patient.db";
 
	private static final String TABLE_PATIENT = "table_patient";
	private static final String COL_ID_PATIENT = "ID_PATIENT";
	private static final int NUM_COL_ID_PATIENT = 0;
	private static final String COL_NOM_PATIENT = "NOM_PATIENT";
	private static final int NUM_COL_NOM_PATIENT = 1;
	private static final String COL_PRENOM_PATIENT = "PRENOM_PATIENT";
	private static final int NUM_COL_PRENOM_PATIENT = 2;
	private static final String COL_DATENAISS_PATIENT = "DATENAISS_PATIENT";
	private static final int NUM_COL_DATENAISS_PATIENT = 3;
	private static final String COL_SYMPTOME_PATIENT = "SYMPTOME_PATIENT";
	private static final int NUM_COL_SYMPTOME_PATIENT = 4;

 
	public SQLiteDatabase bdd;
 
	private MaBddPatient maBaseSQLite;
 
	public PatientBdd(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new MaBddPatient(context, NOM_BDD, null, VERSION_BDD);
	}

    public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}

    @Override
    public String toText(int id)
            throws BddException.BddNoElementException {
        PatientInterface pat= getPatientWithId(id);
        String res=pat.nom_pat+" "+pat.prenom_pat;
        return res;
    }

    @Override
    public ElementInterface[] getAll() throws BddException.BddNoElementException {
        return getAllPatient();
    }

    @Override
    public void deleteWithId(int id)
            throws BddException.BddNoElementException {
        removePatientWithID(id);
    }

    @Override
    public void getByContext(Activity act)
            throws BddException.BddInsertException {
        ContentValues values = new ContentValues();
        values.put(COL_NOM_PATIENT, ((EditText) act.findViewById(R.id.nom)).getText().toString());
        values.put(COL_PRENOM_PATIENT, ((EditText)act.findViewById(R.id.prenom)).getText().toString());
        values.put(COL_DATENAISS_PATIENT, Long.toString(((act.findViewById(R.id.date)).getDrawingTime())));
        values.put(COL_SYMPTOME_PATIENT, ((EditText) act.findViewById(R.id.symptome)).getText().toString());

        if(bdd.insert(TABLE_PATIENT,null,values)==-1){
            throw new BddException.BddInsertException("PatientBdd");
        }
    }

    @Override
    public void setFieldByElement(Activity act,ElementInterface element) {
        EditText et_nom=(EditText) act.findViewById(R.id.nom);
        EditText et_prenom=(EditText) act.findViewById(R.id.prenom);
        DatePicker dp_date=(DatePicker) act.findViewById(R.id.date);
        EditText et_symptome=(EditText) act.findViewById(R.id.symptome);

        PatientInterface pat=(PatientInterface)element;

        Calendar cal=Calendar.getInstance();
        cal.setTime(pat.dateNaiss_pat);

        et_nom.setText(pat.nom_pat);
        et_prenom.setText(pat.prenom_pat);
        dp_date.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), null);
        et_symptome.setText(pat.symptome_pat);

    }

    public long insertPatient(PatientInterface patient)
            throws BddException.BddInsertException {
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_NOM_PATIENT, patient.nom_pat);
		values.put(COL_PRENOM_PATIENT, patient.prenom_pat);
		values.put(COL_DATENAISS_PATIENT, (patient.dateNaiss_pat).getTime());
		values.put(COL_SYMPTOME_PATIENT, patient.symptome_pat);
		//on insère l'objet dans la BDD via le ContentValues
        long res_insertion=bdd.insert(TABLE_PATIENT,null,values);
        if(res_insertion==-1){
            throw new BddException.BddInsertException("PatientBdd");
        }
        return res_insertion;
	}
	
	public void updatePatient(int id, PatientInterface patient)
            throws BddException.BddNoElementException {
		//La mise à jour d'un patient dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quel patient on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(COL_NOM_PATIENT, patient.nom_pat);
		values.put(COL_PRENOM_PATIENT, patient.prenom_pat);
		values.put(COL_DATENAISS_PATIENT, (Long.toString((patient.dateNaiss_pat).getTime())));
		values.put(COL_SYMPTOME_PATIENT, patient.symptome_pat);
		if(bdd.update(TABLE_PATIENT, values, COL_ID_PATIENT + " = " + id, null)==-1){
            throw new BddException.BddNoElementException(TABLE_PATIENT);
        }
	}
	
	public void removePatientWithID(int id)
            throws BddException.BddNoElementException {
		//Suppression d'un PATIENT de la BDD grâce à l'ID
		if(bdd.delete(TABLE_PATIENT, COL_ID_PATIENT + " = " + id, null)==-1){
            throw new BddException.BddNoElementException(TABLE_PATIENT);
        }
	}
	
	public PatientInterface getPatientWithName(String nom)
            throws BddException.BddNoElementException {
		
		//Récupère dans un Cursor les valeur correspondant à un patient contenu dans la BDD (ici on sélectionne le patient grâce à son nom)
		Cursor c = bdd.query(TABLE_PATIENT, new String[] {COL_ID_PATIENT, COL_NOM_PATIENT, COL_PRENOM_PATIENT, COL_DATENAISS_PATIENT, COL_SYMPTOME_PATIENT}, COL_NOM_PATIENT + " LIKE \"" + nom +"\"", null, null, null, null);
		return cursorToPatient(c);
	}
	
	public PatientInterface getPatientWithId(int id)
            throws BddException.BddNoElementException {
		
		//Récupère dans un Cursor les valeur correspondant à un patient contenu dans la BDD (ici on sélectionne le patient grâce à son nom)
		Cursor c = bdd.query(TABLE_PATIENT, new String[] {COL_ID_PATIENT, COL_NOM_PATIENT, COL_PRENOM_PATIENT, COL_DATENAISS_PATIENT, COL_SYMPTOME_PATIENT}, COL_ID_PATIENT + " = " + Integer.toString(id), null, null, null, null);
		return cursorToPatient(c);
	}
	
	
	public PatientInterface[] getAllPatient(){
		int i=0;
		if(!bdd.isOpen()){
			Log.v("Patient BDD", "Bdd non ouverte");
		}
		Log.v("Patient BDD", bdd.getPath());
		Log.v("Patient BDD", bdd.toString());
		Cursor c = bdd.query(TABLE_PATIENT, new String[] {COL_ID_PATIENT, COL_NOM_PATIENT, COL_PRENOM_PATIENT, COL_DATENAISS_PATIENT, COL_SYMPTOME_PATIENT}, null, null, null, null, null);
		if (c.getCount() == 0){
			Log.v("Patient BDD", "cursor null");
			return null;
		}
		PatientInterface tab_pat[]= new PatientInterface[c.getCount()];
		c.moveToFirst();
		while (!c.isAfterLast()){
			Log.v("Patient BDD", "Je suis dans la boucle");
			Date d=new Date(c.getLong(NUM_COL_DATENAISS_PATIENT));
			PatientInterface patient = new PatientInterface(c.getString(NUM_COL_NOM_PATIENT),c.getString(NUM_COL_PRENOM_PATIENT),d,c.getString(NUM_COL_SYMPTOME_PATIENT) );
			patient.id_pat=c.getInt(NUM_COL_ID_PATIENT);
			
			tab_pat[i++]= patient;
			c.moveToNext();
		}
		
		return tab_pat;
		
		
	}
	
	//Cette méthode permet de convertir un cursor en un patient
    private PatientInterface cursorToPatient(Cursor c)
            throws BddException.BddNoElementException {
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("Patient BDD", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_PATIENT);
        }
        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un patient
        Date d=new Date(c.getLong(NUM_COL_DATENAISS_PATIENT));
        PatientInterface patient = new PatientInterface(c.getString(NUM_COL_NOM_PATIENT),c.getString(NUM_COL_PRENOM_PATIENT),d,c.getString(NUM_COL_SYMPTOME_PATIENT) );
        patient.id_pat=c.getInt(NUM_COL_ID_PATIENT);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le patient
        return patient;
		}

    @Override
    public int getXml() {
        return R.layout.formulaire_patient;
    }

    @Override
    public String[] getForeignList(ElementInterface element, int numListe, String action) {
        return new String[0];
    }


    public class MaBddPatient extends SQLiteOpenHelper {
		private static final String TABLE_PATIENT = "table_patient";
		private static final String COL_ID_PATIENT = "ID_PATIENT";
		private static final String COL_NOM_PATIENT = "NOM_PATIENT";
		private static final String COL_PRENOM_PATIENT = "PRENOM_PATIENT";
		private static final String COL_DATENAISS_PATIENT = "DATENAISS_PATIENT";
		private static final String COL_SYMPTOME_PATIENT = "SYMPTOME_PATIENT";
		private static final String CREATE_BDD = 
				"CREATE TABLE " + TABLE_PATIENT + " ("+
						COL_ID_PATIENT + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						COL_NOM_PATIENT + " TEXT NOT NULL, "+
						COL_PRENOM_PATIENT + " TEXT NOT NULL, "+
						COL_DATENAISS_PATIENT +" TEXT NOT NULL, "+
						COL_SYMPTOME_PATIENT+" TEXT);";
		
		public MaBddPatient(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_BDD);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE " + TABLE_PATIENT + ";");
			onCreate(db);
		}
	
	}
	
}


