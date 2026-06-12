package com.pruebasaber.app.seed;

import com.pruebasaber.app.repository.EstudianteRepository;
import com.pruebasaber.app.repository.ResultadoRepository;
import com.pruebasaber.app.repository.UsuarioRepository;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link DataInitializer}.
 */
@Generated
public class DataInitializer__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'dataInitializer'.
   */
  private static BeanInstanceSupplier<DataInitializer> getDataInitializerInstanceSupplier() {
    return BeanInstanceSupplier.<DataInitializer>forConstructor(UsuarioRepository.class, EstudianteRepository.class, ResultadoRepository.class, PasswordEncoder.class)
            .withGenerator((registeredBean, args) -> new DataInitializer(args.get(0), args.get(1), args.get(2), args.get(3)));
  }

  /**
   * Get the bean definition for 'dataInitializer'.
   */
  public static BeanDefinition getDataInitializerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DataInitializer.class);
    beanDefinition.setInstanceSupplier(getDataInitializerInstanceSupplier());
    return beanDefinition;
  }
}
