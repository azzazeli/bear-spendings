package com.alexm.bearspendings.imports;

import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.entity.Product;

import static com.alexm.bearspendings.imports.TestImportCategories.*;

/**
 * @author AlexM
 * Date: 4/27/20
 **/
public enum TestImportProducts {
    Medicamente(1L, "Medicamente", BEBE.category()),
    Calmante(2L, "Calmante", HEALTH.category()),
    Misc(3L, "Misc", MISC.category()),
    Pachet(4L, "Pachet", MISC.category()),
    Vitamine(5L, "Vitamine", HEALTH.category()),
    AppaMorshinska(6L, "Appa Morshinska, 6L", APPA.category()),
    Ridiche(7L, "Ridiche", LEGUME.category()),
    Lamii(8L, "Lamii", FRUITS.category()),
    Banane(9L, "Banane", FRUITS.category()),
    Smintina20(10L, "Smintina 20%",LACTATE.category()),
    ArahideFiesta(11L, "Arahide Fiesta, cu sare, 130g", SNACKS.category()),
    Chefir(12L, "Chefir", LACTATE.category()),
    Lapte05(13L, "Lapte, 0.5l", LACTATE.category()),
    VarzaNoua(14L, "Varza noua", LEGUME.category()),
    Avocado(15L, "Avocado", FOOD_AND_DRINK.category()),
    Brinzica(16L, "Brinzica", LACTATE.category()),
    Bere(17L, "Bere", ALCOHOL.category()),
    Drojdie(18L, "Drojdie uscata", MISC.category()),
    Grapefruit(19L, "Grapefruit", FRUITS.category()),
    Ceapa(20L, "Ceapa", LEGUME.category()),
    PiineFranzela(21L, "Piine, franzela capitala", PIINE.category());

    TestImportProducts(Long id, String name, Category category) {
        this.id = id;
        this.productName = name;
        this.category = category;
    }
    public final Long id;
    public final String productName;
    private final Category category;

    public Product product() {
        return Product.builder().name(productName).id(id).category(category).build();
    }
}
