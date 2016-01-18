package charlot.rodolphe.com.gmail.kine.MyException;

import android.util.Log;

public class BddException extends Exception{
    public BddException(String nomBdd){
        super(nomBdd);
    }
    static public class BddInsertException extends BddException{
        public BddInsertException(String nomBdd) {
            super(nomBdd);
            Log.v("BddInsertException","insertion ratée à "+nomBdd);
        }

    }

    static public class BddDejaExistantException extends BddException{
        public BddDejaExistantException(String nomBdd){
            super(nomBdd);
            Log.v("BddDejaExistantExceptio", "un élément existe déjà à " + nomBdd);
        }
    }

    static public class BddNoElementException extends BddException{
        public BddNoElementException(String nomBdd){
            super(nomBdd);
            Log.v("BddNoElementException","pas d'élément dans ce cursor à "+nomBdd);
        }
    }
}
