package com.rmsvg.livestock.config;

import com.rmsvg.livestock.domain.Enums.*;
import com.rmsvg.livestock.entity.AppSettings;
import com.rmsvg.livestock.entity.Livestock;
import com.rmsvg.livestock.entity.LivestockImage;
import com.rmsvg.livestock.entity.UserAccount;
import com.rmsvg.livestock.repository.AppSettingsRepository;
import com.rmsvg.livestock.repository.LivestockRepository;
import com.rmsvg.livestock.repository.UserAccountRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserAccountRepository users;
    private final LivestockRepository livestockRepository;
    private final AppSettingsRepository settingsRepository;
    private final PasswordEncoder encoder;

    public DataSeeder(UserAccountRepository users,
                      LivestockRepository livestockRepository,
                      AppSettingsRepository settingsRepository,
                      PasswordEncoder encoder) {
        this.users = users;
        this.livestockRepository = livestockRepository;
        this.settingsRepository = settingsRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!users.existsByUsername("admin")) {
            UserAccount admin = new UserAccount();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setName("RMSVG Admin");
            admin.setRole(Role.ADMIN);
            users.save(admin);
        }
        if (settingsRepository.findById(1L).isEmpty()) {
            AppSettings s = new AppSettings();
            s.setId(1L);
            s.setWhatsappNumber("919876543210");
            s.setPhone("9876543210");
            s.setAddress("Namakkal, Tamil Nadu");
            s.setHours("Mon–Sat, 8:00 AM – 7:00 PM");
            s.setMapEmbedUrl("https://maps.google.com/maps?q=Namakkal&t=&z=13&ie=UTF8&iwloc=&output=embed");
            settingsRepository.save(s);
        }
        if (livestockRepository.count() > 0) {
            List<Livestock> available = livestockRepository.findAll().stream()
                    .filter(item -> item.getStatus() == LivestockStatus.AVAILABLE)
                    .toList();
            for (int i = 0; i < available.size(); i++) {
                available.get(i).setFeatured(i < 5);
            }
            livestockRepository.saveAll(available);
            return;
        }

        String[] goatBreeds = {"Native Goat", "Boer Cross", "Tellicherry", "Jamunapari", "Osmanabadi", "Sirohi", "Malabari", "Kanni", "Local Goat", "Crossbred Goat", "Black Goat", "White Goat", "Mountain Goat", "Desi Goat", "Farm Goat"};
        String[] cowBreeds = {"Native Cow", "Kangayam", "Gir", "Sahiwal", "Jersey Cross", "Local Cow", "Murrah Cross", "Red Sindhi", "Holstein", "Cattle", "Fresian", "Brown Cow", "Young Cow", "Milking Cow", "Healthy Cow"};
        String[] chickenBreeds = {"Country Chicken", "Aseel Chicken", "Kadaknath", "Broiler Cross", "Local Chicken", "Village Chicken", "Farm Chicken", "Desi Chicken", "Native Chicken", "Poultry Chicken", "White Feather Chicken", "Brown Chicken", "Red Feather Chicken", "Hill Chicken", "Broiler Chicken"};
        String[] goatImages = {
                "https://images.unsplash.com/photo-1524024977331-8c0b5c6d8c5a?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1551884831-bbf3cdc6469e?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1545468800-85cc9bc1efb7?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1583336663277-620dc1996580?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1474511320723-9a56873867b5?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1504593811423-6dd665756598?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1552728089-57bdde30beb3?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1517849845537-4d257902454a?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1474511320723-9a56873867b5?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1504593811423-6dd665756598?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?auto=format&fit=crop&w=900&q=80"
        };
        String[] cowImages = {
                "https://images.unsplash.com/photo-1500595046743-cd271d694d30?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1546445317-29f4545e9d53?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1570042223111-2a8873b8a9c5?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1560493676-04071c5f467b?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1584515933487-779824d29309?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1609444440599-febc0fd3e6a2?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1532584965868-5d71fb1eb620?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1527153857715-3908f2ba8e56?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1534258936925-c58bed479fcb?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1500595046743-cd271d694d30?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1546445317-29f4545e9d53?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1570042223111-2a8873b8a9c5?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1560493676-04071c5f467b?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80"
        };
        String[] chickenImages = {
                "https://images.unsplash.com/photo-1548550023-2bdb3c5beed7?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1612170153139-6f881ff067e0?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1563281577-a7be47e20f57?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1607574555409-543b0dfe304d?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1511884642898-4c92249e20b6?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1589279003513-467d320f47eb?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1518492104633-130d0f6d1bf0?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1512470876302-972faa2aa9a4?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1548550023-2bdb3c5beed7?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1612170153139-6f881ff067e0?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1563281577-a7be47e20f57?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1607574555409-543b0dfe304d?auto=format&fit=crop&w=900&q=80",
                "https://images.unsplash.com/photo-1511884642898-4c92249e20b6?auto=format&fit=crop&w=900&q=80"
        };

        for (int i = 0; i < 15; i++) {
            boolean featured = i < 5;
            int months = 6 + (i * 2);
            int goatWeight = 20 + (i * 3);
            int goatPrice = 12000 + (i * 1800);
            saveGoat("GOAT-" + String.format("%03d", i + 1), goatBreeds[i], i % 2 == 0 ? Gender.MALE : Gender.FEMALE,
                    months + " Months", months, String.valueOf(goatWeight), String.valueOf(goatPrice), featured, goatImages[i]);
        }

        for (int i = 0; i < 15; i++) {
            boolean featured = i < 5;
            int months = 18 + (i * 6);
            int cowWeight = 260 + (i * 20);
            int cowPrice = 42000 + (i * 5000);
            saveCow("COW-" + String.format("%03d", i + 1), cowBreeds[i], i % 2 == 0 ? Gender.FEMALE : Gender.MALE,
                    (months >= 24 ? (months / 12) + " Years" : months + " Months"), months, String.valueOf(cowWeight), String.valueOf(cowPrice), featured, cowImages[i]);
        }

        for (int i = 0; i < 15; i++) {
            boolean featured = i < 5;
            int ageMonths = 4 + i;
            int qty = 12 + i * 5;
            int price = 260 + i * 25;
            saveChicken("CHICKEN-" + String.format("%03d", i + 1), chickenBreeds[i], ageMonths + " Months", ageMonths,
                    String.valueOf((i + 1) * 1.6), String.valueOf(price), "5", String.valueOf(qty), featured, chickenImages[i]);
        }
    }

    private void saveGoat(String code, String breed, Gender gender, String age, int months, String weight, String price,
                          boolean featured, String image) {
        Livestock l = base(code, Category.GOAT, breed, gender, age, months, weight, PricingType.FIXED, price, featured);
        l.setDescription("Healthy " + breed.toLowerCase() + " raised on native fodder at our Namakkal farm. Suitable for family functions and farm rearing.");
        l.setWhyChoose("Vaccinated and regularly inspected. Fair farm-direct pricing. Clear age, weight and availability.");
        addImage(l, image);
        livestockRepository.save(l);
    }

    private void saveCow(String code, String breed, Gender gender, String age, int months, String weight, String price,
                         boolean featured, String image) {
        Livestock l = base(code, Category.COW, breed, gender, age, months, weight, PricingType.FIXED, price, featured);
        l.setDescription("Well-maintained " + breed.toLowerCase() + " from RMSVG farm stock. Suitable for dairy and farm use.");
        l.setWhyChoose("Native bloodlines, on-farm care, and transparent pricing before you visit or arrange delivery.");
        addImage(l, image);
        livestockRepository.save(l);
    }

    private void saveChicken(String code, String breed, String age, int months, String weight, String price,
                             String minQty, String stock, boolean featured, String image) {
        Livestock l = base(code, Category.CHICKEN, breed, null, age, months, weight, PricingType.PER_KG, price, featured);
        l.setMinOrderQty(new BigDecimal(minQty));
        l.setAvailableQty(new BigDecimal(stock));
        l.setDescription(breed + " sold by weight. Fresh farm supply for homes, hotels and meat shops.");
        l.setWhyChoose("Country stock, minimum order " + minQty + " KG, current farm availability shown live.");
        addImage(l, image);
        livestockRepository.save(l);
    }

    private Livestock base(String code, Category category, String breed, Gender gender, String age, int months,
                           String weight, PricingType pricing, String price, boolean featured) {
        Livestock l = new Livestock();
        l.setAnimalCode(code);
        l.setCategory(category);
        l.setBreed(breed);
        l.setGender(gender);
        l.setAgeLabel(age);
        l.setAgeMonths(months);
        l.setWeightKg(new BigDecimal(weight));
        l.setPricingType(pricing);
        l.setPrice(new BigDecimal(price));
        l.setLocation("Namakkal");
        l.setStatus(LivestockStatus.AVAILABLE);
        l.setFeatured(featured);
        return l;
    }

    private void addImage(Livestock l, String url) {
        LivestockImage img = new LivestockImage();
        img.setLivestock(l);
        img.setImageUrl(url);
        img.setPrimaryImage(true);
        l.getImages().add(img);
    }
}
