package com.example.vaadin;

import com.vaadin.ui.Component;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PaymentTest
{
  public PaymentTest() {
  }

  /**
   * Test of getWindow method, of class Payment.
   */
  @Test
  public void testGetWindow() {
    Payment instance = new Payment(new Purchase());
    assertEquals("5*100kr<br>", instance.formatMoney(500));
    assertEquals("5*100kr<br>1*50kr<br>", instance.formatMoney(550));
    assertEquals("5*100kr<br>1*50kr<br>2*20kr<br>", instance.formatMoney(590));
    assertEquals("5*100kr<br>1*50kr<br>2*20kr<br>1*5kr<br>", instance.formatMoney(595));
    assertEquals("5*100kr<br>1*50kr<br>2*20kr<br>1*5kr<br>3*1kr<br>", instance.formatMoney(598));
    assertEquals("5*100kr<br>1*50kr<br>1*20kr<br>1*10kr<br>1*5kr<br>3*1kr<br>", instance.formatMoney(588));
    assertEquals("1*100kr<br>", instance.formatMoney(100));
    assertEquals("3*100kr<br>4*1kr<br>", instance.formatMoney(304));
    assertEquals("1*20kr<br>1*5kr<br>", instance.formatMoney(25));
    assertEquals("", instance.formatMoney(-1));
    assertEquals("", instance.formatMoney(0));



  }
}
