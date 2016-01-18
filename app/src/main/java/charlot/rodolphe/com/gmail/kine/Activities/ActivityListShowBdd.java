package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import charlot.rodolphe.com.gmail.kine.SuperClass.BddClass;
import charlot.rodolphe.com.gmail.kine.SuperClass.BddInterface;
import charlot.rodolphe.com.gmail.kine.SuperClass.ElementInterface;
import charlot.rodolphe.com.gmail.kine.MyException.BddException;
import charlot.rodolphe.com.gmail.kine.R;

public class ActivityListShowBdd extends ListActivity {
    private ArrayAdapter<String> bdd_adapter;
    private ArrayList<ElementInterface> tab_element;
    private ArrayList<String> tab_text;
    private BddInterface mybdd;
    private String otherAction;
    private String action;
    private String bdd;
    private static final int Menu_suppression= Menu.FIRST+1;
    private static final int Menu_modifier=Menu.FIRST+2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_text=new ArrayList<>();
        tab_element=new ArrayList<>();
        setContentView(R.layout.list_simple);
        ListView list = (ListView) findViewById(android.R.id.list);
        registerForContextMenu(list);

        Intent intent = getIntent();
        if (intent != null) {

            action=intent.getStringExtra("action");
            bdd=intent.getStringExtra("bdd");

            getAction(action);
            getBdd(bdd);
        }
        bdd_adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mysimple_list_item,tab_text);
        setListAdapter(bdd_adapter);
    }

    public void getAction(String _action){
        otherAction="";
        action="";
        switch (_action){
            case "ajout":
                break;
            case "modifier":
                action=_action;
                otherAction="supprimer";
                break;
            case "supprimer":
                action=_action;
                otherAction="modifier";
                break;
            case "voir":
                break;
        }
    }

    public void getBdd(String bdd_string){

        BddClass bdd=new BddClass();

        BddInterface mybdd=bdd.getBdd(bdd_string);

        if (mybdd != null) {
            mybdd.open();
            try {
                ElementInterface tab_temp[]=mybdd.getAll();
                for(ElementInterface el:tab_temp){
                    tab_element.add(el);
                }
                for(ElementInterface element:tab_element){
                    try {
                        tab_text.add(element.toText());
                    } catch (BddException.BddNoElementException e) {
                        e.printStackTrace();
                    }
                }
            } catch (BddException.BddNoElementException e) {
                Toast.makeText(getApplicationContext(),"La base de donnée ne contient pas d'élément",Toast.LENGTH_LONG).show();
            }
            mybdd.close();
        }
        else {
            onBackPressed();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(action.equalsIgnoreCase("supprimer")){
            removeItem(position);
        }
        else if(action.equalsIgnoreCase("modifier")){
            startModifier(position);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        if(otherAction.equalsIgnoreCase("modifier")){
            menu.add(Menu.NONE, Menu_modifier, Menu.NONE, "Modifier");
        }
        else if(otherAction.equalsIgnoreCase("supprimer")){
            menu.add(Menu.NONE, Menu_suppression, Menu.NONE, "Supprimer");
        }
    }


    public void removeItem(int position){
        String element=bdd_adapter.getItem(position);
        ElementInterface myelement=tab_element.get(position);
        bdd_adapter.remove(element);
        tab_element.remove(myelement);
        mybdd.open();
        try {
            mybdd.deleteWithId(myelement.getId());
        } catch (BddException.BddNoElementException e) {
            Toast.makeText(getApplicationContext(),"L'élément n'existe pas",Toast.LENGTH_LONG).show();
        }
        mybdd.close();
        bdd_adapter.notifyDataSetChanged();
    }

    public void startModifier(int position){
        Intent intent=new Intent(ActivityListShowBdd.this,ActivityFormulaire.class);
        intent.putExtra("element",tab_element.get(position));
        intent.putExtra("action",action);
        intent.putExtra("bdd",bdd);
        startActivity(intent);

    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case Menu_suppression:
                removeItem(info.position);
                break;

            case Menu_modifier:
                startModifier(info.position);
                break;
        }
        return (super.onContextItemSelected(item));
    }

    public void onBackPressed(){
        Intent intent=new Intent(ActivityListShowBdd.this,ActivityListMenuBdd.class);
        startActivity(intent);
    }

}
