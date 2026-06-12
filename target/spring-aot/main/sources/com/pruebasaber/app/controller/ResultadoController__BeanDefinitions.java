package com.pruebasaber.app.controller;

import com.pruebasaber.app.repository.ResultadoRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ResultadoController}.
 */
@Generated
public class ResultadoController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'resultadoController'.
   */
  private static BeanInstanceSupplier<ResultadoController> getResultadoControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ResultadoController>forConstructor(ResultadoRepository.class)
            .withGenerator((registeredBean, args) -> new ResultadoController(args.get(0)));
  }

  /**
   * Get the bean definition for 'resultadoController'.
   */
  public static BeanDefinition getResultadoControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ResultadoController.class);
    beanDefinition.setInstanceSupplier(getResultadoControllerInstanceSupplier());
    return beanDefinition;
  }
}
