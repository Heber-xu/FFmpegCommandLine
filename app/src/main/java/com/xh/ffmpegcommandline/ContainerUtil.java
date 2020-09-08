package com.xh.ffmpegcommandline;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * 容器util
 * <p>
 * Created by xuhang on 2019/4/3.
 */
public class ContainerUtil {

    private ContainerUtil() {

    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isEmpty(Queue queue) {
        return queue == null || queue.isEmpty();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static <T> String toString(Collection<T> c) {
        if (c == null) {
            return null;
        }
        if (c.isEmpty()) {
            return "[]";
        }
        int index = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (T t :
                c) {
            sb.append(t.toString());
            index++;
            if (index == c.size()) {
                sb.append("]");
                break;
            }
            sb.append(",");
        }
        return sb.toString();
    }

    public static <K, V> String toString(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return "[]";
        }
        int index = 0;
        Set<K> keySet = map.keySet();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (K k :
                keySet) {
            V v = map.get(k);
            sb.append("{");
            sb.append("Key:");
            sb.append(k.toString());
            sb.append(",");
            sb.append("Value:");
            sb.append(v.toString());
            sb.append("}");
            index++;
            if (index == map.size()) {
                sb.append("]");
                break;
            }
            sb.append(",");
        }
        return sb.toString();
    }

    public static <T> String toString(T[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "[]";
        }
        int index = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (T t :
                array) {
            sb.append("{");
            sb.append(t.toString());
            sb.append("}");
            index++;
            if (index == array.length) {
                sb.append("]");
                break;
            }
            sb.append(",");
        }
        return sb.toString();
    }

    public static <T> T[] listToArray(Class<T> type, List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
//        T[] array = (T[]) new Object[list.size()];//这种方法是不行的
        T[] array = (T[]) Array.newInstance(type, list.size());
        return list.toArray(array);
    }

}
