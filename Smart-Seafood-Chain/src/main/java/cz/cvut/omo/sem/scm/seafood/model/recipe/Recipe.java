package cz.cvut.omo.sem.scm.seafood.model.recipe;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Recipe {
    private String recipeName;
    private List<Item> ingredients = new ArrayList<>();
    private int preparationTimeHours;
    private Map<Item, Item> substitutions = new HashMap<>();
}