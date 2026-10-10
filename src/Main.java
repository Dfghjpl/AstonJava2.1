
void main() {
    testBasicPutGet();
    testUpdateValue();
    testRemove();
    testNullKey();
    testCollisions();
    testResize();
    testRemoveFromMiddle();
    testSizeAfterOperations();
}
// ---------- 1. Базовая вставка и чтение ----------
static void testBasicPutGet() {
    System.out.println("=== 1. Базовый put/get ===");
    MyHashMap<String, Integer> map = new MyHashMap<>();

    map.put("apple", 100);
    map.put("banana", 200);
    map.put("cherry", 300);

    System.out.println("apple  = " + map.get("apple"));
    System.out.println("banana = " + map.get("banana"));
    System.out.println("cherry = " + map.get("cherry"));
    System.out.println("mango  = " + map.get("mango"));
    System.out.println("size   = " + map.size());
    System.out.println();
}

// ---------- 2. Перезапись значения по существующему ключу ----------
static void testUpdateValue() {
    System.out.println("=== 2. Обновление значения ===");
    MyHashMap<String, Integer> map = new MyHashMap<>();

    Integer old = map.put("apple", 100);
    System.out.println("put apple=100  вернулось: " + old);

    old = map.put("apple", 999);
    System.out.println("put apple=999  вернулось: " + old);
    System.out.println("get apple    = " + map.get("apple"));
    System.out.println("size         = " + map.size());
    System.out.println();
}

// ---------- 3. Удаление ----------
static void testRemove() {
    System.out.println("=== 3. Удаление ===");
    MyHashMap<String, Integer> map = new MyHashMap<>();

    map.put("apple", 100);
    map.put("banana", 200);
    map.put("cherry", 300);

    Integer removed = map.remove("banana");
    System.out.println("remove banana  вернулось: " + removed);
    System.out.println("get banana    = " + map.get("banana"));
    System.out.println("size          = " + map.size());

    removed = map.remove("banana");
    System.out.println("remove banana (снова)  " + removed);
    System.out.println();
}

// ---------- 4. null-ключ ----------
static void testNullKey() {
    System.out.println("=== 4. null-ключ ===");
    MyHashMap<String, Integer> map = new MyHashMap<>();

    map.put(null, 42);
    map.put("apple", 100);

    System.out.println("get(null) = " + map.get(null));
    System.out.println("size      = " + map.size());

    map.remove(null);
    System.out.println("после remove(null): get(null) = " + map.get(null));
    System.out.println("size = " + map.size());
    System.out.println();
}

// ---------- 5. Коллизии ----------

static void testCollisions() {
    System.out.println("=== 5. Коллизии ===");
    MyHashMap<BadKey, String> map = new MyHashMap<>();

    BadKey k1 = new BadKey("one");
    BadKey k2 = new BadKey("two");
    BadKey k3 = new BadKey("three");

    map.put(k1, "первый");
    map.put(k2, "второй");
    map.put(k3, "третий");

    System.out.println("get(k1) = " + map.get(k1));
    System.out.println("get(k2) = " + map.get(k2));
    System.out.println("get(k3) = " + map.get(k3));
    System.out.println("size    = " + map.size());

    // Удаляем средний — проверим, что цепочка правильно перецепилась
    map.remove(k2);
    System.out.println("после remove(k2): get(k2) = " + map.get(k2));
    System.out.println("но get(k1) = " + map.get(k1));
    System.out.println("и   get(k3) = " + map.get(k3));
    System.out.println();
}

// ---------- 6. Resize ----------
static void testResize() {
    System.out.println("=== 6. Resize ===");
    MyHashMap<Integer, Integer> map = new MyHashMap<>();


    for (int i = 0; i < 20; i++) {
        map.put(i, i * 10);
    }

    System.out.println("size = " + map.size());
    // Проверим, что ВСЕ элементы на месте после resize
    boolean ok = true;
    for (int i = 0; i < 20; i++) {
        if (map.get(i) != i * 10) {
            ok = false;
            System.out.println("ПОТЕРЯН ключ: " + i);
        }
    }
    System.out.println(ok ? "все 20 элементов на месте" : "ОШИБКА: элементы потерялись");
    System.out.println();
}

// ---------- 7. Удаление из середины цепочки ----------
static void testRemoveFromMiddle() {
    System.out.println("=== 7. Удаление из середины цепочки ===");
    MyHashMap<BadKey, Integer> map = new MyHashMap<>();

    BadKey a = new BadKey("a");
    BadKey b = new BadKey("b");
    BadKey c = new BadKey("c");


    map.put(a, 1);
    map.put(b, 2);
    map.put(c, 3);

    map.remove(b);   // удаляем из середины

    System.out.println("get(a) = " + map.get(a));
    System.out.println("get(b) = " + map.get(b));
    System.out.println("get(c) = " + map.get(c));
    System.out.println("size   = " + map.size());
    System.out.println();
}

// ---------- 8. size() после всех операций ----------
static void testSizeAfterOperations() {
    System.out.println("=== 8. size() ===");
    MyHashMap<String, Integer> map = new MyHashMap<>();

    map.put("a", 1); map.put("b", 2); map.put("c", 3);
    System.out.println("после 3 put   size = " + map.size());

    map.put("a", 100);
    System.out.println("после update  size = " + map.size());

    map.remove("b");
    System.out.println("после remove  size = " + map.size());

    map.remove("нет-такого");
    System.out.println("после remove отсутствующего  size = " + map.size());
    System.out.println();
}


// Вспомогательный ключ с постоянным hashCode
static class BadKey {
    final String name;

    BadKey(String name) { this.name = name; }

    @Override
    public int hashCode() {
        return 42;   // ВСЕГДА одинаковый → все в один бакет
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BadKey)) return false;
        return name.equals(((BadKey) o).name);
    }

    @Override
    public String toString() { return "BadKey(" + name + ")"; }
}