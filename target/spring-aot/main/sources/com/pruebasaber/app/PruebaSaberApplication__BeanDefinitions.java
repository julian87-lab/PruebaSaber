package com.pruebasaber.app;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PruebaSaberApplication}.
 */
@Generated
public class PruebaSaberApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'pruebaSaberApplication'.
   */
  public static BeanDefinition getPruebaSaberApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PruebaSaberApplication.class);
    beanDefinition.setInstanceSupplier(PruebaSaberApplication::new);
    return beanDefinition;
  }
}
