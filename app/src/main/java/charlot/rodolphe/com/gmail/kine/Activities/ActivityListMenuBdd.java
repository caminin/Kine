package charlot.rodolphe.com.gmail.kine.Activities;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import charlot.rodolphe.com.gmail.kine.Adapter.ExpListBddAdapter;
import charlot.rodolphe.com.gmail.kine.R;


public class ActivityListMenuBdd extends ExpandableListActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bdd);

        // get the listview
        expListView = (ExpandableListView) findViewById(android.R.id.list);

        // preparing list data
        prepareListData();

        listAdapter = new ExpListBddAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        for(String element : getApplicationContext().databaseList()){
            if(!element.contains("journal")){//on enlève les journaux des bdd
                listDataHeader.add(element);
                List<String> list_element=new ArrayList<>();
                list_element.add("ajouter un élément");
                list_element.add("modifier un élément");
                list_element.add("supprimer un/des élément(s)");
                list_element.add("voir les éléments");

                listDataChild.put(listDataHeader.get(listDataHeader.size()-1), list_element);
            }

        }

    }

    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String action;
        Intent intent;
        switch(childPosition){
            case 0:
                action="ajout";
                intent=new Intent(ActivityListMenuBdd.this,ActivityFormulaire.class);
                break;
            case 1:
                action="modifier";
                intent=new Intent(ActivityListMenuBdd.this,ActivityListShowBdd.class);
                break;
            case 2:
                action="supprimer";
                intent=new Intent(ActivityListMenuBdd.this,ActivityListShowBdd.class);
                break;
            case 3:
                action="voir";
                intent=new Intent(ActivityListMenuBdd.this,ActivityListShowBdd.class);
                break;
            default:
                action="";
                intent=new Intent(ActivityListMenuBdd.this,ActivityListShowBdd.class);
        }
        intent.putExtra("action",action);
        intent.putExtra("bdd", listDataHeader.get(groupPosition));


        startActivity(intent);
        return false;
    }

}
