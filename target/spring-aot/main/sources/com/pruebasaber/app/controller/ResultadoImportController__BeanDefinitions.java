package com.pruebasaber.app.controller;

import com.pruebasaber.app.service.ExcelImportService;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ResultadoImportController}.
 */
@Generated
public class ResultadoImportController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'resultadoImportController'.
   */
  private static BeanInstanceSupplier<ResultadoImportController> getResultadoImportControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ResultadoImportController>forConstructor(ExcelImportService.class)
            .withGenerator((registeredBean, args) -> new ResultadoImportController(args.get(0)));
  }

  /**
   * Get the bean definition for 'resultadoImportController'.
   */
  public static BeanDefinition getResultadoImportControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ResultadoImportController.class);
    beanDefinition.setInstanceSupplier(getResultadoImportControllerInstanceSupplier());
    return beanDefinition;
  }
}
