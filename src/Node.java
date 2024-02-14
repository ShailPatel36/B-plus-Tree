import java.util.ArrayList;

public class Node<K extends Comparable<K>, V> {

    private boolean isLeaf;
    protected ArrayList<K> keys;

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    public boolean isUnderFlow() {
        return getKeysSize() < BPlusTree.ORDER;
    }
    
    public boolean isOverFlow() {
        return getKeysSize() > BPlusTree.ORDER * 2;
    }

    public int getKeysSize() {
    	return keys.size();
    }
    
    public K getFirstKey() {
    	return getKey(0);
    }
    
    public K getLastKey() {
    	return getKey(getKeysSize() - 1);
    }
    
    public K getKey(int index) {
    	if(getKeysSize() == 0) {
    		return null;
    	}
    	return keys.get(index);
    }

}
