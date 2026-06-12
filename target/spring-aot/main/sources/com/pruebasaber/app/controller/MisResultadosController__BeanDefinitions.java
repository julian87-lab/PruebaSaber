package com.pruebasaber.app.controller;

import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.repository.UsuarioRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MisResultadosController}.
 */
@Generated
public class MisResultadosController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'misResultadosController'.
   */
  private static BeanInstanceSupplier<MisResultadosController> getMisResultadosControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<MisResultadosController>forConstructor(UsuarioRepository.class, EstudianteRepository.class, ResultadoRepository.class)
            .withGenerator((registeredBean, args) -> new MisResultadosController(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'misResultadosController'.
   */
  public static BeanDefinition getMisResultadosControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MisResultadosController.class);
    beanDefinition.setInstanceSupplier(getMisResultadosControllerInstanceSupplier());
    return beanDefinition;
  }
}
