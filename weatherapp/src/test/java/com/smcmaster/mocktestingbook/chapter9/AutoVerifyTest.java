package com.smcmaster.mocktestingbook.chapter9;

import org.easymock.EasyMockSupport;
import org.junit.After;

public abstract class AutoVerifyTest
    extends EasyMockSupport {
  
  @After
  public void tearDown() {
    verifyAll();
  }
}
