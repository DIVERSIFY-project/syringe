package fr.inria.testproject;

/**
 * Created by marcel on 23/02/14.
 *
 * A class to test some coverage. In some method an "explosive" line is introduced
 * which will not be tested.
 *
*/
public class Arithmetic {

    //A dummy Add procedure to test some logic branches
    public int addConditional(int a , int b) {
        if ( b % 2 == 0 ) {
            return a + b;
        }
        else  {
            for ( int i = 0; i < 10; i ++ ) {
                a += i;
            }
            return a + b * 2;
        }


    }

    //Yet another dummy procedure to test some logic branches
    public int subConditional(int a , int b) {
        if ( b % 2 == 0 ) {
            return a - b;
        }
        else if ( b % 3 == 0 ) {
            return b - a;
        }
        else {
            int i = 0;
            while ( i < 10 ) {
                i++;
            }
            return a * i - b * 2;
        }
    }

    //All lines will be tested in this method
    public int fullCoverage(int a) {
        a = a + a / 2;
        return 10 * a;
    }

}
