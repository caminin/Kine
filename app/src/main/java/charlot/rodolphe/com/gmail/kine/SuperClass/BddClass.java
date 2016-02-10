package charlot.rodolphe.com.gmail.kine.SuperClass;

import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Bdd.PathologieBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.ResultatBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.TemoinResBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.UniteBdd;

public class BddClass extends ContextNeeded {
    /*public BddInterface getBdd(String bdd){
        BddInterface mybdd=null;
        switch (bdd) {
            case "patient.db":
                mybdd = new PatientBdd(getContext());
                break;
            case "test.db":
                mybdd = new TestBdd(getContext());
                break;
            case "unite.db":
                mybdd = new UniteBdd(getContext());
                break;
            case "temoin_res.db":
                mybdd = new TemoinResBdd(getContext());
                break;
            case "pathologie.db":
                mybdd = new PathologieBdd(getContext());
                break;
            case "resultat.db":
                mybdd = new ResultatBdd(getContext());
                break;
            default:
                Toast.makeText(getContext(), "La bdd n'est pas reconnue", Toast.LENGTH_LONG).show();
                Log.v("listshowbdd", "bdd non reconnu");
                break;
        }
        return mybdd;
    }*/

    public NewBddInterface getBdd(String s){
        return new PathologieBdd(getContext());
    }

}
