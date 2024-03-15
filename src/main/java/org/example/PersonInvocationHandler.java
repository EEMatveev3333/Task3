package org.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.*;
import java.util.concurrent.*;
import java.sql.Timestamp;

public class PersonInvocationHandler<T>implements InvocationHandler {
    //private Person person;
    public class LifeTimeKillerThread extends Thread {

        @Override
        public void run(){

            while (!this.interrupted())
            {
                try
                {
                    godHashMap.entrySet().removeIf(entry -> entry.getKey().before(new Timestamp(System.currentTimeMillis())));
//                    System.out.println("Выполнен поток KillerThread" + getName());
                    this.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    this.interrupt();
                }
                //System.out.println("Producer");
            }

//            while (true == true)
//            {
//
//                //this.sleep(10); // Ставим небольшую задержку
//            }
        }
    }

    LifeTimeKillerThread KillerThread = new LifeTimeKillerThread();
    // Маркер изменений, устанавливается в true Mutable методами, сбрасывается в false Cache методами, по усолчанию для пересчета = true
    //boolean isChanged = true;

    protected void finalize() throws Throwable {
        // код очистки
        //System.out.println("Завершение потока KillerThread" + KillerThread.getName());
        KillerThread.interrupt();
        //System.out.println("Завершен поток KillerThread" + KillerThread.getName());
    }

    // Универсальный объект
    private T uniObj;

    // Рабочая структура КЭША
    public ConcurrentHashMap<Timestamp, ConcurrentHashMap<String, Object>> godHashMap = new ConcurrentHashMap<>();

    // Размещение в структуре данных для многопоточного окружения
    public ConcurrentHashMap<String, Object> ObjectsCache = new ConcurrentHashMap<>();
    // Размещение в структуре данных для многопоточного окружения
    public ConcurrentHashMap<String, Object> ObjectsMutatorCache = new ConcurrentHashMap<>();
    // Размещение в структуре данных для многопоточного окружения
    public Timestamp timestamp;// = new Timestamp(System.currentTimeMillis()) + KillIntervslMillis;
    // Размещение в структуре данных для многопоточного окружения
    public boolean ExistsTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache)
    {
        //return false;
//        for (ConcurrentHashMap<Timestamp, ConcurrentHashMap<String, Object>> entry : godHashMap.entrySet()) {
//            ConcurrentHashMap<String, Object> value = entry.getValue();
//            Timestamp key = entry.getKey();
//            System.out.println("Key: " + key + ", Value: " + value);
//        }
        Object tmpObj =
                GetTempMapInCacheMap(godHashMap, objectsMutatorTmp, methodNameCache);
        if (GetTempMapInCacheMap(godHashMap, objectsMutatorTmp, methodNameCache) == null)
            return false;
        else
            return true;

    };

    public void PutTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, ConcurrentHashMap objectsCacheTmp, String methodNameCache, int lifeTimeMillisec)
    {
        ConcurrentHashMap<String, Object> tmpConcurrentHashMap = new ConcurrentHashMap<>(objectsMutatorTmp);
        tmpConcurrentHashMap.put(methodNameCache,objectsCacheTmp.get(methodNameCache));
        godHashMap.put(new Timestamp(System.currentTimeMillis() + lifeTimeMillisec/1000L),tmpConcurrentHashMap);
    };

//    public boolean CompareCacheMaps(ConcurrentHashMap godHashMapOneInst, ConcurrentHashMap objectsMutatorTmp, String methodNameCache){
//        if (godHashMapOneInst.size() == objectsMutatorTmp.size() + 1)
//            if (godHashMapOneInst.containsKey(methodNameCache)){
//                boolean isExist = false;
////                for (ConcurrentHashMap objectsMutatorTmpInst : objectsMutatorTmp){
////                    if (godHashMapOneInst.containsKey(objectsMutatorTmpInst.))
////                    if (godHashMapOneInst.get())
////                }
//            }
//        return false;
//    }

    public Object GetCacheMaps(ConcurrentHashMap godHashMapOneInst, ConcurrentHashMap objectsMutatorTmp, String methodNameCache){
        if (godHashMapOneInst.size() == objectsMutatorTmp.size() + 1)
            if (godHashMapOneInst.containsKey(methodNameCache)){
                boolean isExistAllMutators = true;
                Iterator<ConcurrentHashMap.Entry<String, Object>> iteratorCur = godHashMapOneInst.entrySet().iterator();
                while (iteratorCur.hasNext()) {
                    ConcurrentHashMap.Entry<String, Object> entry = iteratorCur.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    //Object object =
                    if (objectsMutatorTmp.containsKey(key))
                        if (objectsMutatorTmp.get(key).equals(value))
                            continue;

                    isExistAllMutators = false;
                    //ConcurrentHashMap<String, Object> curMapentry = entry.getValue();
                    //tmpObj = GetCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache);
//                    if (tmpObj == null)
//                        continue;
//                    else
//                        return tmpObj;
                    //System.out.println("Key: " + key + ", Value: " + value);
                }
//                for (ConcurrentHashMap objectsMutatorTmpInst : objectsMutatorTmp){
//                    if (!godHashMapOneInst.containsKey(objectsMutatorTmpInst.))
//                        return null;
//                }
                if (isExistAllMutators = true)
                    return godHashMapOneInst.get(methodNameCache);
                else
                    return null;
            }
//        return false;
    return null;
    }

    public Object GetTempMapInCacheMap(ConcurrentHashMap godHashMap, ConcurrentHashMap objectsMutatorTmp, String methodNameCache)
    {
        //double dbl1 = 3.333;
        Object tmpObj = null;
        //Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
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
            //System.out.println("Key: " + key + ", Value: " + value);
        }
