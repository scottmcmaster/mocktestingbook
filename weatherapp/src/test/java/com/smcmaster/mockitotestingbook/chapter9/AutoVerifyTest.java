package com.smcmaster.mockitotestingbook.chapter9;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;

public abstract class AutoVerifyTest {
  
  private List<Object> mocks = new ArrayList<>();
  
  public <T> T createMock(Class<T> classToMock) {
     T m = mock(classToMock);
     mocks.add(m);
     return m;
  }
  
  @After
  public void tearDown() {
    verifyNoMoreInteractions(mocks.toArray(new Object[mocks.size()]));
  }
}
