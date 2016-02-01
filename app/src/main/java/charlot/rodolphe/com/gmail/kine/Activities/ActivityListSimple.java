package charlot.rodolphe.com.gmail.kine.Activities;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import charlot.rodolphe.com.gmail.kine.R;

public class ActivityListSimple extends ListActivity {
    private ArrayAdapter<String> bdd_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tab_string[]=getIntent().getStringArrayExtra("content");

        bdd_adapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.mysimple_list_item,tab_string);
        setListAdapter(bdd_adapter);
    }
}
