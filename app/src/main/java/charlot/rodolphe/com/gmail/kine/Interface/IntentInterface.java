package charlot.rodolphe.com.gmail.kine.Interface;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

import charlot.rodolphe.com.gmail.kine.Activities.ActivityMenuApplication;
import charlot.rodolphe.com.gmail.kine.MyException.IntentException;

public class IntentInterface implements Serializable {
    private HashMap<String,String> content_string;
    private HashMap<String,Serializable> content_serializable;
    private String asker;
    private String receiver;

    public IntentInterface(String _asker, String _receiver){
        content_string=new HashMap<>();
        content_serializable=new HashMap<>();
        asker=_asker;
        receiver=_receiver;
    }

    public IntentInterface(Intent intent){
        IntentInterface new_intent=(IntentInterface)intent.getSerializableExtra("content");
        content_string=new_intent.content_string;
        content_serializable=new_intent.content_serializable;
        asker=new_intent.asker;
        receiver=new_intent.receiver;
    }

    public void initiate(){
        String activityName=getActivityByName(receiver).getLocalClassName().replace("Activities.","");
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
        Intent intent=new Intent(getActivityByName(asker),getActivityByName(receiver).getClass());
        intent.putExtra("content",this);
        getActivityByName(asker).startActivity(intent);
    }

    public void putString(String nom, String valeur){
        content_string.put(nom, valeur);
    }

    public String getString(String key) throws IntentException {
        if(content_string.containsKey(key)){
            return content_string.get(key);
        }
        else{
            throw new IntentException(this);
        }
    }

    public void putSerializable(String key, Serializable value){
        content_serializable.put(key,value);
    }

    public Serializable getSerializable(String key) throws IntentException {
        if(content_serializable.containsKey(key)){
            return content_serializable.get(key);
        }
        else{
            throw new IntentException(this);
        }
    }
    public void swapActivitiesAndReplaceWith(Activity new_receiver){
        asker=receiver;
        receiver=new_receiver.getLocalClassName();
    }

    public Activity getAsker() {
        return getActivityByName(asker);
    }

    public Activity getReceiver() {
        return getActivityByName(receiver);
    }

    public Activity getActivityByName(String activityName){
        Activity res=null;
        activityName=activityName.replace("Activities.","");
        switch(activityName){
            case "ActivityMenuApplication":
                res=new ActivityMenuApplication();
                break;
            case "ActivityFormulaire":
                break;
            default:
                Log.v("intentInterface","il n'a pas de get ici");
                Log.v("intentInterface", activityName);
        }
        return res;
    }
}
