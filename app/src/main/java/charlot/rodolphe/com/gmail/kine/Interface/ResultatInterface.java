package charlot.rodolphe.com.gmail.kine.Interface;


import charlot.rodolphe.com.gmail.kine.Bdd.PatientBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.TestBdd;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ContextNeeded;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class ResultatInterface extends ContextNeeded implements ElementInterface {
    public int id_res;
    public int id_test_res;
    public int id_patient_res;
    public String contenu_res;

    public ResultatInterface(int _id_test_res, int _id_patient_res, String _contenu_res){
        id_test_res=_id_test_res;
        id_patient_res=_id_patient_res;
        contenu_res=_contenu_res;
    }

    public String toString(){
        TestBdd test_bdd=new TestBdd(context);
        test_bdd.open();
            TestInterface test= null;
            try {
                test = test_bdd.getTestWithId(id_test_res);
            } catch (BddException.BddNoElementException e) {
                e.printStackTrace();
            }
        test_bdd.close();
        return "Résultat à "+test.nom_test+" est: "+contenu_res;
    }

    public String toText() throws BddException.BddNoElementException {
        PatientBdd pat_bdd=new PatientBdd(context);
        TestBdd test_bdd=new TestBdd(context);

        pat_bdd.open();test_bdd.open();
        PatientInterface pat=pat_bdd.getPatientWithId(id_patient_res);
        TestInterface test=test_bdd.getTestWithId(id_test_res);
        pat_bdd.close();test_bdd.close();

        return "Patient "+pat.nom_pat+" "+pat.prenom_pat+" a eu "+contenu_res+" au test "+test.nom_test;
    }

    @Override
    public int getId() {
        return id_res;
    }


}
