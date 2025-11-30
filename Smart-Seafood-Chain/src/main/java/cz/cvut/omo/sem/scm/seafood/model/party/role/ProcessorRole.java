package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.model.recipe.Recipe;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.List;

public class ProcessorRole implements BusinessRole {

    private final List<Recipe> knownRecipes;

    public ProcessorRole(List<Recipe> knownRecipes) {
        this.knownRecipes = knownRecipes;
    }

    @Override
    public void performLogic(Party context) {
        // TODO: Implement processing logic
        // 1. Check ingredients in context.inventory
        // 2. Consume energy (Device)
        // 3. Produce finished goods
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PROCESSOR;
    }
}