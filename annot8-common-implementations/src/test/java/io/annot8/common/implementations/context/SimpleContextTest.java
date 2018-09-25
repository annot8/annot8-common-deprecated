/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.common.implementations.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.annot8.core.components.Resource;
import io.annot8.core.settings.Settings;

public class SimpleContextTest {

  @Test
  public void testSimpleContextDefault() {
    Resource r1 = Mockito.mock(Resource.class);
    Resource r2 = new TestResource();

    SimpleContext context = new SimpleContext();
    context.addResource("resource1", r1);
    context.addResource("resource2", r2);

    Assertions.assertTrue(context.getSettings().count() == 0);

    Assertions.assertFalse(context.getResource("foo", Resource.class).isPresent());
    Assertions.assertFalse(context.getResource("resource1", TestResource.class).isPresent());
    Assertions.assertTrue(context.getResource("resource1", Resource.class).isPresent());

    List<String> keys = context.getResourceKeys().collect(Collectors.toList());
    Assertions.assertEquals(2, keys.size());
    Assertions.assertTrue(keys.contains("resource1"));
    Assertions.assertTrue(keys.contains("resource2"));

    List<TestResource> resources =
        context.getResources(TestResource.class).collect(Collectors.toList());
    Assertions.assertEquals(1, resources.size());
    Assertions.assertEquals(r2, resources.get(0));
  }

  @Test
  public void testSimpleContextMap() {
    Resource r1 = Mockito.mock(Resource.class);
    Resource r2 = new TestResource();

    Map<String, Resource> r = new HashMap<>();
    r.put("resource1", r1);
    r.put("resource2", r2);

    SimpleContext context = new SimpleContext(r);

    Assertions.assertTrue(context.getSettings().count() == 0);

    Assertions.assertFalse(context.getResource("foo", Resource.class).isPresent());
    Assertions.assertFalse(context.getResource("resource1", NotTestResource.class).isPresent());
    Assertions.assertTrue(context.getResource("resource2", Resource.class).isPresent());
    Assertions.assertTrue(context.getResource("resource2", TestResource.class).isPresent());

    List<String> keys = context.getResourceKeys().collect(Collectors.toList());
    Assertions.assertEquals(2, keys.size());
    Assertions.assertTrue(keys.contains("resource1"));
    Assertions.assertTrue(keys.contains("resource2"));

    List<TestResource> resources =
        context.getResources(TestResource.class).collect(Collectors.toList());
    Assertions.assertEquals(1, resources.size());
    Assertions.assertEquals(r2, resources.get(0));
  }

  @Test
  public void testSimpleContextSettings() {
    Settings s = Mockito.mock(Settings.class);

    SimpleContext context = new SimpleContext(Arrays.asList(s));

    Assertions.assertEquals(1, context.getSettings().count());
    Assertions.assertEquals(s, context.getSettings().findFirst().get());

    List<String> keys = context.getResourceKeys().collect(Collectors.toList());
    Assertions.assertTrue(keys.isEmpty());

    List<Resource> resources = context.getResources(Resource.class).collect(Collectors.toList());
    Assertions.assertTrue(resources.isEmpty());
  }

  @Test
  public void testSimpleContextSettingsAndMap() {
    Resource r1 = Mockito.mock(Resource.class);
    Resource r2 = new TestResource();
    Settings s = Mockito.mock(Settings.class);

    Map<String, Resource> r = new HashMap<>();
    r.put("resource1", r1);
    r.put("resource2", r2);

    SimpleContext context = new SimpleContext(Arrays.asList(s), r);

    Assertions.assertEquals(1, context.getSettings().count());
    Assertions.assertEquals(s, context.getSettings().findFirst().get());

    Assertions.assertFalse(context.getResource("foo", Resource.class).isPresent());
    Assertions.assertFalse(context.getResource("resource1", TestResource.class).isPresent());
    Assertions.assertTrue(context.getResource("resource1", Resource.class).isPresent());

    List<String> keys = context.getResourceKeys().collect(Collectors.toList());
    Assertions.assertEquals(2, keys.size());
    Assertions.assertTrue(keys.contains("resource1"));
    Assertions.assertTrue(keys.contains("resource2"));

    List<TestResource> resources =
        context.getResources(TestResource.class).collect(Collectors.toList());
    Assertions.assertEquals(1, resources.size());
    Assertions.assertEquals(r2, resources.get(0));
  }

  private static class TestResource implements Resource {}

  private static class NotTestResource implements Resource {}
}
