
package com.example.vaadin;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.TextField;
import org.junit.Test;




public class BugTest {


  @Test
  public void bugTest(){
  TextField textField = new TextField();
  textField.setInputPrompt("asdf");
  textField.addListener(new ValueChangeListener() {

      public void valueChange(ValueChangeEvent event) {
      System.out.println("works");
      }
    });
  textField.setValue("asdf");
  }
}
