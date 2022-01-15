package recipes.recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.recipe.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findAllByNameIgnoreCaseContainingOrderByDateDesc(String name);

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}
