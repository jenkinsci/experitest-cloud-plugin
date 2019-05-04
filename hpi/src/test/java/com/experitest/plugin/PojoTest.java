package com.experitest.plugin;


import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import com.experitest.plugin.advanced.ExtraArguments;
import com.experitest.plugin.advanced.KeystoreInfo;
import org.junit.Test;
import pl.pojo.tester.api.assertion.Method;

import java.util.Arrays;
import java.util.List;

public class PojoTest {

    @Test
    public void testThem() {
        List<Class> classes = Arrays.<Class>asList(
            ExperitestCredentials.class,
            ExtraArguments.class,
            KeystoreInfo.class,
            ExperitestEnv.class
        );
        for (Class clz : classes) {
            assertPojoMethodsFor(clz)
                .testing(Method.GETTER)
                .testing(Method.SETTER);

        }
    }
}
