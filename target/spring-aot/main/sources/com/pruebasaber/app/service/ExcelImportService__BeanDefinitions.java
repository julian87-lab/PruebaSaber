package com.pruebasaber.app.service;

import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ExcelImportService}.
 */
@Generated
public class ExcelImportService__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'excelImportService'.
   */
  private static BeanInstanceSupplier<ExcelImportService> getExcelImportServiceInstanceSupplier() {
    return BeanInstanceSupplier.<ExcelImportService>forConstructor(EstudianteRepository.class, ResultadoRepository.class)
            .withGenerator((registeredBean, args) -> new ExcelImportService(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'excelImportService'.
   */
  public static BeanDefinition getExcelImportServiceBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ExcelImportService.class);
    beanDefinition.setInstanceSupplier(getExcelImportServiceInstanceSupplier());
    return beanDefinition;
  }
}
