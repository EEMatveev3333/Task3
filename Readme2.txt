    public static <T> void start(Class<T> c) throws InvocationTargetException, IllegalAccessException,
            NoSuchMethodException, InstantiationException {

//        Constructor<T> constructor = c.getDeclaredConstructor();
//        T o = constructor.newInstance();
        Method[] methods = c.getDeclaredMethods();

//        Queue<Method> queue1 = new PriorityQueue<>();
//                (m1, m2) -> Integer.compare(m2.getDeclaredAnnotation(Cache.class).value(),
//                        m1.getDeclaredAnnotation(Cache.class).value())
//        );
//        Queue<Method> queue2 = new PriorityQueue<>(
//                (m1, m2) -> Integer.compare(m2.getDeclaredAnnotation(Mutator.class).value(),
//                        m1.getDeclaredAnnotation(Mutator.class).value())
//        );
//        Queue<Method> queue3 = new PriorityQueue<>(
//                (m1, m2) -> Integer.compare(m2.getDeclaredAnnotation(AfterSuite.class).value(),
//                        m1.getDeclaredAnnotation(AfterSuite.class).value())
//        );

        for (Method method : methods) {
            //method
            System.out.println(method + " [Anotations:] " + method.getAnnotations());
            for (Annotation annotation : method.getAnnotations())
                System.out.println("            " + annotation.toString() );
            for (Annotation Declaredannotation : method.getDeclaredAnnotations())
                System.out.println("            " + Declaredannotation.toString() );

            Annotation[] annotations = method.getAnnotations();
            System.out.println(Arrays.toString(annotations));
            Annotation[] Declaredannotations = method.getDeclaredAnnotations();
            System.out.println(Arrays.toString(Declaredannotations));
//            if (method.getDeclaredAnnotation(Cache.class) != null) {
//                queue1.add(method);
//            }
//            else if (method.getDeclaredAnnotation(Mutator.class) != null) {
//                    queue1.add(method);
//            }
//                else if (method.getDeclaredAnnotation(AfterSuite.class) != null) {
//                    queue3.add(method);
//                }
//                else {
//                    queue2.add(method);
//                }
//            }
        }

//        Method m;
//        while ((m = queue1.poll()) != null) {
//            m.invoke(o);
//        }
//        while ((m = queue2.poll()) != null) {
//            m.invoke(o);
//        }
//        while ((m = queue3.poll()) != null) {
//            m.invoke(o);
//        }
    }

        System.out.println("===!");
        System.out.println("===!");
        System.out.println("===!");

        start(Fraction.class);
        System.out.println("===!");
        //start(frC.getClass());
        //Class<? extends Car> carClass = car.getClass();