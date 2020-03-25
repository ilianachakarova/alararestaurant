package alararestaurant.repository;

import alararestaurant.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category>findByName(String category);

    /*
    select c.name, i.name, count(i.id), sum(i.price) from categories as c
    join items i on c.id = i.category_id
group by c.id
order by count(i.id) desc , sum(i.price) desc;
     */

    @Query("select c from Category as c join c.items as i " +
            "group by c.id order by size(c.items) desc , sum (i.price) desc ")
    List<Category> findAllByItemsSize();
}
