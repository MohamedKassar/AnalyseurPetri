package ap.javasource.rdp.analyses;

/**
 *
 * @author Tarek
 */
public class CostumInteger{

    private int value;
    
    public CostumInteger(int initialValue) {
        value=initialValue;
    }

    public int get() {
        return value;
    }

    public void set(int newValue) {
        value=newValue; 
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CostumInteger) {
            CostumInteger temp = (CostumInteger) obj;
            return get() == temp.get();
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.value;
        return hash;
    }
}
