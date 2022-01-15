package recipes.recipe;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotBlank
    String category;

    LocalDateTime date;

    @JsonIgnore
    long authorId;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    @NotEmpty
    @Size(min = 1)
    List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_directions",
            joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "direction")
    @NotEmpty
    @Size(min = 1)
    List<String> directions;

}
