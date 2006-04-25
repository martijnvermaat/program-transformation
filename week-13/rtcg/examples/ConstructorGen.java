import gnu.bytecode.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;


public class ConstructorGenerated {


    public static void main(String[] args) 
        throws IOException, NoSuchMethodException, IllegalAccessException, 
        java.lang.reflect.InvocationTargetException {

        ClassType co = GenSimpleClass();

        // Output class file in human-readable format:
        //ClassTypeWriter.print(co, System.out, 0);

        co.writeToFile("MyClass.class");

        // Output class file to array:
        byte[] classFile = co.writeToArray();

        // Load the class file from byte array into the JVM 
        Class ty = new ArrayClassLoader().loadClass("MyClass", classFile);

        // Create an instance of the newly loaded class:
        try {
            Object o = ty.newInstance();
        } catch (InstantiationException exn) {
            throw new Error("Delegate creation error: " + exn); 
        } catch (IllegalAccessException exn) {
            throw new Error("Delegate access error: " + exn); 
        }

    }


    public static ClassType GenSimpleClass() {

        return genclass |[
            public class MyClass {
                public MyClass() {
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
