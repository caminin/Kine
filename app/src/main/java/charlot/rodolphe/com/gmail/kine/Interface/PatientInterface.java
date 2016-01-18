package charlot.rodolphe.com.gmail.kine.Interface;

import android.content.ContentValues;

import java.util.Date;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;

public class PatientInterface implements ElementInterface {
	public int id_pat;
	public String nom_pat;
	public String prenom_pat;
	public Date dateNaiss_pat;
	public String symptome_pat;
	
	public PatientInterface(String _nom,String _prenom,Date _dateNaiss, String _symptome){
		nom_pat=_nom;
		prenom_pat=_prenom;
		dateNaiss_pat=_dateNaiss;
		symptome_pat=_symptome;
	}
	
	public PatientInterface(PatientInterface _pat){
		this.id_pat=_pat.id_pat;
		this.nom_pat=_pat.nom_pat;
		this.prenom_pat=_pat.prenom_pat;
		this.dateNaiss_pat=_pat.dateNaiss_pat;
		this.symptome_pat=_pat.symptome_pat;
	}

    public PatientInterface(ContentValues content){
        nom_pat=content.getAsString("nom");
        prenom_pat=content.getAsString("prenom");
        dateNaiss_pat=(Date)content.get("datenaiss");
        symptome_pat=content.getAsString("symptome");
    }


    public String toText()
            throws BddException.BddNoElementException {
        String res="Patient : "+nom_pat+" "+prenom_pat;
        return res;
    }

    @Override
    public int getId() {
        return id_pat;
    }


}
