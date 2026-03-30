package com.pao.laboratory06.exercise2;

import com.pao.test.IOTest;

public class Test {
    public static void main(String[] args) {
        IOTest.runParts("/C:/Users/Vlad/Desktop/school_maybe/PAO/src/com/pao/laboratory06/exercise2/tests", Main::main);
        IOTest.runPart("/C:/Users/Vlad/Desktop/school_maybe/PAO/src/com/pao/laboratory06/exercise2/tests", "partA", Main::main);
        IOTest.runPart("/C:/Users/Vlad/Desktop/school_maybe/PAO/src/com/pao/laboratory06/exercise2/tests", "partB", Main::main);
        IOTest.runPart("/C:/Users/Vlad/Desktop/school_maybe/PAO/src/com/pao/laboratory06/exercise2/tests", "partC", Main::main);
    }
}
