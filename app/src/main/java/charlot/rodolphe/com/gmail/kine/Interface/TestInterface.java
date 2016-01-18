package charlot.rodolphe.com.gmail.kine.Interface;


import charlot.rodolphe.com.gmail.kine.Enum.LocalisationTest;
import charlot.rodolphe.com.gmail.kine.Enum.TypeTest;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class TestInterface implements ElementInterface {
	public int id_test;
	public String nom_test;
	public TypeTest type_test;
	public String installation_test;
	public String manoeuvre_test;
	public String contre_indication_test;
	public String interpretation_test;
	public LocalisationTest localisation_test;
	
	public TestInterface(String _nom, TypeTest _type, String _installation, String _manoeuvre, String _contre_indication, String _interpretation, LocalisationTest _localisation){
		nom_test=_nom;
		type_test=_type;
		installation_test=_installation;
		manoeuvre_test=_manoeuvre;
		contre_indication_test=_contre_indication;
		interpretation_test=_interpretation;
		localisation_test=_localisation;
	}

    public String toString(){
        return id_test+" "+nom_test+" "+type_test;
    }

    public String toText() throws BddException.BddNoElementException {

        String res="Test : "+nom_test;

        return res;
    }

    @Override
    public int getId() {
        return id_test;
    }



}
