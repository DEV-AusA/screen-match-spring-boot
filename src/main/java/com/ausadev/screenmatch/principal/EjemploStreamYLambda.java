package com.ausadev.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreamYLambda {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Cesar", "Nicolas", "Leonardo", "Karolina", "Sandra");

        nombres.stream()
                .sorted()
                .limit(2)
                .filter(n -> n.startsWith("K"))
                .map(m -> m.toUpperCase())
                .forEach(System.out::println);
    }
}
