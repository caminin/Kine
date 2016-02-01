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
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;

public class ResultatBdd extends BddClass implements BddInterface {
    private Context _context;
    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "resultat.db";

    private static final String TABLE_RES = "table_res";
    private static final String COL_ID_RES = "ID_RES";
    private static final int NUM_COL_ID_RES = 0;
    private static final String COL_ID_PATIENT_RES = "ID_PATIENT_RES";
    private static final int NUM_COL_ID_PATIENT_RES = 1;
    private static final String COL_ID_TEST_RES = "ID_TEST_RES";
    private static final int NUM_COL_ID_TEST_RES = 2;
    private static final String COL_CONTENU_RES = "CONTENU_RES";
    private static final int NUM_COL_CONTENU_RES = 3;

    public SQLiteDatabase bdd;

    private MaBddTest maBaseSQLite;

    public ResultatBdd(Context context){
        //On créer la BDD et sa table
        _context=context;
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
        ResultatInterface res= getResultatWithId(id);
        PatientBdd pat_bdd=new PatientBdd(_context);
        TestBdd test_bdd=new TestBdd(_context);

        pat_bdd.open();test_bdd.open();
            PatientInterface pat=pat_bdd.getPatientWithId(res.id_patient_res);
            TestInterface test=test_bdd.getTestWithId(res.id_test_res);
        pat_bdd.close();test_bdd.close();

        return "Patient "+pat.nom_pat+" "+pat.prenom_pat+" a eu "+res.contenu_res+" au test "+test.nom_test;
    }

    @Override
    public ElementInterface[] getAll() throws BddException.BddNoElementException {
        return getAllResult();
    }

    @Override
    public void deleteWithId(int id) throws BddException.BddNoElementException {
        if(bdd.delete(TABLE_RES,COL_ID_RES+" = "+id,null)==-1){
            throw new BddException.BddNoElementException(TABLE_RES);
        }
    }

    @Override
    public void getByContext(Activity act) throws BddException.BddInsertException {

    }

    @Override
    public void setFieldByElement(Activity act, ElementInterface element) {

    }

    public void insertResultat(ResultatInterface res)
            throws BddException.BddInsertException{
        ContentValues values = new ContentValues();
        values.put(COL_ID_TEST_RES, res.id_test_res);
        values.put(COL_ID_PATIENT_RES,res.id_patient_res);
        values.put(COL_CONTENU_RES,res.contenu_res);

        try {
            getResultatWithIdTest(res.id_test_res);
            deleteResultatWithIdPatientAndIdTest(res.id_patient_res,res.id_test_res);
            Log.v("insertResultat", "J'ai supprimé un test");
        } catch (BddException.BddNoElementException e) {
        }

        bdd.insert(TABLE_RES, null, values);
        if(bdd.insert(TABLE_RES, null, values)==-1)
            throw new BddException.BddInsertException(TABLE_RES);
    }

    public ResultatInterface getResultatWithIdPatientAndIdTest(int idPatient, int idTest)
            throws BddException.BddNoElementException {
        Log.v("resbdd", "idpatient:" + idPatient + " idtest:" + idTest);
        Cursor c = bdd.query(TABLE_RES, new String[] {COL_ID_RES, COL_ID_PATIENT_RES, COL_ID_TEST_RES, COL_CONTENU_RES}, COL_ID_PATIENT_RES + " = "+ idPatient+" AND "+COL_ID_TEST_RES + " = " + idTest, null, null, null, null);
        return cursorToLastRes(c);
    }

    public ResultatInterface[] getResultatWithIdTest(int idTest)
            throws BddException.BddNoElementException {
        Log.v("resbdd", "idtest:"+idTest);
        Cursor c = bdd.query(TABLE_RES, new String[] {COL_ID_RES, COL_ID_PATIENT_RES, COL_ID_TEST_RES, COL_CONTENU_RES}, COL_ID_TEST_RES + " = "+ idTest, null, null, null, null);
        return cursorToAllRes(c);
    }

