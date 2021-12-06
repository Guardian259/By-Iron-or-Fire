package com.guardian.bif.advancments.criterion;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;

public class OnVoidDamageCriterion {


    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(Identifier id, EntityPredicate.Extended playerPredicate, DamageSourcePredicate damageSource) {
            super(id, playerPredicate);
        }
    }
}
