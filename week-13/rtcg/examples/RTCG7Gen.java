// File RTCG7.java --- measure time to generate (much) code
// sestoft@dina.kvl.dk * 2002-09

// Using the gnu.bytecode package from http://www.gnu.org/software/kawa

import gnu.bytecode.*;
import java.io.*;

public class RTCG7Generated {
  public static void main(String[] args) 
    throws IOException, NoSuchMethodException, IllegalAccessException, 
           java.lang.reflect.InvocationTargetException {
    int count = Integer.parseInt(args[0]);
    int calls = Integer.parseInt(args[1]);

    Timer t = new Timer();

    ClassType co = genclass |[
        public class MyClass {
            public static void MyMethod1(int x) {
                /* I don't see a more direct way to translate the
                   code to concrete syntax. Embedding expressions
                   would not help either. */
                #genbstms |[
                    for (int i=count; i>0; i--) {
                        genbstms |[ x = x + x; ]|;
                    }
                ]|;
            }
        }
        ]|;

    // Output class file in human-readable format:
    //ClassTypeWriter.print(co, System.out, 0);

    // Output class file to array:
    byte[] classFile = co.writeToArray();
    // Load the class file into the JVM 
    Class ty = new ArrayClassLoader().loadClass("MyClass", classFile);
    {
        java.lang.reflect.Method m = 
            ty.getMethod("MyMethod1", new Class[] { int.class }); 
        for (int i=calls; i>0; i--) {
            m.invoke(null, new Object[] { new Integer(count) });
        }
        System.out.println("Generating " + 6*count + " instructions and making " 
                           + calls + " calls: " + t.Check() + " sec");
    }
  }
}

// This is needed because defineClass is protected in java.lang.ClassLoader:

class ArrayClassLoader extends ClassLoader {
  public Class loadClass(String name, byte[] classFile) {
    return defineClass(name, classFile, 0, classFile.length);
  }
}

// Crude timing utility ----------------------------------------
   
class Timer {
  private long start;

  public Timer() {
    start = System.currentTimeMillis();
  }

  public double Check() {
    return (System.currentTimeMillis()-start)/1000.0;
  }
}
