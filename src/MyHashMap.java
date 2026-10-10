
@SuppressWarnings("unchecked")
public class MyHashMap<K, V> {

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public MyHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 1) initialCapacity = 1;
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor");
        }

        this.loadFactor = loadFactor;

        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.table = (Node<K, V>[]) new Node[capacity];
        this.threshold = (int) (capacity * loadFactor);
    }

    private int hash(Object key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int index(int hash, int length) {
        return (length - 1) & hash;
    }

    public V put(K key, V value) {
        int hash = hash(key);
        int index = index(hash, table.length);

        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == hash &&
                    (current.key == key || (key != null && key.equals(current.key)))) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        addNode(hash, key, value, index);
        return null;
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;

        if (++size > threshold) {
            resize();
        }
    }

    public V get(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);

        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == hash &&
                    (current.key == key || (key != null && key.equals(current.key)))) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public V remove(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);

        Node<K, V> prev = null;
        Node<K, V> current = table[index];

        while (current != null) {
            if (current.hash == hash &&
                    (current.key == key || (key != null && key.equals(current.key)))) {

                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }

                size--;
                return current.value;
            }

            prev = current;
            current = current.next;
        }

        return null;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;

        if (newCapacity <= 0) {
            throw new IllegalStateException("HashMap слишком большой");
        }

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * loadFactor);

        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> node = oldTable[i];

            if (node != null) {
                oldTable[i] = null;

                if (node.next == null) {
                    newTable[index(node.hash, newCapacity)] = node;
                } else {
                    Node<K, V> loHead = null, loTail = null;
                    Node<K, V> hiHead = null, hiTail = null;
                    Node<K, V> next;

                    do {
                        next = node.next;

                        if ((node.hash & oldCapacity) == 0) {
                            if (loTail == null) loHead = node;
                            else loTail.next = node;
                            loTail = node;
                        } else {
                            if (hiTail == null) hiHead = node;
                            else hiTail.next = node;
                            hiTail = node;
                        }

                        node = next;
                    } while (node != null);

                    if (loTail != null) {
                        loTail.next = null;
                        newTable[i] = loHead;
                    }

                    if (hiTail != null) {
                        hiTail.next = null;
                        newTable[i + oldCapacity] = hiHead;
                    }
                }
            }
        }

        table = newTable;
    }

    public int size() {
        return size;
    }
}