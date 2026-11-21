/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.functors;

import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * COLLECTIONS-580: the unsafe functors must not be serializable, so no stream
 * can be written with them and none can be read back into them.
 */
public class UnsafeFunctorSerializationTest {

    private static void assertNotSerializable(final Object functor, final String expectedClass) throws Exception {
        Assert.assertEquals(expectedClass, functor.getClass().getName());
        Assert.assertFalse(expectedClass + " is Serializable", functor instanceof Serializable);
        final ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
        try {
            out.writeObject(functor);
            Assert.fail(expectedClass + " was serialized");
        } catch (final NotSerializableException ex) {
            Assert.assertEquals(expectedClass, ex.getMessage());
        } finally {
            out.close();
        }
    }

    @Test
    public void testCloneTransformer() throws Exception {
        assertNotSerializable(CloneTransformer.cloneTransformer(), CloneTransformer.class.getName());
    }

    @Test
    public void testForClosure() throws Exception {
        assertNotSerializable(ForClosure.forClosure(2, NOPClosure.nopClosure()), ForClosure.class.getName());
    }

    @Test
    public void testInstantiateFactory() throws Exception {
        assertNotSerializable(InstantiateFactory.instantiateFactory(ArrayList.class, null, null),
                InstantiateFactory.class.getName());
    }

    @Test
    public void testInstantiateTransformer() throws Exception {
        assertNotSerializable(InstantiateTransformer.instantiateTransformer(),
                InstantiateTransformer.class.getName());
    }

    @Test
    public void testInvokerTransformer() throws Exception {
        assertNotSerializable(InvokerTransformer.invokerTransformer("toString"),
                InvokerTransformer.class.getName());
    }

    @Test
    public void testPrototypeCloneFactory() throws Exception {
        assertNotSerializable(PrototypeFactory.prototypeFactory(new Date()),
                PrototypeFactory.PrototypeCloneFactory.class.getName());
    }

    @Test
    public void testPrototypeSerializationFactory() throws Exception {
        assertNotSerializable(PrototypeFactory.prototypeFactory(Integer.valueOf(9)),
                PrototypeFactory.PrototypeSerializationFactory.class.getName());
    }

    @Test
    public void testWhileClosure() throws Exception {
        assertNotSerializable(WhileClosure.whileClosure(FalsePredicate.falsePredicate(), NOPClosure.nopClosure(), false),
                WhileClosure.class.getName());
    }
}
