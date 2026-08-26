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
        if (livestockRepository.count() == 0) {
            saveGoat("GOAT-001", "Native Goat", Gender.MALE, "8 Months", 8, "28", "18000", true,
                    "https://images.unsplash.com/photo-1524024977331-8c0b5c6d8c5a?auto=format&fit=crop&w=900&q=80");
            saveGoat("GOAT-002", "Native Goat", Gender.FEMALE, "10 Months", 10, "26", "16500", true,
                    "https://images.unsplash.com/photo-1551884831-bbf3cdc6469e?auto=format&fit=crop&w=900&q=80");
            saveGoat("GOAT-003", "Boer Cross", Gender.MALE, "14 Months", 14, "38", "24500", false,
                    "https://images.unsplash.com/photo-1545468800-85cc9bc1efb7?auto=format&fit=crop&w=900&q=80");
            saveGoat("GOAT-004", "Tellicherry", Gender.FEMALE, "7 Months", 7, "22", "14200", false,
                    "https://images.unsplash.com/photo-1583336663277-620dc1996580?auto=format&fit=crop&w=900&q=80");
            saveCow("COW-001", "Native Cow", Gender.FEMALE, "3 Years", 36, "320", "65000", true,
                    "https://images.unsplash.com/photo-1500595046743-cd271d694d30?auto=format&fit=crop&w=900&q=80");
            saveCow("COW-002", "Kangayam", Gender.MALE, "2 Years", 24, "380", "72000", true,
                    "https://images.unsplash.com/photo-1546445317-29f4545e9d53?auto=format&fit=crop&w=900&q=80");
            saveCow("COW-003", "Native Cow", Gender.FEMALE, "4 Years", 48, "350", "58000", false,
                    "https://images.unsplash.com/photo-1570042223111-2a8873b8a9c5?auto=format&fit=crop&w=900&q=80");
            saveChicken("CHICKEN-001", "Country Chicken", "6 Months", 6, "1.8", "280", "5", "50", true,
                    "https://images.unsplash.com/photo-1548550023-2bdb3c5beed7?auto=format&fit=crop&w=900&q=80");
            saveChicken("CHICKEN-002", "Aseel Chicken", "8 Months", 8, "2.2", "320", "5", "40", true,
                    "https://images.unsplash.com/photo-1612170153139-6f881ff067e0?auto=format&fit=crop&w=900&q=80");
            saveChicken("CHICKEN-003", "Kadaknath", "5 Months", 5, "1.5", "450", "3", "25", false,
                    "https://images.unsplash.com/photo-1563281577-a7be47e20f57?auto=format&fit=crop&w=900&q=80");
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
