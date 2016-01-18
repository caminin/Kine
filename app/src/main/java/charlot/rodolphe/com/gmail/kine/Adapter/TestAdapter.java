package charlot.rodolphe.com.gmail.kine.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import charlot.rodolphe.com.gmail.kine.Bdd.ResultatBdd;
import charlot.rodolphe.com.gmail.kine.Bdd.UniteBdd;
import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.Interface.TestInterface;
import charlot.rodolphe.com.gmail.kine.Interface.UniteInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;
import charlot.rodolphe.com.gmail.kine.Enum.TypeTest;

public class TestAdapter extends ArrayAdapter<TestInterface> {
    private PatientInterface patient;
	public TestAdapter(Context context, TestInterface[] list_test, PatientInterface _patient) {
		super(context, R.layout.list_simple);
        patient=_patient;
		this.addAll(list_test);
	}

    private void setResFieldVisible(View layout_inflate,TestInterface test){
        ResultatBdd res_bdd=new ResultatBdd(getContext());
        res_bdd.open();
            ResultatInterface res;
            try {
                res = res_bdd.getResultatWithIdPatientAndIdTest(patient.id_pat,test.id_test);
                Button btn=(Button)layout_inflate.findViewById(R.id.button_cancel);
                TextView txv=(TextView)layout_inflate.findViewById(R.id.contenu_résultat);
                btn.setVisibility(View.VISIBLE);
                txv.setVisibility(View.VISIBLE);
                txv.setText(res.contenu_res);
            } catch (BddException.BddNoElementException e) {
                e.printStackTrace();
            }
        res_bdd.close();
    }
	
	@SuppressLint("ViewHolder")
	public View getView (int position, View convertView, ViewGroup parent){
		TestInterface test = getItem(position);
		View layout_inflate;
		LayoutInflater inflater;
		Button btn;
		if(test==null){
			return null;
		}
		if(test.type_test== TypeTest.test1){
			inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout_inflate=inflater.inflate(R.layout.item_list_test_type_1,parent,false);
			Log.v("TestAdapter", "je suis dans le getview");
			TextView ligne=(TextView)layout_inflate.findViewById(R.id.nom_test);
			ligne.setText("Test : " + test.nom_test);

			btn=(Button)layout_inflate.findViewById(R.id.button_type_1_fonctionnel);
			btn.setTag(position);
			btn=(Button)layout_inflate.findViewById(R.id.button_type_1_non_fonctionnel);
			btn.setTag(position);

            setResFieldVisible(layout_inflate,test);
		}
		else if(test.type_test==TypeTest.test2){
			inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout_inflate=inflater.inflate(R.layout.item_list_test_type_2,parent,false);
			Log.v("TestAdapter", "je suis dans le getview");
			TextView ligne=(TextView)layout_inflate.findViewById(R.id.nom_test);
			ligne.setText("Test : "+test.nom_test);
			btn=(Button)layout_inflate.findViewById(R.id.button_type_2_oui);
			btn.setTag(position);
			btn=(Button)layout_inflate.findViewById(R.id.button_type_2_non);
			btn.setTag(position);
			btn=(Button)layout_inflate.findViewById(R.id.button_type_2_neutre);
			btn.setTag(position);

            setResFieldVisible(layout_inflate, test);
			
		}
		else if(test.type_test==TypeTest.test3){
			inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout_inflate=inflater.inflate(R.layout.item_list_test_type_3,parent,false);
			Log.v("TestAdapter", "je suis dans le getview");
			TextView ligne=(TextView)layout_inflate.findViewById(R.id.nom_test);
			ligne.setText("Test : " + test.nom_test);

			TextView unite=(TextView)layout_inflate.findViewById(R.id.unite_test);

            btn=(Button)layout_inflate.findViewById(R.id.button_send_res);
            btn.setTag(position);

			UniteBdd unite_bdd= new UniteBdd(getContext());
			unite_bdd.open();
                UniteInterface myUnite= null;
                try {
                    myUnite = (unite_bdd.getUniteWithTest(test.id_test));
                    unite.setText(myUnite.unite);
                } catch (BddException.BddNoElementException e) {
                    Toast.makeText(getContext(), "Unité non trouvée", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
			unite_bdd.close();

            setResFieldVisible(layout_inflate,test);
		}
		else{
			return null;
		}
		
		
		btn=(Button)layout_inflate.findViewById(R.id.button_cancel);
		btn.setTag(position);
		
		return layout_inflate;
		
	}
	

}
