package wind.ibroker.comm;

public class DataValid {
    public static  boolean isInt(String value)
    {
        try
        {
            Integer.parseInt(value);
        }catch(Exception ex)
        {
            return false;
        }
        return true;
    }
}
