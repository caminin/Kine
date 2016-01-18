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
import charlot.rodolphe.com.gmail.kine.Interface.UniteInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;

public class UniteBdd extends BddClass implements BddInterface {
    private Context _context;
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "unite.db";
	private static final String TABLE_UNITE = "table_unite";
	private static final String COL_ID_TEST = "ID_TEST";
	private static final int NUM_COL_ID_TEST = 0;
	private static final String COL_UNITE = "UNITE";
	private static final int NUM_COL_UNITE= 1;
 
	public SQLiteDatabase bdd;
 
	private MaBddUnite maBaseSQLite;

	public UniteBdd(Context context){
		//On créer la BDD et sa table
        _context=context;
		maBaseSQLite = new MaBddUnite(context, NOM_BDD, null, VERSION_BDD);
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
        UniteInterface unite=getUniteWithId(id);
        TestBdd test_bdd=new TestBdd(_context);
        test_bdd.open();
            TestInterface test=test_bdd.getTestWithId(unite.id_test);
        test_bdd.close();
        String res="Unite du test : "+test.nom_test+ " est "+unite.unite;

        return res;
    }

    @Override
    public ElementInterface[] getAll() throws BddException.BddNoElementException {
        return getAllUnite();
    }

    @Override
    public void deleteWithId(int id) throws BddException.BddNoElementException {
        if(bdd.delete(TABLE_UNITE,COL_ID_TEST+" = "+id,null)==-1){
            throw new BddException.BddNoElementException(TABLE_UNITE);
        }
    }

    @Override
    public void getByContext(Activity act) throws BddException.BddInsertException {

    }

    @Override
    public void setFieldByElement(Activity act, ElementInterface element) {

    }

    public long insertUnite(UniteInterface myUnite)
            throws BddException.BddDejaExistantException, BddException.BddInsertException {
		ContentValues values = new ContentValues();
		values.put(COL_UNITE, myUnite.unite);
		values.put(COL_ID_TEST, myUnite.id_test);

        try {
            getUniteWithTest(myUnite.id_test);
            throw new BddException.BddDejaExistantException(TABLE_UNITE);
        } catch (BddException.BddNoElementException e) {
            if(bdd.insert(TABLE_UNITE, null, values)==-1)
                throw new BddException.BddInsertException(TABLE_UNITE);
        }
		
		return bdd.insert(TABLE_UNITE, null, values);
	}
	
	public UniteInterface getUniteWithTest(int id_test)
            throws BddException.BddNoElementException {
		Cursor c = bdd.query(TABLE_UNITE, new String[] {COL_ID_TEST, COL_UNITE}, COL_ID_TEST + " = "+ id_test , null, null, null, null);

        return cursorToFirstUnite(c);

	}

    public UniteInterface getUniteWithId(int id)
            throws BddException.BddNoElementException {
        return getUniteWithTest(id);
    }

    public UniteInterface[] getAllUnite()
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_UNITE, new String[] {COL_ID_TEST, COL_UNITE}, null, null, null, null, null);

        return cursorToAllUnite(c);

    }


    public UniteInterface cursorToFirstUnite(Cursor c)
            throws BddException.BddNoElementException {
        if(!bdd.isOpen()){
            Log.v("uniteBDD", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_UNITE);
        }

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un test
        UniteInterface unite = new UniteInterface(c.getInt(NUM_COL_ID_TEST),c.getString(NUM_COL_UNITE) );
        Log.v("unite bdd", "unite.id_test");
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return unite;
    }

    public UniteInterface[] cursorToAllUnite(Cursor c)
            throws BddException.BddNoElementException {
        if(!bdd.isOpen()){
            Log.v("uniteBDD", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_UNITE);
        }
        UniteInterface tab_unite[]=new UniteInterface[c.getCount()];
        int i=0;
        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un test
        while(!c.isAfterLast()){
            //On créé un test
            UniteInterface unite = new UniteInterface(c.getInt(NUM_COL_ID_TEST),c.getString(NUM_COL_UNITE) );
            Log.v("unite bdd", "unite.id_test");
            tab_unite[i++]=unite;

            c.moveToNext();
        }

        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return tab_unite;
    }

    @Override
    public int getXml() {
        return 0;
    }

    @Override
    public String[] getForeignList(ElementInterface element, int numListe, String action) throws BddException.BddNoElementException {
        return new String[0];
    }

    public class MaBddUnite extends SQLiteOpenHelper {
		private static final String TABLE_UNITE = "table_unite";
		private static final String COL_ID_TEST = "ID_TEST";
		private static final String COL_UNITE = "UNITE";
		private static final String CREATE_BDD = 
				"CREATE TABLE " + TABLE_UNITE + " ("+
						COL_ID_TEST + " INTEGER PRIMARY KEY, "+
						COL_UNITE + " TEXT NOT NULL);";
		
		public MaBddUnite(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
		
		
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_BDD);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE " + TABLE_UNITE + ";");
			onCreate(db);
		}
	
	}
}
