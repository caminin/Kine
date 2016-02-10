package charlot.rodolphe.com.gmail.kine.Interface;


import android.content.ContentValues;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class PathologieInterface implements ElementInterface {
    public int id_pathologie;
    public String nom_pathologie;

    public PathologieInterface(String _nom_pathologie){
        nom_pathologie=_nom_pathologie;
    }

    public PathologieInterface(ContentValues content){
        nom_pathologie=content.getAsString("nom");
    }

    public String toString(){
        return id_pathologie+" "+nom_pathologie;
    }

    public String toText()
            throws BddException.BddNoElementException {
        String res;
        res=nom_pathologie;
        return res;

    }

    @Override
    public int getId() {
        return id_pathologie;
    }



}
