// File RTCG2.java --- calling a static method with int argument and result
// sestoft@dina.kvl.dk * 2002

// Using the gnu.bytecode package from http://www.gnu.org/software/kawa

import gnu.bytecode.*;
import java.io.*;

public class RTCG2Generated {

  public static void main(String[] args) 
    throws IOException, NoSuchMethodException, IllegalAccessException, 
           java.lang.reflect.InvocationTargetException {

    ClassType co = genclass |[
        public class MyClass {
           public static int MyMethod(int x) {
             return x + 2;
           }
        }
    ]|;

    // Output class file in human-readable format:
    //ClassTypeWriter.print(co, System.out, 0);
    // Output class file to class file on disk:
    co.writeToFile("MyClass.class");
    // Output class file to array:
    byte[] classFile = co.writeToArray();
    // Load the class file from byte array into the JVM 
    Class ty = new ArrayClassLoader().loadClass("MyClass", classFile);
    // Get the MyMethod(int):
    java.lang.reflect.Method m = 
      ty.getMethod("MyMethod", new Class[] { int.class }); 
    // Call the method:    
    System.out.println(m.invoke(null, new Object[] { new Integer(5) }));
  }
}

// This is needed because defineClass is protected in java.lang.ClassLoader:

class ArrayClassLoader extends ClassLoader {
  public Class loadClass(String name, byte[] classFile) {
    return defineClass(name, classFile, 0, classFile.length);
  }
}
