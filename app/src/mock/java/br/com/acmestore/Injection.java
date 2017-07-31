package br.com.acmestore;

import br.com.acmestore.data.service.impl.FakeProductServiceApiImpl;
import br.com.acmestore.data.service.impl.FakeUserServiceApiImpl;

public class Injection {

    public static FakeProductServiceApiImpl provideProductServiceApiImpl() {
        return new FakeProductServiceApiImpl();
    }

    public static FakeUserServiceApiImpl provideUserServiceApiImpl() {
        return new FakeUserServiceApiImpl();
    }
}
