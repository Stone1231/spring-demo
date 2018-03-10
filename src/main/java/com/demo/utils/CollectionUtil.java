package com.demo.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class CollectionUtil {

	private CollectionUtil() {
	}

	// 20120319
	public static <E> boolean isEmpty(Collection<E> values) {
		return (values == null || values.isEmpty());
	}

	public static <E> boolean isNotEmpty(Collection<E> values) {
		return (values != null && !values.isEmpty());
	}

	public static <K, V> boolean isEmpty(Map<K, V> values) {
		return (values == null || values.isEmpty());
	}

	public static <K, V> boolean isNotEmpty(Map<K, V> values) {
		return (values != null && !values.isEmpty());
	}

	public static <E> int size(Collection<E> values) {
		int result = 0;
		if (isNotEmpty(values)) {
			result = values.size();
		}
		return result;
	}

	public static <K, V> int size(Map<K, V> values) {
		int result = 0;
		if (isNotEmpty(values)) {
			result = values.size();
		}
		return result;
	}

	public static <E> boolean notEmpty(Collection<E> values) {
		return (values != null && !values.isEmpty());
	}

	public static <K, V> boolean notEmpty(Map<K, V> values) {
		return (values != null && !values.isEmpty());
	}

	/**
	 * 過濾null,empty,blank
	 *
	 * @param receivers
	 * @return
	 */
	public static List<String> safeGet(List<String> receivers) {
		List<String> result = new LinkedList<>();
		if (CollectionUtil.isNotEmpty(receivers)) {
			for (String receive : receivers) {
				if (StringUtil.isNotBlank(receive)) {
					result.add(receive);
				}
			}
		}
		return result;
	}

	/**
	 * 當size=100w時,用此方式取Queue的element會明顯變慢
	 *
	 * @param queue
	 * @param index
	 * @return
	 */
	public static <E> E get(Queue<E> queue, int index) {
		E result = null;
		int i = 0;
		for (E e : queue) {
			if (i == index) {
				result = e;
				break;
			}
			i++;
		}
		return result;
	}

	/**
	 * 取得map的最後一個entry
	 *
	 * @param values
	 * @return
	 */
	public static <K, V> Map.Entry<K, V> getLastEntry(Map<K, V> values) {
		Map.Entry<K, V> result = null;
		//
		if (isNotEmpty(values)) {
			int size = values.size();
			@SuppressWarnings("unchecked")
			Map.Entry<K, V>[] buffs = new Map.Entry[size];
			values.entrySet().toArray(buffs);
			// 最後一個
			result = buffs[size - 1];
		}
		return result;
	}

	/**
	 * 取得map的最後一個entry
	 *
	 * @param values
	 * @return
	 */
	public static <E> E getLastEntry(List<E> values) {
		E result = null;
		//
		if (isNotEmpty(values)) {
			int size = values.size();
			// 最後一個
			result = values.get(size - 1);
		}
		return result;
	}

	/**
	 * 累計value
	 *
	 * @param values
	 * @param key
	 * @param value
	 * @return
	 */
	public static <K> boolean accuValue(Map<K, Integer> values, K key, int value) {
		boolean result = false;
		if (key != null) {
			int origValue = NumberUtil.safeGet(values.get(key));
			origValue += value;
			values.put((K) key, origValue);
			result = true;
		}
		return result;
	}

	/**
	 * 累計value
	 *
	 * @param values
	 * @return
	 */
	public static <K> boolean accuValue(Map<K, Integer> values, Map<K, Integer> destValues) {
		boolean result = false;
		// 原始
		Map<K, Integer> origValues = new LinkedHashMap<>(values);
		//
		boolean added = true;
		for (Map.Entry<K, Integer> entry : destValues.entrySet()) {
			added &= accuValue(values, entry.getKey(), entry.getValue());
			// 當有一個加入失敗時,則還原
			if (!added) {
				values = origValues;
				break;
			}
			result = added;
		}
		return result;
	}

	/**
	 * 集合內元素轉成Integer
	 *
	 * @param values
	 * @return
	 */
	public static List<Serializable> toInts(List<Serializable> values) {
		List<Serializable> result = new LinkedList<>();
		if (isNotEmpty(values)) {
			for (Serializable entry : values) {
				Integer buff = NumberUtil.toInt(entry);
				result.add(buff);
			}
		}
		return result;
	}

	/**
	 * 集合內元素轉成Long
	 *
	 * @param values
	 * @return
	 */
	public static List<Serializable> toLongs(List<Serializable> values) {
		List<Serializable> result = new LinkedList<>();
		if (isNotEmpty(values)) {
			for (Serializable entry : values) {
				Long buff = NumberUtil.toLong(entry);
				result.add(buff);
			}
		}
		return result;
	}

	public static <T> String toString(Collection<T> values) {
		return toString(values, StringUtil.COMMA_SPACE);
	}

	public static <T> String toString(Collection<T> values, String splitter) {
		StringBuilder result = new StringBuilder();
		if (values != null) {
			int i = 0;
			int size = values.size();
			for (T entry : values) {
				result.append(entry != null ? entry.toString() : "null");
				//
				if (i < size - 1) {
					result.append(splitter);
				}
				i++;
			}
		}
		return result.toString();
	}

	public static <E> Set<E> checkDuplicate(List<E> values) {
		Set<E> result = new LinkedHashSet<>();
		//
		Set<E> buff = new LinkedHashSet<>();
		for (E e : values) {
			if (!buff.add(e)) {
				result.add(e);
			}
		}
		return result;
	}
	
    public static boolean containsAny(Collection<?> list1, Collection<?> list2) {
        if (list1.size() < list2.size()) {
            for (Object item : list1) {
                if (list2.contains(item)) {
                    return true;
                }
            }
        } else {
            for (Object item : list2) {
                if (list1.contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> void addIfNotNull(Collection<T> list, T objToAdd) {
        if (list != null && objToAdd != null) {
            list.add(objToAdd);
        }
    }

    public static <T> void addIfNotNull(Collection<T> list, Collection<T> listToAdd) {
        if (list != null && isNotEmpty(listToAdd) == false) {
            list.addAll(listToAdd);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> subPagingItems(List<T> items, int pageNumber, int itemsPerPage, int prefixCount) {
        if (pageNumber <= 0 || itemsPerPage <= 0) {
            return items;
        }
        int fromIndex = (pageNumber - 1) * itemsPerPage;
        int toIndex = fromIndex + itemsPerPage;
        // simulate size
        List<T> newFolderResponses = new ArrayList<T>();
        if (prefixCount > 0) {
            newFolderResponses.addAll(Arrays.asList((T[]) new Object[prefixCount]));
        }
        newFolderResponses.addAll(items);
        // sublist
        try {
            return newFolderResponses.subList(Math.max(fromIndex, prefixCount), Math.min(toIndex, newFolderResponses.size()));
        } catch (Exception ex) { // ignore any error
        }
        return new ArrayList<T>();
    }

    public static <T> void subPagingItems(List<T> items, int pageNumber, int itemsPerPage) {
        if (pageNumber <= 0 || itemsPerPage <= 0) {
            return;
        }
        int fromIndex = (pageNumber - 1) * itemsPerPage;
        int toIndex = fromIndex + itemsPerPage;
        // collect it again to avoid concurrency exception
        List<T> subItems = new ArrayList<T>(items.subList(fromIndex, Math.min(toIndex, items.size())));
        // for performance issue
        items.clear();
        items.addAll(subItems);
    }    
}
