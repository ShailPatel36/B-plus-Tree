public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
    	setKey(key);
    	setValue(value);
    }
    
    public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String toString() {
        return "<" + key.toString() + ", " + value.toString() + ">";
    }
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair)) {
			return false;
		}
		
		Pair<K, V> other = (Pair<K, V>) obj; 
		
		return getKey().equals(other.getKey()) && getValue().equals(other.getValue()); 
	}
	
}
