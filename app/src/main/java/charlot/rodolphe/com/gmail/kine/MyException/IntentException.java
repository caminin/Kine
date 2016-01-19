package charlot.rodolphe.com.gmail.kine.MyException;

import charlot.rodolphe.com.gmail.kine.Interface.IntentInterface;

public class IntentException extends Exception {
    public IntentException(IntentInterface intent) {
        super("Erreur entre les activités "+intent.getAsker().getLocalClassName()+" et "+intent.getReceiver().getLocalClassName());

    }
}
