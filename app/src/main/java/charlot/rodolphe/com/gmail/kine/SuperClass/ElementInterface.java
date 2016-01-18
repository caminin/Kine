package charlot.rodolphe.com.gmail.kine.SuperClass;


import java.io.Serializable;

import charlot.rodolphe.com.gmail.kine.MyException.BddException;

public interface ElementInterface extends Serializable{
    String toText() throws BddException.BddNoElementException;
    int getId();

}
