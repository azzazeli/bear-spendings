package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Product;

/**
 * @author AlexM
 * Date: 4/27/20
 **/
public enum TestImportProducts {
    Medicamente(1000L, "Medicamente"),
    Calmante(1001L, "Calmante"),
    Misc(1002L, "Misc"),
    Pachet(1003L, "Pachet"),
    Vitamine(1004L, "Vitamine"),
    AppaMorshinska(1005L, "Appa Morshinska, 6L"),
    Ridiche(1006L, "Ridiche"),
    Lamii(1007L, "Lamii"),
    Banane(1008L, "Banane"),
    Smintina20(1009L, "Smintina 20%"),
    ArahideFiesta(1010L, "Arahide Fiesta, cu sare, 130g"),
    Chefir(1011L, "Chefir"),
    Lapte05(1012L, "Lapte, 0.5l"),
    VarzaNoua(1013L, "Varza noua"),
    Avocado(1014L, "Avocado"),
    Brinzica(1015L, "Brinzica"),
    Bere(1016L, "Bere"),
    Drojdie(1017L, "Drojdie uscata"),
    Grapefruit(1018L, "Grapefruit"),
    Ceapa(1019L, "Ceapa"),
    PiineFranzela(1020L, "Piine, franzela capitala");

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