//        for (ConcurrentHashMap<Timestamp, ConcurrentHashMap<String, Object>> curMapentry : godHashMap.entrySet()){
//            tmpObj = GetCacheMaps(curMapentry, objectsMutatorTmp, methodNameCache);
//            if (tmpObj == null)
//                continue;
//            else
//                return tmpObj;
//        }
        return tmpObj;
    };

    // +++ Текущий срез значений мутаторов - до кэширования
    public ConcurrentHashMap<String, Object> ObjectsMutator = new ConcurrentHashMap<>();
    // Рабочая структура КЭША

    // Конструктор
    public PersonInvocationHandler(T t) {
        this.uniObj = t;
        //Запускаем поток автоматического удаления
        //System.out.println("Запуск потока KillerThread" + KillerThread.getName());
        KillerThread.start();
        //System.out.println("Запущен поток KillerThread" + KillerThread.getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("-----------------------");
//                System.out.println("Перевызов из под прокси! для метода " + method.getName() + " для класса ");// + uniObj.class.getName());
//        System.out.println(Arrays.toString(method.getDeclaredAnnotations()));
//        System.out.println(Arrays.toString(method.getAnnotations()));
//        System.out.println(Arrays.toString(method.getgetAnnotations());

        //Здесь добавить анализ на маркировку методов анотациями,
        // если метод @Cache
        //                          - проверить текущее значение isChanged = false;
        //                          - Если isChanged = true перевызов оригинального метода, с сохранением результата tmp, установка isChanged = false
        //                          - Если isChanged = false, возврат результата из tmp
        // если метод @Mutable
        //                          - то после перевызова оригинального метода ставить isChanged = true;
        // если метод не @Cache и @Mutable
        //                          - то после перевызова оригинального метода

        //Object tmpObj;

/*        public double doubleValue(){
            if (isChanged) tmp = fraction.doubleValue();
            else
                System.out.println("tmp double value");
            isChanged = false;
            return tmp;
        };*/


        if (method.isAnnotationPresent(Cache.class))
        {


            // Перед обрабокой чистим неактуальные записи принудительно
            godHashMap.entrySet().removeIf(entry -> entry.getKey().before(new Timestamp(System.currentTimeMillis())));

            //System.out.println("Найдена аннотация Cache в методе " + method.getName());// + " параметры" + args.toString());
            //passportsAndNames.put(212133, "Лидия Аркадьевна Бубликова");
            //if (isChanged)

            // Поправить ошибку - дополнить мапу поиска именем метода cache

            if (!ExistsTempMapInCacheMap(godHashMap,ObjectsMutator,method.getName())) {
                ObjectsCache.put(method.getName(), method.invoke(this.uniObj, args)); //tmp = method.invoke(this.uniObj, args);
                PutTempMapInCacheMap(godHashMap, ObjectsMutator, ObjectsCache, method.getName(),method.getAnnotation(Cache.class).value());
            }
                //Теперь не нужен, смотрим по наличию актуального среза в кэш по именам мутаторов и именам текущего вызова кэша
            //isChanged = false;
 //           System.out.println("Найдена аннотация Cache в методе " + method.getName() + " результат работы method.invoke " + ObjectsCache.get(method.getName()));// + " параметры" + args.toString());
 //           System.out.println("Актуальный набор значений мутаторов - " + ObjectsMutator.toString());



            //return ObjectsCache.get(method.getName());
            return GetTempMapInCacheMap(godHashMap, ObjectsMutator, method.getName());
        }
        else if (method.isAnnotationPresent(Mutator.class))
        {
            //System.out.println("Найдена аннотация Mutator в методе " + method.getName() + " параметры" + Arrays.toString(args));
            Object tmpObj = method.invoke(this.uniObj, args);
            //Теперь не нужен, смотрим по наличию актуального среза в кэш
            //isChanged = true;
//            System.out.println("Найдена аннотация Mutator в методе " + method.getName() + " параметры" + Arrays.toString(args) + " результат работы method.invoke " + ObjectsCache.get(method.getName()));
            ObjectsMutator.put(method.getName(),Arrays.toString(args));
            return tmpObj;
        }
        else {
//            System.out.println("Без аннотаций Cache и Mutator в методе " + method.getName());
            return method.invoke(this.uniObj, args);
        }
    }

}