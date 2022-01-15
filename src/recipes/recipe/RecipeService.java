package recipes.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecipeService {
    RecipeRepository repo;

    @Autowired
    public RecipeService(RecipeRepository repo) {
        this.repo = repo;
    }

    public void newRecipe(Recipe recipe, User byUser) {
        saveRecipe(recipe, byUser);
    }

    private long saveRecipe(Recipe recipe, User byUser) {
        recipe.date = LocalDateTime.now();
        recipe.authorId = byUser.getId();
        repo.save(recipe);
        return recipe.getId();
    }

    public Optional<Recipe> getRecipe(long id) {
        return repo.findById(id);
    }

    public void deleteRecipe(long id) {
        repo.deleteById(id);
    }

    public boolean recipeExists(long id) {
        return repo.existsById(id);
    }

    public void update(long id, Recipe recipe, User byUser) {
        recipe.setId(id);
        saveRecipe(recipe, byUser);
    }

    public List<Recipe> search(SearchRecipeQuery query) {
        if (query.isNameSet()) {
            return repo.findAllByNameIgnoreCaseContainingOrderByDateDesc(query.getName());
        }
        if (query.isCategorySet()) {
            return repo.findByCategoryIgnoreCaseOrderByDateDesc(query.getCategory());
        }
        return Collections.emptyList();
    }

    public boolean isResipeAuthoredBy(long id, User user) {
        return getRecipe(id).get().getAuthorId() == user.getId();
    }
}
