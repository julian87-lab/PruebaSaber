package com.pruebasaber.app.security;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CustomLoginSuccessHandler}.
 */
@Generated
public class CustomLoginSuccessHandler__BeanDefinitions {
  /**
   * Get the bean definition for 'customLoginSuccessHandler'.
   */
  public static BeanDefinition getCustomLoginSuccessHandlerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CustomLoginSuccessHandler.class);
    beanDefinition.setInstanceSupplier(CustomLoginSuccessHandler::new);
    return beanDefinition;
  }
}
