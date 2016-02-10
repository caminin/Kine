package charlot.rodolphe.com.gmail.kine.Bdd;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import charlot.rodolphe.com.gmail.kine.Activities.ActivityFormulaire;
import charlot.rodolphe.com.gmail.kine.Activities.ActivityListSimple;
import charlot.rodolphe.com.gmail.kine.Interface.PathologieInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TemoinResInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.NewBddInterface;

public class PathologieBdd extends BddClass implements NewBddInterface {
    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "pathologie.db";

    private static final String TABLE_PATHOLOGIE = "table_pathologie";
    private static final String COL_ID_PATHOLOGIE = "ID_PATHOLOGIE";
    private static final int NUM_COL_ID_PATHOLOGIE = 0;
    private static final String COL_NOM_PATHOLOGIE = "NOM_PATHOLOGIE";
    private static final int NUM_COL_NOM_PATHOLOGIE = 1;

    public SQLiteDatabase bdd;

    private MaBddTest maBaseSQLite;

    public PathologieBdd(Context context){
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



    public void insertPathologie(PathologieInterface pathologie)
            throws BddException.BddInsertException, BddException.BddDejaExistantException {
        ContentValues values = new ContentValues();
        values.put(COL_NOM_PATHOLOGIE, pathologie.nom_pathologie);
        try {
            getPathologieWithName(pathologie.nom_pathologie);
            throw new BddException.BddDejaExistantException(TABLE_PATHOLOGIE);
        } catch (BddException.BddNoElementException e) {
            if(bdd.insert(TABLE_PATHOLOGIE, null, values)==-1)
                throw new BddException.BddInsertException(TABLE_PATHOLOGIE);
        }


    }

    public PathologieInterface getPathologieWithName(String name)
            throws BddException.BddNoElementException{
        Cursor c = bdd.query(TABLE_PATHOLOGIE,new String[] {COL_ID_PATHOLOGIE,COL_NOM_PATHOLOGIE},COL_NOM_PATHOLOGIE+" LIKE \""+name+"\"",null,null,null,null);
        return cursorToLastPathologie(c);

    }

    public PathologieInterface getPathologieWithId(int id)
            throws BddException.BddNoElementException{
        Cursor c = bdd.query(TABLE_PATHOLOGIE,new String[] {COL_ID_PATHOLOGIE,COL_NOM_PATHOLOGIE},COL_ID_PATHOLOGIE + " = "+ id,null,null,null,null);
        return cursorToLastPathologie(c);

    }


    public PathologieInterface[] getAll()
            throws BddException.BddNoElementException{
        Cursor c = bdd.query(TABLE_PATHOLOGIE,new String[] {COL_ID_PATHOLOGIE,COL_NOM_PATHOLOGIE}
                            ,null
                            ,null,null,null,null);
        return allCursorToPathologie(c);
    }

    private PathologieInterface cursorToLastPathologie(Cursor c)
            throws BddException.BddNoElementException{
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(!bdd.isOpen()){
            Log.v("pathbdd", "Bdd non ouverte");
            open();
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_PATHOLOGIE);
        }
        //Sinon on se place sur le premier élément
        c.moveToLast();
        //On créé un test
        PathologieInterface patho=new PathologieInterface(c.getString(NUM_COL_NOM_PATHOLOGIE));
        patho.id_pathologie=c.getInt(NUM_COL_ID_PATHOLOGIE);
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return patho;
    }

    private TemoinResInterface[] hasTemoinRes(PathologieInterface pathologie){
        TemoinResBdd temoin_bdd=new TemoinResBdd(getContext());
        TemoinResInterface res_list[];
        temoin_bdd.open();
        try {
            res_list=temoin_bdd.getTemoinWithPathologie(pathologie.id_pathologie);
        } catch (BddException.BddNoElementException e) {
            res_list=null;
        }
        temoin_bdd.close();
        return res_list;
    }
    private PathologieInterface[] allCursorToPathologie(Cursor c)
            throws BddException.BddNoElementException{
        if(!bdd.isOpen()){
            Log.v("pathbdd", "Bdd non ouverte");
        }
        if (c.getCount() == 0){
            throw new BddException.BddNoElementException(TABLE_PATHOLOGIE);
        }
        int i=0;
        PathologieInterface tab_path[]=new PathologieInterface[c.getCount()];
        //Sinon on se place sur le premier élément
        c.moveToFirst();
        while(!c.isAfterLast()){
            //On créé un test
            PathologieInterface patho=new PathologieInterface(c.getString(NUM_COL_NOM_PATHOLOGIE));
            patho.id_pathologie=c.getInt(NUM_COL_ID_PATHOLOGIE);
            tab_path[i]=patho;
            i++;
            c.moveToNext();
        }
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor

        //On ferme le cursor
        c.close();

        //On retourne le test
        return tab_path;
    }

