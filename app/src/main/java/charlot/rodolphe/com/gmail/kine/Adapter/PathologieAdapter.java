package charlot.rodolphe.com.gmail.kine.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Hashtable;

import charlot.rodolphe.com.gmail.kine.Interface.PathologieInterface;
import charlot.rodolphe.com.gmail.kine.Interface.ResultatInterface;
import charlot.rodolphe.com.gmail.kine.R;


public class PathologieAdapter extends ArrayAdapter<PathologieInterface> {
        public Hashtable<Integer,ResultatInterface[]> tab_res_par_pathologie;
        public PathologieAdapter(Context context, PathologieInterface[] list_pathologie,Hashtable<Integer,ResultatInterface[]> _tab_res_par_pathologie) {
            super(context, R.layout.list_simple);
            tab_res_par_pathologie=_tab_res_par_pathologie;
            Log.v("PatAdapter", "je suis dans le const");
            this.addAll(list_pathologie);
        }

        @SuppressLint("ViewHolder")
        public View getView (int position, View convertView, ViewGroup parent){
            Log.v("PatAdapter", "je suis dans le getview");
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout_inflate=inflater.inflate(R.layout.item_list_simple,parent,false);
            PathologieInterface pat = getItem(position);
            TextView ligne=(TextView)layout_inflate.findViewById(R.id.text);
            if(pat!=null)
            {
                String s="";
                ResultatInterface res[]=tab_res_par_pathologie.get(pat.id_pathologie);
                for (int i=0;i<res.length;i++){
                    s+=("\t\t"+res[i]+"\n");
                }
                ligne.setText("Pathologie : "+ pat.nom_pathologie+"\n"+s);
            }
            return layout_inflate;
        }

    }
