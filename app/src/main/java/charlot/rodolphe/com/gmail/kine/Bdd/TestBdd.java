package charlot.rodolphe.com.gmail.kine.Bdd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.Enum.LocalisationTest;
import charlot.rodolphe.com.gmail.kine.Enum.TypeTest;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;

public class TestBdd extends BddClass implements BddInterface {
	private static final int VERSION_BDD = 3;
	private static final String NOM_BDD = "test.db";
 
	private static final String TABLE_TEST = "table_test";
	private static final String COL_ID_TEST = "ID_TEST";
	private static final int NUM_COL_ID_TEST = 0;
	private static final String COL_NOM_TEST = "NOM_TEST";
	private static final int NUM_COL_NOM_TEST = 1;
	private static final String COL_TYPE_TEST = "TYPE_TEST";
	private static final int NUM_COL_TYPE_TEST = 2;
	private static final String COL_INSTALLATION = "INSTALLATION";
	private static final int NUM_COL_INSTALLATION = 3;
	private static final String COL_MANOEUVRE = "MANOEUVRE";
	private static final int NUM_COL_MANOEUVRE = 4;
	private static final String COL_CONTREINDICATION = "CONTREINDICATION";
	private static final int NUM_COL_CONTREINDICATION = 5;
	private static final String COL_INTERPRETATION = "INTERPRETATION";
	private static final int NUM_COL_INTERPRETATION = 6;
	private static final String COL_LOCALISATION = "LOCALISATION";
	private static final int NUM_COL_LOCALISATION = 7;
 
	public SQLiteDatabase bdd;
 
	private MaBddTest maBaseSQLite;

