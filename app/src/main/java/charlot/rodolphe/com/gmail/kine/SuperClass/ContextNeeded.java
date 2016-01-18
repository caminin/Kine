package charlot.rodolphe.com.gmail.kine.SuperClass;

import android.content.Context;

public class ContextNeeded {
    protected static Context context;

    public static void setContext(Context _context){
        context=_context;
    }

    public static Context getContext(){
        return context;
    }
}
