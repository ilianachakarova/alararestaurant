package alararestaurant.service;

import alararestaurant.domain.dtos.ItemSeedDto;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final CategoryRepository categoryRepository;


    private final static String ITEM_FILE = "src/main/resources/files/items.json";

    public ItemServiceImpl(ItemRepository itemRepository, ModelMapper modelMapper, FileUtil fileUtil, Gson gson, ValidationUtil validationUtil, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean itemsAreImported() {
        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return this.fileUtil.readFile(ITEM_FILE);
    }

    @Override
    public String importItems(String items) throws FileNotFoundException {
      StringBuilder importResult = new StringBuilder();
        ItemSeedDto[] itemSeedDtos = this.gson.fromJson(new FileReader(ITEM_FILE), ItemSeedDto[].class);
        for (ItemSeedDto itemSeedDto : itemSeedDtos) {
            if(this.validationUtil.isValid(itemSeedDto)){
                Item item = this.itemRepository.findByName(itemSeedDto.getName()).orElse(null);
                if(item == null){
                    Category category =
                            this.categoryRepository.findByName(itemSeedDto.getCategory())
                                    .orElse(new Category(itemSeedDto.getCategory()));

                    item = this.modelMapper.map(itemSeedDto, Item.class);
                    item.setCategory(category);

                    this.itemRepository.saveAndFlush(item);
                }
            }else {
                importResult.append("Invalid data").append(System.lineSeparator());
            }
        }
        return importResult.toString().trim();
    }
}