	public TestBdd(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new MaBddTest(context, NOM_BDD, null, VERSION_BDD);
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
    public String toText(int id) throws BddException.BddNoElementException {
        TestInterface test=getTestWithId(id);

        String res="Test : "+test.nom_test;

        return res;
    }

    @Override
    public ElementInterface[] getAll() throws BddException.BddNoElementException {
        return getAllTest();
    }

    @Override
    public void deleteWithId(int id)
            throws BddException.BddNoElementException {
        if(bdd.delete(TABLE_TEST,COL_ID_TEST+" = "+id,null)==-1){
            throw new BddException.BddNoElementException(TABLE_TEST);
        }
    }

    @Override
    public void getByContext(Activity act) throws BddException.BddInsertException {

    }

    @Override
    public void setFieldByElement(Activity act, ElementInterface element) {

    }

    public void insertTest(TestInterface test)
            throws BddException.BddInsertException, BddException.BddDejaExistantException {
		ContentValues values = new ContentValues();
		values.put(COL_NOM_TEST, test.nom_test);
		values.put(COL_TYPE_TEST, test.type_test.toString());
		values.put(COL_INSTALLATION,test.installation_test);
		values.put(COL_INTERPRETATION,test.interpretation_test);
		values.put(COL_LOCALISATION, test.localisation_test.toString());
		values.put(COL_MANOEUVRE, test.manoeuvre_test);
		values.put(COL_CONTREINDICATION, test.contre_indication_test);

        try {
            getTestWithName(test.nom_test);
            Log.v("testbdd",test.toString());
            throw new BddException.BddDejaExistantException(TABLE_TEST);
        } catch (BddException.BddNoElementException e) {
            if(bdd.insert(TABLE_TEST, null, values)==-1)
                throw new BddException.BddInsertException(TABLE_TEST);
        }
	}

	public TestInterface[] getAllTestWithLocalisation(String localisation)
            throws BddException.BddNoElementException {
		Log.v("testbdd", "localisation=" + localisation);
		Cursor c = bdd.query(TABLE_TEST, new String[] {COL_ID_TEST, COL_NOM_TEST, COL_TYPE_TEST, COL_INSTALLATION, COL_MANOEUVRE, COL_CONTREINDICATION, COL_INTERPRETATION, COL_LOCALISATION}, COL_LOCALISATION + " LIKE \""+ localisation +"\"", null, null, null, null);
		return cursorToAllTest(c);
	}

    public TestInterface[] getAllTest()
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEST, new String[] {COL_ID_TEST, COL_NOM_TEST, COL_TYPE_TEST, COL_INSTALLATION, COL_MANOEUVRE, COL_CONTREINDICATION, COL_INTERPRETATION, COL_LOCALISATION}, null, null, null, null, null);
        return cursorToAllTest(c);
    }

    public TestInterface getTestWithName(String name)
            throws BddException.BddNoElementException {
        Log.v("testbdd", "name="+name);
        Cursor c = bdd.query(TABLE_TEST, new String[] {COL_ID_TEST, COL_NOM_TEST, COL_TYPE_TEST, COL_INSTALLATION, COL_MANOEUVRE, COL_CONTREINDICATION, COL_INTERPRETATION, COL_LOCALISATION}, COL_NOM_TEST + " LIKE \""+ name +"\"", null, null, null, null);
        return cursorToTest(c);
    }

    public TestInterface getTestWithId(int id)
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEST, new String[] {COL_ID_TEST, COL_NOM_TEST, COL_TYPE_TEST, COL_INSTALLATION, COL_MANOEUVRE, COL_CONTREINDICATION, COL_INTERPRETATION, COL_LOCALISATION}, COL_ID_TEST + " LIKE \""+ id +"\"", null, null, null, null);
        return cursorToTest(c);
    }

	//Cette méthode permet de convertir un cursor en un test
	private TestInterface cursorToTest(Cursor c)
            throws BddException.BddNoElementException {
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if(!bdd.isOpen()){
			Log.v("Test BDD", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_TEST);
        }
 
		//Sinon on se place sur le premier élément
		c.moveToLast();
		//On créé un test
		TestInterface test = new TestInterface(c.getString(NUM_COL_NOM_TEST), TypeTest.valueOf(c.getString(NUM_COL_TYPE_TEST)),c.getString(NUM_COL_INSTALLATION),c.getString(NUM_COL_MANOEUVRE),c.getString(NUM_COL_CONTREINDICATION),c.getString(NUM_COL_INTERPRETATION), LocalisationTest.valueOf(c.getString(NUM_COL_LOCALISATION)) );
		test.id_test=c.getInt(NUM_COL_ID_TEST);
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		
		//On ferme le cursor
		c.close();
 
		//On retourne le test
		return test;
	}
	
	//Cette méthode permet de convertir un cursor en tous les tests correspondants
		private TestInterface[] cursorToAllTest(Cursor c)
                throws BddException.BddNoElementException {
			int i=0;
			//si aucun élément n'a été retourné dans la requête, on renvoie null
            if(!bdd.isOpen()){
                Log.v("Test BDD", "Bdd non ouverte");
                open();
            }
            if (c.getCount() == 0){
                throw new BddException.BddNoElementException(TABLE_TEST);
            }
			TestInterface tab_test[]=new TestInterface[c.getCount()];
			//Sinon on se place sur le premier élément
			c.moveToFirst();
			while(!c.isAfterLast()){
				//On créé un test
				TestInterface test = new TestInterface(c.getString(NUM_COL_NOM_TEST),TypeTest.valueOf(c.getString(NUM_COL_TYPE_TEST)),c.getString(NUM_COL_INSTALLATION),c.getString(NUM_COL_MANOEUVRE),c.getString(NUM_COL_CONTREINDICATION),c.getString(NUM_COL_INTERPRETATION),LocalisationTest.valueOf(c.getString(NUM_COL_LOCALISATION)) );
				test.id_test=c.getInt(NUM_COL_ID_TEST);
				tab_test[i++]=test;
				
				c.moveToNext();
				
			}
			//On ferme le cursor
			c.close();
	 
			//On retourne le test
			return tab_test;
		}

    @Override
    public int getXml() {
        return 0;
    }

    @Override
    public String[] getForeignList(ElementInterface element, int numListe, String action) throws BddException.BddNoElementException {
        return new String[0];
    }

    public class MaBddTest extends SQLiteOpenHelper {
		private static final String TABLE_TEST = "table_test";
		private static final String COL_ID_TEST = "ID_TEST";
		private static final String COL_NOM_TEST = "NOM_TEST";
		private static final String COL_TYPE_TEST = "TYPE_TEST";
		private static final String COL_INSTALLATION = "INSTALLATION";
		private static final String COL_MANOEUVRE = "MANOEUVRE";
		private static final String COL_INDICATION = "INDICATION";
		private static final String COL_CONTREINDICATION = "CONTREINDICATION";
		private static final String COL_INTERPRETATION = "INTERPRETATION";
		private static final String COL_LOCALISATION = "LOCALISATION";
		private static final String CREATE_BDD = 
				"CREATE TABLE " + TABLE_TEST + " ("+
						COL_ID_TEST + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
						COL_NOM_TEST + " TEXT NOT NULL, "+ 
						COL_TYPE_TEST + " TEXT NOT NULL, "+
						COL_INSTALLATION +" TEXT , "+
						COL_MANOEUVRE +" TEXT , "+
						COL_INDICATION +" TEXT, "+
						COL_CONTREINDICATION +" TEXT , "+
						COL_INTERPRETATION +" TEXT NOT NULL, "+
						COL_LOCALISATION+" TEXT NOT NULL);";
		
		public MaBddTest(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_BDD);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE " + TABLE_TEST + ";");
			onCreate(db);
		}
	
	}
}
