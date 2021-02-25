package com.alexm.bearspendings.test;

import com.alexm.bearspendings.dto.ProductCommand;
import com.alexm.bearspendings.entity.Category;
import com.alexm.bearspendings.entity.Product;

import static com.alexm.bearspendings.test.TestCategories.*;

/**
 * @author AlexM
 * Date: 4/27/20
 **/
public enum TestProducts {
    MEDICAMENTE(1L, "Medicamente", BEBE.category()),
    CALMANTE(2L, "Calmante", HEALTH.category()),
    MISC(3L, "Misc", TestCategories.MISC.category()),
    PACHET(4L, "Pachet", TestCategories.MISC.category()),
    VITAMINE(5L, "Vitamine", HEALTH.category()),
    APPA_MORSHINSKA(6L, "Appa Morshinska, 6L", APPA.category()),
    RIDICHE(7L, "Ridiche", LEGUME.category()),
    LAMII(8L, "Lamii", FRUITS.category()),
    BANANE(9L, "Banane", FRUITS.category()),
    SMINTINA_20(10L, "Smintina 20%", LACTATE.category()),
    ARAHIDE_FIESTA(11L, "Arahide Fiesta, cu sare, 130g", SNACKS.category()),
    CHEFIR(12L, "Chefir", LACTATE.category()),
    LAPTE_05(13L, "Lapte, 0.5l", LACTATE.category()),
    VARZA_NOUA(14L, "Varza noua", LEGUME.category()),
    AVOCADO(15L, "Avocado", FOOD_AND_DRINK.category()),
    BRINZICA(16L, "Brinzica", LACTATE.category()),
    BERE(17L, "Bere", ALCOHOL.category()),
    DROJDIE(18L, "Drojdie uscata", TestCategories.MISC.category()),
    GRAPEFRUIT(19L, "Grapefruit", FRUITS.category()),
    CEAPA(20L, "Ceapa", LEGUME.category()),
    PIINE_FRANZELA(21L, "Piine, franzela capitala", PIINE.category()),
    CARTOFI(22L, "Cartofi", LEGUME.category());

    TestProducts(Long id, String name, Category category) {
        this.id = id;
        this.productName = name;
        this.category = category;
        this.productCommand = ProductCommand.builder().id(id).name(productName).build();
        this.product = Product.builder().name(productName).id(id).category(category).build();
    }
    public final Long id;
    public final String productName;
    public final ProductCommand productCommand;
    public final Product product;
    public final Category category;
}
