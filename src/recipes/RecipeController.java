package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.recipe.Recipe;
import recipes.recipe.RecipeService;
import recipes.recipe.SearchRecipeQuery;
import recipes.user.User;
import recipes.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class RecipeController {

    private RecipeService recipesSrv;
    private UserService usersSrv;
    private PasswordEncoder passEncoder;

    @Autowired
    public RecipeController(RecipeService recipesSrv, UserService usersSrv, PasswordEncoder passEncoder) {
        this.recipesSrv = recipesSrv;
        this.usersSrv = usersSrv;
        this.passEncoder = passEncoder;
    }

    @PostMapping("/api/register")
    public void registerUser(@AuthenticationPrincipal @Valid @RequestBody User user) {
        if (!usersSrv.emailExists(user)) {
            user.setPassword(passEncoder.encode(user.getPassword()));
            usersSrv.newUser(user);
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/recipe/new")
    public Map<String, Long> newRecipe(@AuthenticationPrincipal User user, @Valid @RequestBody Recipe recipe) {
        recipesSrv.newRecipe(recipe, user);
        return Map.of("id", recipe.getId());
    }

    @ResponseBody
    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id) {

        Optional<Recipe> recipeOptional = recipesSrv.getRecipe(id);
        if (recipeOptional.isPresent()) {
            return recipeOptional.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @GetMapping("/api/recipe/search/")
    public List<Recipe> searchRecipes(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        SearchRecipeQuery query = new SearchRecipeQuery(category, name);
        if (query.isValidQuery()) {
            return recipesSrv.search(query);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<Void> updateRecipe(@AuthenticationPrincipal User user, @Valid @RequestBody Recipe recipe, @PathVariable long id) {
        if (recipesSrv.recipeExists(id)) {
            if (recipesSrv.isResipeAuthoredBy(id, user)) {
                recipesSrv.update(id, recipe, user);
                return ResponseEntity.noContent().build();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@AuthenticationPrincipal User user, @PathVariable long id) {
        if (recipesSrv.recipeExists(id)) {
            if (recipesSrv.isResipeAuthoredBy(id, user)) {
                recipesSrv.deleteRecipe(id);
                return ResponseEntity.noContent().build();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
