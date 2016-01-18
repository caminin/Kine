package charlot.rodolphe.com.gmail.kine.Adapter;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import charlot.rodolphe.com.gmail.kine.Interface.PatientInterface;
import charlot.rodolphe.com.gmail.kine.R;

public class PatientAdapter extends ArrayAdapter<PatientInterface> implements Filterable {
	
	private ArrayList<PatientInterface> patValue;
	private ArrayList<PatientInterface> patOriginalValue;
    LayoutInflater inflater;
	
	public PatientAdapter(Context context, PatientInterface[] list_pat) {
		super(context, R.layout.list_patient);
		patValue=new ArrayList<PatientInterface>();
		patOriginalValue=new ArrayList<PatientInterface>();
		for(int i=0;i<list_pat.length;i++){
			patValue.add(list_pat[i]);
			patOriginalValue.add(list_pat[i]);
		}
        inflater = LayoutInflater.from(context);
	}

    @Override
    public void remove(PatientInterface object) {
        super.remove(object);
        patValue.remove(object);
        patOriginalValue.remove(object);
    }

    @SuppressLint("ViewHolder")
	public View getView (int position, View convertView, ViewGroup parent){
		/*LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout_inflate=inflater.inflate(R.layout.item_list_patient,parent,false);
		if((patValue==null)||((position)>patValue.size()-1))//si on a pas de patient ou que le patient demandé est en dehors
			return convertView;
		PatientInterface pat = patValue.get(position);
		Log.v("PatientAdapter", "je suis dans le getview");
		TextView ligne=(TextView)layout_inflate.findViewById(R.id.text_file);
		ImageView img=(ImageView)layout_inflate.findViewById(R.id.pic_articulation);
		
		if(pat!=null)
			ligne.setText(pat.nom_pat + " " + pat.prenom_pat);
		
		return layout_inflate;*/

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_patient, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.text_file);
            holder.imageView= (ImageView) convertView
                    .findViewById(R.id.pic_articulation);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(patValue.get(position).nom_pat + " " + patValue.get(position).prenom_pat);
        return convertView;
		
	}
	
	public PatientInterface getItem(int position) {
		return patValue.get(position);
	}

    public int getCount() {
        return patValue.size();
    }

    public long getItemId(int position) {
        return position;
    }

	
	public Filter getFilter() {

        return new Filter() {

           @SuppressWarnings("unchecked")
           protected void publishResults(CharSequence constraint,
                   FilterResults results) {
               patValue = (ArrayList<PatientInterface>) results.values;

               notifyDataSetChanged();

           }

           protected FilterResults performFiltering(CharSequence constraint) {
               FilterResults results = new FilterResults();
               // Holds the results of a filtering operation in values
               ArrayList<PatientInterface> FilteredArrList = new ArrayList<PatientInterface>();

               if (patOriginalValue == null) {
                   patOriginalValue = new ArrayList<PatientInterface>(patValue);
                   // saves data in OriginalValues
               }
               if (constraint == null || constraint.length() == 0) {
                   // set the Original result to return
                   results.count = patOriginalValue.size();
                   results.values = patOriginalValue;
               } else {
                   constraint = constraint.toString().toLowerCase();
                   for (int i = 0; i < patOriginalValue.size(); i++) {
                       String data = patOriginalValue.get(i).nom_pat;
                       if (data.toLowerCase().startsWith(constraint.toString())) {
                           FilteredArrList.add(new PatientInterface(patOriginalValue.get(i)));
                       }
                   }
                   // set the Filtered result to return
                   results.count = FilteredArrList.size();
                   results.values = FilteredArrList;
               }
               return results;
           }

       };
	}

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

}
