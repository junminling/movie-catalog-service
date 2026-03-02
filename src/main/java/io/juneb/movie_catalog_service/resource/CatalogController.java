package io.juneb.movie_catalog_service.resource;

import io.juneb.movie_catalog_service.api.CatalogApi;
import io.juneb.movie_catalog_service.model.CatalogItem;
import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CatalogController implements CatalogApi {

    private static final int MAX_ITEMS_PER_USER = 100;
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 10;

    private final Map<String, List<CatalogItem>> userCatalogs = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void initializeSampleData() {
        // Initialize sample data for demo user
        String demoUserId = "demo";
        List<CatalogItem> demoItems = new ArrayList<>();

        CatalogItem item1 = new CatalogItem();
        item1.setId(idGenerator.getAndIncrement());
        item1.setName("The Shawshank Redemption");
        item1.setDesc("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
        item1.setRating(9);
        demoItems.add(item1);

        CatalogItem item2 = new CatalogItem();
        item2.setId(idGenerator.getAndIncrement());
        item2.setName("The Godfather");
        item2.setDesc("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
        item2.setRating(9);
        demoItems.add(item2);

        CatalogItem item3 = new CatalogItem();
        item3.setId(idGenerator.getAndIncrement());
        item3.setName("The Dark Knight");
        item3.setDesc("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest tests.");
        item3.setRating(9);
        demoItems.add(item3);

        CatalogItem item4 = new CatalogItem();
        item4.setId(idGenerator.getAndIncrement());
        item4.setName("Inception");
        item4.setDesc("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea.");
        item4.setRating(8);
        demoItems.add(item4);

        CatalogItem item5 = new CatalogItem();
        item5.setId(idGenerator.getAndIncrement());
        item5.setName("Pulp Fiction");
        item5.setDesc("The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption.");
        item5.setRating(8);
        demoItems.add(item5);

        userCatalogs.put(demoUserId, demoItems);
    }

    @Override
    public ResponseEntity<CatalogItem> createCatalogItem(String userId, CatalogItem catalogItem) {
        // Business logic: Check if user has reached maximum items limit
        userCatalogs.putIfAbsent(userId, new ArrayList<>());
        List<CatalogItem> items = userCatalogs.get(userId);

        if (items.size() >= MAX_ITEMS_PER_USER) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic: Validate rating range
        if (catalogItem.getRating() < MIN_RATING || catalogItem.getRating() > MAX_RATING) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic: Check for duplicate names within user's catalog
        boolean duplicateName = items.stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(catalogItem.getName()));
        if (duplicateName) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic: Trim and validate name
        if (catalogItem.getName() == null || catalogItem.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        catalogItem.setName(catalogItem.getName().trim());

        // Business logic: Trim description if present
        if (catalogItem.getDesc() != null) {
            catalogItem.setDesc(catalogItem.getDesc().trim());
        }

        catalogItem.setId(idGenerator.getAndIncrement());
        items.add(catalogItem);

        return ResponseEntity.ok(catalogItem);
    }

    @Override
    public ResponseEntity<Void> deleteCatalogItem(String userId, Long id) {
        List<CatalogItem> items = userCatalogs.get(userId);
        if (items == null) {
            return ResponseEntity.notFound().build();
        }

        boolean removed = items.removeIf(item -> item.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<CatalogItem>> getAllCatalogItems(String userId) {
        List<CatalogItem> items = userCatalogs.getOrDefault(userId, new ArrayList<>());
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<CatalogItem> getCatalogItemById(String userId, Long id) {
        List<CatalogItem> items = userCatalogs.get(userId);
        if (items == null) {
            return ResponseEntity.notFound().build();
        }

        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CatalogItem> updateCatalogItem(String userId, Long id, CatalogItem catalogItem) {
        List<CatalogItem> items = userCatalogs.get(userId);
        if (items == null) {
            return ResponseEntity.notFound().build();
        }

        // Business logic: Validate rating range
        if (catalogItem.getRating() < MIN_RATING || catalogItem.getRating() > MAX_RATING) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic: Validate name
        if (catalogItem.getName() == null || catalogItem.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Business logic: Check for duplicate names (excluding current item)
        String trimmedName = catalogItem.getName().trim();
        boolean duplicateName = items.stream()
                .filter(item -> !item.getId().equals(id))
                .anyMatch(item -> item.getName().equalsIgnoreCase(trimmedName));
        if (duplicateName) {
            return ResponseEntity.badRequest().build();
        }

        for (CatalogItem item : items) {
            if (item.getId().equals(id)) {
                item.setName(trimmedName);
                item.setDesc(catalogItem.getDesc() != null ? catalogItem.getDesc().trim() : null);
                item.setRating(catalogItem.getRating());
                return ResponseEntity.ok(item);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
