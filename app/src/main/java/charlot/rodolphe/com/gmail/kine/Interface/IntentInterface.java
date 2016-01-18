package charlot.rodolphe.com.gmail.kine.Interface;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

public class IntentInterface implements Serializable {
    private HashMap<String,String> content;
    private Activity asker;
    private Activity receiver;

    public IntentInterface(Activity _asker, Activity _receiver){
        content=new HashMap<>();
        asker=_asker;
        receiver=_receiver;
    }

    private void initiate(){
        String activityName=receiver.getLocalClassName().replace("Activities.","");
        switch(activityName){
            case "ActivityMenuApplication":
                break;
            case "ActivityFormulaire":
                break;
            default:
                Log.v("intentInterface","il n'a pas de get ici");
                Log.v("intentInterface", activityName);
        }
    }

    public void startIntent(){
        initiate();
        Intent intent=new Intent(asker,receiver.getClass());
        intent.putExtra("content",content);
        asker.startActivity(intent);
    }

    public void putString(String nom, String valeur){
        content.put(nom, valeur);
    }

    public void swapActivitiesAndReplaceWith(Activity new_receiver){
        asker=receiver;
        receiver=new_receiver;
    }
}
