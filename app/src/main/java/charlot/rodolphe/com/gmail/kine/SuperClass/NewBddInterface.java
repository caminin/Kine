package charlot.rodolphe.com.gmail.kine.SuperClass;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;


public interface NewBddInterface {
    void open();
    void close();
    SQLiteDatabase getBDD();

    String toText(int id) throws BddException.BddNoElementException;
    ElementInterface[] getAll() throws BddException.BddNoElementException;
    void deleteWithId(int id) throws BddException.BddNoElementException;

    void getByContext(Activity act) throws BddException.BddInsertException;
    void setFieldByElement(Activity act,ElementInterface element,ArrayList<Integer> id_list);
    int getXml();
    ElementInterface getNewElement();
    int getForeignList(ElementInterface element,int numListe) throws BddException.BddNoElementException;
    void switchButtonVisibility(Activity act,int numListe,int ajoutVisible, int otherVisible);
    void deleteListWithNumAndId(int numListe, int id) throws BddException.BddNoElementException;
}
