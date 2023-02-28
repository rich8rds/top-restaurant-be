package com.richards.mealsapp.utils;

import com.richards.mealsapp.entity.Category;
import com.richards.mealsapp.entity.Product;
import com.richards.mealsapp.repository.CategoryRepository;
import com.richards.mealsapp.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FakeDB {
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository, CategoryRepository categoryRepository) {
        return args -> {

//            if (!categoryRepository.existsById(1L)) {

                Category lamp2 = Category.builder()
                        .name("Table".toUpperCase())
                        .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/RBSJBF@2x-300x300.jpg")
                        .build();
                Category chair = Category.builder()
                        .name("Chair".toUpperCase())
                        .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-WAX664T@2x-300x300.jpg")
                        .build();
                Category cupboard = Category.builder()
                        .name("Cupboard".toUpperCase())
                        .imageUrl("https://th.bing.com/th/id/R.283fb4960158a3608b8b2fbb553d10ab?rik=74UYA0mjX3VG1g&pid=ImgRaw&r=0")
                        .build();
                List<Category> categories = List.of(lamp2, cupboard, chair);
                categoryRepository.saveAll(categories);


                List<Product> products = List.of(
                        Product.builder()
                                .name("TRYSIL WARDROBE")
                                .price(3000.00)
                                .availableQty(0)
                                .imageUrl("https://cdn.shopify.com/s/files/1/1185/9434/products/trysil-wardrobe-with-sliding-doors-14244301511_300x.jpg?v=1586962362")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("CAPE BED SET")
                                .price(5000.00)
                                .availableQty(400)
                                .imageUrl("https://baffihomeng.com/cape-bed-single-bed-1289-20-K.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("DIVA BED")
                                .price(6000.00)
                                .availableQty(400)
                                .imageUrl("https://baffihomeng.com/diva-bed-single-bed-1291-20-K.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("CABINET")
                                .price(4000.00)
                                .availableQty(400)
                                .imageUrl("https://baffihomeng.com/throw-pillow-duvet-mattress-1581-28-K.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("MILANO CENTER TABLE")
                                .price(5000.00)
                                .availableQty(400)
                                .imageUrl("https://baffihomeng.com/milano-4-1-center-table-center-table-1974-37-K.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),

                        Product.builder()
                                .name("SHELVES WOOD")
                                .price(1000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-U5BW8PS@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("WOODEN RACK")
                                .price(2000.00)
                                .availableQty(100)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-92DNEPD@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely furnished rack with inspiration from last summer's Rack")
                                .build(),
                        Product.builder()
                                .name("COFFEE TABLE")
                                .price(20000.00)
                                .availableQty(440)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/J7ZW2XK@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely furniture")
                                .build(),
                        Product.builder()
                                .name("END TABLE")
                                .price(40000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-RBSJBF@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("TABLE WOOD")
                                .price(42000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/3N8FQJ@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("LOUNGE SOFA")
                                .price(43000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/JD46ETY@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("LUXURY SOFA")
                                .price(44000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/4PHLD2@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely furniture")
                                .build(),
                        Product.builder()
                                .name("MODERN CHAIR")
                                .price(45000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-WAX664T@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("MODERN TABLE")
                                .price(46000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Group-1@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("ARM CHAIR")
                                .price(52000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Armc.png")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("SILVER WARDROBE")
                                .price(53000.00)
                                .availableQty(400)
                                .imageUrl("https://th.bing.com/th/id/OIP.mPfATCzk4nEXhbG2DWWyfgHaHa?pid=ImgDet&w=700&h=700&rs=1")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("TALL LAMP 2")
                                .price(54000.00)
                                .availableQty(400)
                                .imageUrl("https://th.bing.com/th/id/OIP.TFrnJVWYb0pdZgJWG2GnlgHaHa?pid=ImgDet&rs=1")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("DECO LAMP")
                                .price(47000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/XFSNSK@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("TABLE LAMP")
                                .price(48000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/Image-NAM2CS@2x-300x300.jpg")
                                .category(lamp2)
                                .description("lovely fur")
                                .build(),
                        Product.builder()
                                .name("DRESSER")
                                .price(49000.00)
                                .availableQty(400)
                                .imageUrl("https://templatekit.jegtheme.com/funiture/wp-content/uploads/sites/18/2020/11/YMN7ZV@2x-300x300.jpg")
                                .category(cupboard)
                                .description("lovely fur")
                                .build()
                );


                productRepository.saveAll(products);
//            }
        };
    }
}
