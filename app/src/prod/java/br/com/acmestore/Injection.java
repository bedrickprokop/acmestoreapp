package br.com.acmestore;

import br.com.acmestore.data.service.impl.ProductServiceApiImpl;
import br.com.acmestore.data.service.impl.UserServiceApiImpl;

public class Injection {

    public static ProductServiceApiImpl provideProductServiceApiImpl() {
        return new ProductServiceApiImpl();
    }

    public static UserServiceApiImpl provideUserServiceApiImpl() {
        return new UserServiceApiImpl();
    }
}
