package charlot.rodolphe.com.gmail.kine.Interface;


import android.content.Context;

import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ContextNeeded;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class UniteInterface extends ContextNeeded implements ElementInterface {
    public static Context context;
	public int id_test;
	public String unite;
	
	public UniteInterface(int _id_test, String _unite){
		id_test=_id_test;
		unite=_unite;
	}

    public static void setContext(Context _context){
        context=_context;
    }

    public String toText()
            throws BddException.BddNoElementException {
        TestBdd test_bdd=new TestBdd(context);
        test_bdd.open();
            TestInterface test=test_bdd.getTestWithId(id_test);
        test_bdd.close();
        String res="Unite du test : "+test.nom_test+ " est "+unite;

        return res;
    }

    @Override
    public int getId() {
        return id_test;
    }



}
