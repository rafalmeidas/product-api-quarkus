package com.pdi;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class GreetingResourceIT extends ProductResourceTest {
    // Executa os mesmos testes de ProductResourceTest no modo packaged (JAR/native).
}
