package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        StringBuilder exportResult = new StringBuilder();
        List<Category>categories = this.categoryRepository.findAllByItemsSize();
        for (Category category : categories) {
            exportResult.append("Category: "+category.getName()).append(System.lineSeparator());
            category.getItems().forEach(item ->{
                exportResult.append("---Item Name: ").append(item.getName()).append(System.lineSeparator());
                exportResult.append("---Item Price: ").append(item.getPrice()).append(System.lineSeparator());
                exportResult.append(System.lineSeparator());
            } );

        }
        return exportResult.toString().trim();
    }
}
