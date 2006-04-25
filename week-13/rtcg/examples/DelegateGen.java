// Creation of delegates from reified static and instance methods in Java
// sestoft@dina.kvl.dk * 2002-05-08, 2006-03-10

// Requires the gnu.bytecode package from http://www.gnu.org/software/kawa

/* todo: Make DelegateGen.java work. */

import gnu.bytecode.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/* (1) Creating a delegate from a static method m of class C, with
   return type R and parameter types T1,...,Tn, represented by method
   object mo.  The call createDelegate(I.class, mo) returns an object
   dlg that can be cast to interface I, and so that a call to
   dlg.call(...) will call the method as if by C.m(...).

   This works by constructing, at runtime, a class Dlg that implements
   interface I and contains a method `call' that calls the given
   method, as if written according to this schema:
    
      public class Dlg implements I extends Object {
        public Dlg() { super(); }

        public R call(T1 p1, ..., Tn pn) {
          return C.m(p1, ..., pn);  
        }
      } 

   The new class Dlg is loaded into the JVM, and an instance of the
   class is created and returned.

   (2) Creating a delegate from an instance method m of class C, with
   return type R and parameter types T1,...,Tn, represented by method
   object mo.  The call createDelegate(I.class, mo, o) returns an
   object dlg that can be cast to interface I, and so that a call to
   dlg.call(...) will call the method as if by o.m(...).

   This works as above, except that the constructed class looks like
   this:
    
      public class Dlg implements I extends Object { 
        private C obj;

        public Dlg() { super(); }

        public void setObj(Object obj) { 
          this.obj = (C)obj; 
        }

        public R call(T1 p1, ..., Tn pn) {
          return obj.m(p1, ..., pn);  
        }
      } 

   The new class Dlg is loaded into the JVM, an instance dlg of the
   class is created, the dlg.setObj method is called on the given
   object o, and dlg is returned.

   TODO: Reuse a previously generated MyDelegate class when creating a
   new delegate for an interface I and method M for which a class was
   previously generated.  This can be done with a HashMap from Pair of
   Class and Method to Class.  */

class DelegateGenerated {
  private static int delegateNumber = 0;
  private static ArrayClassLoader classLoader = new ArrayClassLoader();

  public static Object createDelegate(Class intrface, 
                                      java.lang.reflect.Method method) {
    return createDelegate(intrface, method, null);
  }

