import java.util.ArrayList;
import java.util.List;

public class LeafNode<K extends Comparable<K>, V> extends Node<K, V> {
	protected LeafNode<K,V> next, previous;
    protected ArrayList<V> values;

    public LeafNode(K key, V value) {
        setLeaf(true);
        keys = new ArrayList<>();
        values = new ArrayList<>();
        keys.add(key);
        values.add(value);
    }

    public LeafNode(ArrayList<K> keys, ArrayList<V> values) {
        setLeaf(true);
        this.keys = keys;
        this.values = values;
    }

    public void insert(K key, V value) {
        if (key.compareTo(getFirstKey()) < 0) {
            /**
             * This means key < 1st Key in keys list
             */
            keys.add(0, key);
            values.add(0, value);
        } else if (key.compareTo(getLastKey()) > 0) {
            /**
             * This means key > last Key in keys list
             */
        	keys.add(key);
            values.add(value);
        } else {
        	for(int i = 0; i < getKeysSize(); i++) {
                if (getKey(i).compareTo(key) > 0) {
                	keys.add(i, key);
                    values.add(i, value);
                    return;
                }
            }
        }

    }
    
    public V getValue(K key) {
    	for(int i = 0; i< getKeysSize(); i++) {
    		if(key.compareTo(getKey(i)) == 0) {
    			return getValue(i);
    		}
    	}
    	return null;
    }
    
    public int getValuesSize() {
    	return values.size();
    }
    
    public V getFirstValue() {
    	return getValue(0);
    }

    public V getLastValue() {
    	return getValue(getValuesSize() - 1);
    }
    
    public V getValue(int index) {
    	if(getValuesSize() == 0) {
    		return null;
    	}
    	return values.get(index);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < getKeysSize(); i++) {
            sb.append("(");
            sb.append(getKey(i));
            sb.append(":");
            sb.append(getValue(i));
            sb.append(")");

            if(i < getKeysSize() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
