package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.*;
import java.util.concurrent.*;
import java.sql.Timestamp;

public class PersonInvocationHandler<T> implements InvocationHandler {

    // Автономный поток очистки кэша по таймеру
    public class LifeTimeKillerThread extends Thread {
        @Override
        public void run() {
            while (!this.interrupted()) {
                try {
                    godHashMap.entrySet().removeIf(entry -> entry.getKey().before(new Timestamp(System.currentTimeMillis())));
                    this.sleep(1000);
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }

    LifeTimeKillerThread KillerThread = new LifeTimeKillerThread();

    private T uniObj;
    public ConcurrentHashMap<Timestamp, ConcurrentHashMap<String, Object>> godHashMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> ObjectsCache = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> ObjectsMutatorCache = new ConcurrentHashMap<>();
    public Timestamp timestamp;

    public boolean ExistsTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache) {
        if (GetTempMapInCacheMap(godHashMap, objectsMutatorTmp, methodNameCache) == null)
            return false;
        else
            return true;
    }

    ;

    public void PutTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, ConcurrentHashMap objectsCacheTmp, String methodNameCache, int lifeTimeMillisec) {
        ConcurrentHashMap<String, Object> tmpConcurrentHashMap = new ConcurrentHashMap<>(objectsMutatorTmp);
        tmpConcurrentHashMap.put(methodNameCache, objectsCacheTmp.get(methodNameCache));
        try {
            Thread.currentThread().sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(" <first key> cur       " + new Timestamp(System.currentTimeMillis()));
        System.out.println(" <first key>           " + new Timestamp(System.currentTimeMillis() + lifeTimeMillisec));
        System.out.println("godHashMap.size() до до " + godHashMap.size());
        godHashMap.put(new Timestamp(System.currentTimeMillis() + lifeTimeMillisec), tmpConcurrentHashMap);
        System.out.println("godHashMap.size() после после " + godHashMap.size());
    }

    ;

    public Object GetCacheMaps(ConcurrentHashMap godHashMapOneInst, ConcurrentHashMap objectsMutatorTmp, String methodNameCache) {
        if (godHashMapOneInst.size() == objectsMutatorTmp.size() + 1)
            if (godHashMapOneInst.containsKey(methodNameCache)) {
                boolean isExistAllMutators = true;
                Iterator<ConcurrentHashMap.Entry<String, Object>> iteratorCur = godHashMapOneInst.entrySet().iterator();
                while (iteratorCur.hasNext()) {
                    ConcurrentHashMap.Entry<String, Object> entry = iteratorCur.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    // По мутатору
                    if (objectsMutatorTmp.containsKey(key)) {
                        if (objectsMutatorTmp.get(key).equals(value)) {
                            continue;
                        }
                    }
                    // По кэшу
                    if (key.equals(methodNameCache)) {
                        continue;
                    }
                    isExistAllMutators = false;
                }
                if (isExistAllMutators == true) {
                    return godHashMapOneInst.get(methodNameCache);
                } else {
                    return null;
                }
            }
        return null;
    }

    public Object GetTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache) {
        Object tmpObj = null;
        Iterator<ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>>> iterator = godHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>> entry = iterator.next();
            Timestamp key = entry.getKey();
            ConcurrentHashMap<String, Object> curMapentry = entry.getValue();
            System.out.println("curMapentry = " + Arrays.asList(curMapentry));
            tmpObj = GetCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache);
            if (tmpObj == null)
                continue;
            else
                return tmpObj;
        }
        return tmpObj;
    }

    ;


    public void tmpPutCacheMaps(ConcurrentHashMap godHashMapOneInst, ConcurrentHashMap objectsMutatorTmp, String methodNameCache, Object tmpObj) {
        if (godHashMapOneInst.size() == objectsMutatorTmp.size() + 1)
            if (godHashMapOneInst.containsKey(methodNameCache)) {
                boolean isExistAllMutators = true;
                Iterator<ConcurrentHashMap.Entry<String, Object>> iteratorCur = godHashMapOneInst.entrySet().iterator();
                while (iteratorCur.hasNext()) {
                    ConcurrentHashMap.Entry<String, Object> entry = iteratorCur.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (objectsMutatorTmp.containsKey(key)) {
                        if (objectsMutatorTmp.get(key).equals(value)) {
                            continue;
                        }
                    }
                    if (key.equals(methodNameCache)) {
                        continue;
                    }
                    isExistAllMutators = false;
                }
                if (isExistAllMutators = true) {
                    godHashMapOneInst.put(methodNameCache, tmpObj);
                }
            }
    }

    public void tmpPutTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache, Object tmpObj) {
        Iterator<ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>>> iterator = godHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>> entry = iterator.next();
            Timestamp key = entry.getKey();
            ConcurrentHashMap<String, Object> curMapentry = entry.getValue();
            tmpPutCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache, tmpObj);
        }
    }

    ;
    //

    public ConcurrentHashMap<String, Object> ObjectsMutator = new ConcurrentHashMap<>();

    // Конструктор
    public PersonInvocationHandler(T t) {
        this.uniObj = t;
        KillerThread.start();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method tmpMethod = uniObj.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (tmpMethod.isAnnotationPresent(Cache.class)) {
            System.out.println("Обработка через кэш " + tmpMethod.getName());
            System.out.println("godHashMap.size() до " + godHashMap.size());
            // ???
            godHashMap.entrySet().removeIf(entry -> entry.getKey().before(new Timestamp(System.currentTimeMillis())));
            System.out.println("godHashMap.size() до(после очистки) " + godHashMap.size());
            if (!ExistsTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName())) {
                ObjectsCache.put(method.getName(), method.invoke(this.uniObj, args));
                System.out.println("ObjectsCache " + Arrays.asList(ObjectsCache));
                PutTempMapInCacheMap(godHashMap, ObjectsMutator, ObjectsCache, method.getName(), tmpMethod.getAnnotation(Cache.class).value());
                System.out.println("Обработка через кэш с задержкой " + tmpMethod.getAnnotation(Cache.class).value());
            }
            System.out.println("godHashMap.size() после " + godHashMap.size());
            System.out.println("ObjectsCache = " + Arrays.asList(ObjectsCache));
            System.out.println("ObjectsMutator = " + Arrays.asList(ObjectsMutator));
            System.out.println("GetTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName()) = " + GetTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName()));
            return GetTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName());
        } else if (tmpMethod.isAnnotationPresent(Mutator.class)) {
            System.out.println("Обработка через мутатор " + method.getName() + " Arrays.toString(args) " + Arrays.toString(args));
            Object tmpObj = method.invoke(this.uniObj, args);
            ObjectsMutator.put(method.getName(), Arrays.toString(args));
            return tmpObj;
        } else {
            System.out.println("Обработка не через кэш и мутатор " + method.getName());
            return method.invoke(this.uniObj, args);
        }
    }

}