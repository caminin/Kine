package charlot.rodolphe.com.gmail.kine.Bdd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import charlot.rodolphe.com.gmail.kine.Interface.TemoinResInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class TemoinResBdd extends BddClass implements BddInterface {
    private Context _context;
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "temoin_res.db";
    private static final String TABLE_TEMOIN_RES_BDD = "table_temoin_res_bdd";
    private static final String COL_ID_TEMOIN = "ID_TEMOIN";
    private static final int NUM_COL_ID_TEMOIN = 0;
    private static final String COL_ID_LIST_RES = "ID_LIST_RES";
    private static final int NUM_COL_ID_LIST_RES = 1;
    private static final String COL_ID_TEST_TEMOIN = "ID_TEST_TEMOIN";
    private static final int NUM_COL_ID_TEST_TEMOIN = 2;
    private static final String COL_COMPARATEUR = "COMPARATEUR";
    private static final int NUM_COL_COMPARATEUR = 3;
    private static final String COL_VARIABLE_1 = "VARIABLE_1";
    private static final int NUM_COL_VARIABLE_1 = 4;
    private static final String COL_VARIABLE_2 = "VARIABLE_2";
    private static final int NUM_COL_VARIABLE_2 = 5;



    public SQLiteDatabase bdd;

    private MaBddTemoinRes maBaseSQLite;

    public TemoinResBdd(Context context){
        //On créer la BDD et sa table
        _context=context;
        maBaseSQLite = new MaBddTemoinRes(context, NOM_BDD, null, VERSION_BDD);
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
        TemoinResInterface temoin=getTemoinWithId(id);

        TestBdd test_bdd=new TestBdd(_context);

        test_bdd.open();
            TestInterface test=test_bdd.getTestWithId(temoin.id_test_temoin);
        test_bdd.close();


        String res="Temoin de la liste "+temoin.id_list_res+" au test "+test.nom_test+" qui donne "
                +temoin.comparateur_temoin+temoin.variable_1+temoin.variable_2;
        return res;
    }

    @Override
    public void deleteWithId(int id)
            throws BddException.BddNoElementException {
        if(bdd.delete(TABLE_TEMOIN_RES_BDD,COL_ID_TEMOIN+" = "+id,null)==-1){
            throw new BddException.BddNoElementException(TABLE_TEMOIN_RES_BDD);
        }
    }

    @Override
    public void getByContext(Activity act) throws BddException.BddInsertException {

    }

    @Override
    public void setFieldByElement(Activity act, ElementInterface element) {

    }

    public void insertTemoin(TemoinResInterface mytemoin)
            throws BddException.BddInsertException, BddException.BddDejaExistantException{
        ContentValues values = new ContentValues();
        values.put(COL_ID_LIST_RES, mytemoin.id_list_res);
        values.put(COL_ID_TEST_TEMOIN, mytemoin.id_test_temoin);
        values.put(COL_COMPARATEUR, mytemoin.comparateur_temoin);
        values.put(COL_VARIABLE_1, mytemoin.variable_1);
        values.put(COL_VARIABLE_2, mytemoin.variable_2);

        try {
            TemoinResInterface temoin[]=getAll();
            for (TemoinResInterface aTemoin : temoin) {
                if(aTemoin.sameValue(mytemoin)){
                    throw new BddException.BddDejaExistantException(TABLE_TEMOIN_RES_BDD);
                }
            }
        } catch (BddException.BddNoElementException e) {
            e.printStackTrace();
        }

        if(bdd.insert(TABLE_TEMOIN_RES_BDD, null, values)==-1){
            throw new BddException.BddInsertException(TABLE_TEMOIN_RES_BDD);
        }
    }

    public TemoinResInterface[] getTemoinWithListRes(int id_list_res)
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEMOIN_RES_BDD,new String[] {COL_ID_TEMOIN,COL_ID_LIST_RES,COL_ID_TEST_TEMOIN,COL_COMPARATEUR,COL_VARIABLE_1,COL_VARIABLE_2},COL_ID_LIST_RES+" = "+id_list_res,null,null,null,null);
        return allCursorToTemoin(c);
    }

    public TemoinResInterface[] getTemoinWithIdTest(int id_test)
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEMOIN_RES_BDD,new String[] {COL_ID_TEMOIN,COL_ID_LIST_RES,COL_ID_TEST_TEMOIN,COL_COMPARATEUR,COL_VARIABLE_1,COL_VARIABLE_2},COL_ID_TEST_TEMOIN+" = "+id_test,null,null,null,null);
        return allCursorToTemoin(c);
    }

    public TemoinResInterface getTemoinWithId(int id)
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEMOIN_RES_BDD,new String[] {COL_ID_TEMOIN,COL_ID_LIST_RES,COL_ID_TEST_TEMOIN,COL_COMPARATEUR,COL_VARIABLE_1,COL_VARIABLE_2},COL_ID_TEMOIN+" = "+id,null,null,null,null);
        return cursorToLastTemoin(c);
    }

    public TemoinResInterface[] getAll()
            throws BddException.BddNoElementException {
        Cursor c = bdd.query(TABLE_TEMOIN_RES_BDD,new String[] {COL_ID_TEMOIN,COL_ID_LIST_RES,COL_ID_TEST_TEMOIN,COL_COMPARATEUR,COL_VARIABLE_1,COL_VARIABLE_2},null,null,null,null,null);
        return allCursorToTemoin(c);
    }

    private TemoinResInterface[] allCursorToTemoin(Cursor c)
            throws BddException.BddNoElementException{
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("temresbdd", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_TEMOIN_RES_BDD);
        }
        int i=0;
        TemoinResInterface tab_temoin[]=new TemoinResInterface[c.getCount()];
        //Sinon on se place sur le premier élément
        c.moveToFirst();
        while(!c.isAfterLast()){
            //On créé un test
            TemoinResInterface temoin;
            if(c.getColumnCount()==5){
                temoin=new TemoinResInterface(c.getInt(NUM_COL_ID_LIST_RES),c.getInt(NUM_COL_ID_TEST_TEMOIN),c.getString(NUM_COL_COMPARATEUR),c.getString(NUM_COL_VARIABLE_1));
            }
            else{
                temoin=new TemoinResInterface(c.getInt(NUM_COL_ID_LIST_RES),c.getInt(NUM_COL_ID_TEST_TEMOIN),c.getString(NUM_COL_COMPARATEUR),c.getString(NUM_COL_VARIABLE_1),c.getString(NUM_COL_VARIABLE_2));
            }
            temoin.id_temoin_res=c.getInt(NUM_COL_ID_TEMOIN);
            tab_temoin[i]=temoin;
            i++;
            c.moveToNext();
        }
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return tab_temoin;
    }

    private TemoinResInterface cursorToLastTemoin(Cursor c)
            throws BddException.BddNoElementException{
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("temresbdd", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_TEMOIN_RES_BDD);
        }
        //Sinon on se place sur le premier élément
        c.moveToLast();
        //On créé un test
        TemoinResInterface temoin=new TemoinResInterface(c.getInt(NUM_COL_ID_LIST_RES),c.getInt(NUM_COL_ID_TEST_TEMOIN),c.getString(NUM_COL_COMPARATEUR),c.getString(NUM_COL_VARIABLE_1),c.getString(NUM_COL_VARIABLE_2));
        temoin.id_temoin_res=c.getInt(NUM_COL_ID_TEMOIN);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return temoin;
    }

    @Override
    public int getXml() {
        return 0;
    }

    @Override
    public String[] getForeignList(ElementInterface element, int numListe, String action) throws BddException.BddNoElementException {
        return new String[0];
    }

    public class MaBddTemoinRes extends SQLiteOpenHelper {
        private static final String TABLE_TEMOIN_RES_BDD = "table_temoin_res_bdd";
        private static final String COL_ID_TEMOIN = "ID_TEMOIN";
        private static final String COL_ID_LIST_RES = "ID_LIST_RES";
        private static final String COL_ID_TEST_TEMOIN = "ID_TEST_TEMOIN";
        private static final String COL_COMPARATEUR = "COMPARATEUR";
        private static final String COL_VARIABLE_1 = "VARIABLE_1";
        private static final String COL_VARIABLE_2 = "VARIABLE_2";
        private static final String CREATE_BDD =
                "CREATE TABLE " + TABLE_TEMOIN_RES_BDD + " ("+
                        COL_ID_TEMOIN + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COL_ID_LIST_RES + " INTEGER NOT NULL,"+
                        COL_ID_TEST_TEMOIN + " INTEGER NOT NULL,"+
                        COL_COMPARATEUR + " TEXT NOT NULL,"+
                        COL_VARIABLE_1 + " FLOAT NOT NULL,"+
                        COL_VARIABLE_2 + " FLOAT ,"+
                        "FOREIGN KEY("+COL_ID_TEST_TEMOIN+") REFERENCES table_test(ID_TEST));";

        public MaBddTemoinRes(Context context, String name, CursorFactory factory,
                                int version) {
            super(context, name, factory, version);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BDD);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_TEMOIN_RES_BDD + ";");
            onCreate(db);
        }

    }
}
