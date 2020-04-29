import javax.swing.*;

public class Lab5 {

    private static Lab5Controller lab5Controller;

    public static void main(String args[]){

        int setupFlag = Integer.parseInt(args[0]);
        lab5Controller = new Lab5Controller(setupFlag);

    }

}