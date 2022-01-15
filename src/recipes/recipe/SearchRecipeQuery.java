package recipes.recipe;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchRecipeQuery {
    String category;
    String name;

    public SearchRecipeQuery(String category, String name) {
        this.category = category;
        this.name = name;
    }

    protected boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }

    public boolean isNameSet() {
        return isNotBlank(name);
    }

    public boolean isCategorySet() {
        return isNotBlank(category);
    }

    public boolean isValidQuery() {
//        true if exactly 1 of params present
        return isNameSet() ^ isCategorySet();
    }

}
