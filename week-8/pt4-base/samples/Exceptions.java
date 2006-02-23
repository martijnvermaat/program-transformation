import java.io.IOException;
import java.util.NoSuchElementException;

public class Exceptions
{
  public void thrower() throws IOException, Exception, IllegalArgumentException
  {
  }

  public void f() throws Exception
  {
    thrower();
  }

  public void g() throws Exception
  {
    try
    {
      thrower();
    }
    catch(IllegalArgumentException exc)  {}
  }

  public void h() throws Exception
  {
    try
    {
      thrower();
    }
    catch(RuntimeException exc)  {}
  }

  public void i() throws Exception
  {
    try
    {
      thrower();
    }
    catch(Exception exc)  {}
  }

  public void j() throws IOException
  {
    throw new IOException();
  }

  public void k() 
  {
    class Foo
    {
      void innerF()
      {
        throw new NoSuchElementException();
      }
    }

    throw new NullPointerException();
  }
}
