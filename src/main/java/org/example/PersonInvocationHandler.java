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
                    //e.printStackTrace();
                    this.interrupt();
                }
            }
        }
    }

    LifeTimeKillerThread KillerThread = new LifeTimeKillerThread();

//    protected void finalize() throws Throwable {
//        KillerThread.interrupt();
//    }

    private T uniObj;
    public ConcurrentHashMap<Timestamp, ConcurrentHashMap<String, Object>> godHashMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> ObjectsCache = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> ObjectsMutatorCache = new ConcurrentHashMap<>();
    public Timestamp timestamp;// = new Timestamp(System.currentTimeMillis()) + KillIntervslMillis;

    public boolean ExistsTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache) {
        Object tmpObj =
                GetTempMapInCacheMap(godHashMap, objectsMutatorTmp, methodNameCache);
        if (GetTempMapInCacheMap(godHashMap, objectsMutatorTmp, methodNameCache) == null)
            return false;
        else
            return true;
    }

    ;

    public void PutTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, ConcurrentHashMap objectsCacheTmp, String methodNameCache, int lifeTimeMillisec) {
        ConcurrentHashMap<String, Object> tmpConcurrentHashMap = new ConcurrentHashMap<>(objectsMutatorTmp);
        tmpConcurrentHashMap.put(methodNameCache, objectsCacheTmp.get(methodNameCache));
        godHashMap.put(new Timestamp(System.currentTimeMillis() + lifeTimeMillisec / 1000L), tmpConcurrentHashMap);
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

                    if (objectsMutatorTmp.containsKey(key)) {
                        if (objectsMutatorTmp.get(key).equals(value)) {
                            continue;
                        }
                    }
                    isExistAllMutators = false;
                }
                if (isExistAllMutators = true) {
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
            tmpObj = GetCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache);
            if (tmpObj == null)
                continue;
            else
                return tmpObj;
        }
        return tmpObj;
    }

    ;

    //
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
                    isExistAllMutators = false;
                }
                if (isExistAllMutators = true) {
                    //return
                    godHashMapOneInst.put(methodNameCache, tmpObj);
                } else {
//                    return null;
                }
            }
//        return null;
    }

    public void tmpPutTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache, Object tmpObj) {
        // = null;
        Iterator<ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>>> iterator = godHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            ConcurrentHashMap.Entry<Timestamp, ConcurrentHashMap<String, Object>> entry = iterator.next();
            Timestamp key = entry.getKey();
            ConcurrentHashMap<String, Object> curMapentry = entry.getValue();
            //tmpObj =
            tmpPutCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache, tmpObj);
//            if (tmpObj == null)
//                continue;
//            else
//                return tmpObj;
        }
//        return tmpObj;
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
            godHashMap.entrySet().removeIf(entry -> entry.getKey().before(new Timestamp(System.currentTimeMillis())));

            if (!ExistsTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName())) {
                ObjectsCache.put(method.getName(), method.invoke(this.uniObj, args));
                PutTempMapInCacheMap(godHashMap, ObjectsMutator, ObjectsCache, method.getName(), tmpMethod.getAnnotation(Cache.class).value());
            }
            return GetTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName());
        } else if (tmpMethod.isAnnotationPresent(Mutator.class)) {
            System.out.println("Обработка через мутатор");
            Object tmpObj = method.invoke(this.uniObj, args);
            ObjectsMutator.put(method.getName(), Arrays.toString(args));
            return tmpObj;
        } else {
            System.out.println("Обработка не через кэш и мутатор");
            return method.invoke(this.uniObj, args);
        }
    }

}