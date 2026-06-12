package com.pruebasaber.app.controller;

import com.pruebasaber.app.repository.UsuarioRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MiPerfilController}.
 */
@Generated
public class MiPerfilController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'miPerfilController'.
   */
  private static BeanInstanceSupplier<MiPerfilController> getMiPerfilControllerInstanceSupplier() {
    return BeanInstanceSupplier.<MiPerfilController>forConstructor(UsuarioRepository.class)
            .withGenerator((registeredBean, args) -> new MiPerfilController(args.get(0)));
  }

  /**
   * Get the bean definition for 'miPerfilController'.
   */
  public static BeanDefinition getMiPerfilControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MiPerfilController.class);
    beanDefinition.setInstanceSupplier(getMiPerfilControllerInstanceSupplier());
    return beanDefinition;
  }
}
