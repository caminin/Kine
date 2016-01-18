package charlot.rodolphe.com.gmail.kine.Interface;


import android.content.Context;
import android.util.Log;

import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ContextNeeded;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class TemoinResInterface extends ContextNeeded implements ElementInterface {
    public static Context _context;
    public int id_temoin_res;
    public int id_list_res;
    public int id_test_temoin;
    public String comparateur_temoin;
    public String variable_1;
    public String variable_2;

    public TemoinResInterface(int _id_list_res, int _id_test_temoin, String _comparateur_temoin, String _variable_1){
        id_list_res=_id_list_res;
        id_test_temoin=_id_test_temoin;
        comparateur_temoin=_comparateur_temoin;
        variable_1=_variable_1;
        variable_2="";

    }

    public static void setContext(Context context){
        _context=context;
    }

    public TemoinResInterface(int _id_list_res, int _id_test_temoin, String _comparateur_temoin, String _variable_1, String _variable_2){
        id_list_res=_id_list_res;
        id_test_temoin=_id_test_temoin;
        comparateur_temoin=_comparateur_temoin;
        variable_1=_variable_1;
        variable_2=_variable_2;

    }
    public boolean comparaison_res_temoin(ResultatInterface res){
        boolean bool_return = true;


        String string_res = res.contenu_res;
        if (string_res.contains(comparateur_temoin)) {
            int index_of_comparateur = string_res.indexOf(comparateur_temoin);
            string_res = string_res.substring(0, index_of_comparateur) + string_res.substring(comparateur_temoin.length() + index_of_comparateur);
            Log.v("temoinres", string_res);
            if (string_res.contains("Non") || string_res.contains("Oui") || string_res.contains("Neutre")) {
                bool_return = (string_res.contains("Non") && variable_1.contains(("Non")))
                        || (string_res.contains("Oui") && variable_1.contains(("Oui")))
                        || (string_res.contains("Neutre") && variable_1.contains(("Neutre")));
            } else if (string_res.contains("Fonctionnel") || string_res.contains("Non Fonctionnel")) {
                bool_return = (string_res.contains("Non Fonctionnel") && variable_1.contains(("Non Fonctionnel")))
                        || (string_res.contains("Fonctionnel") && variable_1.contains(("Fonctionnel")));
            } else {
                String tab_string[] = string_res.split(" ");
                if (tab_string.length == 1) {
                    bool_return = variable_1.equalsIgnoreCase(tab_string[0]);
                } else if (tab_string.length == 2) {
                    bool_return = variable_1.equalsIgnoreCase(tab_string[0]) || (variable_2.equalsIgnoreCase(tab_string[1]));

                } else {
                    Log.v("temoinres", "j'ai trouvé une tab_string de longueur : " + tab_string.length);
                }
                Log.v("temoinres", "j'ai trouvé " + variable_1 + " et " + comparateur_temoin);
                if (comparateur_temoin.equalsIgnoreCase("=")) {
                    Log.v("temoinres", "j'ai trouvé " + variable_1 + " et " + comparateur_temoin);
                }
            }
        } else {
            bool_return = false;
        }
        return bool_return;
    }

    public String toString(){
        return id_list_res+" "+id_temoin_res+" "+id_test_temoin+" et comme res-> "+comparateur_temoin+" "+variable_1+" "+variable_2;
    }

    public boolean sameValue(TemoinResInterface _temoin) {
        return (_temoin.id_temoin_res == id_temoin_res &&
                _temoin.id_list_res == id_list_res &&
                _temoin.id_test_temoin == id_test_temoin &&
                _temoin.comparateur_temoin.equalsIgnoreCase(comparateur_temoin) &&
                _temoin.variable_1.equalsIgnoreCase(variable_1) &&
                _temoin.variable_2.equalsIgnoreCase(variable_2));
    }

    public String toText()
            throws BddException.BddNoElementException {

        TestBdd test_bdd=new TestBdd(_context);

        test_bdd.open();
            TestInterface test=test_bdd.getTestWithId(id_test_temoin);
        test_bdd.close();


        String res="Temoin de la liste "+id_list_res+" au test "+test.nom_test+" qui donne "
                +comparateur_temoin+variable_1+variable_2;
        return res;
    }

    @Override
    public int getId() {
        return id_temoin_res;
    }


}