    public class MaBddTest extends SQLiteOpenHelper {
        private static final String TABLE_PATHOLOGIE = "table_pathologie";
        private static final String COL_ID_PATHOLOGIE = "ID_PATHOLOGIE";
        private static final String COL_NOM_PATHOLOGIE = "NOM_PATHOLOGIE";
        private static final String COL_ID_LIST_RES_PATHOLOGIE = "ID_LIST_RES_PATHOLOGIE";
        private static final String CREATE_BDD =
                "CREATE TABLE " + TABLE_PATHOLOGIE + " ("+
                        COL_ID_PATHOLOGIE + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COL_NOM_PATHOLOGIE +" TEXT NOT NULL,"+
                        COL_ID_LIST_RES_PATHOLOGIE +" INTEGER"+
                        ");";

        public MaBddTest(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BDD);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_PATHOLOGIE + ";");
            onCreate(db);
        }

    }

    @Override
    public int getXml() {
        return R.layout.formulaire_pathologie;
    }

    @Override
    public ElementInterface getNewElement() {
        PathologieInterface pat=new PathologieInterface("");
        return pat;

    }

    @Override
    public int getForeignList(ElementInterface element, int numListe)
            throws BddException.BddNoElementException {
        return 0;
    }

    @Override
    public String toText(int id)
            throws BddException.BddNoElementException {
        String res;
        PathologieInterface pat=getPathologieWithId(id);
        res=pat.nom_pathologie;
        return res;
    }

    @Override
    public void deleteWithId(int id)
            throws BddException.BddNoElementException {
        if(bdd.delete(TABLE_PATHOLOGIE, COL_ID_PATHOLOGIE + " = " + id, null)==-1){
            throw new BddException.BddNoElementException(TABLE_PATHOLOGIE);
        }
    }

    @Override
    public void getByContext(Activity act) throws BddException.BddInsertException {
        ContentValues values = new ContentValues();
        values.put(COL_NOM_PATHOLOGIE, ((EditText) act.findViewById(R.id.nom)).getText().toString());

        if(bdd.insert(TABLE_PATHOLOGIE, null, values)==-1){
            throw new BddException.BddInsertException("PathologieBdd");
        }
    }

    @Override
    public void setFieldByElement(Activity act, ElementInterface element, ArrayList<Integer> id_list) {
        PathologieInterface pat=((PathologieInterface)element);
        EditText nom=(EditText) act.findViewById(R.id.nom);
        TextView list_res=(TextView)act.findViewById(R.id.list_res);

        TemoinResInterface res_list[]=hasTemoinRes(pat);
        nom.setText(pat.nom_pathologie);
        if(res_list!=null){
            list_res.setText("Liste numéro : "+pat.getId());
            switchButtonVisibility(act,0,View.INVISIBLE,View.VISIBLE);
            id_list.add(pat.getId());
        }
        else{
            list_res.setText("Liste  non trouvée");
            switchButtonVisibility(act, 0, View.VISIBLE, View.INVISIBLE);
            id_list.add(-1);
        }

    }

    @Override
    public void switchButtonVisibility(Activity act, int numListe, int ajoutVisible, int otherVisible){
        switch(numListe){
            case 0:
                Button btnAjout=(Button)act.findViewById(R.id.btnAjout);
                Button btnModif=(Button)act.findViewById(R.id.btnModif);
                Button btnSupprimer=(Button)act.findViewById(R.id.btnSupprimer);
                Button btnVoir=(Button)act.findViewById(R.id.btnVoir);

                btnAjout.setVisibility(otherVisible);
                btnModif.setVisibility(ajoutVisible);
                btnSupprimer.setVisibility(ajoutVisible);
                btnVoir.setVisibility(ajoutVisible);
                break;
        }
    }

    @Override
    public void deleteListWithNumAndId(int numListe, int id) throws BddException.BddNoElementException {
        TemoinResBdd temoin_res=new TemoinResBdd(getContext());
        temoin_res.open();
            temoin_res.deleteWithIdPathologie(id);
        temoin_res.close();
    }
}
