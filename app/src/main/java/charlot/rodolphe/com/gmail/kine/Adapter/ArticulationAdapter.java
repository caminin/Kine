package charlot.rodolphe.com.gmail.kine.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import charlot.rodolphe.com.gmail.kine.Enum.LocalisationTest;
import charlot.rodolphe.com.gmail.kine.R;

public class ArticulationAdapter extends ArrayAdapter<LocalisationTest> {

	public ArticulationAdapter(Context context, LocalisationTest[] list_localisation) {
		super(context, R.layout.menu_patient);
		Log.v("ArtAdapter", "je suis dans le const");
		this.addAll(list_localisation);
	}
	
	@SuppressLint("ViewHolder")
	public View getView (int position, View convertView, ViewGroup parent){
		Log.v("ArtAdapter", "je suis dans le getview");
		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout_inflate=inflater.inflate(R.layout.item_menu_patient,parent,false);
		LocalisationTest test = getItem(position);
		TextView ligne=(TextView)layout_inflate.findViewById(R.id.nom_articulation);
		if(test!=null)
			ligne.setText(test.toString());
		
		return layout_inflate;
		
	}

}