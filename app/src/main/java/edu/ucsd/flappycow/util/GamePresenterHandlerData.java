package edu.ucsd.flappycow.util;
public class GamePresenterHandlerData {

    public String Demand;
    public Object[] varargs;

    public GamePresenterHandlerData(String Demand, Object... varargs) {
        this.Demand = Demand;
        this.varargs = varargs;
    }


}
