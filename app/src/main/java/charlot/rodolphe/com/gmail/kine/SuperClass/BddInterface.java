package charlot.rodolphe.com.gmail.kine.SuperClass;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;


public interface BddInterface {
    void open();
    void close();
    SQLiteDatabase getBDD();

    String toText(int id) throws BddException.BddNoElementException;
    ElementInterface[] getAll() throws BddException.BddNoElementException;
    void deleteWithId(int id) throws BddException.BddNoElementException;
    void getByContext(Activity act) throws BddException.BddInsertException;
    void setFieldByElement(Activity act,ElementInterface element);
    int getXml();
    ElementInterface getNewElement();
    int getForeignList(ElementInterface element,int numListe) throws BddException.BddNoElementException;
    int addForeignList(int numListe) throws BddException.BddNoElementException;

}