  public static synchronized Object 
    createDelegate(Class intrface, java.lang.reflect.Method method, Object obj)
  {
    // Check that intrface is a public interface with a single call method
    if (!(intrface.isInterface() 
          && Modifier.isPublic(intrface.getModifiers())))
      throw new Error("Delegate type must be a public interface");
    java.lang.reflect.Method[] intrfaceMethods = intrface.getMethods();
    if (!(intrfaceMethods.length == 1 
          && intrfaceMethods[0].getName().equals("call"))) 
      throw new Error("Interface must describe a single call method");
    java.lang.reflect.Method call = intrfaceMethods[0];

    // Check that intrface and method agree on return and parameter types:
    Class returnType = call.getReturnType();
    if (returnType != method.getReturnType())
      throw new Error("Wrong call() return type: " + returnType);
    Class[] parameterTypes = call.getParameterTypes();
    if (!equalClasses(parameterTypes, method.getParameterTypes()))
      throw new Error("Wrong call() parameter types");
    
    // Check that object is given iff method is instance method:
    boolean isStatic = Modifier.isStatic(method.getModifiers());
    if (isStatic) {
      if (obj != null)
        throw new Error("Delegate to static method needs no object");
    } else 
      if (obj == null)
        throw new Error("Delegate to instance method needs an object");

    // Check that the method and the declaring class are public:
    Class methodClass = method.getDeclaringClass();
    if (!(Modifier.isPublic(methodClass.getModifiers())
          && Modifier.isPublic(method.getModifiers())))
      throw new Error("Method and its class must be public");

    ClassType methodClassType = (ClassType)Type.make(methodClass);

    String dlgClassName = "MyDelegate" + delegateNumber++;

    ClassType dlgClass = new ClassType(dlgClassName);
    dlgClass.setSuper("java.lang.Object");
    dlgClass.addInterface((ClassType)Type.make(intrface));
    dlgClass.setModifiers(Access.PUBLIC);
    {
      // Build constructor: public MyDelegate117() { super(); }
      gnu.bytecode.Method initMethod = 
        dlgClass.addMethod("<init>", new Type[] {}, Type.void_type, 0);
      initMethod.setModifiers(Access.PUBLIC);
      initMethod.initCode();
      CodeAttr jvmg = initMethod.getCode();
      Scope scope = initMethod.pushScope();
      Variable thisVar = scope.addVariable(jvmg, dlgClass, "this");
      jvmg.emitLoad(thisVar);
      jvmg.emitInvokeSpecial(ClassType.make("java.lang.Object")
                                      .getMethod("<init>", new Type[] {}));
      initMethod.popScope();
      jvmg.emitReturn();
    }
    gnu.bytecode.Field objField = null;
    if (!isStatic) {
      objField = dlgClass.addField("obj", methodClassType);
      // Build method: public void setObj(Object o) { this.obj = (<class>)o; }
      gnu.bytecode.Method setObjMethod = 
        dlgClass.addMethod("setObj", new Type[] { Type.make(Object.class) }, 
                           Type.void_type, 0);
      setObjMethod.setModifiers(Access.PUBLIC);
      setObjMethod.initCode();
      CodeAttr jvmg = setObjMethod.getCode();
      Scope scope = setObjMethod.pushScope();
      Variable thisVar = scope.addVariable(jvmg, dlgClass, "this");
      Variable objVar = scope.addVariable(jvmg, dlgClass, "obj");
      jvmg.emitLoad(thisVar);
      jvmg.emitLoad(objVar);
      jvmg.emitCheckcast(methodClassType);
      jvmg.emitPutField(objField);
      setObjMethod.popScope();
      jvmg.emitReturn();
    }
    {
      // Build: public static <returnType> call(<parameterTypes>) { ... }
      String signature = signature(parameterTypes, returnType);
      gnu.bytecode.Method dlgMethod = dlgClass.addMethod("call");
      dlgMethod.setSignature(signature);
      dlgMethod.setModifiers(Access.PUBLIC);
      dlgMethod.initCode();
      CodeAttr jvmg = dlgMethod.getCode();
      Scope scope = dlgMethod.pushScope();
      Variable thisVar = scope.addVariable(jvmg, dlgClass, "this");
      if (!isStatic) {
        jvmg.emitLoad(thisVar);
        jvmg.emitGetField(objField);
      }
      // Push arguments
      for (int i=0; i<parameterTypes.length; i++) {
        Variable xi = 
          scope.addVariable(jvmg, Type.make(parameterTypes[i]), "x" + i); 
        jvmg.emitLoad(xi);
      }
      // Call method in class methodClass:
      gnu.bytecode.Method realMethod = 
        methodClassType.getMethod(method.getName(), types(parameterTypes));
      if (isStatic) 
        jvmg.emitInvokeStatic(realMethod);
      else
        jvmg.emitInvokeVirtual(realMethod);
      dlgMethod.popScope();
      jvmg.emitReturn();
    }
    byte[] classBytes;
    // Change "false" to "true" for debugging or to satisfy your curiosity:
    if (false) { 
      try {
        ClassTypeWriter.print(dlgClass, System.out, 0); // Pretty-print class
        dlgClass.writeToFile(dlgClassName + ".class");  // Save class file
      } catch (java.io.IOException exn) {
        throw new Error("Could not write delegate class");
      }
    }
    classBytes = dlgClass.writeToArray();               // Class bytearray
    // Load the class file from byte array into the JVM :
    Class newClass = classLoader.loadClass(dlgClassName, classBytes);
    try {
      // Create an instance of the newly loaded class:
      Object dlg = newClass.newInstance();
      if (!isStatic)
        newClass.getMethod("setObj", new Class[] { Object.class })
                .invoke(dlg, new Object[] { obj });
      return dlg;
    } catch (InstantiationException exn) {
      throw new Error("Delegate creation error: " + exn); 
    } catch (IllegalAccessException exn) {
      throw new Error("Delegate access error: " + exn); 
    } catch (NoSuchMethodException exn) {
      throw new Error("Delegate.createDelegate internal error: " + exn); 
    } catch (InvocationTargetException exn) {
      throw new Error("Could not create delegate from instance: " + exn); 
    }
  }

  // Compare two arrays of Class objects for equality

  private static boolean equalClasses(Class[] cs1, Class[] cs2) {
    if (cs1.length != cs2.length)
      return false;
    for (int i=0; i<cs1.length; i++)
      if (!cs1[i].equals(cs2[i]))
        return false;
    return true;
  }

  // Build type signature for method, such as "(Ljava.lang.String;I)I":

  private static String signature(Class[] parameterTypes, Class returnType) {
    StringBuffer sb = new StringBuffer();
    sb.append("(");
    for (int i=0; i<parameterTypes.length; i++)
      sb.append(Type.make(parameterTypes[i]).getSignature());
    sb.append(")");
    sb.append(Type.make(returnType).getSignature());
    return sb.toString();
  }

  // Convert parameterTypes from java.lang.Class[] to gnu.bytecode.Type[]:

  private static Type[] types(Class[] parameterTypes) {
    Type[] res = new Type[parameterTypes.length];
    for (int i=0; i<parameterTypes.length; i++)
      res[i] = Type.make(parameterTypes[i]);
    return res;
  }

  // Needed because defineClass is protected in java.lang.ClassLoader:
  
  private static class ArrayClassLoader extends ClassLoader {
    public Class loadClass(String name, byte[] classBytes) {
      return defineClass(name, classBytes, 0, classBytes.length);
    }
  }

  public static class Error extends java.lang.Error { 
    public Error(String str) {
      super(str);
    }
  }
}