    public ResultatInterface getResultatWithId(int id)
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_RES, new String[] {COL_ID_RES, COL_ID_PATIENT_RES, COL_ID_TEST_RES, COL_CONTENU_RES}, COL_ID_RES + " = "+ id, null, null, null, null);
        return cursorToLastRes(c);
    }

    public ResultatInterface[] getResultatWithIdPatient(int idPatient)
            throws BddException.BddNoElementException {
        Log.v("resbdd", "idpatient:"+idPatient);
        Cursor c = bdd.query(TABLE_RES, new String[] {COL_ID_RES, COL_ID_PATIENT_RES, COL_ID_TEST_RES, COL_CONTENU_RES}, COL_ID_PATIENT_RES + " = "+ idPatient, null, null, null, null);
        return cursorToAllRes(c);
    }

    public ResultatInterface[] getAllResult()
        throws BddException.BddNoElementException {
            Cursor c = bdd.query(TABLE_RES, new String[] {COL_ID_RES, COL_ID_PATIENT_RES, COL_ID_TEST_RES, COL_CONTENU_RES},null, null, null, null, null);
            return cursorToAllRes(c);
        }

    public void deleteResultatWithIdPatientAndIdTest(int idPatient, int idTest){
        bdd.delete(TABLE_RES, COL_ID_PATIENT_RES + " = " + idPatient + " AND " + COL_ID_TEST_RES + " = " + idTest, null);
    }

    private ResultatInterface cursorToLastRes(Cursor c)
            throws BddException.BddNoElementException{
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("resbdd", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_RES);
        }
        //Sinon on se place sur le premier élément
        c.moveToLast();
        //On créé un test
        ResultatInterface res = new ResultatInterface(c.getInt(NUM_COL_ID_TEST_RES),c.getInt(NUM_COL_ID_PATIENT_RES),c.getString(NUM_COL_CONTENU_RES) );
        res.id_res=c.getInt(NUM_COL_ID_RES);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return res;
    }

    private ResultatInterface[] cursorToAllRes(Cursor c)
            throws BddException.BddNoElementException{
        int i=0;
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("Res BDD", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_RES);
        }
        ResultatInterface tab_res[]=new ResultatInterface[c.getCount()];
        //Sinon on se place sur le premier élément
        c.moveToFirst();
        while(!c.isAfterLast()){
            //On créé un test
            ResultatInterface res = new ResultatInterface(c.getInt(NUM_COL_ID_TEST_RES),c.getInt(NUM_COL_ID_PATIENT_RES),c.getString(NUM_COL_CONTENU_RES) );
            res.id_res=c.getInt(NUM_COL_ID_RES);
            tab_res[i++]=res;

            c.moveToNext();

        }
        //On ferme le cursor
        c.close();

        //On retourne le test
        return tab_res;
    }

    @Override
    public int getXml() {
        return 0;
    }

    @Override
    public ElementInterface getNewElement() {
        return null;
    }

    @Override
    public int getForeignList(ElementInterface element, int numListe) throws BddException.BddNoElementException {
        return -1;
    }

    public class MaBddTest extends SQLiteOpenHelper {
        private static final String TABLE_RES = "table_res";
        private static final String COL_ID_RES = "ID_RES";
        private static final String COL_ID_PATIENT_RES = "ID_PATIENT_RES";
        private static final String COL_ID_TEST_RES = "ID_TEST_RES";
        private static final String COL_CONTENU_RES = "CONTENU_RES";
        private static final String CREATE_BDD =
                "CREATE TABLE " + TABLE_RES + " ("+
                        COL_ID_RES + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COL_ID_PATIENT_RES + " INTEGER NOT NULL, "+
                        COL_ID_TEST_RES + " INTEGER NOT NULL, "+
                        COL_CONTENU_RES +" TEXT NOT NULL,"+
                        "FOREIGN KEY("+COL_ID_PATIENT_RES+") REFERENCES table_patient(ID),"+
                        "FOREIGN KEY("+COL_ID_TEST_RES+") REFERENCES table_test(ID_TEST)"+

                        ");";

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
            db.execSQL("DROP TABLE " + TABLE_RES + ";");
            onCreate(db);
        }

    }
}
