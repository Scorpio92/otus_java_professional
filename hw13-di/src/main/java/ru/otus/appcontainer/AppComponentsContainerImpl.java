package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        try {
            processConfig(initialConfigClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse config", e);
        }
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        checkConfigClass(configClass);

        Object config = configClass.getConstructor().newInstance();
        findAllBeanMethods(configClass).forEach(method -> {
            String componentName = getComponentByMethod(method).name();
            Object[] methodArguments = Arrays.stream(method.getParameterTypes())
                    .map((Function<Class<?>, Object>) AppComponentsContainerImpl.this::getAppComponent)
                    .toArray();
            Object bean;
            try {
                bean = method.invoke(config, methodArguments);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get bean", e);
            }
            appComponents.add(bean);
            appComponentsByName.put(componentName, bean);
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return appComponents.stream()
                .filter(o -> {
                    Class<?> c = o.getClass();
                    return c == componentClass
                            || c.getSuperclass() == componentClass
                            || componentClass.isAssignableFrom(c);
                })
                .map(o -> (C) o)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Component by class [%s] not found", componentClass.getSimpleName())));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object o = appComponentsByName.getOrDefault(componentName, null);
        if (o == null) throw new RuntimeException(String.format("Component by name [%s] not found", componentName));
        return (C) o;
    }

    private Collection<Method> findAllBeanMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> Arrays.stream(method.getDeclaredAnnotations()).count() == 1)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> getComponentByMethod(m).order()))
                .collect(Collectors.toList());
    }

    private AppComponent getComponentByMethod(Method method) {
        return method.getAnnotationsByType(AppComponent.class)[0];
    }
}
