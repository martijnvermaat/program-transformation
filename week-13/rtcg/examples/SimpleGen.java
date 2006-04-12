import gnu.bytecode.*;
import java.io.*;


public class SimpleGenerated {


    public static void main(String[] args) 
        throws IOException, NoSuchMethodException, IllegalAccessException, 
        java.lang.reflect.InvocationTargetException {

        ClassType co = GenSimpleClass();

        // Output class file in human-readable format:
        //ClassTypeWriter.print(co, System.out, 0);

        // Output class file to array:
        byte[] classFile = co.writeToArray();

        // Load the class file from byte array into the JVM 
        Class ty = new ArrayClassLoader().loadClass("MyClass", classFile);

        // Get the method
        java.lang.reflect.Method m = ty.getMethod("MySimple", new Class[] { });

        // Call the method:    
        System.out.println(m.invoke(null, new Object[] { }));

    }


    public static ClassType GenSimpleClass() {

        return genclass |[
            public class MyClass {
                public static int MySimple() {
                    return 38;
                }
            }
            ]|;

    }


}


// This is needed because defineClass is protected in java.lang.ClassLoader:
class ArrayClassLoader extends ClassLoader {
    public Class loadClass(String name, byte[] classFile) {
        return defineClass(name, classFile, 0, classFile.length);
    }
}
