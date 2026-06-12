package com.pruebasaber.app.controller;

import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.repository.UsuarioRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link DashboardController}.
 */
@Generated
public class DashboardController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'dashboardController'.
   */
  private static BeanInstanceSupplier<DashboardController> getDashboardControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DashboardController>forConstructor(UsuarioRepository.class, EstudianteRepository.class, ResultadoRepository.class)
            .withGenerator((registeredBean, args) -> new DashboardController(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'dashboardController'.
   */
  public static BeanDefinition getDashboardControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DashboardController.class);
    beanDefinition.setInstanceSupplier(getDashboardControllerInstanceSupplier());
    return beanDefinition;
  }
}
