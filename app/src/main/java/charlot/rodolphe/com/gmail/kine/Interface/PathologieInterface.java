package charlot.rodolphe.com.gmail.kine.Interface;


import android.content.ContentValues;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class PathologieInterface implements ElementInterface {
    public int id_pathologie;
    public String nom_pathologie;
    public int id_list_res_pathologie;

    public PathologieInterface(String _nom_pathologie,int _id_list_res_pathologie){
        nom_pathologie=_nom_pathologie;
        id_list_res_pathologie=_id_list_res_pathologie;
    }

    public PathologieInterface(ContentValues content){
        nom_pathologie=content.getAsString("nom");
        id_list_res_pathologie=content.getAsInteger("id_list");
    }

    public String toString(){
        return id_pathologie+" "+nom_pathologie+" "+id_list_res_pathologie;
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
