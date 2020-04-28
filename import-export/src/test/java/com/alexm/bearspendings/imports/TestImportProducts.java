package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Product;

/**
 * @author AlexM
 * Date: 4/27/20
 **/
public enum TestImportProducts {
    Medicamente(1L, "Medicamente"),
    Calmante(2L, "Calmante"),
    Misc(3L, "Misc"),
    Pachet(4L, "Pachet"),
    Vitamine(5L, "Vitamine"),
    AppaMorshinska(6L, "Appa Morshinska, 6L"),
    Ridiche(7L, "Ridiche"),
    Lamii(8L, "Lamii"),
    Banane(9L, "Banane"),
    Smintina20(10L, "Smintina 20%"),
    ArahideFiesta(11L, "Arahide Fiesta, cu sare, 130g"),
    Chefir(12L, "Chefir"),
    Lapte05(13L, "Lapte, 0.5l"),
    VarzaNoua(14L, "Varza noua"),
    Avocado(15L, "Avocado"),
    Brinzica(16L, "Brinzica"),
    Bere(17L, "Bere"),
    Drojdie(18L, "Drojdie uscata"),
    Grapefruit(19L, "Grapefruit"),
    Ceapa(20L, "Ceapa"),
    PiineFranzela(21L, "Piine, franzela capitala");

    TestImportProducts(Long id, String name) {
        this.id = id;
        this.productName = name;
    }
    public final Long id;
    public final String productName;

    public Product product() {
        return Product.builder().name(productName).id(id).build();
    }
}
